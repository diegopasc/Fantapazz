package it.fantapazz.asta.core.protocol;

import it.fantapazz.chat.Message;

public class MsgText extends Message {
	
	private static final long serialVersionUID = 1L;
	
	private String content;
	
	public MsgText() {}
	
	public MsgText(String content) {
		super();
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
