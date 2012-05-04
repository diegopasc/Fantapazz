package it.fantapazz.asta.controller.protocol;

import it.fantapazz.asta.controller.bean.HelloControllerBean;
import it.fantapazz.chat.Message;

public class MsgHelloAstaServer extends Message {

	private static final long serialVersionUID = 1L;
	
	private HelloControllerBean content;

	public MsgHelloAstaServer() {}
	
	public MsgHelloAstaServer(String sourceID) {
		super(sourceID);
	}

	public MsgHelloAstaServer(String sourceID, HelloControllerBean content) {
		this(sourceID);
		this.content = content;
	}
	
	public HelloControllerBean getContent() {
		return content;
	}

	public void setContent(HelloControllerBean content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MsgHelloClient [content=" + content + ", source=" + getSource()
				+ "]";
	}
	
}
