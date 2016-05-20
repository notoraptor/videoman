package videoman;

import videoman.core.StreamGobbler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

public class Utils {
	static final public String endl = System.getProperty("line.separator");
	static final public String hex = "0123456789ABCDEF";
	static final public Charset UTF8 = StandardCharsets.UTF_8;
	static final private String pattern = "(?U)[^\\p{Alpha}0-9]+";
	static public String hash(String message) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] hash = md.digest(message.getBytes(Utils.UTF8));
		return hexString(hash);
	}
	static public String hexString(byte[] array) {
		char[] val = new char[2 * array.length];
		for (int i = 0; i < array.length; i++) {
			int b = array[i] & 0xff;
			val[2 * i] = hex.charAt(b >>> 4);
			val[2 * i + 1] = hex.charAt(b & 15);
		}
		return String.valueOf(val);
	}
	static public StringBuilder execute(String[] commands) throws IOException, InterruptedException {
		//System.err.print("<execution>\t");
		//System.err.println(Utils.implode(" ", Arrays.asList(commands)));
		Process p = Runtime.getRuntime().exec(commands);
		StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
		StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
		errorGobbler.start();
		outputGobbler.start();
		p.waitFor();
		//System.err.println("</execution exit=" + status + ">");
		errorGobbler.join();
		outputGobbler.join();
		return outputGobbler.get();
	}
	static public <T> StringBuilder implode(String delimiter, Iterable<T> set) {
		StringBuilder imploded = new StringBuilder();
		Iterator<T> iterator = set.iterator();
		if (iterator.hasNext())
			imploded.append(iterator.next().toString());
		while (iterator.hasNext())
			imploded.append(delimiter).append(iterator.next().toString());
		return imploded;
	}
	static public <T> StringBuilder implode(String delimiter, T[] set) {
		StringBuilder imploded = new StringBuilder();
		if(set.length > 0)
			imploded.append(set[0].toString());
		for(int i = 1; i < set.length; ++i)
			imploded.append(delimiter).append(set[i].toString());
		return imploded;
	}
	static public String getExtension(File file) {
		String name = file.getName();
		int position = name.lastIndexOf('.');
		return position < 0 ? "" : name.substring(position + 1).toLowerCase();
	}
	static public String getName(File file) {
		String name = file.getName();
		int position = name.lastIndexOf('.');
		return position < 0 ? name : name.substring(0, position);
	}
	static public String[] explode(String string) {
		return string.split(pattern);
	}
	static public String orthograph(int size, String word, String suffix) {
		return size + " " + word + (size == 0 || size == 1 ? "" : suffix);
	}
	static public String orthograph(int size, String word) {
		return orthograph(size, word, "s");
	}
}
