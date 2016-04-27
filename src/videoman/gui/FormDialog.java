package videoman.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import videoman.DialogController;
import videoman.form.Form;

public class FormDialog {
	public FormDialog(Form<? extends DialogController> form) throws Exception {
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		Parent root = form.root();
		DialogController dialogController = form.getController();
		Action action = () -> dialogStage.close();
		dialogController.setCancelAction(action);
		dialogController.setOkAction(action);
		dialogStage.setScene(new Scene(root));
		dialogStage.setTitle(form.title());
		dialogStage.showAndWait();
	}
}
