package videoman.notification;

public interface Notifier {
	void good(String notification);
	void bad(String notification);
	default void info(Info info) {
		// Nothing.
	}
}
