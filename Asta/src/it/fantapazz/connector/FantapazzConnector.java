package it.fantapazz.connector;

import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.asta.controller.bean.AstaInfo.Status;
import it.fantapazz.asta.core.bean.PlayerStatus;
import it.fantapazz.connector.bean.CalciatoreComm;
import it.fantapazz.connector.bean.LegaComm;
import it.fantapazz.connector.bean.SquadraComm;
import it.fantapazz.connector.bean.UserComm;
import it.fantapazz.connector.cache.Cache;
import it.fantapazz.connector.cache.ObjectCache;
import it.fantapazz.utility.FilterInputStream;
import it.fantapazz.utility.PrintStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class FantapazzConnector implements ConnectorI {
	
	private static final Log logger = LogFactory.getLog(FantapazzConnector.class);

	private static final String URL_FANTAPAZZ = "http://www.beta.fantapazz.com";

	private static final String TOKEN = "69222d343f634e55796a54687d553c786f637e7a5a55757d6f72293328";

	private static final String LISTA_CALCIATORI = "/servizi/fantacalcio/asta/lista_calciatori";
	
	private static final String INFO_LEGA = "/servizi/fantacalcio/asta/info_lega?ID_Lega=";
	
	private static final String LOGIN = "/servizi/fantacalcio/asta/login/?user=%s&pass=%s";
	
	private static final String GET_USER = "/servizi/fantacalcio/asta/get_user?uid=%s";
	
	private static final String GET_ASTE = "/servizi/fantacalcio/asta/getAste";
	
	private static final String GET_ASTA = "/servizi/fantacalcio/asta/getAsta/%s";
	
	private static final String GET_ASTA_STATUS = "/servizi/fantacalcio/asta/getAstaStatus?astaID=%s";
	
	private static final String SET_ASTA_STATUS = "/servizi/fantacalcio/asta/setAstaStatus?astaID=%s&status=%s";

	private static final String GET_ASTE_FOR_USER = "/servizi/fantacalcio/asta/getAsteForUser?uid=%s";

	private static final String GET_PLAYER_STATUS = "/servizi/fantacalcio/asta/getPlayerStatus?ID_Lega=%s&playerID=%s";

	private static final String GET_SQUADRA = "/servizi/fantacalcio/asta/getSquadra?playerID=%s";

	private static final String ASSIGN = "/servizi/fantacalcio/asta/assignCalciatore?ID_Lega=%s&playerID=%s&calciatoreID=%s&value=%s&uid=%s";

	private static final String REMOVE = "/servizi/fantacalcio/asta/removeCalciatore?acquistoID=%s";

	private static final String SELL = "/servizi/fantacalcio/asta/sellCalciatore?ID_Lega=%s&playerID=%s&calciatoreID=%s&value=%s&uid=%s";

	private boolean calciatoriLoaded;
		
	private ObjectCache objectCache;
				
	private ObjectMapper mapper;
		
	public FantapazzConnector() {
		objectCache = new ObjectCache();
		mapper = new ObjectMapper();
		calciatoriLoaded = false;
	}
			
	public Collection<CalciatoreComm> getCalciatori() throws ConnectionException {
		ensureCalciatori();
		return objectCache.getObjectsForContext(CalciatoreComm.class.getName());
	}	
	
	public UserComm getUser(String userID) throws ConnectionException {
		UserComm user = objectCache.get(userID, UserComm.class.getName());
		if ( user == null ) {
			String url = String.format(URL_FANTAPAZZ + GET_USER, userID);
			InputStream in = prepareURL(url, true);
			try {
				user = mapper.readValue(in, UserComm.class);
			} catch (JsonParseException e) {
				throw new ConnectionException(e);
			} catch (JsonMappingException e) {
				throw new ConnectionException(e);
			} catch (IOException e) {
				throw new ConnectionException(e);
			}
			user.setID(userID);
			objectCache.set(userID, user, UserComm.class.getName());
			return user;
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public UserComm getUser(String username, String password) throws ConnectionException {

		String url = String.format(URL_FANTAPAZZ + LOGIN, username, password);
		InputStream in = prepareURL(url, true);
		JsonNode node;
		Map<String, String> response;
		try {
			node = mapper.readTree(in);
			logger.debug("Obtained: " + node);
			response = mapper.readValue(node, Map.class);
		} catch (JsonParseException e) {
			throw new ConnectionException(e);			
		} catch (JsonMappingException e) {
			throw new ConnectionException(e);			
		} catch (IOException e) {
			throw new ConnectionException(e);			
		}
		String uid = response.get("uid");
		UserComm user = getUser(uid);
		user.setUsername(username);
		user.setPassword(password);
		return user;
	}

	public LegaComm getLega(String ID) throws ConnectionException {
		LegaComm lega = objectCache.get(ID, LegaComm.class.getName());
		if ( lega == null ) {
			String url = URL_FANTAPAZZ + INFO_LEGA + ID;
			InputStream in = prepareURL(url, true);
			JsonNode list;
			try {
				list = mapper.readTree(in);
			} catch (JsonParseException e) {
				throw new ConnectionException(e);			
			} catch (JsonMappingException e) {
				throw new ConnectionException(e);			
			} catch (IOException e) {
				throw new ConnectionException(e);			
			}
			try {
				if ( ! list.isArray() ) {
					lega = mapper.readValue(list, LegaComm.class);
				}
				else {
					lega = mapper.readValue(list.get(0), LegaComm.class);
				}
			} catch (JsonParseException e) {
				throw new ConnectionException(e);			
			} catch (JsonMappingException e) {
				throw new ConnectionException(e);			
			} catch (IOException e) {
				throw new ConnectionException(e);			
			}
			objectCache.set(ID, lega, LegaComm.class.getName());
		}
		return lega;
	}
		
	public CalciatoreComm getCalciatore(String ID) throws ConnectionException {
		ensureCalciatori();
		CalciatoreComm calciatore = objectCache.get(ID, CalciatoreComm.class.getName());
		if ( calciatore != null )
			return calciatore;
		throw new ConnectionException("Calciatore not found: " + ID);			
	}
	
	private void loadCalciatori() throws ConnectionException {
		
		String url = URL_FANTAPAZZ + LISTA_CALCIATORI;
		InputStream in = prepareURL(url, true);

		JsonNode list;
		try {
			list = mapper.readTree(in);
		} catch (JsonProcessingException e) {
			throw new ConnectionException(e);
		} catch (IOException e) {
			throw new ConnectionException(e);
		}
		
		if ( ! list.isArray() )
			throw new ConnectionException("Result is not an array");
		
		for ( int i = 0; i < list.size(); i ++ ) {
			CalciatoreComm calciatore;
			try {
				calciatore = mapper.readValue(list.get(i), CalciatoreComm.class);
			} catch (JsonParseException e) {
				throw new ConnectionException(e);
			} catch (JsonMappingException e) {
				throw new ConnectionException(e);
			} catch (IOException e) {
				throw new ConnectionException(e);
			}
			objectCache.set(calciatore.getID_Calciatore(), calciatore, CalciatoreComm.class.getName());
		}
		
	}
				
	public List<AstaInfo> getAste() throws ConnectionException {
		String url = String.format(URL_FANTAPAZZ + GET_ASTE);
		InputStream in = prepareURL(url, false);
		JsonNode response;
		try {
			response = mapper.readTree(in);
		} catch (JsonProcessingException e) {
			throw new ConnectionException(e);			
		} catch (IOException e) {
			throw new ConnectionException(e);			
		}
		List<AstaInfo> list = new ArrayList<AstaInfo>(response.size());
		for ( int i = 0; i < response.size(); i ++ ) {
			JsonNode node = response.get(i);
			AstaInfo info;
			try {
				info = mapper.readValue(node, AstaInfo.class);
			} catch (JsonParseException e) {
				throw new ConnectionException(e);
			} catch (JsonMappingException e) {
				throw new ConnectionException(e);
			} catch (IOException e) {
				throw new ConnectionException(e);
			}
			list.add(info);
		}
		return list;
	}

	public AstaInfo getAsta(String astaID) throws ConnectionException {
		String url = String.format(URL_FANTAPAZZ + GET_ASTA, astaID);
		InputStream in = prepareURL(url, false);
		AstaInfo info;
		try {
			info = mapper.readValue(in, AstaInfo.class);
		} catch (JsonParseException e) {
			throw new ConnectionException(e);
		} catch (JsonMappingException e) {
			throw new ConnectionException(e);
		} catch (IOException e) {
			throw new ConnectionException(e);
		}
		return info;
	}

	public void setAstaStatus(String astaID, Status status) throws ConnectionException {
		String url = String.format(URL_FANTAPAZZ + SET_ASTA_STATUS, astaID, status.toString());
		InputStream in = prepareURL(url, false);
		boolean ret;
		try {
			ret = mapper.readValue(in, Boolean.class);
		} catch (JsonParseException e) {
			throw new ConnectionException(e);
		} catch (JsonMappingException e) {
			throw new ConnectionException(e);
		} catch (IOException e) {
			throw new ConnectionException(e);
		}
		if ( ret == false )
			throw new ConnectionException("Cannot change asta status: " + astaID);
	}

	public Status getAstaStatus(String astaID) throws ConnectionException {
		String url = String.format(URL_FANTAPAZZ + GET_ASTA_STATUS, astaID);
		InputStream in = prepareURL(url, false);
		String value;
		try {
			value = mapper.readValue(in, String.class);
		} catch (JsonParseException e) {
			throw new ConnectionException(e);
		} catch (JsonMappingException e) {
			throw new ConnectionException(e);
		} catch (IOException e) {
			throw new ConnectionException(e);
		}
		return Status.valueOf(value);
	}
	
	public List<AstaInfo> getAsteForUser(String userID) throws ConnectionException {
		String url = String.format(URL_FANTAPAZZ + GET_ASTE_FOR_USER, userID);
		InputStream in = prepareURL(url, false);
		JsonNode response;
		try {
			response = mapper.readTree(in);
		} catch (JsonProcessingException e) {
			throw new ConnectionException(e);			
		} catch (IOException e) {
			throw new ConnectionException(e);			
		}
		List<AstaInfo> list = new ArrayList<AstaInfo>(response.size());
		for ( int i = 0; i < response.size(); i ++ ) {
			JsonNode node = response.get(i);
			AstaInfo info;
			try {
				info = mapper.readValue(node, AstaInfo.class);
			} catch (JsonParseException e) {
				throw new ConnectionException(e);
			} catch (JsonMappingException e) {
				throw new ConnectionException(e);
			} catch (IOException e) {
				throw new ConnectionException(e);
			}
			list.add(info);
		}
		return list;
	}
	
	public Integer assignCalciatore(String astaID, String playerID, String calciatoreID, double value, String userUID) throws ConnectionException {
		String url = String.format(URL_FANTAPAZZ + ASSIGN, astaID, playerID, calciatoreID, "" + value, userUID);
		InputStream in = prepareURL(url, false);
		Integer ret;
		try {
			ret = mapper.readValue(in, Integer.class);
		} catch (JsonProcessingException e) {
			throw new ConnectionException(e);			
		} catch (IOException e) {
			throw new ConnectionException(e);			
		}
		if ( ret < 0 )
			throw new ConnectionException("Error in assignCalciatore: " + ret);
		return ret;
	}

	public void removeCalciatore(String acquistoID) throws ConnectionException {
		String url = String.format(URL_FANTAPAZZ + REMOVE, acquistoID);
		InputStream in = prepareURL(url, false);
		Integer ret;
		try {
			ret = mapper.readValue(in, Integer.class);
		} catch (JsonProcessingException e) {
			throw new ConnectionException(e);			
		} catch (IOException e) {
			throw new ConnectionException(e);			
		}
		if ( ret < 0 )
			throw new ConnectionException("Error in assignCalciatore: " + ret);
	}

	public Integer sellCalciatore(String legaID, String squadraID, String calciatoreID, double value, String playerID) throws ConnectionException {
		String url = String.format(URL_FANTAPAZZ + SELL, legaID, squadraID, calciatoreID, "" + value, playerID);
		InputStream in = prepareURL(url, false);
		Integer ret;
		try {
			ret = mapper.readValue(in, Integer.class);
		} catch (JsonProcessingException e) {
			throw new ConnectionException(e);			
		} catch (IOException e) {
			throw new ConnectionException(e);			
		}
		if ( ret < 0 )
			throw new ConnectionException("Error in sell Calciatore: " + ret);
		return ret;
	}

	public PlayerStatus getPlayerStatus(String legaID, String squadraID) throws ConnectionException {
		String url = String.format(URL_FANTAPAZZ + GET_PLAYER_STATUS, legaID, squadraID);
		InputStream in = prepareURL(url, false);
		PlayerStatus status;
		try {
			status = mapper.readValue(in, PlayerStatus.class);
		} catch (JsonProcessingException e) {
			throw new ConnectionException(e);			
		} catch (IOException e) {
			throw new ConnectionException(e);			
		}
		return status;
	}
	
	public SquadraComm getSquadra(String playerID) throws ConnectionException {
		SquadraComm squadra = objectCache.get(playerID, SquadraComm.class.getName());
		if ( squadra == null ) {
			String url = String.format(URL_FANTAPAZZ + GET_SQUADRA, playerID);
			InputStream in = prepareURL(url, true);
			try {
				squadra = mapper.readValue(in, SquadraComm.class);
			} catch (JsonProcessingException e) {
				throw new ConnectionException(e);			
			} catch (IOException e) {
				throw new ConnectionException(e);			
			}
			objectCache.set(playerID, squadra, SquadraComm.class.getName());
		}
		return squadra;
	}

	public SquadraComm getSquadraForAstaAndPlayer(AstaInfo astaInfo, String playerID) throws ConnectionException {
		for ( String squadraID : astaInfo.getPlayers()) {
			SquadraComm squadra = getSquadra(squadraID);
			if ( squadra.getUidPresidente().equals(playerID))
				return squadra;
			if ( squadra.getAllenatori().contains(playerID))
				return squadra;
		}
		return null;
	}

	/**
	 * Prepare URL 
	 * 
	 */
	private InputStream prepareURL(String url, boolean useCache) {
		url += "&token=" + TOKEN;
		if ( ! useCache ) {
			url += "&r=" + new Random().nextLong();
		}
		return new FilterInputStream(Cache.instance().read(url, useCache));
	}
	
	private void ensureCalciatori() throws ConnectionException {
		if ( calciatoriLoaded )
			return;
		loadCalciatori();
		calciatoriLoaded = true;
	}

}
