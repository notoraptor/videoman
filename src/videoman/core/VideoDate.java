package videoman.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoDate implements Comparable<VideoDate> {
	private long milliseconds;
	public VideoDate(long millitime) {
		milliseconds = millitime;
	}
	public VideoDate() {
		milliseconds = System.currentTimeMillis();
	}
	public long getMilliseconds() {
		return milliseconds;
	}
	@Override
	public String toString() {
		Date date = new Date(milliseconds);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd LLL yyyy - HH' : 'mm' : 'ss");
		return sdf.format(date);
	}
	@Override
	public int compareTo(VideoDate other) {
		long t = milliseconds - other.milliseconds;
		return t == 0 ? 0 : (t < 0 ? -1 : 1);
	}
	@Override
	public boolean equals(Object other) {
		return other instanceof VideoDate && milliseconds == ((VideoDate) other).milliseconds;
	}
	@Override
	public int hashCode() {
		return Long.hashCode(milliseconds);
	}
}