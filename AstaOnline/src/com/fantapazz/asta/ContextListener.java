package com.fantapazz.asta;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jwebsocket.factory.JWebSocketFactory;
import org.jwebsocket.server.TokenServer;

public class ContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		// start the jWebSocket server sub system
		JWebSocketFactory.start();

		// if not loaded by jWebSocket.xml config file...
		TokenServer lTS = (TokenServer) JWebSocketFactory.getServer("ts0");
		SimplePlugIn lSP = new SimplePlugIn();
		lTS.getPlugInChain().addPlugIn(lSP);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// stop the jWebSocket server sub system
		JWebSocketFactory.stop(
		);
	}
}