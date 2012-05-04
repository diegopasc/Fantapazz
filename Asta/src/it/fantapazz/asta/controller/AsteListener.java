package it.fantapazz.asta.controller;

import it.fantapazz.asta.controller.bean.AstaInfo;

public interface AsteListener {
	
	/**
	 * Notify the status of an asta
	 * 
	 * @param astaInfo Asta informations
	 */
	public void updateAsta(AstaInfo astaInfo);
	
	/**
	 * Notify removal of an Asta
	 * 
	 * @param astaInfo Asta informations
	 */
	public void removedAsta(AstaInfo astaInfo);
	
}
