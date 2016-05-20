package videoman.core;

import java.io.File;

public class FileDesc extends File {
	private String name;
	private String extension;
	public FileDesc(String path) {
		super(new File(path).getAbsolutePath());
		init();
	}
	public FileDesc(File file) {
		this(file.getAbsolutePath());
	}
	public FileDesc(File parent, String name) {
		super(new File(parent.getAbsolutePath()), name);
		init();
	}
	private void init() {
		name = super.getName();
		int lastPosition = name.lastIndexOf('.');
		if (lastPosition < 0) {
			extension = "";
		} else {
			extension = name.substring(lastPosition + 1);
			name = name.substring(0, lastPosition);
		}
	}
	public String getName() {
		return name;
	}
	public String getExtension() {
		return extension;
	}
	static public File parentFolder(File path) {
		File parent = path.getParentFile();
		return parent == null ? new File(".") : parent;
	}
	public String getAbsoluteName() {
		File file = new File(parentFolder(this), name);
		return file.getAbsolutePath();
	}
}
