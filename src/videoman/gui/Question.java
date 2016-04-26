package videoman.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import videoman.form.QuestionForm;

public class Question {
	private String title;
	private String question;
	private String positiveLabel;
	private String negativeLabel;
	private Action positiveAction;
	private Action negativeAction;
	public Question() {
		title = "Question";
		positiveLabel = "Oui";
		negativeLabel = "Annuler";
	}
	public void setTitle(String theTitle) {
		title = theTitle;
	}
	public void setQuestion(String theQuestion) {
		question = theQuestion;
	}
	public void setPositiveLabel(String theLabel) {
		positiveLabel = theLabel;
	}
	public void setNegativeLabel(String theLabel) {
		negativeLabel = theLabel;
	}
	public void setPositiveAction(Action theAction) {
		positiveAction = theAction;
	}
	public void setNegativeAction(Action theAction) {
		negativeAction = theAction;
	}
	public String getTitle() {
		return title;
	}
	public String getQuestion() {
		return question;
	}
	public String getNegativeLabel() {
		return negativeLabel;
	}
	public String getPositiveLabel() {
		return positiveLabel;
	}
	public Action getNegativeAction() {
		return negativeAction;
	}
	public Action getPositiveAction() {
		return positiveAction;
	}
	public void show() throws Exception {
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		Parent root = new QuestionForm(this, dialogStage).root();
		dialogStage.setScene(new Scene(root));
		dialogStage.setTitle(title);
		dialogStage.show();
	}
}
