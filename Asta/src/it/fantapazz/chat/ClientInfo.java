package it.fantapazz.chat;

import java.net.InetAddress;

/**
 * Bean that contains information about a connected client
 * 
 * @author Michele Mastrogiovanni
 */
public class ClientInfo {
	
	private String ID;
	
	private InetAddress address;
	
	public ClientInfo(String iD, InetAddress address) {
		super();
		ID = iD;
		this.address = address;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "ClientInfo [ID=" + ID + ", address=" + address + "]";
	}

}
