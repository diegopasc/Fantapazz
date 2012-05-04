package it.fantapazz.connector.bean;

import java.util.HashMap;
import java.util.Map;

import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.asta.core.bean.PlayerStatus;

public class AstaDB {

	private String owner;
	
	private AstaInfo astaInfo;
	
	private Map<String, PlayerStatus> playerStatus;
	
	public AstaDB() {
		astaInfo = new AstaInfo();
		playerStatus = new HashMap<String, PlayerStatus>();
	}

	public AstaInfo getAstaData() {
		return astaInfo;
	}

	public void setAstaData(AstaInfo astaInfo) {
		this.astaInfo = astaInfo;
	}

	public Map<String, PlayerStatus> getPlayerStatus() {
		return playerStatus;
	}

	public void setPlayerStatus(Map<String, PlayerStatus> playerStatus) {
		this.playerStatus = playerStatus;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
