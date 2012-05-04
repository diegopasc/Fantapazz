package it.fantapazz.test.chat;

import it.fantapazz.chat.ClientI;
import it.fantapazz.chat.ClientListener;
import it.fantapazz.chat.ChatFactory;
import it.fantapazz.chat.Message;

import java.io.IOException;
import java.net.UnknownHostException;

public class FakeClient {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		ClientI client = ChatFactory.getClient("127.0.0.1", 5555, args[0]);

		client.addListener(new ClientListener() {
			public void receive(Message message) {
				System.out.println(message);
			}
		});
		
		while ( true ) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			client.send(new Message());
		}

	}

}
