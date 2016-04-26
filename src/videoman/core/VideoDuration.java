package videoman.core;

public class VideoDuration implements Comparable<VideoDuration> {
	private double duration;
	private long[] values;
	public VideoDuration(double videoDuration) {
		duration = videoDuration;
		values = new long[Const.DURATION_COUNT];
		values[Const.SECONDS] = Math.round(duration);
		long remainingSeconds = values[Const.SECONDS] % 60;
		values[Const.MINUTES] = values[Const.SECONDS] / 60;
		values[Const.SECONDS] = remainingSeconds;
		long remainingMinutes = values[Const.MINUTES] % 60;
		values[Const.HOURS] = values[Const.MINUTES] / 60;
		values[Const.MINUTES] = remainingMinutes;
	}
	private String string(long value) {
		return value > 9 ? String.valueOf(value) : '0' + String.valueOf(value);
	}
	public double toDouble() {
		return duration;
	}
	public long toLong() {
		return values[Const.SECONDS] + (values[Const.MINUTES] * 60L) + (values[Const.HOURS] * 3600L);
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < values.length; ++i) {
			if (s.length() == 0) {
				if (values[i] != 0)
					s.append(string(values[i])).append(Const.durationNames[i]);
			} else
				s.append(' ').append(string(values[i])).append(Const.durationNames[i]);
		}
		if (s.length() == 0)
			s.append(0).append(Const.durationNames[Const.durationNames.length - 1]);
		return s.toString();
		//return hours + "h " + minutes + "min " + seconds + "sec";
	}
	@Override
	public int compareTo(VideoDuration other) {
		return Double.compare(duration, other.duration);
	}
	@Override
	public boolean equals(Object o) {
		return o instanceof VideoDuration && Double.compare(duration, ((VideoDuration) o).duration) == 0;
	}
	@Override
	public int hashCode() {
		return Double.hashCode(duration);
	}
}
