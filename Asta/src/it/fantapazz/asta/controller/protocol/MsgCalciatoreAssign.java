package it.fantapazz.asta.controller.protocol;

import it.fantapazz.chat.Message;

/**
 * This message is sent from a running Asta to controller
 * to notify that a player was assigned at a given price.
 * 
 * @author Michele Mastrogiovanni
 */
public class MsgCalciatoreAssign extends Message {
	
	private static final long serialVersionUID = 1L;

	private String playerID;
	
	private String calciatoreID;
	
	private double money;
	
	public MsgCalciatoreAssign() {}

	public MsgCalciatoreAssign(String source, String playerID, String calciatoreID, double money) {
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
