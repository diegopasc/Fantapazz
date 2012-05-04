package it.fantapazz.asta.controller.bean;

public class AstaPlayer {
	
	private String astaID;
	
	private String playerID;
	
	public AstaPlayer(String astaID, String playerID) {
		super();
		this.astaID = astaID;
		this.playerID = playerID;
	}

	public String getAstaID() {
		return astaID;
	}

	public void setAstaID(String astaID) {
		this.astaID = astaID;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((astaID == null) ? 0 : astaID.hashCode());
		result = prime * result
				+ ((playerID == null) ? 0 : playerID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AstaPlayer other = (AstaPlayer) obj;
		if (astaID == null) {
			if (other.astaID != null)
				return false;
		} else if (!astaID.equals(other.astaID))
			return false;
		if (playerID == null) {
			if (other.playerID != null)
				return false;
		} else if (!playerID.equals(other.playerID))
			return false;
		return true;
	}
	
}
