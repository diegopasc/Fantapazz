package it.fantapazz.asta.core.bean;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Status of a Player during an Asta: this bean is used to 
 * maintain informations about a player during a running Asta.
 * 
 * @author Michele Mastrogiovanni
 */
public class PlayerStatus implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Money of user
	 */
	@JsonProperty("money")
	private double money;
		
	/**
	 * List of calciatori that player won and their costs
	 */
	@JsonProperty(value = "CalciatoriCosto")
	private List<CalciatoreCost> calciatoriWon;
	
	public PlayerStatus() {
		calciatoriWon = new LinkedList<CalciatoreCost>();
	}
		
	public void removeCalciatore(String calciatoreID, double value) {
		// calciatoriToSelect.add(calciatoreID);
		CalciatoreCost result = null;
		for ( CalciatoreCost c : calciatoriWon ) {
			if ( c.getID().equals(calciatoreID)) {
				result = c;
				break;
			}
		}
		calciatoriWon.remove(result);
		money += value;
	}

	/**
	 * Current player won a calciatore for a given amount of money
	 * 
	 * @param calciatoreID ID of calciatore won
	 * @param value Value of this calciatore
	 */
	public void winCalciatore(String calciatoreID, double value) {
		calciatoriWon.add(new CalciatoreCost(calciatoreID, value));
		money -= value;
	}

	public double getMoney() {
		return money;
	}

	public List<CalciatoreCost> getCalciatoriWon() {
		return calciatoriWon;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public void setCalciatoriWon(List<CalciatoreCost> calciatoriWon) {
		this.calciatoriWon = calciatoriWon;
	}

	@Override
	public String toString() {
		return "{ money=" + money + ", calciatoriWon="
				+ calciatoriWon + "}";
	}
	
}
