package it.fantapazz.utility;

import it.fantapazz.ConfigSettings;

public class StatsCommunication implements Runnable {
		
	private static StatsCommunication instance;

	private Metric bytesSent;
	private Metric bytesReceived;
	private Metric timeToMarshal;
	private Metric timeToUnmarshal;
	private Metric timeToMarshalPerByte;
	private Metric timeToUnmarshalPerByte;
	private Metric zipRatio;
	private Metric pingTime;

	public static StatsCommunication instance() {
		if (instance == null) {
			instance = new StatsCommunication();
			if ( ConfigSettings.instance().isActivateStats()) {
				new Thread(instance).start();
			}
		}
		return instance;
	}
	
	private StatsCommunication() {
		zipRatio = new Metric();
		bytesSent = new Metric();
		bytesReceived = new Metric();
		timeToMarshal = new Metric();
		timeToUnmarshal = new Metric();
		timeToMarshalPerByte = new Metric();
		timeToUnmarshalPerByte = new Metric();
		pingTime = new Metric();
	}
		
	public void clear() {
		zipRatio.clear();
		bytesSent.clear();
		bytesReceived.clear();
		timeToMarshal.clear();
		timeToUnmarshal.clear();
		timeToMarshalPerByte.clear();
		timeToUnmarshalPerByte.clear();
		pingTime.clear();
	}
	
	public void addPingTime(double time) {
		pingTime.add(time);
	}

	public void sent(int bytes) {
		bytesSent.add(bytes);
	}

	public void received(int bytes) {
		bytesReceived.add(bytes);
	}
	
	public void addTimeMarshal(int bytes, long time) {
		timeToMarshalPerByte.add((double) bytes / (double) time * 1000.0);
		timeToMarshal.add((double) time / 1000.0);
	}

	public void addTimeUnmarshal(int bytes, long time) {
		timeToUnmarshalPerByte.add((double) bytes / (double) time * 1000.0);
		timeToUnmarshal.add((double) time / 1000.0);
	}
	
	public void addRatio(double ratio) {
		zipRatio.add(ratio);		
	}
	
	public void dump() {
		synchronized (System.out) {
			System.out.println("------- STATS ------");
			System.out.println("TX: " + bytesSent.avg() + " bytes");
			System.out.println("RX: " + bytesReceived.avg() + " bytes");
			System.out.println("Marshal Per Byte: " + timeToMarshalPerByte.avg() + " bytes/sec");
			System.out.println("Unmarshal Per Byte: " + timeToUnmarshalPerByte.avg() + " bytes/sec");
			System.out.println("Marshal Time: " + timeToMarshal.sum() + " sec");
			System.out.println("Unmarshal Time: " + timeToUnmarshal.sum() + " sec");
			System.out.println("Zip Ratio: " + zipRatio.avg() + " %");
			System.out.println("Ping Time: " + pingTime.avg() + " sec");
			System.out.println("--------------------");
		}
	}

	public void run() {
		while (true) {
			dump();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
