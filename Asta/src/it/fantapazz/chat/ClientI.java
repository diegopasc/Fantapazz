package it.fantapazz.chat;


/**
 * Interface for a Chat client.
 * It allows to add a listener to receive messages
 * and to send its own messages.
 * 
 * @author Michele Mastrogiovanni
 */
public interface ClientI extends Startable {

	public void addListener(ClientListener clientListener);

	public void send(Message message);

}
