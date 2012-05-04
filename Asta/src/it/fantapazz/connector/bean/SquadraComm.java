package it.fantapazz.connector.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(value = {"ID"} )
public class SquadraComm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "Dal")
	private String dal;

	@JsonProperty(value = "ID_Lega")
	private String legaID;
	
	@JsonProperty(value = "ID_fSquadra")
	private String squadraID;
	
	@JsonProperty(value = "InvariantName")
	private String invariantName;
	
	@JsonProperty(value = "Alias")
	private String alias;
	
	@JsonProperty(value = "Stemma")
	private String stemma;
	
	@JsonProperty(value = "Uniforme")
	private String uniforme;
	
	@JsonProperty(value = "icoStemma")
	private String icoStemma;
	
	@JsonProperty(value = "uidPresidente")
	private String uidPresidente;
	
	@JsonProperty(value = "Creator")
	private String creator;

	@JsonProperty(value = "Creator_pic")
	private String creatorPic;

	@JsonProperty(value = "uidAllenatori")
	private List<String> allenatori;

	public String getLegaID() {
		return legaID;
	}

	public void setLegaID(String legaID) {
		this.legaID = legaID;
	}

	public String getSquadraID() {
		return squadraID;
	}

	public void setSquadraID(String squadraID) {
		this.squadraID = squadraID;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getStemma() {
		return stemma;
	}

	public void setStemma(String stemma) {
		this.stemma = stemma;
	}

	public String getUniforme() {
		return uniforme;
	}

	public void setUniforme(String uniforme) {
		this.uniforme = uniforme;
	}

	public String getIcoStemma() {
		return icoStemma;
	}

	public void setIcoStemma(String icoStemma) {
		this.icoStemma = icoStemma;
	}

	public String getUidPresidente() {
		return uidPresidente;
	}

	public void setUidPresidente(String uidPresidente) {
		this.uidPresidente = uidPresidente;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorPic() {
		return creatorPic;
	}

	public void setCreatorPic(String creatorPic) {
		this.creatorPic = creatorPic;
	}

	public List<String> getAllenatori() {
		return allenatori;
	}

	public void setAllenatori(List<String> allenatori) {
		this.allenatori = allenatori;
	}
	
	public String getDal() {
		return dal;
	}

	public void setDal(String dal) {
		this.dal = dal;
	}
	
	public String getInvariantName() {
		return invariantName;
	}

	public void setInvariantName(String invariantName) {
		this.invariantName = invariantName;
	}

	@Override
	public String toString() {
		return "SquadraComm [legaID=" + legaID + ", squadraID="
				+ squadraID + ", alias=" + alias + ", stemma=" + stemma
				+ ", uniforme=" + uniforme + ", icoStemma=" + icoStemma
				+ ", uidPresidente=" + uidPresidente + ", creator=" + creator
				+ ", creatorPic=" + creatorPic + ", allenatori=" + allenatori
				+ "]";
	}

}
