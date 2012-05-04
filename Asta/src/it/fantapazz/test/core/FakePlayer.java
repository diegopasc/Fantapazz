package it.fantapazz.test.core;

import it.fantapazz.asta.AstaConfig;
import it.fantapazz.asta.core.PlayerControl;
import it.fantapazz.asta.core.PlayerListener;
import it.fantapazz.asta.core.PlayerServer;
import it.fantapazz.asta.core.bean.HelloAstaServerBean;
import it.fantapazz.asta.core.bean.PlayerStatus;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FakePlayer implements PlayerListener {

	private static final Log logger = LogFactory.getLog(FakePlayer.class);

	private PlayerStatus playerStatus;

	private String ID;
	
	private Thread chooseGiocatore;
	
	private Thread offerThread;
	
	private AstaConfig config;
	
	private List<String> calciatoriToSelect;
	
	public static void main(String[] args) throws IOException {
		if ( args.length != 3 ) {
			System.out.println("usage: java FakePlayer <ID> <Host> <Port>\n\n");
			System.exit(0);
		}
		FakePlayer player = new FakePlayer();
		int port = Integer.parseInt(args[2]);
		PlayerServer server = new PlayerServer(args[0], args[1], port, player);
		server.setConfig(new AstaConfig("/config-test.properties"));
		Thread thread = new Thread(server);
		thread.start();
	}
	
	public FakePlayer() throws IOException {
		super();
		this.config = new AstaConfig("/config-test.properties");
	}

	public void yourID(PlayerControl playerControl, String ID) {
		logger.debug("My ID is: " + ID);
		this.ID = ID;
	}

	public void chooseCalciatore(final PlayerControl playerControl, String playerID) {

		logger.debug("Choose calciatore: " + playerID);

		if (!ID.equals(playerID))
			return;
			
		chooseGiocatore = new Thread() {
			@Override
			public void run() {

				logger.debug("Begin to choose");

				try {
					Thread.sleep(config.getTimeoutTurnoGiocatore() / 2);
				} catch (InterruptedException e) {
					// e.printStackTrace();
					return;
				}
				String calciatore = calciatoriToSelect.get(0); 
				logger.debug("I select calciatore: " + calciatore);
				playerControl.selectCalciatore(calciatore);
			}
		};
		chooseGiocatore.start();
	}

	public void tooLateToChooseCalciatore(PlayerControl playerControl) {
		if ( chooseGiocatore != null )
			chooseGiocatore.interrupt();
		logger.debug("Too late to choose");
	}

	public void calciatoreChosen(PlayerControl playerControl, String playerID, String calciatoreID) {
		logger.debug("Giocatore Chosen by " + playerID + ": " + calciatoreID);
		if ( offerThread != null )
			offerThread.interrupt();
		if (playerID.equals(ID) )
			return;
		offer(playerControl, calciatoreID, 1);
	}

	public void playerOffered(PlayerControl playerControl, String playerID, final String calciatoreID, final double offer) {
		logger.debug("Player " + playerID + " offered from " + playerID + ": " + offer + " for " + calciatoreID);
		if ( offerThread != null )
			offerThread.interrupt();
		if (playerID.equals(ID) )
			return;
		offer(playerControl, calciatoreID, offer);
	}
	
	private void offer(final PlayerControl playerControl, final String calciatoreID, final double offer) {
		if ( offer + 1 > playerStatus.getMoney() )
			return;
		offerThread = new Thread() {
			public void run() {
				try {
					Thread.sleep((long) (new Random().nextDouble() * config.getTimeoutRaising() * 2));
				} catch (InterruptedException e) {
					// e.printStackTrace();
					return;
				}
				logger.debug("I (" + FakePlayer.this.ID + ") offering " + (offer + 1.0) + " for " + calciatoreID);
				playerControl.makeOffer(calciatoreID, offer + 1.0);
			};
		};
		offerThread.start();
	}

	public void winCalciatore(PlayerControl playerControl, String calciatoreID, double offer) {
		if ( offerThread != null )
			offerThread.interrupt();
		logger.debug("Won calciatore: " + calciatoreID + " with " + offer);
		playerStatus.winCalciatore(calciatoreID, offer);
		calciatoriToSelect.remove(calciatoreID);
	}

	public void lostCalciatore(PlayerControl playerControl, String winner, String calciatoreID, double offer) {
		if ( offerThread != null )
			offerThread.interrupt();
		logger.debug("Lost calciatore: " + calciatoreID + " for " + offer + " (winner: " + winner + ")");
		calciatoriToSelect.remove(calciatoreID);
	}

	public void receivedHello(Object content) {
		HelloAstaServerBean hello = (HelloAstaServerBean) content;
		logger.debug("Received hello: " + content);
		playerStatus = hello.getPlayerStatus();
		calciatoriToSelect = hello.getAstaStatus().getRemainCalciatori();
	}

	public void receivedTextMessage(String from, String content) {
		System.out.println("Received text (" + from + "): " + content);
	}

	public void pauseStarted(String from, double duration) {
		System.out.println("Pause started");
	}

	public void pauseEnded() {
		System.out.println("Pause ended");
	}

}
