package it.fantapazz.connector;

import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.asta.controller.bean.AstaInfo.Status;
import it.fantapazz.asta.core.bean.PlayerStatus;
import it.fantapazz.connector.bean.CalciatoreComm;
import it.fantapazz.connector.bean.LegaComm;
import it.fantapazz.connector.bean.SquadraComm;
import it.fantapazz.connector.bean.UserComm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class FakeConnector implements ConnectorI {
	
	private Map<String, CalciatoreComm> calciatori;
	private Map<String, UserComm> users;
	private Map<String, SquadraComm> squadre;
	private Map<String, LegaComm> leghe;
	private List<AstaInfo> aste;
	private Map<String, PlayerStatus> status;
	
	public FakeConnector() throws ConnectionException {
		calciatori = new HashMap<String, CalciatoreComm>();
		for ( int i = 0; i < 100; i ++) {
			CalciatoreComm calciatore = new CalciatoreComm();
			calciatore.setID_Calciatore("" + i);
			calciatore.setID_Ruolo("");
			calciatore.setCalciatore("" + i);
			calciatore.setClub("");
			calciatore.setQuotazione("");
			calciatore.setRuolo("");
			calciatore.setRuolo_short("");
			calciatore.setRuolo_long("");
			calciatori.put("" + i, calciatore);
		}
		// System.out.println(calciatori);
		users = new HashMap<String, UserComm>();
		for ( int i = 0; i < 20; i ++) {
			UserComm user = new UserComm();
			user.setID("" + i);
			user.setName("Player " + i);
			user.setUsername("username" + i);
			user.setPassword("password" + i);
			users.put("" + i, user);
		}
		// System.out.println(users);
		leghe = new HashMap<String, LegaComm>();
		for ( int i = 0; i < 4; i ++) {
			LegaComm lega = new LegaComm();
			lega.setID_Lega("" + i);
			lega.setID_Anno("" + i);
			lega.setNomeLega("Lega " + i);
			lega.setID_TipoLega(i % 2 == 0 ? LegaComm.TIPO_LEGA_PRIVATA : LegaComm.TIPO_LEGA_PUBBLICA);
			lega.setCreditiIniziali(300);
			leghe.put("" + i, lega);
		}
		// System.out.println(leghe);
		squadre = new HashMap<String, SquadraComm>();
		for ( int i = 0; i < 20; i ++) {
			SquadraComm squadra = new SquadraComm();
			squadra.setLegaID("" + (i % 5));
			squadra.setSquadraID("" + i);
			squadra.setAlias("Squadra " + i);
			setupSquadra(squadra);
			squadre.put("" + i, squadra);
		}
		// System.out.println(squadre);
		aste = new LinkedList<AstaInfo>();
		status = new HashMap<String, PlayerStatus>();
		for ( LegaComm lega : leghe.values()) {
			AstaInfo asta = new AstaInfo();
			asta.setID(lega.getID_Lega());
			asta.setIDLega(lega.getID_Lega());
			setupPlayersForAsta(asta);
			asta.setCalciatori(new LinkedList<String>());
			asta.setRemainCalciatori(new LinkedList<String>());
			for ( CalciatoreComm calciatore : getCalciatori() ) {
				asta.getCalciatori().add(calciatore.getID_Calciatore());
				asta.getRemainCalciatori().add(calciatore.getID_Calciatore());
			}
			asta.setStartMoney(lega.getCreditiIniziali());
			asta.setStatus(AstaInfo.Status.NOT_STARTED);
			aste.add(asta);
			
			// System.out.println(asta);
			
			for ( String playerID : asta.getPlayers()) {
				PlayerStatus playerStatus = new PlayerStatus();
				playerStatus.setMoney(asta.getStartMoney());
				status.put(asta.getID() + "-" + playerID, playerStatus);
			}
		}
		// System.out.println(aste);
	}
	
	private void setupPlayersForAsta(AstaInfo asta) {
		int count = new Random().nextInt(5) + 2;
		Set<String> set = new HashSet<String>();
		for ( int i = 0; i < count; i ++ ) {
			set.add("" + new Random().nextInt(squadre.size()));
		}
		asta.setPlayers(new LinkedList<String>(set));
	}
	
	private void setupSquadra(SquadraComm squadra) {
		String presidenteUID = "" + new Random().nextInt(users.size());
		int count = new Random().nextInt(5) + 1;
		Set<String> set = new HashSet<String>();
		set.add(presidenteUID);
		while ( set.size() < count ) {
			set.add("" + new Random().nextInt(users.size()));
		}
		squadra.setUidPresidente(presidenteUID);
		squadra.setAllenatori(new LinkedList<String>(set));
	}

	public Collection<CalciatoreComm> getCalciatori() throws ConnectionException {
		return calciatori.values();
	}
	
	public CalciatoreComm getCalciatore(String ID) throws ConnectionException {
		return calciatori.get(ID);
	}

	public UserComm getUser(String userID) throws ConnectionException {
		return users.get(userID);
	}

	public UserComm getUser(String username, String password) throws ConnectionException {
		for ( UserComm user : users.values()) {
			if (!user.getUsername().equals(username))
				continue;
			if (!user.getPassword().equals(password))
				continue;
			return user;
		}
		return null;
	}

	public LegaComm getLega(String ID) throws ConnectionException {
		return leghe.get(ID);
	}

	public SquadraComm getSquadra(String squadraID) throws ConnectionException {
		return squadre.get(squadraID);
	}

	public List<AstaInfo> getAste() throws ConnectionException {
		return aste;
	}

	public void setAstaStatus(String astaID, Status status) throws ConnectionException {
		getAsta(astaID).setStatus(status);
	}

	public Status getAstaStatus(String astaID) throws ConnectionException {
		return getAsta(astaID).getStatus();
	}

	public List<AstaInfo> getAsteForUser(String userID) throws ConnectionException {
		List<AstaInfo> list = new LinkedList<AstaInfo>();
		for ( AstaInfo asta : aste ) {
			for ( String squadraID : asta.getPlayers()) {
				SquadraComm squadra = getSquadra(squadraID);
				if ( squadra.getUidPresidente().equals(userID)) {
					list.add(asta);
					break;
				}
				if ( squadra.getAllenatori().contains(userID)) {
					list.add(asta);
					break;
				}
			}
		}			
		return list;
	}

	public AstaInfo getAsta(String astaID) throws ConnectionException {
		for ( AstaInfo asta : aste ) {
			if ( asta.getID().equals(astaID))
				return asta;
		}
		return null;
	}

	public PlayerStatus getPlayerStatus(String legaID, String squadraID) throws ConnectionException {
		return status.get(legaID + "-" + squadraID);
	}

	public Integer assignCalciatore(String astaID, String playerID, String calciatoreID, double value, String userUID) throws ConnectionException {
		PlayerStatus playerStatus = getPlayerStatus(astaID, playerID);
		playerStatus.winCalciatore(calciatoreID, value);
		return 0;
	}

	public void removeCalciatore(String acquistoID) throws ConnectionException {
		throw new ConnectionException("Usupported");
//		PlayerStatus playerStatus = getPlayerStatus(astaID, playerID);
//		playerStatus.removeCalciatore(calciatoreID, costo);
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

	public Integer sellCalciatore(String legaID, String squadraID, String calciatoreID, double value, String playerID) throws ConnectionException {
		throw new ConnectionException("Usupported");
	}

}
