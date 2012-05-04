package it.fantapazz.websocket;

import it.fantapazz.asta.controller.AstaControllerServer;
import it.fantapazz.asta.core.AstaServer;
import it.fantapazz.chat.ClientInfo;
import it.fantapazz.chat.Message;
import it.fantapazz.chat.ServerI;
import it.fantapazz.chat.ServerListener;
import it.fantapazz.connector.ConnectionException;
import it.fantapazz.utility.MessageSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jwebsocket.api.PluginConfiguration;
import org.jwebsocket.api.WebSocketConnector;
import org.jwebsocket.api.WebSocketEngine;
import org.jwebsocket.kit.CloseReason;
import org.jwebsocket.kit.PlugInResponse;
import org.jwebsocket.plugins.TokenPlugIn;
import org.jwebsocket.token.Token;
import org.jwebsocket.token.TokenFactory;

public class AstaControllerPlugin extends TokenPlugIn implements ServerI {
	
	private static final Log logger = LogFactory.getLog(AstaServer.class);

	private String NS_ASTA_CONTROLLER_DEFAULT = "org.jWebSocket.plugins.asta.controller";
	
	private List<ServerListener> listeners;
	
	private boolean started;

	private Map<String, ClientInfo> infos;

	private Map<String, WebSocketConnector> connectors;

	private AstaControllerServer controller;
	
	public AstaControllerPlugin() throws IOException, ConnectionException {
		super();
		setNamespace(NS_ASTA_CONTROLLER_DEFAULT);
	}

	public AstaControllerPlugin(PluginConfiguration configuration) {
		super(configuration);
		setNamespace(NS_ASTA_CONTROLLER_DEFAULT);
	}

	@Override
	public void engineStarted(WebSocketEngine aEngine) {
		
		listeners = new LinkedList<ServerListener>();
		connectors = new HashMap<String, WebSocketConnector>();
		infos = new HashMap<String, ClientInfo>();

		start();
		super.engineStarted(aEngine);
	}
	
	@Override
	public void engineStopped(WebSocketEngine aEngine) {
		stop();
		super.engineStopped(aEngine);
	}

	@Override
	public void connectorStarted(WebSocketConnector aConnector) {
		ClientInfo info = new ClientInfo(aConnector.getId(), aConnector.getRemoteHost());
		connectors.put(aConnector.getId(), aConnector);
		infos.put(aConnector.getId(), info);
		for ( ServerListener listener : listeners) {
			listener.clientConnected(info);
		}
		super.connectorStarted(aConnector);
	}
	
	@Override
	public void connectorStopped(WebSocketConnector aConnector, CloseReason aCloseReason) {
		ClientInfo info = infos.get(aConnector.getId());
		connectors.remove(aConnector.getId());
		infos.remove(aConnector.getId());
		for ( ServerListener listener : listeners) {
			listener.clientEvicted(info);
		}
		super.connectorStopped(aConnector, aCloseReason);
	}

	@Override
	public void processToken(PlugInResponse aResponse, WebSocketConnector aConnector, Token aToken) {
				
		String lType = aToken.getType();
		
		String lNS = aToken.getNS();

		if (lType == null)
			return;
		
		if (lNS == null)
			return;
		
		if (!lNS.equalsIgnoreCase(getNamespace()))
			return;
				
		String value = aToken.getString("value");
		Message message;
		try {
			message = MessageSerializer.readMessage(value);
			for ( ServerListener listener : listeners) {
				listener.receive(infos.get(aConnector.getId()), message);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
				
	}

	public void start() {
		
		if ( started == true )
			return;
		started = true;
				
		if ( controller == null ) {
			try {
				controller = new AstaControllerServer(this);
				controller.start();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		if ( started == false )
			return;
		started = false;
		controller.stop();
	}

	public void addListener(ServerListener serverListener) {
		listeners.add(serverListener);
	}

	public void broadcast(Message message) {
		Token token = TokenFactory.createToken(getNamespace(), "asta");
		String value;
		try {
			value = MessageSerializer.writeMessage(message);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		token.setString("value", value);
		getServer().broadcastToken(token);
		// broadcastToken(null, token);
	}

	public void sendTo(ClientInfo to, Message message) {
		Token token = TokenFactory.createToken(getNamespace(), "asta");
		String value;
		try {
			value = MessageSerializer.writeMessage(message);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		token.setString("value", value);
		WebSocketConnector destination = connectors.get(to.getID());
		sendToken(null, destination, token);
	}

	public void broadcastExcept(ClientInfo except, Message message) {
		for ( ClientInfo info : infos.values() ) {
			if ( info.getID().equals(except.getID()))
				continue;
			sendTo(info, message);
		}
	}

	public List<ClientInfo> getClientInfos() {
		logger.debug("INFOS");
		return null;
	}

	public void kickClient(ClientInfo clientInfo) {
		logger.debug("KICK: " + clientInfo);
	}

}
