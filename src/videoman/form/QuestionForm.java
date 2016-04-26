package videoman.form;

import javafx.stage.Stage;
import videoman.GUI;
import videoman.control.QuestionController;
import videoman.gui.Question;

public class QuestionForm extends Form<QuestionController> {
	private Question question;
	private Stage dialogStage;
	public QuestionForm(Question theQuestion, Stage stage) {
		super((GUI)null, FormName.question, null);
		question = theQuestion;
		dialogStage = stage;
	}
	public Question question() {
		return question;
	}
	public Stage dialogStage() {
		return dialogStage;
	}
}
