package it.fantapazz.connector.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class CalciatoreComm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value="ID_Calciatore")
	private String ID_Calciatore;
	
	@JsonProperty(value="ID_Ruolo")
	private String ID_Ruolo;
	
	@JsonProperty(value="Calciatore")
	private String Calciatore;
	
	@JsonProperty(value="Club")
	private String Club;
	
	@JsonProperty(value="Quotazione")
	private String Quotazione;
	
	@JsonProperty(value="Ruolo")
	private String Ruolo;
	
	@JsonProperty(value="Ruolo_short")
	private String Ruolo_short;
	
	@JsonProperty(value="Ruolo_long")
	private String Ruolo_long;
	
	public String getID_Calciatore() {
		return ID_Calciatore;
	}
	
	public void setID_Calciatore(String iD_Calciatore) {
		ID_Calciatore = iD_Calciatore;
	}
	
	public String getID_Ruolo() {
		return ID_Ruolo;
	}
	
	public void setID_Ruolo(String iD_Ruolo) {
		ID_Ruolo = iD_Ruolo;
	}
	
	public String getCalciatore() {
		return Calciatore;
	}
	
	public void setCalciatore(String calciatore) {
		Calciatore = calciatore;
	}
	
	public String getClub() {
		return Club;
	}
	
	public void setClub(String club) {
		Club = club;
	}
	
	public String getQuotazione() {
		return Quotazione;
	}
	
	public void setQuotazione(String quotazione) {
		Quotazione = quotazione;
	}
	
	public String getRuolo() {
		return Ruolo;
	}
	
	public void setRuolo(String ruolo) {
		Ruolo = ruolo;
	}
	
	public String getRuolo_short() {
		return Ruolo_short;
	}
	
	public void setRuolo_short(String ruolo_short) {
		Ruolo_short = ruolo_short;
	}
	
	public String getRuolo_long() {
		return Ruolo_long;
	}
	
	public void setRuolo_long(String ruolo_long) {
		Ruolo_long = ruolo_long;
	}

	@Override
	public String toString() {
		return "Giocatore [ID_Calciatore=" + ID_Calciatore + ", ID_Ruolo="
		+ ID_Ruolo + ", Calciatore=" + Calciatore + ", Club="
		+ Club + ", Quotazione=" + Quotazione + ", Ruolo=" + Ruolo
		+ ", Ruolo_short=" + Ruolo_short + ", Ruolo_long="
		+ Ruolo_long + "]";
	}

}
