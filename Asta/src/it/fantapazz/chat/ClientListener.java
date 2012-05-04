package it.fantapazz.chat;

/**
 * Listener for incoming messages
 *  
 * @author Michele Mastrogiovanni
 */
public interface ClientListener {

	public void receive(Message message);

}
