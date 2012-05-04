package it.fantapazz.asta.core.protocol;

import it.fantapazz.chat.Message;

public class MsgOffer extends Message {

	private static final long serialVersionUID = 1L;

	private double offer;
	
	private String calciatoreID;
	
	public MsgOffer() {}
	
	public MsgOffer(String source, double offer, String calciatoreID) {
		super(source);
		this.offer = offer;
		this.calciatoreID = calciatoreID;
	}

	public double getOffer() {
		return offer;
	}

	public void setOffer(double offer) {
		this.offer = offer;
	}

	public String getCalciatoreID() {
		return calciatoreID;
	}

	public void setCalciatoreID(String calciatoreID) {
		this.calciatoreID = calciatoreID;
	}

	@Override
	public String toString() {
		return "MsgOffer [offer=" + offer + ", calciatoreID=" + calciatoreID
				+ "]";
	}
	
}
