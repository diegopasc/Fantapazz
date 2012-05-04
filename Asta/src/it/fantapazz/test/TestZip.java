package it.fantapazz.test;

import it.fantapazz.chat.Message;
import it.fantapazz.utility.MessageSerializer;

import java.io.IOException;

public class TestZip {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		
		String content = "Michele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\nMichele e' bello\n";
		Message message = new Message(content);
		String msg = MessageSerializer.writeMessage(message);
		System.out.println(msg);
		Message messageNew = MessageSerializer.readMessage(msg);
		System.out.println(messageNew);
		
//		byte[] zipped = MessageSerializer.tryZip(.getBytes());
//		System.out.println("Zipped: " + zipped[0]);
//		byte[] normal = MessageSerializer.tryUnzip(zipped);
//		System.out.println("'" + new String(normal) + "'");

	}

}
