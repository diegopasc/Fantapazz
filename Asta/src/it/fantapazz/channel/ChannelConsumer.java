package it.fantapazz.channel;

import it.fantapazz.chat.Message;

public interface ChannelConsumer {
	
	public void channelOpened();

	public void receive(Message message);

	public void channelClosed();

}
