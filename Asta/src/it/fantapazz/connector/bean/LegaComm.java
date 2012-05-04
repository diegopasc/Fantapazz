package it.fantapazz.connector.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSetter;

@JsonIgnoreProperties(value = { "uid" })
public class LegaComm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String TIPO_LEGA_PRIVATA = "1";
	
	public static final String TIPO_LEGA_PUBBLICA = "2";
	
	@JsonProperty(value = "ID_Lega")
	private String ID_Lega;
	
	@JsonProperty(value = "ID_Anno")
	private String ID_Anno;

	@JsonProperty(value = "NomeLega")
	private String NomeLega;
	
	@JsonProperty(value = "ID_TipoLega")
	private String ID_TipoLega;
	
	@JsonProperty(value = "ID_TipoAsta")
	private String ID_TipoAsta; 
	
	@JsonProperty(value = "CreditiIniziali")
	private double CreditiIniziali;
	
	@JsonIgnore
	private Date Orario_inizio_prima_partita;
	
	@JsonSetter(value = "Orario_inizio_prima_partita")
	public void setOrario_inizio_prima_partita(String data) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(data);
			setOrario_inizio_prima_partita(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@JsonIgnore
	private Date Orario_inizio_ultima_partita;

	@JsonSetter(value = "Orario_inizio_ultima_partita")
	public void setOrario_inizio_ultima_partita(String data) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(data);
			setOrario_inizio_ultima_partita(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@JsonIgnore
	private Date created;
	
	@JsonSetter(value = "Created")
	public void setCreated(String data) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(data);
			setCreated(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@JsonProperty(value = "MaxNumfSquadre")
	private int MaxNumfSquadre;
	
	@JsonProperty(value = "CurNumfSquadre")
	private int CurNumfSquadre;
	
	public boolean isPrivate() {
		return TIPO_LEGA_PRIVATA.equalsIgnoreCase(getID_TipoLega());
	}

	public boolean isPublic() {
		return TIPO_LEGA_PUBBLICA.equalsIgnoreCase(getID_TipoLega());
	}

	public String getID_Lega() {
		return ID_Lega;
	}
	public void setID_Lega(String iD_Lega) {
		ID_Lega = iD_Lega;
	}
	public String getID_Anno() {
		return ID_Anno;
	}
	public void setID_Anno(String iD_Anno) {
		ID_Anno = iD_Anno;
	}
	public String getNomeLega() {
		return NomeLega;
	}
	public void setNomeLega(String nomeLega) {
		NomeLega = nomeLega;
	}
	public String getID_TipoLega() {
		return ID_TipoLega;
	}
	public void setID_TipoLega(String iD_TipoLega) {
		ID_TipoLega = iD_TipoLega;
	}
	public String getID_TipoAsta() {
		return ID_TipoAsta;
	}
	public void setID_TipoAsta(String iD_TipoAsta) {
		ID_TipoAsta = iD_TipoAsta;
	}
	public double getCreditiIniziali() {
		return CreditiIniziali;
	}
	public void setCreditiIniziali(double creditiIniziali) {
		CreditiIniziali = creditiIniziali;
	}
	public Date getOrario_inizio_prima_partita() {
		return Orario_inizio_prima_partita;
	}
	public void setOrario_inizio_prima_partita(Date orario_inizio_prima_partita) {
		Orario_inizio_prima_partita = orario_inizio_prima_partita;
	}
	public Date getOrario_inizio_ultima_partita() {
		return Orario_inizio_ultima_partita;
	}
	public void setOrario_inizio_ultima_partita(Date orario_inizio_ultima_partita) {
		Orario_inizio_ultima_partita = orario_inizio_ultima_partita;
	}
	public int getMaxNumfSquadre() {
		return MaxNumfSquadre;
	}
	public void setMaxNumfSquadre(int maxNumfSquadre) {
		MaxNumfSquadre = maxNumfSquadre;
	}
	public int getCurNumfSquadre() {
		return CurNumfSquadre;
	}
	public void setCurNumfSquadre(int curNumfSquadre) {
		CurNumfSquadre = curNumfSquadre;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "LegaComm [ID_Lega=" + ID_Lega + ", ID_Anno=" + ID_Anno
				+ ", NomeLega=" + NomeLega + ", ID_TipoLega=" + ID_TipoLega
				+ ", ID_TipoAsta=" + ID_TipoAsta + ", CreditiIniziali="
				+ CreditiIniziali + ", Orario_inizio_prima_partita="
				+ Orario_inizio_prima_partita
				+ ", Orario_inizio_ultima_partita="
				+ Orario_inizio_ultima_partita + ", created=" + created
				+ ", MaxNumfSquadre=" + MaxNumfSquadre + ", CurNumfSquadre="
				+ CurNumfSquadre + "]";
	}
	
}
