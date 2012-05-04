package it.fantapazz.asta.core.protocol;

import it.fantapazz.chat.Message;

/**
 * Send a pause message to server and others.
 * This message witll stop dispatch of other messages
 * at server (not text and pause messages) and will 
 * start a pause of a given period.
 * 
 * This message can be sent by a player that 
 * must choose a giocatore.
 * 
 * When pause terminated, again players receive
 * the MsgChoose message
 * 
 * @author Michele Mastrogiovanni
 */
public class MsgPause extends Message {

	private static final long serialVersionUID = 1L;
	
	private double duration;
	
	public MsgPause() {}
	
	public MsgPause(double duration) {
		super();
		this.duration = duration;
	}
	
	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getDuration() {
		return duration;
	}

}
