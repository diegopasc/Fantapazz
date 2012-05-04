package it.fantapazz.chat.impl;

import it.fantapazz.channel.Channel;
import it.fantapazz.channel.ChannelConsumer;
import it.fantapazz.chat.ClientI;
import it.fantapazz.chat.ClientListener;
import it.fantapazz.chat.Message;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Client implements ClientI, ChannelConsumer /*, Runnable */ {
	
	private static final Log logger = LogFactory.getLog(Client.class);
	
	private String ID;
	
	protected Thread listener;
	
	private Channel channel;
	
	private List<ClientListener> clientListeners = new LinkedList<ClientListener>();

	public Client(Channel channel, String ID) throws UnknownHostException, IOException {
		this.ID = ID;
		clientListeners = new LinkedList<ClientListener>();
		this.channel = channel;
		channel.setChannelConsumer(this);
		logger.debug("Client connected successfully");
	}
	
	public void addListener(ClientListener clientListener) {
		clientListeners.add(clientListener);
	}
	
	public void receive(final Message message) {
		new Thread() {
			public void run() {
				for ( ClientListener clientListener : clientListeners ) {
					clientListener.receive(message);
				}
			}
		}.start();
	}
	
	public void send(Message message) {
		message.setSource(ID);
		channel.send(message);
	}
	
	public void start() {
		channel.start();
	}

	public void stop() {
		channel.stop();
	}

	public void channelOpened() {
		logger.debug("Client connected");
	}

	public void channelClosed() {
		logger.debug("Client disconnected");
	}
			
}
