package videoman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import videoman.core.database.Person;
import videoman.gui.EditableList;
import videoman.gui.SharedPropertyLabel;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		VBox vBox = new VBox();
		EditableList<String> list = new EditableList<>(vBox);
		SharedPropertyLabel<String> element = new SharedPropertyLabel<>(list, "essai", 1, 2);
		list.add(element);
		Scene scene = new Scene(vBox);
		stage.setTitle("Table View Sample");
		stage.setWidth(700);
		stage.setHeight(500);
		stage.setScene(scene);
		stage.show();
	}
}