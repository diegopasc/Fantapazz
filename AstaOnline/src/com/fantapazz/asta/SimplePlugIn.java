package com.fantapazz.asta;

import java.util.Date;
import java.util.logging.Logger;

import org.jwebsocket.api.WebSocketConnector;
import org.jwebsocket.api.WebSocketEngine;
import org.jwebsocket.config.JWebSocketServerConstants;
import org.jwebsocket.kit.CloseReason;
import org.jwebsocket.kit.PlugInResponse;
import org.jwebsocket.logging.Logging;
import org.jwebsocket.plugins.TokenPlugIn;
import org.jwebsocket.token.Token;

public class SimplePlugIn extends TokenPlugIn {

	private static org.apache.log4j.Logger log = Logging.getLogger(SimplePlugIn.class);
	
	// if namespace changed update client plug-in accordingly!
	private static String NS_SAMPLE = JWebSocketServerConstants.NS_BASE + ".plugins.sample";
	
	private static String SAMPLE_VAR = NS_SAMPLE + ".started";

	public SimplePlugIn() {
		if (log.isDebugEnabled()) {
			log.debug("Instantiating sample plug-in...");
		}
		// specify default name space for sample plugin
		this.setNamespace(NS_SAMPLE);
	}

	@Override
	public void connectorStarted(WebSocketConnector aConnector) {
		// this method is called every time when a client
		// connected to the server
		aConnector.setVar(SAMPLE_VAR, new Date().toString());
	}

	@Override
	public void connectorStopped(WebSocketConnector aConnector, CloseReason aCloseReason) {
		// this method is called every time when a client
		// disconnected from the server
	}

	@Override
	public void engineStarted(WebSocketEngine aEngine) {
		// this method is called when the engine has started
		super.engineStarted(aEngine);
	}

	@Override
	public void engineStopped(WebSocketEngine aEngine) {
		// this method is called when the engine has stopped
		super.engineStopped(aEngine);
	}

	@Override
	public void processToken(PlugInResponse aResponse, WebSocketConnector aConnector, Token aToken) {

		// get the type of the token
		// the type can be associated with a "command"
		String lType = aToken.getType();

		// get the namespace of the token
		// each plug-in should have its own unique namespace
		String lNS = aToken.getNS();

		// check if token has a type and a matching namespace
		if (lType != null && lNS != null && lNS.equals(getNamespace())) {

			// get the server time
			if (lType.equals("requestServerTime")) {
				// create the response token
				// this includes the unique token-id
				Token lResponse = createResponse(aToken);

				// add the "time" and "started" field
				lResponse.put("time", new Date().toString());
				lResponse.put("started", aConnector.getVar(SAMPLE_VAR));

				// send the response token back to the client
				sendToken(null, aConnector, lResponse);
			}
		}
	}

}