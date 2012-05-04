package it.fantapazz.asta.controller.bean;

import java.io.Serializable;
import java.util.List;

public class HelloControllerBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<AstaInfo> infos;
	
	public HelloControllerBean() {}
	
	public HelloControllerBean(List<AstaInfo> infos) {
		this.infos = infos;
	}

	public List<AstaInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<AstaInfo> infos) {
		this.infos = infos;
	}

}
