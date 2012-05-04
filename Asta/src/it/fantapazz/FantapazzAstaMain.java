package it.fantapazz;

import it.fantapazz.asta.AstaConfig;
import it.fantapazz.asta.core.AstaServer;
import it.fantapazz.chat.ChatFactory;
import it.fantapazz.chat.ServerI;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.utility.RoundIterator;

import java.io.IOException;

public class FantapazzAstaMain {
	
	public static void main(String[] args) throws IOException, ConnectionException {
		
		if ( args.length < 2 ) {
			System.out.println("usage: FantapazzAstaMain <AstaID> <Port>");
			return;
		}
		
		String astaID = args[0];
		
		int port = Integer.parseInt(args[1]);
		
		// Load and start server
		ServerI server = ChatFactory.getServer(port);
		AstaConfig config = new AstaConfig("/config-test.properties");
		AstaServer asta = new AstaServer(astaID, server);
		asta.setTurnSelector(new RoundIterator<String>());
		asta.setConfig(config);
		asta.start();
		
	}

}
