package it.fantapazz.asta.core.protocol;

import it.fantapazz.chat.Message;

public class MsgSelect extends Message {

	private static final long serialVersionUID = 1L;
	
	private String calciatoreID;
	
	public MsgSelect() {}

	public MsgSelect(String source, String calciatoreID) {
		super(source);
		this.calciatoreID = calciatoreID;
	}

	public String getCalciatoreID() {
		return calciatoreID;
	}

	public void setCalciatoreID(String calciatoreID) {
		this.calciatoreID = calciatoreID;
	}

	@Override
	public String toString() {
		return "MsgSelect [calciatoreID=" + calciatoreID + "]";
	}
	
}
