package videoman.notification;

public class DefaultNotifier implements Notifier {
	@Override
	public void good(String notification) {
		System.out.println(notification);
	}
	@Override
	public void bad(String notification) {
		System.err.println(notification);
	}
}
