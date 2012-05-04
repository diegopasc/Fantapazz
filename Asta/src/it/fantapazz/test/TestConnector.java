package it.fantapazz.test;

import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.connector.Connector;
import it.fantapazz.connector.bean.SquadraComm;
import it.fantapazz.connector.bean.UserComm;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

public class TestConnector {
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, ConnectionException {
		
		// dumpSquadra("40");
		
		Connector.instance().setAstaStatus("1", AstaInfo.Status.NOT_STARTED);
		// Connector.instance().setAstaStatus("2", AstaInfo.Status.NOT_STARTED);
		
//		PlayerStatus status = Connector.instance().getPlayerStatus("6", "40");
//		double sum = status.getMoney();
//		for ( CalciatoreCost cc : status.getCalciatoriWon()) {
//			sum += cc.getCost();
//		}
//		System.out.println("Totale: " + sum);

		//		Cache.instance().setEnabled(false);
		
		// testInfoAste();

//		testCalciatori();
//		testUser("michele", "pippo80");
				
	}

	private static void dumpSquadra(String ID) throws ConnectionException {
		System.out.println("-------- TEST SQUADRA " + ID + " -------");
		SquadraComm squadra = Connector.instance().getSquadra(ID);
		System.out.println("Squadra: " + squadra.getAlias());
		UserComm presidente = Connector.instance().getUser(squadra.getUidPresidente());
		System.out.println("Presidente: (" + squadra.getUidPresidente() + ")" + presidente.getName());
		for ( String uid : squadra.getAllenatori()) {
			UserComm user = Connector.instance().getUser(uid);
			System.out.println("\tAllenatore: (" + uid + ") " + user.getName());
		}
	}

	private static void testLega(String ID) throws ConnectionException {
		System.out.println("-------- TEST LEGA " + ID + " -------");
		System.out.println(Connector.instance().getLega(ID));
	}

	private static void testUser(String username, String password) throws ConnectionException {
		System.out.println("-------- TEST USER (" + username + ":" + password + ") -------");
		UserComm user = Connector.instance().getUser(username, password);
		System.out.println(user);
		List<AstaInfo> aste = Connector.instance().getAsteForUser(user.getID()); 
		System.out.println(aste);
	}

	private static void testInfoAsta(String ID) throws ConnectionException {
		System.out.println("-------- TEST INFO ASTA " + ID + " -------");
		System.out.println(Connector.instance().getAsta(ID));
	}

	private static void testAstaStatus(String ID) throws ConnectionException {
		System.out.println("-------- TEST ASTA " + ID + " STATUS -------");
		AstaInfo.Status oldStatus = Connector.instance().getAstaStatus("1"); 
		System.out.println(oldStatus);
		Connector.instance().setAstaStatus(ID, AstaInfo.Status.TERMINATED);
		System.out.println(Connector.instance().getAstaStatus(ID));
		Connector.instance().setAstaStatus(ID, oldStatus);
		System.out.println(Connector.instance().getAstaStatus(ID));
	}

	private static void testCalciatori() throws ConnectionException {
		System.out.println("-------- TEST CALCIATORI -------");
		System.out.println(Connector.instance().getCalciatori());
	}

	private static void testInfoAste() throws ConnectionException {
		System.out.println("-------- TEST INFO ASTE -------");
		List<AstaInfo> aste = Connector.instance().getAste();
		for ( AstaInfo asta : aste ) {			

			System.out.println(asta);
			testInfoAsta(asta.getID());
			
			for ( String playerID : asta.getPlayers()) {
				System.out.println("-Squadra: " + playerID);
				dumpSquadra(playerID);
			}

			testAstaStatus(asta.getID());
			
			testLega(asta.getIDLega());

		}
	}

}
