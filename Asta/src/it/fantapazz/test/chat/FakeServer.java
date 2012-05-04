
package it.fantapazz.test.chat;

import it.fantapazz.chat.ClientInfo;
import it.fantapazz.chat.ChatFactory;
import it.fantapazz.chat.Message;
import it.fantapazz.chat.ServerI;
import it.fantapazz.chat.ServerListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FakeServer {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		final ServerI server = ChatFactory.getServer(5555);
		final Map<String, ClientInfo> infos = new HashMap<String, ClientInfo>();

		server.addListener(new ServerListener() {

			public void clientConnected(ClientInfo client) {
				System.out.println("Client Connected: " + client);
				infos.put(client.getID(), client);
			}
			
			public void clientEvicted(ClientInfo client) {
				System.out.println("Client Evicted: " + client);
				infos.remove(client.getID());
			}
			
			public void receive(ClientInfo from, Message message) {
				System.out.println("Server received from " + message.getSource() + " { ID: " + from.getID() + " }" );
				ClientInfo except = infos.get(message.getSource());
				server.broadcastExcept(except, message);
			}

		});
		
		server.start();

	}
	
}
