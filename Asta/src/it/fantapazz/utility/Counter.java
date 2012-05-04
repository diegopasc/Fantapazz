package it.fantapazz.utility;

public class Counter {
	
	private static int count = 0;
	
	public static synchronized int getCounter() {
		return count ++;
	}

}
