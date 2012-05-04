package it.fantapazz.chat;

import java.util.List;

/**
 * Interface for chat server.
 * It allows to start and stop server, to get client informations,
 * to close connection with a given client, to broadcast a message
 * to any client and to send message to a given client.
 * 
 * @author Michele Mastrogiovanni
 */
public interface ServerI extends Startable {
	
	public List<ClientInfo> getClientInfos();
	
	public void kickClient(ClientInfo clientInfo);

	public void addListener(ServerListener serverListener);
	
	public void broadcast(Message message);
	
	public void sendTo(ClientInfo to, Message message);
	
	public void broadcastExcept(ClientInfo except, Message message);
	
}
