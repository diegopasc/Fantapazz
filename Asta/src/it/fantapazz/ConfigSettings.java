package it.fantapazz;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ConfigSettings {
	
	private static ConfigSettings instance;
	
	// Dump messages sent and received
	private boolean dumpMessages;
	
	// Enable variable zipping of messages 
	private boolean enableZip;
	
	// Use json format for messages
	private boolean useJson;
	
	// Activate statistics about communication
	private boolean activateStats;

	// If this flag is false, asta will not be changed
	// in status on fantapazz (default: true)
	private boolean terminateAsta;

	// Use fake connector and not to fantapazz connector
	private boolean useFakeConnector;
	
	// Use json format for messages
	private boolean enablePing;

	// Time every while player will ping server to establish
	// the channel behaviour
	private double pingTime;

	// Auto-login parameters 
	// public static final boolean autoLogin = false;
	// public static final String loginUsername = "michele";
	// public static final String loginPassword = "pippo80";

	// Host connection
	private String hostConnection;

	// Enable proxy for connection
	private boolean proxyEnabled;
	private String proxyHost;
	private String proxyPort;
	private String proxyUsername;
	private String proxyPassword;
	
	public static ConfigSettings instance() {
		if (instance == null) {
			Resource resource = new ClassPathResource("config.xml");
			BeanFactory beanFactory = new XmlBeanFactory(resource);
			instance = (ConfigSettings) beanFactory.getBean("settings");
		}
		return instance;
	}

	public boolean isDumpMessages() {
		return dumpMessages;
	}

	public void setDumpMessages(boolean dumpMessages) {
		this.dumpMessages = dumpMessages;
	}

	public boolean isEnableZip() {
		return enableZip;
	}

	public void setEnableZip(boolean enableZip) {
		this.enableZip = enableZip;
	}

	public boolean isUseJson() {
		return useJson;
	}

	public void setUseJson(boolean useJson) {
		this.useJson = useJson;
	}

	public boolean isActivateStats() {
		return activateStats;
	}

	public void setActivateStats(boolean activateStats) {
		this.activateStats = activateStats;
	}

	public boolean isTerminateAsta() {
		return terminateAsta;
	}

	public void setTerminateAsta(boolean terminateAsta) {
		this.terminateAsta = terminateAsta;
	}

	public boolean isUseFakeConnector() {
		return useFakeConnector;
	}

	public void setUseFakeConnector(boolean useFakeConnector) {
		this.useFakeConnector = useFakeConnector;
	}

	public boolean isEnablePing() {
		return enablePing;
	}

	public void setEnablePing(boolean enablePing) {
		this.enablePing = enablePing;
	}

	public double getPingTime() {
		return pingTime;
	}

	public void setPingTime(double pingTime) {
		this.pingTime = pingTime;
	}

	public String getHostConnection() {
		return hostConnection;
	}

	public void setHostConnection(String hostConnection) {
		this.hostConnection = hostConnection;
	}

	public boolean isProxyEnabled() {
		return proxyEnabled;
	}

	public void setProxyEnabled(boolean proxyEnabled) {
		this.proxyEnabled = proxyEnabled;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUsername() {
		return proxyUsername;
	}

	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

}
