package it.fantapazz.channel;

import it.fantapazz.ConfigSettings;
import it.fantapazz.chat.Message;
import it.fantapazz.utility.MessageSerializer;
import it.fantapazz.utility.StatsCommunication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPChannel implements Channel, Runnable {

	private ChannelConsumer consumer;

	private Socket socket;

	private ObjectInputStream in;

	private ObjectOutputStream out;

	private Thread receiver;

	public TCPChannel(Socket socket) throws UnknownHostException, IOException {
		this.socket = socket;
		out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}

	public void send(Message message) {
		try {
			String messageValue = MessageSerializer.writeMessage(message);
			if ( ConfigSettings.instance().isDumpMessages() ) {
				System.out.println("Send: " + messageValue);
			}
			out.writeUTF(messageValue);
			out.flush();
			out.reset();
		} catch (IOException ex) {
			ex.printStackTrace();
			receiver.interrupt();
		}		
	}

	public void setChannelConsumer(ChannelConsumer consumer) {
		this.consumer = consumer;
	}

	public void start() {
		if ( receiver == null ) {
			receiver = new Thread(this);
			receiver.start();
		}
	}

	public void stop() {
		if ( receiver != null ) {
			receiver.interrupt();
			receiver = null;
		}
	}
	
	public void run() {
		try {
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while (!Thread.interrupted()) {
				String value = in.readUTF();
				if ( ConfigSettings.instance().isActivateStats() ) {
					StatsCommunication.instance().sent(value.getBytes().length);
				}
				if ( ConfigSettings.instance().isDumpMessages()) {
					System.out.println("Received: " + value);
				}
				Message message = (Message) MessageSerializer.readMessage(value);
				consumer.receive(message);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			consumer = null;
			try {
				out.close ();
			} catch (IOException ex) {
				ex.printStackTrace ();
			}
		}
	}

}
