package it.fantapazz.connector;

public class ConnectionException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConnectionException() {
		super();
	}

	public ConnectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ConnectionException(String arg0) {
		super(arg0);
	}

	public ConnectionException(Throwable arg0) {
		super(arg0);
	}

}
