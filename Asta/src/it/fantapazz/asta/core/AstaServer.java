package it.fantapazz.asta.core;

import it.fantapazz.asta.AstaConfig;
import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.asta.core.bean.AstaStatus;
import it.fantapazz.asta.core.bean.CalciatoreCost;
import it.fantapazz.asta.core.bean.HelloAstaServerBean;
import it.fantapazz.asta.core.bean.PlayerStatus;
import it.fantapazz.asta.core.protocol.MsgChoose;
import it.fantapazz.asta.core.protocol.MsgHelloPlayer;
import it.fantapazz.asta.core.protocol.MsgHelloServer;
import it.fantapazz.asta.core.protocol.MsgOffer;
import it.fantapazz.asta.core.protocol.MsgPause;
import it.fantapazz.asta.core.protocol.MsgPauseStop;
import it.fantapazz.asta.core.protocol.MsgPing;
import it.fantapazz.asta.core.protocol.MsgSelect;
import it.fantapazz.asta.core.protocol.MsgText;
import it.fantapazz.asta.core.protocol.MsgWinner;
import it.fantapazz.chat.ClientInfo;
import it.fantapazz.chat.Message;
import it.fantapazz.chat.ServerI;
import it.fantapazz.chat.ServerListener;
import it.fantapazz.chat.Startable;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;
import it.fantapazz.connector.bean.CalciatoreComm;
import it.fantapazz.connector.bean.SquadraComm;
import it.fantapazz.utility.Timeout;
import it.fantapazz.utility.Timer;
import it.fantapazz.utility.TurnSelector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Server for an Asta with a given number of players and
 * calciatori to choose.
 * 
 * @author Michele Mastrogiovanni
 */
public class AstaServer implements Startable, Timeout, ServerListener {
		
	private static final Log logger = LogFactory.getLog(AstaServer.class);
	
	public static final int FORMAZIONE_NUM_POR = 3;
	public static final int FORMAZIONE_NUM_DIF = 8;
	public static final int FORMAZIONE_NUM_CEN = 8;
	public static final int FORMAZIONE_NUM_ATT = 6;

	private ServerI server;
	
	private enum Status {
		IDLE,			// AstaServer is starting
		TURN,			// A player is choosing a Calciatore
		SELECTED,		// Waiting first offer
		OFFER,			// Waiting for raising
		WON				// Won
	};
	
	private boolean paused;
	private String playerPaused;
	private long pausedRemainTime;
	
	private Map<String, ClientInfo> playerIDToClientID;
	
	private Map<String, String> clientIDToPlayerID;
	
	private Map<String, PlayerStatus> playersStatus;
	
	private AstaStatus astaStatus;
	
	private AstaConfig config;

	// Players in asta
	private List<String> players;
	
	// All calciatori to select
	private List<String> calciatori;
	
	// Status on asta
	private Status status;
	
	// Current player selecting calciatore
	private String currentPlayer;
		
	// Calciatore currently selected for asta
	private String currentCalciatore;
	
	// Current offer performed
	private double currentOffer;

	// Current offerer
	private String currentOfferer;

	// Algorithm used to selected next player
	private TurnSelector<String> turnSelector;
	
	// Timeout manager
	private Timer timer;
	
	// ID of current Asta
	private String astaID;

	// Info about asta
	private AstaInfo astaInfo;
	
	/**
	 * Create a new asta for given set of calciatori and players
	 * 
	 * @param players List of id of players
	 * @param calciatori List of id of calciatori to select
	 * @throws IOException 
	 * @throws ConnectionException 
	 */
	public AstaServer(String astaID, ServerI server) throws IOException, ConnectionException {
		
		this.astaID = astaID;
		
		status = Status.IDLE;
		playerIDToClientID = new HashMap<String, ClientInfo>();
		clientIDToPlayerID = new HashMap<String, String>();
		playersStatus = new HashMap<String, PlayerStatus>();

		loadData();
		
		astaStatus = new AstaStatus();
		astaStatus.setPlayers(players);
		
		this.server = server;
		server.addListener(this);
		
	}
	
	private void loadData() throws ConnectionException {
		logger.debug("Starting asta: " + astaID);
		this.astaInfo = Connector.instance().getAsta(astaID);
		logger.debug("Loadeded info about asta");
		this.players = astaInfo.getPlayers();
		this.calciatori = astaInfo.getCalciatori();
		logger.debug("Asta started - players: " + this.players);
		// Force cache of squadra info
		int count = 1;
		for ( String playerID : this.players) {

			// Load squadra infos
			SquadraComm squadra = Connector.instance().getSquadra(playerID);

			// Load and cache info about player status
			getPlayerStatus(playerID);			

			logger.debug("Loaded squadra: (" + count + "/" + this.players.size() + ") - " + squadra.getAlias());
			count ++;
			
		}
	}
	
