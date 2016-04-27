package videoman.core;

public class VideoQuality implements Comparable<VideoQuality> {
	double quality;
	public VideoQuality(Video video) {
		int width = video.getWidth();
		int height = video.getHeight();
		double duration = video.getDuration().toDouble();
		long fileize = video.getSize();
		int notation = video.getNotation().ordinal();
		quality = 16*Math.log(width) + 8*Math.log(duration) + 4*Math.log(height) + 2*Math.log(fileize) + Math.log(notation + 1);
	}
	@Override
	public boolean equals(Object o) {
		return o instanceof VideoQuality && Double.compare(quality, ((VideoQuality) o).quality) == 0;
	}
	@Override
	public int hashCode() {
		return Double.hashCode(quality);
	}
	@Override
	public String toString() {
		return String.valueOf(Math.round(quality * 100L) / (double) 100);
	}
	@Override
	public int compareTo(VideoQuality other) {
		return Double.compare(quality, other.quality);
	}
}
