package it.fantapazz.connector;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamCounter extends OutputStream {
	
	private int bytes;
	
	private OutputStream out;
	
	public OutputStreamCounter(OutputStream out) {
		this.out = out;
	}

	public int getBytes() {
		return bytes;
	}
	
	@Override
	public void write(int value) throws IOException {
		bytes ++;
		out.write(bytes);
	}
	
	@Override
	public void flush() throws IOException {
		out.flush();
	}

}
