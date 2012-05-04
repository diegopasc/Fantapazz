package it.fantapazz.asta.core.bean;

import java.io.Serializable;


public class HelloAstaServerBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private PlayerStatus playerStatus;
	
	private AstaStatus astaStatus;
	
	public HelloAstaServerBean() {}
	
	public HelloAstaServerBean(PlayerStatus playerStatus, AstaStatus astaStatus) {
		super();
		this.playerStatus = playerStatus;
		this.astaStatus = astaStatus;
	}

	public PlayerStatus getPlayerStatus() {
		return playerStatus;
	}

	public void setPlayerStatus(PlayerStatus playerStatus) {
		this.playerStatus = playerStatus;
	}

	public AstaStatus getAstaStatus() {
		return astaStatus;
	}

	public void setAstaStatus(AstaStatus astaStatus) {
		this.astaStatus = astaStatus;
	}

	@Override
	public String toString() {
		return "HelloContentBean [playerStatus=" + playerStatus
				+ ", astaStatus=" + astaStatus + "]";
	}

}
