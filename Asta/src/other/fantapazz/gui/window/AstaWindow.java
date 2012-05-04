package other.fantapazz.gui.window;

import it.fantapazz.asta.AstaConfig;
import it.fantapazz.asta.controller.PlayerControlServer;
import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.asta.core.PlayerControl;
import it.fantapazz.asta.core.PlayerListener;
import it.fantapazz.asta.core.PlayerServer;
import it.fantapazz.asta.core.bean.AstaStatus;
import it.fantapazz.asta.core.bean.CalciatoreCost;
import it.fantapazz.asta.core.bean.HelloAstaServerBean;
import it.fantapazz.asta.core.bean.PlayerStatus;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;
import it.fantapazz.connector.bean.CalciatoreComm;
import it.fantapazz.connector.bean.SquadraComm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import other.fantapazz.gui.interfaces.ChatListener;
import other.fantapazz.gui.interfaces.ChatMessagePanel;
import other.fantapazz.gui.interfaces.ChooseCalciatoreI;
import other.fantapazz.gui.interfaces.PlayerPanel;
import other.fantapazz.gui.interfaces.PlayerReceiver;
import other.fantapazz.gui.model.CalciatoriWonTableColumnModel;
import other.fantapazz.gui.model.CalciatoriWonTableModel;
import other.fantapazz.gui.utility.CenteredFrame;

/**
 * Main Asta panel 
 * 
 * @author Michele Mastrogiovannim
 */
public class AstaWindow extends CenteredFrame implements ChooseCalciatoreI, PlayerReceiver, PlayerListener, ChatListener {
	
	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(AstaWindow.class);

	// Minimum remain time to start timeout
	private static long MIN_REMAIN_TIME = 2000;

	private PlayerPanel playerPanel;
	
	private PlayerServer playerServer;

	private PlayerStatus playerStatus;
	
	private String myPlayerID;

	private String serverID;
	
	private String currentCalciatore;
	
	private double offer;

	private AstaConfig config;
	
	private CalciatoriWonTableModel calciatoriWon;
	
	private boolean initialized;
	
	private PlayerControlServer playerController;
	
	private AstaInfo currentAsta;
	
	private JInternalFrame parentFrame;
	
	private ChooseCalciatoreWindow chooseWindow;	
	
	private ChatMessagePanel chatPanel;
		
	public AstaWindow(JInternalFrame parentFrame, PlayerControlServer playerController) {
		super();
		setTitle("Fantapazz - Asta Online");

		this.parentFrame = parentFrame;
		this.playerController = playerController;
		
		initialized = false;
		
		initComponents();
	}
	
