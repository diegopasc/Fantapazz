package it.fantapazz.utility;

import java.io.IOException;
import java.io.InputStream;

public class PrintStream extends InputStream {
	
	private InputStream in;
	
	public PrintStream(InputStream in) {
		this.in = in;
	}

	@Override
	public int read() throws IOException {
		int value = in.read();
		if ( value <= 0 ) {
			System.out.println();
			return value;
		}
		System.out.print(new String(new byte[] { (byte) value }));
		return value;
	}

}
