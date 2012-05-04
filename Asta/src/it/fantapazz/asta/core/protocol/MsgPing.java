package it.fantapazz.asta.core.protocol;

import it.fantapazz.chat.Message;

import java.util.Date;

public class MsgPing extends Message {

	private static final long serialVersionUID = 1L;
	
	private long timestamp;
	
	public MsgPing() {}
	
	public MsgPing(Date date) {
		timestamp = date.getTime();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
