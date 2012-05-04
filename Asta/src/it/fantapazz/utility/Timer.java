package it.fantapazz.utility;

import java.util.TimerTask;

public class Timer {
	
	private long startTime;
	
	private long endTime;
	
	private Timeout timeout;

	private TimeoutImpl timeoutImpl;

	public Timer(Timeout timeout, long delay) {
		this.timeout = timeout;
		this.timeoutImpl = new TimeoutImpl();
		this.timeoutImpl.start(delay);
	}
	
	public long getRemainTime() {
		if ( timeoutImpl.isStarted() )
			return endTime - System.currentTimeMillis();
		return -1;
	}
	
	public void stop() {
		timeoutImpl.stop();
	}
	
	class TimeoutImpl extends TimerTask {
		
		private java.util.Timer timer;
		
		public void start(long delay) {
			if (this.timer == null ) {
				this.timer = new java.util.Timer();
				startTime = System.currentTimeMillis();				
				this.timer.schedule(this, delay);
				endTime = startTime + delay;
			}
		}
		
		public void stop() {
			if ( this.timer != null ) {
				this.timer.cancel();
				this.timer = null;
			}
		}
		
		public boolean isStarted() {
			return this.timer != null;
		}

		@Override
		public void run() {
			stop();
			timeout.timeout(Timer.this);
		}

	}

}
