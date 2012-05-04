package it.fantapazz.utility;

public class Metric {

	private double value;
	private double count;
	
	public void add(double v) {
		value += v;
		count += 1.0;
	}
	
	public void clear() {
		value = 0.0;
		count = 0.0;
	}
	
	public double sum() {
		return value;
	}
	
	public double count() {
		return count;
	}
	
	public double avg() {
		return (value / count);
	}
	
}
