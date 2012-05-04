package it.fantapazz.chat;

/**
 * Listener for client connection and eviction and
 * to receive message coming from clients.  
 * 
 * @author Michele Mastrogiovanni
 */
public interface ServerListener {
	
	public void clientConnected(ClientInfo client);
	
	public void clientEvicted(ClientInfo client);
	
	public void receive(ClientInfo from, Message message);

}
