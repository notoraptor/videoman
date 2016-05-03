package videoman.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import videoman.DialogController;
import videoman.form.Form;

public class FormDialog {
	public FormDialog(Form<? extends DialogController> form) throws Exception {
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		Action action = () -> dialogStage.close();
		Parent root = form.root();
		root.setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.ESCAPE)) try {
				action.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		DialogController dialogController = form.getController();
		dialogController.setCancelAction(action);
		dialogController.setOkAction(action);

		dialogStage.setScene(new Scene(root, 700, 400));
		dialogStage.setTitle(form.title());
		dialogStage.showAndWait();
	}
}
