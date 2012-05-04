package it.fantapazz.asta.controller.protocol;

import it.fantapazz.chat.Message;

/**
 * Remove a giocatore from a player: money specified must be
 * re-assigned to the current player.
 * 
 * @author Michele Mastrogiovanni
 */
public class MsgCalciatoreRemove extends Message {

	private static final long serialVersionUID = 1L;

	private String playerID;
	
	private String calciatoreID;
	
	private double money;
	
	public MsgCalciatoreRemove() {}

	public MsgCalciatoreRemove(String source, String playerID, String calciatoreID, double money) {
		super(source);
		this.playerID = playerID;
		this.calciatoreID = calciatoreID;
		this.money = money;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public String getCalciatoreID() {
		return calciatoreID;
	}

	public void setCalciatoreID(String calciatoreID) {
		this.calciatoreID = calciatoreID;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

}
