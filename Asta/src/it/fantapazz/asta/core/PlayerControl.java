package it.fantapazz.asta.core;

public interface PlayerControl {
	
	public void pause(String from, double duration);
	
	public void selectCalciatore(String calciatoreID);
	
	public void makeOffer(String calciatoreID, double offer);
	
	public void sendTextMessage(String content);
	
}
