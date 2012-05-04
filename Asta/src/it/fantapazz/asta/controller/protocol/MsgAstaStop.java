package it.fantapazz.asta.controller.protocol;

import it.fantapazz.chat.Message;

public class MsgAstaStop extends Message {

	private static final long serialVersionUID = 1L;
	
	private String astaID;
	
	public MsgAstaStop() {}

	public MsgAstaStop(String astaID) {
		super();
		this.astaID = astaID;
	}

	public MsgAstaStop(String source, String astaID) {
		super(source);
		this.astaID = astaID;
	}

	public String getAstaID() {
		return astaID;
	}

	public void setAstaID(String astaID) {
		this.astaID = astaID;
	}
	
}