	private void initComponents() {

		setSize(new Dimension(700, 500));
		
		setClosable(true);
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		
		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				try {
					stop(currentAsta);
				} catch (ConnectionException e1) {
					logger.error("Cannot exit from");
				}
			}
		});
		
		getContentPane().setLayout(new BorderLayout());
		
		// Player panel
		playerPanel = new PlayerPanel(this);
								
		chatPanel = new ChatMessagePanel(this);
		chatPanel.setBorder(BorderFactory.createTitledBorder("Chat con le altre squadre"));
		
		calciatoriWon = new CalciatoriWonTableModel(); 		
		JTable table = new JTable(calciatoriWon, new CalciatoriWonTableColumnModel());
		JPanel calciatori = new JPanel(new BorderLayout());
		calciatori.add(table.getTableHeader(), BorderLayout.PAGE_START);
		calciatori.add(table, BorderLayout.CENTER);
		table.setFillsViewportHeight(true);		
		JScrollPane scrollPane = new JScrollPane(calciatori);
		scrollPane.setMinimumSize(new Dimension(100, 100));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, chatPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(300);		
		
		JSplitPane verticalSeparator = new JSplitPane(JSplitPane.VERTICAL_SPLIT, playerPanel, splitPane);
		verticalSeparator.setDividerLocation(300);
		getContentPane().add(verticalSeparator, BorderLayout.CENTER);
		
		playerPanel.enableOffer(false);

	}

	public void start(AstaInfo astaInfo) throws ConnectionException {
		currentAsta = astaInfo;
		playerController.enterAsta(astaInfo, this);
		this.myPlayerID = playerController.getPlayerID();
		this.config = playerController.getConfig();
		logger.debug("Client has ID: " + myPlayerID);
		playerServer = playerController.getPlayerServer();
		setVisible(true);
	}

	public void stop(AstaInfo astaInfo) throws ConnectionException {
		setVisible(false);
		parentFrame.setVisible(true);		
		playerController.exitAsta();
		playerServer = null;
		currentAsta = null;
	}

	public void pushedPlusOne() {
		playerServer.makeOffer(currentCalciatore, offer + 1.0);
		playerPanel.startTimer(config.getTimeoutRaising());
	}

	public void pushedPlusFive() {
		playerServer.makeOffer(currentCalciatore, offer + 5.0);
		playerPanel.startTimer(config.getTimeoutRaising());
	}

	public void pushedPlusTen() {
		playerServer.makeOffer(currentCalciatore, offer + 10.0);
		playerPanel.startTimer(config.getTimeoutRaising());
	}

	public void yourID(PlayerControl playerControl, String ID) {
		logger.debug("My ID is: " + ID);
		this.serverID = ID;
	}
	
	public List<CalciatoreComm> convert(List<CalciatoreCost> list) throws ConnectionException {
		List<CalciatoreComm> response = new ArrayList<CalciatoreComm>(list.size());
		for ( CalciatoreCost calciatoreCost : list ) {
			response.add(Connector.getInfo().getCalciatore(calciatoreCost.getID()));
		}
		return response;
	}

	public List<CalciatoreComm> convertIDList(List<String> list) {
		List<CalciatoreComm> response = new ArrayList<CalciatoreComm>(list.size());
		for ( String ID : list ) {
			try {
				response.add(Connector.instance().getCalciatore(ID));
			} catch (ConnectionException e) {
				CalciatoreComm calciatore = new CalciatoreComm();
				calciatore.setID_Calciatore(ID);
				response.add(calciatore);
			}
		}
		return response;
	}

	public void receivedHello(Object content) {
		
		HelloAstaServerBean helloContent = (HelloAstaServerBean) content;
		
		playerPanel.setStatusMessage("Connesso!!!");
		
		playerStatus = (PlayerStatus) helloContent.getPlayerStatus();

		logger.debug("Received hello: " + content);

		AstaStatus astaStatus = helloContent.getAstaStatus();
		
		long currentTime = System.currentTimeMillis();

		// Calciatori vinti
		calciatoriWon.setCalciatori(new ArrayList<CalciatoreCost>(playerStatus.getCalciatoriWon()));

		playerPanel.setStatusMessage("Carico informazioni sui calciatori...");

		// Choose calciatori
		chooseWindow = new ChooseCalciatoreWindow();
		chooseWindow.setCalciatori(convertIDList(helloContent.getAstaStatus().getRemainCalciatori()));
		getDesktopPane().add(chooseWindow);
		chooseWindow.center();
		chooseWindow.setVisible(false);
		
		initialized = true;
		
		playerPanel.setStatusMessage("Carico le informazioni sugli altri giocatori...");

		// Loading players
		for ( String playerID : astaStatus.getPlayers() ) {
			try {
				Connector.instance().getSquadra(playerID);
			} catch (ConnectionException e) {
				logger.error("Error in preloading of squadra: " + playerID, e);
			}
		}
		
		long elapsedTime = System.currentTimeMillis() - currentTime;
		long remainTime = (long) astaStatus.getRemainTime() - elapsedTime;
				
		// Choosing and its your turn
		if ( astaStatus.isInChoosePhase() ) {
			
			playerPanel.enableOffer(false);
			
			if ( myPlayerID.equals(astaStatus.getCurrentPlayerTurn())) {
				
				// Simulate going in choose status
				playerServer.setInChooseStatus(remainTime, astaStatus.getCurrentPlayerTurn());
				
				playerPanel.enableOffer(false);
				
				if ( remainTime > MIN_REMAIN_TIME  ) {
					chooseWindow.start(remainTime, this);
					setVisible(false);
				}
			}
			else {
				SquadraComm squadra;
				try {
					squadra = Connector.instance().getSquadra(astaStatus.getCurrentPlayerTurn());
					playerPanel.setStatusMessage(squadra.getAlias() + " sta scegliendo un calciatore...");
				} catch (ConnectionException e) {
					logger.error("Cannot get squadra: " + astaStatus.getCurrentPlayerTurn());
					playerPanel.setStatusMessage("Squadra " + astaStatus.getCurrentPlayerTurn() + " sta scegliendo un calciatore...");
				}
			}
		}
		
		if ( astaStatus.isInRaisePhase() ) {
			
			// Selected a giocatore
			playerServer.selectCalciatore(astaStatus.getCurrentCalciatore());
			
			playerPanel.enableOffer(! myPlayerID.equals(astaStatus.getCurrentOfferer())); 

			// Update GUI
			updateCurrentCalciatore(astaStatus.getCurrentOfferer(), astaStatus.getCurrentCalciatore(), astaStatus.getCurrentOffer());

			// Restart timer
			// playerPanel.stopTimer();
			
			if ( remainTime > MIN_REMAIN_TIME ) {

				playerPanel.startTimer(remainTime);

			}

		}
		
	}

	/**
	 * From GUI a player was selected
	 */
	public void selectedCalciatore(CalciatoreComm calciatore) {
		
		// Selected a giocatore
		playerServer.selectCalciatore(calciatore.getID_Calciatore());
		
		// Update message
		playerPanel.setStatusMessage("Ho scelto il calciatore: " + calciatore.getCalciatore() + " (" + calciatore.getRuolo() + ")");
		
		// Update GUI
		updateCurrentCalciatore(this.myPlayerID, calciatore.getID_Calciatore(), config.getMinimumCostCalciatore());

		setVisible(true);

	}

	/**
	 * Notification to choose a player
	 */
	public void chooseCalciatore(PlayerControl playerControl, String playerID) {
		
		if (! initialized)
			return;
		
		playerPanel.enableOffer(false);

		logger.debug("Choose " + playerID + ". I'm " + myPlayerID);

		if ( myPlayerID.equals(playerID)) {
			chooseWindow.start(config.getTimeoutTurnoGiocatore(), this);
			setVisible(false);
		}
		else {
			SquadraComm squadra;
			try {
				squadra = Connector.instance().getSquadra(playerID);
				playerPanel.setStatusMessage(squadra.getAlias() + " sta scegliendo un calciatore...");
			} catch (ConnectionException e) {
				logger.error("Cannot get squadra: " + playerID);
				playerPanel.setStatusMessage(playerID + " sta scegliendo un calciatore...");
			} 
		}

	}

	public void tooLateToChooseCalciatore(PlayerControl playerControl) {
		
		if (!initialized)
			return;

		// Update message
		playerPanel.setStatusMessage("Hai perduto il turno per la scelta del calciatore");

		setVisible(true);

		// Stop choosing
		chooseWindow.stop();

		logger.debug("Too late to choose");
		
	}

	public void calciatoreChosen(PlayerControl playerControl, String playerID, String calciatoreID) {

		if (!initialized)
			return;

		setVisible(true);
		
		playerPanel.enableOffer(! playerID.equals(myPlayerID) );		

		// Start timer for raising
		playerPanel.startTimer(config.getTimeoutRaising());
		
		// Update GUI
		updateCurrentCalciatore(playerID, calciatoreID, config.getMinimumCostCalciatore());

		logger.debug("Giocatore Chosen by " + playerID + ": " + calciatoreID);

	}

	public void playerOffered(PlayerControl playerControl, String playerID, String calciatoreID, double offer) {
		
		if (!initialized)
			return;

		playerPanel.enableOffer(! playerID.equals(myPlayerID) );		

		// Restart timer
		playerPanel.stopTimer();
		playerPanel.startTimer(config.getTimeoutRaising());
		
		// Update GUI
		updateCurrentCalciatore(playerID, calciatoreID, offer);

		logger.debug("Player " + playerID + " offered from " + playerID + ": " + offer + " for " + calciatoreID);
	}
	
	private void updateCurrentCalciatore(String playerID, String calciatoreID, double offer) {

		String calciatoreName = "Calciatore " + calciatoreID;
		String calciatoreRuolo = "-";
		String calciatoreClub = "-";
		CalciatoreComm calciatore;
		try {
			calciatore = Connector.getInfo().getCalciatore(calciatoreID);
			calciatoreName = calciatore.getCalciatore();
			calciatoreRuolo = calciatore.getRuolo_long();
			calciatoreClub = calciatore.getClub();
		} catch (ConnectionException e) {
			logger.error("Cannot get calciatore: " + calciatoreID);
		}

		playerPanel.setGiocatore(calciatoreName, calciatoreRuolo, calciatoreClub);

		String squadraName = "Squadra " + playerID;
		SquadraComm squadra;
		try {
			squadra = Connector.instance().getSquadra(playerID);
			squadraName = squadra.getAlias();
		} catch (ConnectionException e) {
			logger.error("Cannot get suqadra: " + playerID);
		}
		
		playerPanel.setOfferta(squadraName, "" + offer);
		
		currentCalciatore = calciatoreID;
		this.offer = offer;
		
		// Update message
		playerPanel.setStatusMessage(squadraName + " offre " + offer + " per " + calciatoreName + " (" + calciatoreRuolo + ")");
		
	}

	public void winCalciatore(PlayerControl playerControl, String calciatoreID, double offer) {

		if (!initialized)
			return;

		logger.debug("Won calciatore: " + calciatoreID + " with " + offer);

		// Won calciatore
		CalciatoreCost cc = new CalciatoreCost();
		cc.setCost(offer);
		cc.setID(calciatoreID);
		calciatoriWon.addCalciatore(cc);

		CalciatoreComm calciatore;
		try {
			calciatore = Connector.getInfo().getCalciatore(calciatoreID);
			playerPanel.setStatusMessage("Hai vinto il giocatore: " + calciatore.getCalciatore() + " (" + calciatore.getRuolo() + ")");
		} catch (ConnectionException e) {
			logger.error("Cannot get calciatore: " + calciatoreID);
			playerPanel.setStatusMessage("Hai vinto il giocatore: " + calciatoreID + " (?)");
		}
		
		playerPanel.stopTimer();
		playerStatus.winCalciatore(calciatoreID, offer);
		
	}

	public void lostCalciatore(PlayerControl playerControl, String winner, String calciatoreID, double offer) {

		if (!initialized)
			return;

		try {
			CalciatoreComm calciatore = Connector.getInfo().getCalciatore(calciatoreID);
			SquadraComm squadra = Connector.instance().getSquadra(winner);
			playerPanel.setStatusMessage(squadra.getAlias() + " ha vinto il giocatore: " + calciatore.getCalciatore() + " (" + calciatore.getRuolo() + ")");
		} catch (ConnectionException e) {
			logger.error("Cannot get info about calciatore", e);
		}

		logger.debug("Lost calciatore: " + calciatoreID + " for " + offer + " (winner: " + winner + ")");
		
		playerPanel.stopTimer();
				
	}

	public String getServerID() {
		return serverID;
	}

	public void receivedTextMessage(String from, String content) {
		chatPanel.receiveMessage(from, content);
	}

	public void sendTextMessage(String text) {
		playerServer.sendTextMessage(text);
	}

	public void pauseStarted(String from, double duration) {
		chooseWindow.stop();
		playerPanel.setEnabled(false);
		setVisible(true);
		try {
			SquadraComm squadra = Connector.instance().getSquadra(from);
			playerPanel.setStatusMessage(squadra.getAlias() + " ha messo in pausa l'asta per " + duration + " secondi");
		} catch (ConnectionException e) {
			playerPanel.setStatusMessage(from + " ha messo in pausa l'asta per " + duration + " secondi");
		}
		playerPanel.startTimer((long)( duration * 1000 ));
	}

	public void pauseEnded() {
		playerPanel.stopTimer();
		playerPanel.setEnabled(true);
		if ( playerServer.isInChooseStatus()) {
			setVisible(false);
			chooseWindow.setVisible(true);
			chooseWindow.start(config.getTimeoutTurnoGiocatore(), this);
		}
	}

	public void paused() {
		playerServer.startPause(30.0);
	}

}
