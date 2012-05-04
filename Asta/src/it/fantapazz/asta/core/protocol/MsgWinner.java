package it.fantapazz.asta.core.protocol;

import it.fantapazz.chat.Message;

public class MsgWinner extends Message {

	private static final long serialVersionUID = 1L;

	private boolean youWin;
	
	private double value;

	private String calciatoreID;
	
	public MsgWinner() {}
	
	public MsgWinner(boolean youWin, String winner, double value, String calciatoreID) {
		super(winner);
		this.youWin = youWin;
		this.value = value;
		this.calciatoreID = calciatoreID;
	}

	public boolean isYouWin() {
		return youWin;
	}

	public void setYouWin(boolean youWin) {
		this.youWin = youWin;
	}

	public String getCalciatoreID() {
		return calciatoreID;
	}

	public void setCalciatoreID(String calciatoreID) {
		this.calciatoreID = calciatoreID;
	}
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MsgWinner [youWin=" + youWin + ", value=" + value
				+ ", calciatoreID=" + calciatoreID + "]";
	}
	
}
