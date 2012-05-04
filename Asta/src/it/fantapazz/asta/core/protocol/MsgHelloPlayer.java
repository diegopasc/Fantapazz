package it.fantapazz.asta.core.protocol;

import it.fantapazz.chat.Message;

public class MsgHelloPlayer extends Message {

	private static final long serialVersionUID = 1L;
	
	public MsgHelloPlayer() {}
	
	public MsgHelloPlayer(String sourceID) {
		super(sourceID);
	}
	
	@Override
	public String toString() {
		return "MsgHelloClient [source=" + getSource()
				+ "]";
	}
	
}
