package it.fantapazz.channel;

import it.fantapazz.chat.Message;
import it.fantapazz.chat.Startable;

public interface Channel extends Startable {
	
	public void setChannelConsumer(ChannelConsumer consumer);
	
	public void send(Message message);

}
