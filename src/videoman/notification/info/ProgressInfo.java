package videoman.notification.info;

import videoman.notification.Info;

public class ProgressInfo extends Info<Long> {
	private long endValue;
	public ProgressInfo(long from, long end) {
		super(from);
		endValue = end;
	}
	public ProgressInfo(long v) {
		super(v);
		endValue = v;
	}
	public long current() {
		return get();
	}
	public long length() {
		return endValue;
	}
}
