package videoman.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import videoman.GUI;
import videoman.form.AlertForm;

public class Alert {
	public Alert(GUI gui, String title, String message, String buttonTitle) throws Exception {
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.WINDOW_MODAL);
		Parent root = new AlertForm(gui, title, message, buttonTitle).root();
		Button okButton = (Button) root.lookup("#button");
		okButton.setOnAction((event) -> dialogStage.close());
		dialogStage.setScene(new Scene(root));
		dialogStage.setTitle(title);
		dialogStage.show();
	}
	public Alert(GUI gui, String title, String message) throws Exception {
		this(gui, title, message, "OK");
	}
}