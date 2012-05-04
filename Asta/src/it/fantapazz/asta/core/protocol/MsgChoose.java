package it.fantapazz.asta.core.protocol;

import it.fantapazz.chat.Message;

public class MsgChoose extends Message {

	private static final long serialVersionUID = 1L;
	
	private String playerID;
	
	public MsgChoose() {
	}
	
	public MsgChoose(String playerID) {
		super();
		this.playerID = playerID;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	@Override
	public String toString() {
		return "MsgChoose [playerID=" + playerID + ", getSource()="
				+ getSource() + "]";
	}

}
