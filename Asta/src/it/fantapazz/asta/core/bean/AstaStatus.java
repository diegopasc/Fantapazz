package it.fantapazz.asta.core.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Status of an Asta: this bean is used to send startup 
 * informations to a client and to save local informations of
 * running asta.
 * 
 * @author Michele Mastrogiovanni
 */
@JsonIgnoreProperties(value = {"inChoosePhase", "inRaisePhase"})
public class AstaStatus implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * List of partecipating to asta
	 */
	private List<String> players;
	
	/**
	 * List of remaining calciatori
	 */
	private List<String> remainCalciatori;

	/**
	 * ID of current calciatore chosen: if this field is null
	 * then the remain time indicate the time that remain to
	 * choose a calciatore  
	 */
	private String currentCalciatore;
	
	/**
	 * Turn of the player that is choosing (or must choose)
	 * the calciatore for the asta. This can be used to re-attach
	 * to a cacliatore choose turn.  
	 */
	private String currentPlayerTurn;
	
	/**
	 * Time remained to choose calciatore or to raise the offer
	 */
	private double remainTime;

	/**
	 * Current value of the offer for the calciatore selected.
	 * If currentCalciatore is null, than this value is undeterminated
	 */
	private double currentOffer;
	
	/**
	 * ID of player that submitted the last offer for current calciatore.
	 * If currentCalciatore is null than this value is indeterminated.
	 */
	private String currentOfferer;
	
	/**
	 * Set turn of player and remain time
	 */
	public void setTurnOf(String playerID, double remainTime) {
		this.currentCalciatore = null;
		this.currentPlayerTurn = playerID;
		this.remainTime = remainTime;
	}

	/**
	 * Set last offer
	 * 
	 * @param offererID ID of player offered last offer
	 * @param calciatoreID ID of calciatore selected
	 * @param offer Offer
	 * @param remainTime Time remaining to raise
	 */
	public void setLastOffer(String offererID, String calciatoreID, double offer) {
		this.currentOfferer = offererID;
		this.currentCalciatore = calciatoreID;
		this.currentOffer = offer;
	}

	/**
	 * @return Return true if it is in choosing time
	 */
	public boolean isInChoosePhase() {
		return ( currentCalciatore == null );
	}

	/**
	 * @return Return true is it is in the raising phase
	 */
	public boolean isInRaisePhase() {
		return ( currentCalciatore != null );
	}

	public String getCurrentCalciatore() {
		return currentCalciatore;
	}

	public void setCurrentCalciatore(String currentCalciatore) {
		this.currentCalciatore = currentCalciatore;
	}

	public double getCurrentOffer() {
		return currentOffer;
	}

	public void setCurrentOffer(double currentOffer) {
		this.currentOffer = currentOffer;
	}

	public double getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(double remainTime) {
		this.remainTime = remainTime;
	}
	
	public String getCurrentPlayerTurn() {
		return currentPlayerTurn;
	}

	public void setCurrentPlayerTurn(String currentPlayerTurn) {
		this.currentPlayerTurn = currentPlayerTurn;
	}

	public String getCurrentOfferer() {
		return currentOfferer;
	}

	public void setCurrentOfferer(String currentOfferer) {
		this.currentOfferer = currentOfferer;
	}
	
	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}
	
	public List<String> getRemainCalciatori() {
		return remainCalciatori;
	}

	public void setRemainCalciatori(List<String> remainCalciatori) {
		this.remainCalciatori = remainCalciatori;
	}

	@Override
	public String toString() {
		return "{ players=" + players + ", remainCalciatori="
				+ remainCalciatori + ", currentCalciatore=" + currentCalciatore
				+ ", currentPlayerTurn=" + currentPlayerTurn + ", remainTime="
				+ remainTime + ", currentOffer=" + currentOffer
				+ ", currentOfferer=" + currentOfferer + "}";
	}
	
}
