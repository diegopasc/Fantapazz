package other.fantapazz.gui.utility;

import it.fantapazz.utility.Timeout;
import it.fantapazz.utility.Timer;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class TimerComponent extends JProgressBar implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private Timer timer;
	
	private long delay;
	
	private long start;
	
	private Thread checkThread;
	
	private JLabel label;
	
	public TimerComponent(JLabel label) {
		super(0, 100);
		this.label = label;
	}
	
	public void start(final long delay) {
		if ( timer == null ) {
			setValue(0);
			this.delay = delay;
			this.start = System.currentTimeMillis();
			timer = new Timer(new Timeout() {
				public void timeout(Timer timer) {
					checkThread.interrupt();
					checkThread = null;
					setDelay(delay);
				}
			}, delay);
			checkThread = new Thread(this);
			checkThread.start();
		}
	}
	
	public void stop() {
		if ( timer != null ) {
			setValue(0);
			if ( checkThread != null ) {
				checkThread.interrupt();
				checkThread = null;
			}
			timer.stop();
			timer = null;
		}
	}
	
	public void run() {
		while ( true ) {
			long currentTime = System.currentTimeMillis();
			long delta = currentTime - start;
			setDelay(delta);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void setDelay(long delta) {
		if ( delta >= delay ) {
			setValue(100);
			if ( label != null ) {
				String text = String.format("%.1f s", 0.0 );
				label.setText(text);
			}
		}
		else {
			double percentage = ( (double) delta / (double) delay ) * 100.0;
			setValue((int) percentage);
			if ( label != null ) {
				String text = String.format("%.1f s", (double) (delay - delta) / 1000.0);
				label.setText(text);
			}
		}
	}

}
