package it.fantapazz.test.other;

import it.fantapazz.utility.Timeout;
import it.fantapazz.utility.Timer;

public class TestTimeout {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Timer timer = new Timer(new Timeout() {
			public void timeout(Timer timer) {
				System.out.println("Out of time");
			}
		}, 5000);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		timer.stop();
		
		System.out.println("Ciao");

	}

}
