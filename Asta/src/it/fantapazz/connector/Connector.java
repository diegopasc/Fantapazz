package it.fantapazz.connector;

import it.fantapazz.ConfigSettings;

public class Connector {

	public static ConnectorI instance;

	public static ConnectorI instance() {
		if ( instance == null ) {
			if ( ConfigSettings.instance().isUseFakeConnector()) {
				try {
					instance = new FakeConnector();
				} catch (ConnectionException e) {
					e.printStackTrace();
				}
			}
			else {
				instance = new FantapazzConnector();
			}
		}
		return instance;
	}

	public static InfoI getInfo() {
		return instance();
	}
	
	public static AstaControllerI getAstaController() {
		return instance();
	}

}
