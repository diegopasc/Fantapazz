package it.fantapazz.asta.core.protocol;

import it.fantapazz.asta.core.bean.HelloAstaServerBean;
import it.fantapazz.chat.Message;

public class MsgHelloServer extends Message {

	private static final long serialVersionUID = 1L;
	
	private HelloAstaServerBean content;
	
	public MsgHelloServer() {}

	public MsgHelloServer(String sourceID) {
		super(sourceID);
	}
	
	public MsgHelloServer(String sourceID, HelloAstaServerBean content) {
		super(sourceID);
		this.content = content;
	}
	
	public HelloAstaServerBean getContent() {
		return content;
	}

	public void setContent(HelloAstaServerBean content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MsgHelloServer [source=" + getSource()
				+ "]";
	}
	
}
