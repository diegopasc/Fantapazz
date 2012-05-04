package it.fantapazz.chat.impl;


import it.fantapazz.channel.Channel;
import it.fantapazz.channel.TCPChannel;
import it.fantapazz.chat.ClientInfo;
import it.fantapazz.chat.Message;
import it.fantapazz.chat.ServerI;
import it.fantapazz.chat.ServerListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Server implements Runnable, ServerI {

	private static final Log logger = LogFactory.getLog(Server.class);

	private static int clientCounter = 0;

	private ServerSocket server;

	private Stack<MessageAndClientInfo> items;
	
	private List<ServerListener> serverListeners;

	private Map<String, ClientInfo> infos;

	private Map<String, Channel> channels;

	private Thread threadReceiver;

	private Thread threadServer;

	private int port;

	public Server(int port) throws IOException {
		this.port = port;
		infos = new HashMap<String, ClientInfo>();
		serverListeners = new LinkedList<ServerListener>();
		channels = new HashMap<String, Channel>();
		items = new Stack<MessageAndClientInfo>();
	}

	public void addListener(ServerListener serverListener) {
		serverListeners.add(serverListener);
	}

	public void notifyListeners(ClientInfo info, Message message) {
		for ( ServerListener serverListener : serverListeners ) {
			serverListener.receive(info, message);
		}
	}

	public void pushMessage(ClientInfo info, Message message) {
		synchronized (items) {
			items.push(new MessageAndClientInfo(message, info));
			items.notifyAll();
		}
	}

	public MessageAndClientInfo popMessage() throws InterruptedException {
		synchronized (items) {
			while (items.isEmpty()) {
				items.wait();
			}
			if ( ! items.isEmpty() )
				return items.pop();
		}
		return null;
	}

	public void evict(String ID) {
		Channel channel = channels.get(ID);
		channel.stop();
		ClientInfo info = null;
		synchronized (channels) {
		// synchronized (dispatchers) {
			info = infos.get(ID); 
			infos.remove(ID);
			channels.remove(channel);
			logger.debug("Evicted client (ID: " + ID + "): ");
			logger.debug("Remained " + channels.size() + " clients");
		}
		// Notify connection
		if ( info != null ) {
			for ( ServerListener serverListener : serverListeners ) {
				serverListener.clientEvicted(info);
			}
		}
	}

	public void kickClient(ClientInfo clientInfo) {
		evict(clientInfo.getID());
	}

	public List<ClientInfo> getClientInfos() {
		return new ArrayList<ClientInfo>( infos.values() );
	}

	public void broadcast(Message message) {
		// logger.debug("broadcast: " + message);
		for ( Channel channel : channels.values() ) {
			channel.send(message);
		}
	}

	public void sendTo(ClientInfo to, Message message) {
		if ( to == null )
			return;
		Channel channel = channels.get(to.getID());
		if ( channel == null )
			return;
		channel.send(message);
	}

	public void broadcastExcept(ClientInfo except, Message message) {
		for ( Entry<String, Channel> entry : channels.entrySet() ) {
			if ( ! entry.getKey().equals(except.getID())) {
				entry.getValue().send(message);
			}
		}
	}

	public void start() {
		if ( threadServer == null ) {
			threadServer = new Thread(this);
			threadServer.start();
		}
	}

	public void stop() {
		if ( threadServer != null ) {
			threadServer.interrupt();
			threadServer = null;
			synchronized (channels) {
				for ( Channel channel : channels.values() ) {
					channel.stop();
				}
			}
			threadReceiver.interrupt();
			try {
				server.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
	}

	public void run() {

		// Startup server
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			logger.error("Cannot start server", e);
			return;
		}

		// Receiver thread
		threadReceiver = new Thread() {
			public void run() {
				while ( true ) {
					MessageAndClientInfo messageAndClientInfo;
					try {
						messageAndClientInfo = popMessage();
					} catch (InterruptedException e) {
						// e.printStackTrace();
						return;
					}
					if ( messageAndClientInfo == null )
						break;
					notifyListeners(messageAndClientInfo.getInfo(), messageAndClientInfo.getMessage());
				}
			};
		};
		threadReceiver.start();

		logger.debug("Server started on port: " + port);
		while ( true ) {

			Socket socket = null;

			try {
				socket = server.accept();
			} catch (IOException e1) {
				break;
			}

			logger.debug("Accepted connection: " + socket.getInetAddress());

			synchronized ( channels ) {

				String clientID = "" + clientCounter;
				ClientInfo info = new ClientInfo(clientID, socket.getInetAddress());
				Channel channel = null;

				try {
					channel = new TCPChannel(socket);
				} catch (IOException e) {
					logger.error("Error in server client connection", e);
					continue;
				}

				clientCounter ++;
				infos.put(clientID, info);
				channels.put(clientID, channel);
				channel.start();

				// Notify connection
				for ( ServerListener serverListener : serverListeners ) {
					serverListener.clientConnected(info);
				}

			}
			
		}
	}

	class MessageAndClientInfo {
		
		private Message message;
		
		private ClientInfo info;
		
		public MessageAndClientInfo(Message message, ClientInfo info) {
			super();
			this.message = message;
			this.info = info;
		}

		public Message getMessage() {
			return message;
		}

		public void setMessage(Message message) {
			this.message = message;
		}

		public ClientInfo getInfo() {
			return info;
		}

		public void setInfo(ClientInfo info) {
			this.info = info;
		}

	}

}
