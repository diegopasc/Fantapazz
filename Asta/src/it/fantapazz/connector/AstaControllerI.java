package it.fantapazz.connector;

import it.fantapazz.asta.controller.bean.AstaInfo;
import it.fantapazz.asta.core.bean.PlayerStatus;
import it.fantapazz.connector.bean.SquadraComm;

import java.util.List;

/**
 * Retrieve and update information about asta 
 * 
 * @author Michele Mastrogiovanni
 */
public interface AstaControllerI {
	
	public SquadraComm getSquadraForAstaAndPlayer(AstaInfo astaInfo, String playerID) throws ConnectionException;

	/**
	 * List of all aste in the system
	 * 
	 * @return Return list of aste in the system (running or not)
	 */
	public List<AstaInfo> getAste() throws ConnectionException;
	
	/**
	 * Change status of asta (NOT_STARTED, RUNNING, PAUSED, TERMINATED) 
	 */
	public void setAstaStatus(String astaID, AstaInfo.Status status) throws ConnectionException;
	
	/**
	 * @param astaID ID of asta to query
	 * @return Current status of asta
	 * @throws ConnectionException
	 */
	public AstaInfo.Status getAstaStatus(String astaID) throws ConnectionException;
	
	/**
	 * Assign a calciatore to a given user at a given cost (cost will be removed
	 * from User's money account). 
	 * 
	 * @param astaID ID of asta
	 * @param squadraID ID of player
	 * @param calciatoreID ID of calciatore
	 * @param value Value of calciatore bought
	 * @param userUID UID of user that perform the assignment
	 * 
	 * @return ID of assignment (to be used for a removal)
	 */
	public Integer assignCalciatore(String legaID, String playerID, String calciatoreID, double value, String userUID) throws ConnectionException;

	/**
	 * @param acquistoID ID of a previous assignment of a calciatore
	 */
	public void removeCalciatore(String acquistoID) throws ConnectionException;
	
	/**
	 * Sell a calciatore and make this available for an asta
	 * 
	 * @param legaID ID lega
	 * @param squadraID Squadra that sell calciatore
	 * @param calciatoreID ID of calciatore
	 * @param value Cost of calciatore
	 * 
	 * @return ID of acquisto if successfull, -1 otherwise.
	 * 
	 * @throws ConnectionException
	 */
	public Integer sellCalciatore(String legaID, String squadraID, String calciatoreID, double value, String playerID) throws ConnectionException;
	
	/**
	 * Get list of aste that a player can see
	 *  
	 * @param playerID ID of player
	 * 
	 * @return Return information about asta 
	 */
	public List<AstaInfo> getAsteForUser(String userID) throws ConnectionException;
	
	/**
	 * Return updated informations about asta
	 * 
	 * @param astaID ID of asta
	 * 
	 * @return Data and status of asta
	 */
	public AstaInfo getAsta(String astaID) throws ConnectionException;

	/**
	 * Return status of player in asta
	 * 
	 * @param astaID ID of asta
	 * @param playerID ID of player
	 * 
	 * @return Status of player in the asta
	 */
	public PlayerStatus getPlayerStatus(String legaID, String squadraID) throws ConnectionException; 
		
}