	public boolean isTerminated() {
		if ( astaStatus == null )
			return false;
		if ( astaStatus.getRemainCalciatori() == null )
			return false;
		if (astaStatus.getRemainCalciatori().size() == 0) {
			logger.debug("Asta terminated since no remain calciatori");
			return true;
		}
		if ( playersStatus == null )
			return false;
		if ( playersStatus.size() == 0 )
			return false;
		for ( Entry<String, PlayerStatus> entry : playersStatus.entrySet() ) {
			if (!hasPlayerTerminated(entry.getValue())) {
				return false;
			}
			SquadraComm squadra;
			try {
				squadra = Connector.instance().getSquadra(entry.getKey());
				logger.debug("Squadra " + squadra.getAlias() + " terminated in asta: " + astaInfo.getID() + ", lega: " + astaInfo.getIDLega());
			} catch (ConnectionException e) {
				logger.debug("Squadra " + entry.getKey() + " terminated in asta: " + astaInfo.getID() + ", lega: " + astaInfo.getIDLega());
			}
		}
		logger.debug("All player terminated");
		return true;
	}
	
	private boolean hasPlayerTerminated(PlayerStatus status) {
		int numP = 0;
		int numD = 0;
		int numC = 0;
		int numA = 0;
		for ( CalciatoreCost cc : status.getCalciatoriWon()) {
			CalciatoreComm calciatore;
			try {
				calciatore = Connector.instance().getCalciatore(cc.getID());
			} catch (ConnectionException e) {
				logger.error("Cannot get info of calciatore: " + cc.getID());
				return false;
			}
			if (calciatore.getRuolo_short().equals("Por")) {
				numP++;
				continue;
			}
			if (calciatore.getRuolo_short().equals("Dif")) {
				numD++;
				continue;
			}
			if (calciatore.getRuolo_short().equals("Cen")) {
				numC++;
				continue;
			}
			if (calciatore.getRuolo_short().equals("Att")) {
				numA++;
				continue;
			}
		}
		if ( numP < FORMAZIONE_NUM_POR )
			return false;
		if ( numD < FORMAZIONE_NUM_DIF )
			return false;
		if ( numC < FORMAZIONE_NUM_CEN )
			return false;
		if ( numA < FORMAZIONE_NUM_ATT )
			return false;
		return true;
		
	}
	
	public double getCompletition() {
		return 100.0 - (( (double) astaStatus.getRemainCalciatori().size() / (double) calciatori.size() ) * 100.0);
	}

	/**
	 * Run until all players are selected
	 */
	public void start() {
		turnSelector.init(players);
		server.start();
		astaStatus.setRemainCalciatori(new ArrayList<String>(calciatori));
		status = Status.IDLE;
		if ( astaStatus.getRemainCalciatori().size() > 0 ) {
			step();
		}
	}

	public void stop() {
		stopTimer();
		server.stop();
	}
	
	/**
	 * Single round of play
	 */
	private void step() {
		
		if ( status != Status.IDLE )
			return;

		if ( astaStatus.getRemainCalciatori().size() == 0 ) {
			logger.debug("ASTA TERMINATED");
			stopTimer();
			server.stop();
			return;
		}

		// Choose next player to select a calciatore
		status = Status.TURN;

		// Select next player
		currentPlayer = turnSelector.next();

		// Set current player turn
		astaStatus.setTurnOf(currentPlayer, config.getTimeoutTurnoGiocatore());		

		logger.debug("Turn of player: " + currentPlayer);

		// Notify current player choice
		server.broadcast(new MsgChoose(currentPlayer));
		
		// Start timer
		startTimer(config.getTimeoutTurnoGiocatore());
		
	}

