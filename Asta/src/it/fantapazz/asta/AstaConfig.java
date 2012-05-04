package it.fantapazz.asta;

import java.io.IOException;
import java.util.Properties;

public class AstaConfig {

	private double minimumCostCalciatore;

	private long timeoutTurnoGiocatore;

	private long timeoutRaising;
	
	private long divisor;
	
	private double startMoney;
	
	public static final String PROP_MIN_COST = "min.cost.calciatore";
	
	public static final String PROP_TIMEOUT_RAISING = "timeout.raising";
	
	public static final String PROP_TIMEOUT_TURN = "timeout.player.turn";

	public static final String PROP_DEBUG_DIVISOR = "debug.divisor";

	public AstaConfig() {
		minimumCostCalciatore = 1;
		timeoutTurnoGiocatore = 30 * 1000;
		timeoutRaising = 10 * 1000;
		divisor = 0;
		startMoney = 300;

		System.out.println("CONFIG: " + this);
	}
	
	public AstaConfig(String fileName) throws IOException {
		this();
		
		Properties props = new Properties();
		props.load(getClass().getResourceAsStream(fileName));
		minimumCostCalciatore = Long.parseLong(props.getProperty(PROP_MIN_COST));
		timeoutTurnoGiocatore = Long.parseLong(props.getProperty(PROP_TIMEOUT_TURN));
		timeoutRaising = Long.parseLong(props.getProperty(PROP_TIMEOUT_RAISING));
		
		try {
			divisor = Long.parseLong(props.getProperty(PROP_DEBUG_DIVISOR));
		}
		catch (Throwable t) {
		}
		
		if ( divisor > 0 ) {
			timeoutTurnoGiocatore /= divisor;
			timeoutRaising /= divisor;
		}
		
		System.out.println("CONFIG: " + this);
	}

	public double getMinimumCostCalciatore() {
		return minimumCostCalciatore;
	}

	public void setMinimumCostCalciatore(double minimumCostCalciatore) {
		this.minimumCostCalciatore = minimumCostCalciatore;
	}

	public long getTimeoutTurnoGiocatore() {
		return timeoutTurnoGiocatore;
	}

	public void setTimeoutTurnoGiocatore(long timeoutTurnoGiocatore) {
		this.timeoutTurnoGiocatore = timeoutTurnoGiocatore;
	}

	public long getTimeoutRaising() {
		return timeoutRaising;
	}

	public void setTimeoutRaising(long timeoutRaising) {
		this.timeoutRaising = timeoutRaising;
	}
	
	public double getStartMoney() {
		return startMoney;
	}

	public void setStartMoney(double startMoney) {
		this.startMoney = startMoney;
	}

	@Override
	public String toString() {
		return "AstaConfig [minimumCostCalciatore=" + minimumCostCalciatore
				+ ", timeoutTurnoGiocatore=" + timeoutTurnoGiocatore
				+ ", timeoutRaising=" + timeoutRaising + "]";
	}
	
}
