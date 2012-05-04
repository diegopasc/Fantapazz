package it.fantapazz.test.other;


public class TestThread implements Runnable {
	
	public void run() {

		Thread.currentThread().interrupt();
		
		System.out.println("Ciao");
		
		try {
			Thread.sleep(5000);
		}
		catch (InterruptedException e) {
			System.out.println("Interrupted");
			e.printStackTrace();
		}
		
		System.out.println("Ended");
		
	}
	
	public static void main(String[] args) {
		
		Thread t = new Thread(new TestThread());
		
		t.start();
		
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		t.interrupt();
		
	}

}
