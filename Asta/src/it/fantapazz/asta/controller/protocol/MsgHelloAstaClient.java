package it.fantapazz.asta.controller.protocol;

import it.fantapazz.chat.Message;

public class MsgHelloAstaClient extends Message {

	private static final long serialVersionUID = 1L;
	
	public MsgHelloAstaClient() {}
	
	public MsgHelloAstaClient(String sourceID) {
		super(sourceID);
	}
	
	@Override
	public String toString() {
		return "MsgHelloClient [source=" + getSource()
				+ "]";
	}
	
}
