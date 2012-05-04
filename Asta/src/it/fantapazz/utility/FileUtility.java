package it.fantapazz.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtility {

	/**
	 * Join two streams: reverse a input stream into an output stream
	 * 
	 * @param in Input where data must be taken
	 * @param out Output where data must be reversed
	 * @throws IOException
	 */
	public static void joinStreams(InputStream in, OutputStream out) throws IOException {
		int len = 0;
		byte[] tmp = new byte[2048];
		while ((len = in.read(tmp)) != -1) {
			out.write(tmp, 0, len);
		}
		out.flush();
	}

}