	/**
	 * Expiration of time
	 */
	public void timeout(Timer timer) {

		// reset timer
		stopTimer();		
		
		if ( paused ) {
			paused = false;
			// Start timer to choose
			startTimer(config.getTimeoutTurnoGiocatore());
			return;
		}
		
		if ( status == Status.TURN ) {
			
			// Return idle
			status = Status.IDLE;
			
			logger.debug("Player " + currentPlayer + " lost its turn");
			
			// Giocatore lost its right to choose a calciatore
			currentPlayer = null;
			step();
			
		}
		else if ( status == Status.SELECTED ) {

			// Do not received the first offer: 
			// calciatore is assigned to the current player
			
			status = Status.WON;

			logger.debug("No offers for calciatore: " + currentCalciatore);
			
			// Update fantapazz status
			try {
				String uidPresidente = Connector.instance().getSquadra(currentPlayer).getUidPresidente();
				Connector.instance().assignCalciatore(astaInfo.getIDLega(), currentPlayer, currentCalciatore, currentOffer, uidPresidente);
			}
			catch (ConnectionException e) {
				logger.debug("Cannot connect to Fantapazz for update");
			}

			// Notify that currentPlayer won the current Calciatore
			server.broadcastExcept(getClientIDFromPlayerID(currentPlayer), new MsgWinner(false, currentOfferer, currentOffer, currentCalciatore));
			server.sendTo(getClientIDFromPlayerID(currentPlayer), new MsgWinner(true, currentOfferer, currentOffer, currentCalciatore));

			// Update player status
			playersStatus.get(currentPlayer).winCalciatore(currentCalciatore, currentOffer);

			logger.debug("Player: " + currentPlayer + " won " + playersStatus.get(currentPlayer).getCalciatoriWon().size());
			logger.debug("Player: " + currentPlayer + " remain money: " + playersStatus.get(currentPlayer).getMoney());
			
			// Return idle
			astaStatus.getRemainCalciatori().remove(currentCalciatore);
			currentCalciatore = null;
			currentPlayer = null;
			status = Status.IDLE;
			
			step();

		}
		else if ( status == Status.OFFER ) {

			// Do not received the first offer: 
			// calciatore is assigned to the current player
			
			status = Status.WON;

			// logger.debug("No MORE offers for calciatore: " + currentCalciatore);
			
			// Update fantapazz status
			try {
				String uidPresidente = Connector.instance().getSquadra(currentPlayer).getUidPresidente();
				Connector.instance().assignCalciatore(astaInfo.getIDLega(), currentPlayer, currentCalciatore, currentOffer, uidPresidente);
			}
			catch (ConnectionException e) {
				logger.debug("Cannot connect to Fantapazz for update");
			}

			// Notify that currentPlayer won the current Calciatore
			server.broadcastExcept(getClientIDFromPlayerID(currentOfferer), new MsgWinner(false, currentOfferer, currentOffer, currentCalciatore));
			server.sendTo(getClientIDFromPlayerID(currentOfferer), new MsgWinner(true, currentOfferer, currentOffer, currentCalciatore));

			playersStatus.get(currentOfferer).winCalciatore(currentCalciatore, currentOffer);

			// Return idle
			astaStatus.getRemainCalciatori().remove(currentCalciatore);
			currentCalciatore = null;
			currentPlayer = null;
			status = Status.IDLE;
			
			step();

		}

	}
	
	/**
	 * Receive a message
	 */
	public void receive(ClientInfo from, Message message) {
		
		// logger.debug("Received message: " + message);
		
		// Answer to ping reques
		if ( message instanceof MsgPing ) {
			server.sendTo(from, message);
			return;
		}
		
		// An user can pause the server or enlarge the current pause
		if ( message instanceof MsgPause ) {
			MsgPause pause = (MsgPause) message;
			if ( !paused || pause.getSource().equals(playerPaused)) {
				if ( ! paused ) {
					// First pause
					pausedRemainTime = getRemainTime(); 
					stopTimer();
				}
				paused = true;
				long pauseTime = (long)( pause.getDuration() * 1000 );
				startTimer(pauseTime);
				server.broadcast(message);
			}
			return;
		}

		// Stop pause from current player 
		if ( message instanceof MsgPauseStop ) {
			if ( paused && message.getSource().equals(playerPaused)) {
				stopTimer();
				paused = false;
				playerPaused = null;
				if ( pausedRemainTime > 0 ) {
					startTimer(pausedRemainTime);
					pausedRemainTime = 0;
				}
				server.broadcast(message);
			}
			return;
		}
				
		// Chat messages
		if ( message instanceof MsgText ) {
			server.broadcast(message);
			return;
		}

		if ( message instanceof MsgHelloPlayer ) {
			
			MsgHelloPlayer hello = (MsgHelloPlayer) message;
			
			if ( ! players.contains(hello.getSource() )) {
				server.kickClient(getClientIDFromPlayerID(hello.getSource()));
				return;
			}
									
			// Load player status
			PlayerStatus playerStatus;
			try {
				playerStatus = getPlayerStatus(hello.getSource());
			} catch (ConnectionException e) {
				logger.error("Cannot connect client", e);
				server.kickClient(getClientIDFromPlayerID(hello.getSource()));
				return;
			}

			// Save client information
			playerIDToClientID.put(hello.getSource(), from);
			clientIDToPlayerID.put(from.getID(), hello.getSource());

			// Update asta status
			if ( timer != null ) {
				astaStatus.setRemainTime(timer.getRemainTime());
			}
			
			// Prepare hello content
			HelloAstaServerBean content = new HelloAstaServerBean(playerStatus, astaStatus);
			
			logger.debug("Received hello from: " + hello.getSource());
			
			// Send hello message
			server.sendTo(getClientIDFromPlayerID(hello.getSource()), new MsgHelloServer(hello.getSource(), content));
			
		}

		// Discard any further message during pause status
		if ( paused ) {
			return;
		}

		if ( message instanceof MsgSelect ) {
			MsgSelect select = (MsgSelect) message;
			calciatoreSelected(message.getSource(), select.getCalciatoreID());
			return;
		}
		
		if ( message instanceof MsgOffer ) {
			MsgOffer offer = (MsgOffer) message;
			offerReceived(offer.getSource(), offer.getOffer(), offer.getCalciatoreID());
			return;
		}
				
	}

	
	private void calciatoreSelected(String from, String calciatoreID) {

		if ( status != Status.TURN )
			return;

		if ( ! astaStatus.getRemainCalciatori().contains(calciatoreID) )
			return;

		if ( ! currentPlayer.equals(from))
			return;
		
		stopTimer();
		status = Status.SELECTED;

		currentCalciatore = calciatoreID;
		currentOfferer = currentPlayer;
		currentOffer = config.getMinimumCostCalciatore();

		// Update player info
		
		astaStatus.setLastOffer(currentOfferer, currentCalciatore, currentOffer);
				
		logger.debug("Player " + currentOfferer + " chosen calciatore: " + currentCalciatore);

		// Notify calciatore chosen to others
		server.broadcast(new MsgSelect(currentPlayer, currentCalciatore));

		// Start timer
		startTimer(config.getTimeoutRaising());

	}
	
