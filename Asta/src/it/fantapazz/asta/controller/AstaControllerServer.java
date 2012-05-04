package it.fantapazz.asta.controller;

import it.fantapazz.ConfigSettings;
import it.fantapazz.asta.AstaConfig;
import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.asta.controller.bean.HelloControllerBean;
import it.fantapazz.asta.controller.protocol.MsgAstaRun;
import it.fantapazz.asta.controller.protocol.MsgAstaStop;
import it.fantapazz.asta.controller.protocol.MsgAstaUpdate;
import it.fantapazz.asta.controller.protocol.MsgHelloAstaClient;
import it.fantapazz.asta.controller.protocol.MsgHelloAstaServer;
import it.fantapazz.asta.core.AstaServer;
import it.fantapazz.chat.ClientInfo;
import it.fantapazz.chat.Message;
import it.fantapazz.chat.ServerI;
import it.fantapazz.chat.ServerListener;
import it.fantapazz.chat.Startable;
import it.fantapazz.connector.AstaControllerI;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;
import it.fantapazz.utility.RoundIterator;
import it.fantapazz.websocket.AstaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jwebsocket.api.WebSocketEngine;
import org.jwebsocket.api.WebSocketPlugIn;
import org.jwebsocket.factory.JWebSocketFactory;
import org.jwebsocket.plugins.TokenPlugInChain;
import org.jwebsocket.server.TokenServer;

/**
 * Asta controller is a server that manintain and 
 * update information about running Aste.
 * 
 * @author Michele Mastrogiovanni
 */
public class AstaControllerServer implements Runnable, Startable, ServerListener {

	private static final Log logger = LogFactory.getLog(AstaControllerServer.class);
	
	public static final int CONTROLLER_PORT = 5555;

	public static final int SERVERS_START_PORT = 5556;

	// Server to control Aste system 
	private ServerI server;
	
	private List<AstaInfo> aste;

	// Info on connected clients
	private Map<String, ClientInfo> playerIDToClientID;
	private Map<String, String> clientIDToPlayerID;

	// Thread checker: it verifies if an asta is terminated
	private Thread checker;

	public AstaControllerServer(ServerI server) throws IOException, ConnectionException {
				
		// Aste
		aste = new LinkedList<AstaInfo>();

		this.server = server;

		playerIDToClientID = new HashMap<String, ClientInfo>();
		clientIDToPlayerID = new HashMap<String, String>();
		
		// Checker for terminated aste
		if ( ConfigSettings.instance().isTerminateAsta()) {
			checker = new Thread(this);
		}

		// Main server to accept connection
		server.addListener(this);

		// Load aste from fantapazz
		loadFantapazzAste();

	}
	
	/**
	 * Load aste and their status as known on Fantapazz site
	 * 
	 * @throws IOException
	 * @throws ConnectionException 
	 */
	private void loadFantapazzAste() throws ConnectionException, IOException {
		
		// Start connection to aste
		AstaControllerI controller = Connector.getAstaController();

		// Start already running aste
		for ( AstaInfo astaInfo : controller.getAste() ) {
			
			logger.debug("Found asta " + astaInfo.getID() + " (Lega: " + astaInfo.getIDLega() + ") status: " + astaInfo.getStatus());
			
			// Save asta
			aste.add(astaInfo);
			
			// Create asta
			restartAsta(astaInfo);
			
		}
		
	}
	
	/**
	 * Start controller
	 */
	public void start() {
		if ( ConfigSettings.instance().isTerminateAsta() ) {
			checker.start();
		}
		server.start();
	}

	/**
	 * Stop controller
	 */
	public void stop() {
		if ( ConfigSettings.instance().isTerminateAsta()) {
			checker.interrupt();
		}
		server.stop();
	}

