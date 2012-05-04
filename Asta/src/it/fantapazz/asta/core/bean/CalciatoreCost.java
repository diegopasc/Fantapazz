package it.fantapazz.asta.core.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class CalciatoreCost implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("calciatoreID")
	private String ID;

	@JsonProperty("acquistoID")
	private String acquistoID;
	
	@JsonProperty("costo")
	private double cost;
	
	public CalciatoreCost() {
	}
	
	public CalciatoreCost(String iD, double cost) {
		super();
		ID = iD;
		this.cost = cost;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getAcquistoID() {
		return acquistoID;
	}

	public void setAcquistoID(String acquistoID) {
		this.acquistoID = acquistoID;
	}

	@Override
	public String toString() {
		return "{ ID=" + ID + ", cost=" + cost + "}";
	}

}
