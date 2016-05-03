package videoman.core;

public class VideoSize implements Comparable<VideoSize> {
	static private final String B = " o";
	static private final String KB = " Ko";
	static private final String MB = " Mo";
	static private final String GB = " Go";
	static private final long oneKB = 1024L;
	static private final long oneMB = 1024L*oneKB;
	static private final long oneGB = 1024L*oneMB;
	static private final char thousandSeparator = ' ';
	static private final char decimalSeparator = ',';
	private long byteCount;
	private double value;
	private String unit;
	private String representation;
	public VideoSize(long theByteCount) {
		byteCount = theByteCount;
		if(byteCount - oneGB >= 0) {
			value = byteCount / (double) oneGB;
			unit = GB;
		} else if (byteCount - oneMB >= 0) {
			value = byteCount / (double) oneMB;
			unit = MB;
		} else if (byteCount - oneKB >= 0) {
			value = byteCount / (double) oneKB;
			unit = KB;
		} else {
			value = byteCount;
			unit = B;
		}
		value = Math.round(value * 100L) / (double) 100;
		String integerPart = String.valueOf((long) value);
		int integerPartLength = integerPart.length();
		int residualDigitLength = integerPartLength % 3;
		int thousandCount = integerPartLength / 3;
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < residualDigitLength; ++i)
			s.append(integerPart.charAt(i));
		for (int i = 0; i < thousandCount; ++i) {
			int start = residualDigitLength + (3 * i);
			int end = start + 3;
			if(s.length() > 0) s.append(thousandSeparator);
			for (int j = start; j < end; ++j)
				s.append(integerPart.charAt(j));
		}
		long decimalPart = (long)((value - (long) value) * 100L);
		if(decimalPart > 0) {
			s.append(decimalSeparator).append(decimalPart);
		}
		s.append(unit);
		representation = s.toString();
	}
	@Override
	public int compareTo(VideoSize other) {
		return Long.compare(byteCount, other.byteCount);
	}
	@Override
	public int hashCode() {
		return Long.hashCode(byteCount);
	}
	@Override
	public boolean equals(Object o) {
		return o instanceof VideoSize && byteCount == ((VideoSize) o).byteCount;
	}
	@Override
	public String toString() {
		return representation;
	}
}
