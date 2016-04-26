package videoman.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import videoman.form.AlertForm;

public class AlertController extends Controller<AlertForm> {

	@FXML
	private Label title;

	@FXML
	private Label message;

	@FXML
	private Button button;

	@Override
	public void init(AlertForm form) {
		title.setText(form.alertTitle);
		message.setText(form.alertMessage);
		button.setText(form.buttonTitle);
	}
}
