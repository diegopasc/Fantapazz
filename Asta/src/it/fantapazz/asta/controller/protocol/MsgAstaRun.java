package it.fantapazz.asta.controller.protocol;

import it.fantapazz.chat.Message;

/**
 * With this message an Asta is started.
 * 
 * @author Michele Mastrogiovanni
 */
public class MsgAstaRun extends Message {

	private static final long serialVersionUID = 1L;
	
	private String astaID;
	
	public MsgAstaRun() {}

	public MsgAstaRun(String astaID) {
		super();
		this.astaID = astaID;
	}

	public MsgAstaRun(String source, String astaID) {
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
