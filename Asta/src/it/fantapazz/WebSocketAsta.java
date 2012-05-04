package it.fantapazz;

import it.fantapazz.connector.ConnectionException;
import it.fantapazz.websocket.AstaControllerPlugin;

import java.io.IOException;

import org.jwebsocket.api.WebSocketEngine;
import org.jwebsocket.factory.JWebSocketFactory;
import org.jwebsocket.plugins.TokenPlugInChain;
import org.jwebsocket.server.TokenServer;

public class WebSocketAsta {
	
	public static void main(String[] args) throws IOException, ConnectionException {
		
		JWebSocketFactory.start(null);
		
		// if not loaded by jWebSocket.xml config file...
	    TokenServer lServer = (TokenServer) JWebSocketFactory.getServer("ts0");
	    WebSocketEngine engine = JWebSocketFactory.getEngine();
	    TokenPlugInChain chain = lServer.getPlugInChain();
	    AstaControllerPlugin plugin = new AstaControllerPlugin();
	    chain.addPlugIn(plugin);
	    plugin.engineStarted(engine);
	    	    
	    // Thread.sleep(10000);
	    // JWebSocketFactory.stop();
		
	}

}
