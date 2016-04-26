package videoman.core;

import videoman.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
	private InputStream is;
	private String type;
	private StringBuilder content;
	public StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
		this.content = new StringBuilder();
	}
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null)
				content.append(line).append(Utils.endl);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	public StringBuilder get() {
		return content;
	}
}
