package it.fantapazz.utility;

import java.io.IOException;
import java.io.InputStream;

public class FilterInputStream extends InputStream {
	
	private InputStream in;
	
	public FilterInputStream(InputStream in) {
		this.in = in;
	}

	@Override
	public int read() throws IOException {
		
		int value = in.read();
		
		if ( value <= 0 )
			return value;
		
		if ( value == '\\')
			return read();
		
		return value;
		
	}

}
