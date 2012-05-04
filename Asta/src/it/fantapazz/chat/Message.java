package it.fantapazz.chat;

import java.io.Serializable;

/**
 * Simple chat message.
 *  
 * @author Michele Mastrogiovanni
 */
public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String source;
	
	public Message() {
		this(null);
	}
	
	public Message(String source) {
		super();
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@Override
	public String toString() {
		return "Message [type=" + getClass() + ", source=" + source + "]";
	}
	
}
