package it.fantapazz.connector;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamCounter extends InputStream {
	
	private int bytes;
	
	private InputStream in;
	
	private boolean ended;
	
	public InputStreamCounter(InputStream in) {
		this.in = in;
		ended = false;
	}

	@Override
	public int read() throws IOException {
		
		int value = in.read();
		
		if ( value <= 0 ) {
			ended = true;
			return value;
		}
		
		bytes ++;
		
		return value;
		
	}

	public int getBytes() {
		return bytes;
	}
	
	public boolean isEnded() {
		return ended;
	}

}
