package it.fantapazz.asta.controller.protocol;

import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.chat.Message;

/**
 * Update informations about an asta.
 * This message is used from server to send updated 
 * informations about one of the managed Asta.
 * 
 * @author Michele Mastrogiovanni
 */
public class MsgAstaUpdate extends Message {

	private static final long serialVersionUID = 1L;
	
	private AstaInfo astaInfo;
	
	public MsgAstaUpdate() {}
	
	public MsgAstaUpdate(AstaInfo astaInfo) {
		super();
		this.astaInfo = astaInfo;
	}

	public AstaInfo getAstaInfo() {
		return astaInfo;
	}

	public void setAstaInfo(AstaInfo astaInfo) {
		this.astaInfo = astaInfo;
	}
	
}
