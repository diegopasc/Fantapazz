package it.fantapazz.test.other;

public class TestSynch implements Runnable {

	public void run() {

		synchronized (this) {
			System.out.println("Prepare");
			try {
				wait(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Pippo");
		}

	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		TestSynch t1 = new TestSynch();
		Thread t2 = new Thread(t1);
		t2.start();
		
		synchronized (t2) {
			
			System.out.println("alskjd laksCiao");
			
			for (int i = 0; i < 1000; i ++)
				System.out.println(i);

			System.out.println("Ciao");
			
		}
		
	}

}
