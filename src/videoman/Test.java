package videoman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import videoman.core.Notation;
import videoman.gui.NotationChooser;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		NotationChooser notationChooser = new NotationChooser(Notation.FOUR);
		Scene scene = new Scene(notationChooser.getNode());
		stage.setTitle("Table View Sample");
		stage.setWidth(700);
		stage.setHeight(500);
		stage.setScene(scene);
		stage.show();
	}
}