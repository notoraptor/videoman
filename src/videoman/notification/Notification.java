package videoman.notification;

public class Notification {
	static private Notifier manager = new DefaultNotifier();
	static public void setManager(Notifier notifier) {
		manager = notifier;
	}
	static public boolean good(String notification) {
		if (manager != null)
			manager.good(notification);
		return true;
	}
	static public boolean good(StringBuilder notification) {
		if (manager != null)
			manager.good(notification.toString());
		return true;
	}
	static public boolean bad(String notification) {
		if (manager != null)
			manager.bad(notification);
		return false;
	}
	static public void info(Info info) {
		if (manager != null) manager.info(info);
	}
}
