package it.fantapazz.test.controller;

import it.fantapazz.asta.controller.AstaControllerServer;
import it.fantapazz.asta.controller.AsteListener;
import it.fantapazz.asta.controller.PlayerControlServer;
import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;
import it.fantapazz.connector.bean.UserComm;
import it.fantapazz.test.core.FakePlayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FakeAstaControllerClient implements AsteListener {

	private Map<String, AstaInfo> aste;
	
	private PlayerControlServer client;
	
	public FakeAstaControllerClient(String username, String password, String host) throws ConnectionException {
		aste = new HashMap<String, AstaInfo>();		
		UserComm user = Connector.instance().getUser(username, password);
		client = new PlayerControlServer(user.getID(), host, AstaControllerServer.CONTROLLER_PORT, this);
	}

	public void updateAsta(AstaInfo astaInfo) {
		System.out.println("Discovered asta: " + astaInfo.getID() + ", status: " + astaInfo.getStatus() + ", port: " + astaInfo.getPort());
		aste.put(astaInfo.getID(), astaInfo);
	}
	
	public void start() {
		
		String astaID = "2";
		
		new Thread(client).start();
		
		while ( true ) {
			
			try { Thread.sleep(3000); } catch (InterruptedException e) {}
			
			System.out.println("Aste: " + aste.size());
			
			AstaInfo info = aste.get(astaID);
			
			if ( info == null ) {
				continue;
			}
			
			client.startAsta(info);
			
			try { Thread.sleep(3000); } catch (InterruptedException e) {}
			
			try {
				client.enterAsta(info, new FakePlayer());
			} catch (ConnectionException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try { Thread.sleep(120000); } catch (InterruptedException e) {}
			
			try {
				client.exitAsta();
			} catch (ConnectionException e) {
				e.printStackTrace();
			}

			try { Thread.sleep(60000); } catch (InterruptedException e) {}

			client.stopAsta(info);
			break;
		}
	}

	public static void main(String[] args) throws IOException, ConnectionException, InterruptedException {
		FakeAstaControllerClient client = new FakeAstaControllerClient("michele", "pippo80", "localhost");
		client.start();
	}

	public void removedAsta(AstaInfo astaInfo) {
		
	}
	
}
