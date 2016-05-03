package videoman;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import videoman.core.database.Database;
import videoman.form.Form;
import videoman.form.HomeForm;
import videoman.gui.GuiNotifier;
import videoman.notification.Informer;
import videoman.notification.Notification;

import java.io.File;
import java.util.LinkedList;

public class GUI extends Application {
	private GuiNotifier notifier;
	private LinkedList<Form> history;
	private Stage stage;
	private Database database;
	static public void main(String[] args) {
		launch(args);
	}
	public void setInformer(Informer informer) {
		notifier.setInformer(informer);
	}
	public File getFile(FileChooser fileChooser) {
		return fileChooser.showOpenDialog(stage);
	}
	public File getDirectory(DirectoryChooser directoryChooser) {
		return directoryChooser.showDialog(stage);
	}
	public File createFile(FileChooser fileChooser) {
		return fileChooser.showSaveDialog(stage);
	}
	private void loadFirst(Form form) throws Exception {
		Scene scene = new Scene((Parent) form.root());
		scene.getStylesheets().add("/videoman/resource/style.css");
		stage.setTitle(form.title());
		stage.setScene(scene);
		history.push(form);
	}
	public void load(Form form) throws Exception {
		notifier.setInformer(null);
		stage.setTitle(form.title());
		stage.getScene().setRoot((Parent) form.root());
		history.push(form);
	}
	public void back() throws Exception {
		if (!history.isEmpty()) history.pop();
		if (!history.isEmpty()) load(history.pop());
	}
	public void fullBack() throws Exception {
		Form form = null;
		while (!history.isEmpty()) form = history.pop();
		if (form != null) load(form);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		notifier = new GuiNotifier(this);
		history = new LinkedList<>();
		stage = primaryStage;
		stage.setMinWidth(700);
		stage.setMinHeight(500);
		Notification.setManager(notifier);
		loadFirst(new HomeForm(this));
		stage.show();
	}
	@Override
	public void stop() throws Exception {
		System.out.println("sortie");
		if (database != null)
			database.save();
		super.stop();
	}
	public void setDatabase(Database database) {
		this.database = database;
	}
	public Database getDatabase() {
		return database;
	}
}
