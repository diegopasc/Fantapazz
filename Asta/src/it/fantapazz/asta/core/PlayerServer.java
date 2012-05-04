package it.fantapazz.asta.core;

import it.fantapazz.ConfigSettings;
import it.fantapazz.asta.AstaConfig;
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
import it.fantapazz.chat.ChatFactory;
import it.fantapazz.chat.ClientI;
import it.fantapazz.chat.ClientListener;
import it.fantapazz.chat.Message;
import it.fantapazz.chat.Startable;
import it.fantapazz.utility.StatsCommunication;
import it.fantapazz.utility.Timeout;
import it.fantapazz.utility.Timer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PlayerServer implements Runnable, PlayerControl, Timeout, ClientListener, Startable {
	
	private static final Log logger = LogFactory.getLog(PlayerServer.class);
	
	enum Status {
		IDLE,			// Client is idle
		CHOOSE,			// Player is choosing a calciatore
		OFFER_OTHER,	// Others must raise your offer
		OFFER_YOU		// You can raise the offer
	};
	
	private String lastChooseID;
	
	private long lastChooseTimeout;
	
	private boolean paused;
	
	private String ID;
	
	private boolean initialized;
	
	private Status status;

	private AstaConfig config;
	
	private PlayerListener listener;
		
	private String currentCalciatore;

	private double currentOffer;
	
	private String host;
	
	private int port;
	
	private ClientI client;
	
	// Timeout manager
	private Timer timer;
	
	private Thread runningThread;
	
	public PlayerServer(String ID, String host, int port, PlayerListener listener) {
		super();
		this.host = host;
		this.port = port;
		this.ID = ID;
		this.listener = listener;
		initialized = false;
	}

	public void run() {
		
		currentCalciatore = null;
		currentOffer = 0.0;
		
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
		this.client.send(new MsgHelloPlayer(ID));
		
	}
	
	public boolean isInChooseStatus() {
		return status == Status.CHOOSE; 
	}
	
	public void setInChooseStatus(long timeout, String playerID) {
		if ( playerID.equals(ID)) {
			status = Status.CHOOSE;
			lastChooseID = playerID;
			lastChooseTimeout = timeout;
			startTimer(timeout);
		}
		listener.chooseCalciatore(this, playerID);		
	}
		
	public void timeout(Timer timer) {
		
		stopTimer();
		
		if ( paused ) {
			paused = false;
			listener.pauseEnded();
			if ( status == Status.CHOOSE) {
				setInChooseStatus(lastChooseTimeout, lastChooseID);				
			}
			return;
		}
		
		if ( status == Status.CHOOSE) {
			lastChooseID = null;
			lastChooseTimeout = 0;
			status = Status.IDLE;
			listener.tooLateToChooseCalciatore(this);
		}
		
	}

	public void receive(Message message) {
		
		if ( message instanceof MsgPing ) {
			long now = new Date().getTime();
			long before = ((MsgPing)message).getTimestamp();
			if ( ConfigSettings.instance().isActivateStats()) {
				StatsCommunication.instance().addPingTime((double)(now - before) / 1000.0);
			}
		}
		
		// Hello message (the first message, the server)
		if ( message instanceof MsgHelloServer ) {
			MsgHelloServer hello = (MsgHelloServer) message;
			listener.yourID(this, ID);
			listener.receivedHello(hello.getContent());
			initialized = true;
			
			// Ping conenction
			if ( ConfigSettings.instance().isEnablePing() ) {
				logger.debug("Enable PING feature");
				new Thread() {
					public void run() {
						while ( true ) {
							client.send(new MsgPing(new Date()));
							try {
								Thread.sleep((long)(ConfigSettings.instance().getPingTime() * 1000));
							} catch (InterruptedException e) {
							}
						}
					};
				}.start();
			}
			
			return;
		}
		
		// Discard if not received hello
		if ( ! initialized )
			return;

		// Text message (always forwarded)
		if ( message instanceof MsgText ) {
			MsgText text = (MsgText) message;
			listener.receivedTextMessage(text.getSource(), text.getContent());
			return;
		}

		// Pause message
		if ( message instanceof MsgPause ) {
			MsgPause pause = (MsgPause) message;
			pause(pause.getSource(), pause.getDuration());
			return;
		}

		// Stop pause
		if ( message instanceof MsgPauseStop ) {
			if ( paused ) {
				stopTimer();
				paused = false;
				listener.pauseEnded();
			}
			return;
		}
		
		// Discard messages if it is in pause
		if ( paused )
			return;

		if ( message instanceof MsgChoose ) {
			MsgChoose choose = (MsgChoose) message;
			setInChooseStatus(config.getTimeoutTurnoGiocatore(), choose.getPlayerID());
			return;
		}
		
		if ( message instanceof MsgSelect ) {
			MsgSelect select = (MsgSelect) message;
			// logger.debug("Received " + select + " from " + select.getSource() + " my ID: " + ID);
			if ( select.getSource().equals(ID) )
				status = Status.OFFER_OTHER;
			else
				status = Status.OFFER_YOU;
			currentCalciatore = select.getCalciatoreID();
			listener.calciatoreChosen(this, select.getSource(), select.getCalciatoreID());
			return;
		}
		
		if ( message instanceof MsgOffer ) {
			MsgOffer offer = (MsgOffer) message;
			if ( offer.getSource().equals(ID) )
				status = Status.OFFER_OTHER;
			else
				status = Status.OFFER_YOU;
			listener.playerOffered(this, offer.getSource(), offer.getCalciatoreID(), offer.getOffer());
			return;
		}
		
		if ( message instanceof MsgWinner ) {
			MsgWinner winner = (MsgWinner) message;
			if ( winner.isYouWin() ) {
				listener.winCalciatore(this, winner.getCalciatoreID(), winner.getValue());
			}
			else {
				listener.lostCalciatore(this, winner.getSource(), winner.getCalciatoreID(), winner.getValue());
			}
			currentCalciatore = null;
			currentOffer = 0.0;
			return;
		}
				
	}

	private void startTimer(long delay) {
		if ( delay <= 0 )
			return;
		if ( timer == null ) {
			timer = new Timer(this, delay);
			// logger.debug("Started timer " + delay);
		}
	}

	private void stopTimer() {
		if ( timer != null ) {
			timer.stop();
			timer = null;
			// logger.debug("Stopped timer");
		}
	}

	public void makeOffer(String calciatoreID, double offer) {
		
		if ( status != Status.OFFER_YOU )
			return;
		
		if ( offer <= currentOffer )
			return;

		if (!calciatoreID.equals(currentCalciatore))
			return;

		stopTimer();
		client.send(new MsgOffer(ID, offer, calciatoreID));
		
	}

	public void selectCalciatore(String calciatoreID) {
		
		if ( ! initialized )
			return;
		
		if ( status != Status.CHOOSE )
			return;

		stopTimer();
		client.send(new MsgSelect(ID, calciatoreID));
		
	}
	
	public String getID() {
		return ID;
	}

	public AstaConfig getConfig() {
		return config;
	}

	public void setConfig(AstaConfig config) {
		this.config = config;
	}

	public void start() {
		if (runningThread != null)
			return;
		runningThread = new Thread(this);
		runningThread.start();
	}

	public void stop() {
		stopTimer();
		client.stop();
		client = null;
		runningThread.interrupt();
		runningThread = null;
	}

	public void sendTextMessage(String content) {
		MsgText text = new MsgText(content);
		client.send(text);
	}
	
	public void startPause(double duration) {
		MsgPause pause = new MsgPause(duration);
		client.send(pause);
	}

	public void pause(String from, double duration) {
		// Stop any timer
		stopTimer();
		// Go in paused state
		paused = false;
		// Start pause
		long pauseTime = (long)( duration * 1000 );
		startTimer(pauseTime);
		paused = true;
		// Notify pause start
		listener.pauseStarted(from, duration);
	}
		
}