	private void offerReceived(String source, double offer, String calciatoreID) {

		if ( status != Status.SELECTED && status != Status.OFFER )
			return;

		if ( ! currentCalciatore.equals(calciatoreID) )
			return;

		if ( offer <= currentOffer )
			return;
		
		// Not allowed offers from same offerer
		if ( currentOfferer.equals(source))
			return;
		
		stopTimer();
		status = Status.OFFER;
		
		// Save current values
		currentOfferer = source;
		currentOffer = offer;

		// Update offer status
		astaStatus.setLastOffer(currentOfferer, currentCalciatore, currentOffer);

		// Notify offer to all
		server.broadcast(new MsgOffer(currentOfferer, currentOffer, currentCalciatore));
		
		// Give time to see the offer performed
		startTimer(config.getTimeoutRaising());

	}
		
	private void startTimer(long delay) {
		if ( delay <= 0 )
			return;
		if ( timer == null ) {
			timer = new Timer(this, delay);
			// logger.debug("Started timer " + delay);
		}
	}
	
	private long getRemainTime() {
		if ( timer != null ) {
			return timer.getRemainTime();
		}
		return 0;
	}

	private void stopTimer() {
		if ( timer != null ) {
			timer.stop();
			timer = null;
			// logger.debug("Stopped timer");
		}
	}
	
	public ClientInfo getClientIDFromPlayerID(String playerID) {
		return playerIDToClientID.get(playerID);	
	}

	public String getPlayerIDFromClientID(String clientID) {
		return clientIDToPlayerID.get(clientID);	
	}

	public AstaConfig getConfig() {
		return config;
	}

	public void setConfig(AstaConfig config) {
		this.config = config;
	}
	
	public List<String> getPlayers() {
		return players;
	}

	public List<String> getCalciatori() {
		return calciatori;
	}

	public Status getStatus() {
		return status;
	}

	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public String getCurrentCalciatore() {
		return currentCalciatore;
	}

	public double getCurrentOffer() {
		return currentOffer;
	}

	public String getCurrentOfferer() {
		return currentOfferer;
	}

	public TurnSelector<String> getTurnSelector() {
		return turnSelector;
	}

	public void setTurnSelector(TurnSelector<String> turnSelector) {
		this.turnSelector = turnSelector;
	}

	private PlayerStatus getPlayerStatus(String clientID) throws ConnectionException {
		// Create a new player entry for a player
		if ( playersStatus.get(clientID) == null ) {
			// Load updated info from fantapazz
			PlayerStatus playerStatus = Connector.instance().getPlayerStatus(astaInfo.getIDLega(), clientID);
			playersStatus.put(clientID, playerStatus);
		}
		return playersStatus.get(clientID);
	}

	public void clientConnected(ClientInfo client) {
		logger.debug("Client connected: " + client);
	}

	public void clientEvicted(ClientInfo client) {
		logger.debug("Client evicted: " + client);
		String playerID = getPlayerIDFromClientID(client.getID()); 
		clientIDToPlayerID.remove(client.getID());
		playerIDToClientID.remove(playerID);
	}
	
	private void startPause() {
		
	}
	
	private void stopPause() {
		
	}
			
}
