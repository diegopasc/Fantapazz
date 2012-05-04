package it.fantapazz.asta.controller;

import it.fantapazz.asta.AstaConfig;
import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.asta.controller.bean.HelloControllerBean;
import it.fantapazz.asta.controller.protocol.MsgAstaRun;
import it.fantapazz.asta.controller.protocol.MsgAstaStop;
import it.fantapazz.asta.controller.protocol.MsgAstaUpdate;
import it.fantapazz.asta.controller.protocol.MsgHelloAstaClient;
import it.fantapazz.asta.controller.protocol.MsgHelloAstaServer;
import it.fantapazz.asta.core.PlayerListener;
import it.fantapazz.asta.core.PlayerServer;
import it.fantapazz.chat.ChatFactory;
import it.fantapazz.chat.ClientI;
import it.fantapazz.chat.ClientListener;
import it.fantapazz.chat.Message;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;
import it.fantapazz.connector.bean.SquadraComm;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PlayerControlServer implements Runnable, ClientListener {

	private static final Log logger = LogFactory.getLog(PlayerControlServer.class);

	private ClientI client;
	
	private String ID;
	
	private String host;
	
	private int port;
	
	private AsteListener listener;
	
	private List<AstaInfo> astaEntries;
	
	private PlayerServer playerServer;
	
	private AstaConfig config;

	public PlayerControlServer(String ID, String host, int port, AsteListener listener) {
		this.ID = ID;
		this.host = host;
		this.port = port;
		this.listener = listener;
		astaEntries = new LinkedList<AstaInfo>();
	}

	public void run() {
		
		while ( true ) {

			try {
				this.client = ChatFactory.getClient(host, port, ID);
				logger.debug("Client connected");
			} catch (UnknownHostException e) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// e1.printStackTrace();
				}

				logger.error("Unknown host: " + host + ":" + port);
				continue;
			} catch (IOException e) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// e1.printStackTrace();
				}

				logger.error("Error in connection: retry");
				continue;
			}

			break;

		}
		
		// Start client
		this.client.start();

		// Register as listener
		this.client.addListener(this);

		// Present itself
		this.client.send(new MsgHelloAstaClient(ID));

	}

	public void receive(Message message) {
		
		System.out.println("RX: " + message);
		
		if ( message instanceof MsgHelloAstaServer ) {
			
			MsgHelloAstaServer hello = (MsgHelloAstaServer) message;
			
			HelloControllerBean content = hello.getContent();
			astaEntries.addAll(content.getInfos());

			logger.debug("Discovered aste: " + astaEntries);

			for ( AstaInfo asta : content.getInfos() ) {
				listener.updateAsta(asta);
			}
			
		}
		
		else if ( message instanceof MsgAstaUpdate ) {
			
			MsgAstaUpdate update = (MsgAstaUpdate) message;

			AstaInfo updatedAsta = update.getAstaInfo();

			logger.debug("Asta update: " + updatedAsta.getID() + ", status: " + updatedAsta.getStatus());
			
			AstaInfo localAsta = getEntryForID(updatedAsta.getID());
			
			// Update local data
			// TODO verify if it is better to set all fields
			localAsta.setStatus(updatedAsta.getStatus());
			localAsta.setRemainCalciatori(updatedAsta.getRemainCalciatori());
			
			listener.updateAsta(localAsta);
		}
				
	}
	
	public void startAsta(AstaInfo asta) {
		logger.debug("Try to start asta: " + asta.getID());
		this.client.send(new MsgAstaRun(asta.getID()));
	}

	public void stopAsta(AstaInfo asta) {
		logger.debug("Try to stop asta: " + asta.getID());
		this.client.send(new MsgAstaStop(asta.getID()));
	}
	
	public SquadraComm enterAsta(AstaInfo asta, PlayerListener listener) throws ConnectionException {
		
		SquadraComm squadra = Connector.instance().getSquadraForAstaAndPlayer(asta, ID);
		
		if ( squadra == null )
			throw new ConnectionException("Cannot get squadra for player: " + ID + " in asta: " + asta);
		
		if (playerServer != null)
			throw new ConnectionException("Player " + ID + " is already in an asta");
		
		try {
			config = new AstaConfig("/config-test.properties");
		} catch (IOException e) {
			throw new ConnectionException("Cannot load configuration", e);
		}
		
		playerServer = new PlayerServer(squadra.getSquadraID(), host, Integer.parseInt(asta.getPort()), listener);
		playerServer.setConfig(config);
		playerServer.start();
		
		return squadra;
	}
	
	public void exitAsta() throws ConnectionException {
		if (playerServer == null)
			throw new ConnectionException("Player is not in any asta");
		playerServer.stop();
		playerServer = null;
	}
	
	public PlayerServer getPlayerServer() {
		return playerServer;
	}

	private AstaInfo getEntryForID(String ID) {
		for (AstaInfo entry : astaEntries)
			if ( entry.getID().equals(ID))
				return entry;
		return null;
	}
	
	public String getSquadraID() {
		return ID;
	}
	
	public String getPlayerID() {
		if ( playerServer == null )
			return null;
		return playerServer.getID();
	}
	
	public AstaConfig getConfig() {
		return config;
	}

}
