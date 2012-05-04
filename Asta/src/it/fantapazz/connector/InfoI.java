package it.fantapazz.connector;

import it.fantapazz.connector.bean.CalciatoreComm;
import it.fantapazz.connector.bean.LegaComm;
import it.fantapazz.connector.bean.SquadraComm;
import it.fantapazz.connector.bean.UserComm;

import java.util.Collection;

public interface InfoI {

	public Collection<CalciatoreComm> getCalciatori() throws ConnectionException;
		
	public UserComm getUser(String userID) throws ConnectionException;

	public UserComm getUser(String username, String password) throws ConnectionException;
		
	public LegaComm getLega(String ID) throws ConnectionException;
		
	public CalciatoreComm getCalciatore(String ID) throws ConnectionException;

	/**
	 * Get squadra with a given ID
	 * 
	 * @param squadraID ID of squadra
	 * @return Return Squadra information
	 * @throws ConnectionException
	 */
	public SquadraComm getSquadra(String squadraID) throws ConnectionException;

}
