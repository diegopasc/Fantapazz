package other.fantapazz;

import it.fantapazz.asta.controller.AstaControllerServer;
import it.fantapazz.chat.ChatFactory;
import it.fantapazz.chat.ServerI;
import it.fantapazz.connector.ConnectionException;

import java.io.IOException;

public class FantapazzAsteMain {

	public static void main(String[] args) throws IOException, ConnectionException {
		ServerI	server = ChatFactory.getServer(AstaControllerServer.CONTROLLER_PORT);
		AstaControllerServer controller = new AstaControllerServer(server );
		controller.start();
	}

}
