package it.fantapazz.asta.core;

/**
 * This interface is used to receive notifications from an Asta player.
 * 
 * @author Michele Mastrogiovannim
 */
public interface PlayerListener {
	
	public void pauseStarted(String from, double duration);
	
	public void pauseEnded();
	
	public void receivedTextMessage(String from, String content);
	
	public void receivedHello(Object content);

	public void yourID(PlayerControl playerControl, String ID);
	
	public void chooseCalciatore(PlayerControl playerControl, String playerID);

	public void tooLateToChooseCalciatore(PlayerControl playerControl);
	
	public void calciatoreChosen(PlayerControl playerControl, String playerID, String calciatoreID);
	
	public void playerOffered(PlayerControl playerControl, String playerID, String calciatoreID, double offer);
	
	public void winCalciatore(PlayerControl playerControl, String calciatoreID, double offer);

	public void lostCalciatore(PlayerControl playerControl, String winner, String calciatoreID, double offer);

}
