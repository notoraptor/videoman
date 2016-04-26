package videoman.core;

import videoman.Utils;

import java.io.File;

public class FileException extends Exception {
	public FileException(File filename, String error) {
		super("Exception pour le fichier: " + filename.getAbsolutePath() + Utils.endl + error);
	}
}
