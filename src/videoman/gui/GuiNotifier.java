package videoman.gui;

import javafx.application.Platform;
import videoman.GUI;
import videoman.notification.DefaultNotifier;
import videoman.notification.Info;
import videoman.notification.Informer;
import videoman.notification.Notifier;

public class GuiNotifier implements Notifier {
	final private GUI gui;
	final private Long synchronizer;
	private DefaultNotifier defaultNotifier;
	private Informer informer;
	public GuiNotifier(GUI gui) {
		this.gui = gui;
		synchronizer = 0L;
		defaultNotifier = new DefaultNotifier();
		informer = null;
	}
	public void setInformer(Informer im) {
		synchronized (synchronizer) {
			informer = im;
		}
	}
	@Override
	public void good(String notification) {
		Platform.runLater(() -> {
			try {
				new Alert(gui, "VideoMan", notification);
			} catch (Exception e) {
				defaultNotifier.good(notification);
				e.printStackTrace();
			}
		});
	}
	@Override
	public void bad(String notification) {
		Platform.runLater(() -> {
			try {
				new Alert(gui, "VideoMan", notification);
			} catch (Exception e) {
				defaultNotifier.bad(notification);
				e.printStackTrace();
			}
		});
	}
	@Override
	public void info(Info info) {
		Platform.runLater(() -> {
			synchronized (synchronizer) {
				if (informer != null) try {
					informer.inform(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
