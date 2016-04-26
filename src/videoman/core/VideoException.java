package videoman.core;

import java.io.File;

public class VideoException extends FileException {
	public VideoException(File file, String error) {
		super(file, error);
	}
}
