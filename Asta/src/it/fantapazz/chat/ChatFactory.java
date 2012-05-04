package it.fantapazz.chat;

import it.fantapazz.channel.TCPChannel;
import it.fantapazz.chat.impl.Client;
import it.fantapazz.chat.impl.Server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Factory for chat client and server
 *  
 * @author Michele Mastrogiovanni
 */
public class ChatFactory {
	
	/**
	 * Create a server listening to given port
	 * 
	 * @param port Port where server must listen
	 * @return Return a chat server
	 * @throws IOException
	 */
	public static ServerI getServer(int port) throws IOException {
		return new Server(port);
	}

	/**
	 * Create a client connected to a given host and port.
	 * 
	 * @param host Host where connect to
	 * @param port Port where server is listening.
	 * @param ID ID of client used in source field of outgoing mesages
	 * @return Return a client
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static ClientI getClient(String host, int port, String ID) throws UnknownHostException, IOException {
		return new Client(new TCPChannel(new Socket(host, port)), ID);
	}

}
