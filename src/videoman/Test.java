package videoman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import videoman.gui.EditableList;
import videoman.gui.RemovableLabel;
import videoman.gui.SharedLabel;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		VBox vBox = new VBox();
		EditableList<String> list = new EditableList<>(vBox);
		SharedLabel<String> element1 = new SharedLabel<>(list, "element 1 lorem ipsum", 1, 2);
		RemovableLabel<String> element2 = new RemovableLabel<>(list, "element 2 lorem ipsum");
		list.add(element1);
		list.add(element2);
		Scene scene = new Scene(vBox);
		stage.setTitle("Table View Sample");
		stage.setWidth(700);
		stage.setHeight(500);
		stage.setScene(scene);
		stage.show();
	}
}