	/**
	 * Check if an asta server is terminated and, remove it from list
	 * and update asta data 
	 */
	public void run() {
		
		while ( true ) {
						
			AstaInfo found = null;
			
			for (AstaInfo asta : aste) {
				if (asta.getStatus() != AstaInfo.Status.RUNNING)
					continue;
				AstaServer server = asta.getServer();
				if ( server.isTerminated() ) {
					found = asta;
					break;
				}				
			}
			
			if ( found != null ) {
				
				// Update data on Fantapazz
				AstaControllerI controller = Connector.getAstaController();
				try {
					
					controller.setAstaStatus(found.getID(), AstaInfo.Status.TERMINATED);
					found.setStatus(AstaInfo.Status.TERMINATED);
					logger.debug("Asta terminated: " + found);

					// Notify all the new asta is up or not
					server.broadcast(new MsgAstaUpdate(found));

				} catch (ConnectionException e) {
					logger.error("Cannot change status on server: retry later", e);
				}

			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	private void stopAsta(String ID) {
		
		AstaInfo asta = getAstaWithID(ID);

		if ( asta == null ) {
			logger.debug("Asta not found: " + ID);
			return;
		}
		
		if (asta.getStatus() != AstaInfo.Status.RUNNING) {
			logger.debug("Cannot stop asta in status: " + asta.getStatus());
			return;
		}
		
		// Start asta
		asta.getServer().stop();

		// Set asta status 
		try {
			Connector.getAstaController().setAstaStatus(asta.getID(), AstaInfo.Status.NOT_STARTED);
			asta.setStatus(AstaInfo.Status.NOT_STARTED);
		} catch (ConnectionException e) {
			logger.debug("Cannot stop asta on Fantapazz: ignore command (restart)");
			asta.getServer().start();
			return;
		}
					
		logger.debug("Asta " + ID + " stopped");	
		
		// Notify all the new asta is up or not
		server.broadcast(new MsgAstaUpdate(asta));
		
	}

	/**
	 * Start an asta that is NOT_STARTED and save its status on Fantapazz
	 * 
	 * @param ID ID of asta to start
	 */
	private void runAsta(String ID) {
		
		AstaInfo asta = getAstaWithID(ID);

		if ( asta == null ) {
			logger.debug("Asta not found: " + ID);
			return;
		}
		
		if (asta.getStatus() != AstaInfo.Status.NOT_STARTED) {
			logger.debug("Cannot run asta in status: " + asta.getStatus());
			return;
		}

		// Start asta
		asta.getServer().start();

		// Set asta status 
		try {
			Connector.getAstaController().setAstaStatus(asta.getID(), AstaInfo.Status.RUNNING);
			asta.setStatus(AstaInfo.Status.RUNNING);
		} catch (ConnectionException e) {
			logger.debug("Cannot start asta on Fantapazz: ignore command (stop)");
			asta.getServer().stop();
			return;
		}
					
		logger.debug("Asta " + ID + " started on port: " + asta.getPort());

		// Notify all the new asta is up or not
		server.broadcast(new MsgAstaUpdate(asta));

	}
	
	private void restartAsta(AstaInfo astaInfo) throws IOException, ConnectionException {
		
		// Create entry for an asta server
		createAstaServerEntry(astaInfo);
		
		// If it was running re-start it
		if ( astaInfo.getStatus() == AstaInfo.Status.RUNNING ) {
			
			astaInfo.getServer().start();
			
			logger.debug("Asta " + astaInfo.getID() + " (Lega: " + astaInfo.getIDLega() + ") started on port: " + astaInfo.getPort());
			
		}

		// Notify all the new asta is up or not
		server.broadcast(new MsgAstaUpdate(astaInfo));
				
	}
	
	private void createAstaServerEntry(AstaInfo astaInfo) throws IOException, ConnectionException {
		
		// Look for first free port
		int port = firstFreePort();
		
		AstaConfig config = new AstaConfig("/config-test.properties");

		// Server allocated
		// ServerI server = ChatFactory.getServer(port);

		// if not loaded by jWebSocket.xml config file...
	    TokenServer lServer = (TokenServer) JWebSocketFactory.getServer("ts0");
	    WebSocketEngine engine = JWebSocketFactory.getEngine();
	    TokenPlugInChain chain = lServer.getPlugInChain();
	    AstaPlugin plugin = new AstaPlugin("" + port, astaInfo.getID());
	    chain.addPlugIn(plugin);
	    plugin.engineStarted(engine);
	    ServerI server = plugin;
		
		AstaServer asta = new AstaServer(astaInfo.getID(), server);
		asta.setTurnSelector(new RoundIterator<String>());
		asta.setConfig(config);

		// Asta not yet started
		astaInfo.setPort("" + port);
		astaInfo.setServer(asta);

	}

	public void receive(ClientInfo from, Message message) {

		if ( message instanceof MsgHelloAstaClient ) {

			MsgHelloAstaClient hello = (MsgHelloAstaClient) message;
			
			logger.debug("Received Hello: " + hello);

			// Save client informations
			playerIDToClientID.put(hello.getSource(), from);
			clientIDToPlayerID.put(from.getID(), hello.getSource());

			HelloControllerBean content = new HelloControllerBean(aste);

			logger.debug("Received hello from: " + hello.getSource());
			
			// Send hello message
			server.sendTo(from, new MsgHelloAstaServer(hello.getSource(), content));

		}
		else if ( message instanceof MsgAstaRun ) {
			
			// Run existing asta
			MsgAstaRun run = (MsgAstaRun) message;
			logger.debug("Starting asta: " + run.getAstaID());
			runAsta(run.getAstaID());
			
		}
		else if ( message instanceof MsgAstaStop ) {
			
			// Run existing asta
			MsgAstaStop stop = (MsgAstaStop) message;
			logger.debug("Stopping asta: " + stop.getAstaID());
			stopAsta(stop.getAstaID());
			
		}

	}

	private AstaInfo getEntryForPort(int port) {
		for (AstaInfo entry : aste) {
			if ( entry.getPort() == null )
				continue;
			if ( entry.getPort().equals("" + port) )
				return entry;
		}
		return null;
	}

	private Integer firstFreePort() {
		int port = 0;
		for ( port = SERVERS_START_PORT; getEntryForPort(port) != null; port ++ );
		return port;
	}

	public ClientInfo getClientIDFromPlayerID(String playerID) {
		return playerIDToClientID.get(playerID);	
	}

	public String getPlayerIDFromClientID(String clientID) {
		return clientIDToPlayerID.get(clientID);	
	}

	public void clientConnected(ClientInfo client) {
	}

	public void clientEvicted(ClientInfo client) {
	}
	
	private AstaInfo getAstaWithID(String ID) {
		for (AstaInfo asta : aste) {
			if (asta.getID().equals(ID)) {
				return asta;
			}
		}
		return null;
	}

}
