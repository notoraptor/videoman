package videoman.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import videoman.form.QuestionForm;
import videoman.gui.Question;

public class QuestionController extends Controller<QuestionForm> {
	@FXML
	private Label messageLabel;

	@FXML
	private Label detailsLabel;

	@FXML
	private HBox actionParent;

	@FXML
	private Button cancelButton;

	@FXML
	private HBox okParent;

	@FXML
	private Button okButton;
	@Override
	public void init(QuestionForm questionForm) {
		Question question = questionForm.question();
		Stage dialogStage = questionForm.dialogStage();
		messageLabel.setText(question.getTitle());
		detailsLabel.setText(question.getQuestion());
		cancelButton.setText(question.getNegativeLabel());
		okButton.setText(question.getPositiveLabel());
		cancelButton.setOnAction(event -> {
			dialogStage.close();
			if(question.getNegativeAction() != null) {
				try {
					question.getNegativeAction().execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		okButton.setOnAction(event -> {
			dialogStage.close();
			if(question.getPositiveAction() != null) {
				try {
					question.getPositiveAction().execute();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
