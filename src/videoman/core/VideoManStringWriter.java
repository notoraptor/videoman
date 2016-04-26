package videoman.core;

import videoman.Utils;

import java.io.StringWriter;

public class VideoManStringWriter extends StringWriter {
	public void newLine() {
		write(Utils.endl);
	}
}
