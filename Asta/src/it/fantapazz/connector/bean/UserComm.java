package it.fantapazz.connector.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class UserComm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "ID")
	private String ID;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "username")
	private String username;

	@JsonProperty(value = "password")
	private String password;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserComm [ID=" + ID + ", name=" + name + "]";
	}
	
}
