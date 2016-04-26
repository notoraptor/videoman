package videoman.notification.info;

import videoman.notification.Info;
import videoman.notification.Notification;

public class MessageInfo extends Info<String> {
	private MessageInfo(String message) {
		super(message);
	}
	static public void inform(String message) {
		System.err.println(message);
		Notification.info(new MessageInfo(message));
	}
	static public void inform(Object o) {
		Notification.info(new MessageInfo(o.toString()));
	}
}
