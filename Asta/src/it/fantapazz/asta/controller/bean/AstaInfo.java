package it.fantapazz.asta.controller.bean;

import it.fantapazz.asta.core.AstaServer;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonGetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSetter;

/**
 * Configuration of Asta used to express info about a running asta or
 * to create a new one.
 * 
 * @author Michele Mastrogiovanni
 */
@JsonIgnoreProperties(value = {"server"})
public class AstaInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum Status { 
		NOT_STARTED,		// Never started 
		RUNNING, 			// Started and running
		// PAUSED, 			// Started and not runnig
		TERMINATED 			// Terminated
	};
	
	@JsonProperty(value = "ID_Asta")
	private String ID;
	
	@JsonProperty(value = "ID_Lega")
	private String IDLega;
	
	private String port;
	
	@JsonProperty(value = "ID_fSquadre")
	private List<String> players;
	
	@JsonProperty(value = "ID_Calciatori")
	private List<String> calciatori;
	
	@JsonSetter(value = "startDate")
	public void setStartDateFormatted(String data) {
		if ( data == null ) {
			this.startDate = null;
			return;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(data);
			this.startDate = date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@JsonGetter(value = "startDate")
	public String getStartDateFormatted() {
		if (startDate == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(startDate);
	}
	
	@JsonSetter(value = "Status")
	public void setStatus(String value) {
		setStatus(AstaInfo.Status.valueOf(value));
	}
	
	private List<String> remainCalciatori;
	
	private Status status;
	
	private double startMoney;

	@JsonIgnore
	private Date startDate;
	
	@JsonIgnore
	private transient AstaServer server;
	
	public AstaInfo() {
		status = Status.NOT_STARTED;
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

	public List<String> getCalciatori() {
		return calciatori;
	}

	public void setCalciatori(List<String> calciatori) {
		this.calciatori = calciatori;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public double getStartMoney() {
		return startMoney;
	}

	public void setStartMoney(double startMoney) {
		this.startMoney = startMoney;
	}

//	public Date getStartDate() {
//		return startDate;
//	}

//	@JsonIgnore
//	public void setStartDate(Date startDate) {
//		this.startDate = startDate;
//	}

	public List<String> getRemainCalciatori() {
		return remainCalciatori;
	}

	public void setRemainCalciatori(List<String> remainCalciatori) {
		this.remainCalciatori = remainCalciatori;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public AstaServer getServer() {
		return server;
	}

	public void setServer(AstaServer server) {
		this.server = server;
	}

	public String getIDLega() {
		return IDLega;
	}

	public void setIDLega(String iDLega) {
		IDLega = iDLega;
	}

	@Override
	public String toString() {
		return "AstaInfo [ID=" + ID + ", IDLega=" + IDLega + ", port=" + port
				+ ", players=" + players + ", calciatori=" + calciatori
				+ ", remainCalciatori=" + remainCalciatori + ", status="
				+ status + ", startMoney=" + startMoney + ", startDate="
				+ startDate + "]";
	}
	
}
