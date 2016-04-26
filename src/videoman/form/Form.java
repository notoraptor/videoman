package videoman.form;

import javafx.fxml.FXMLLoader;
import videoman.GUI;
import videoman.control.Controller;

abstract public class Form<C extends Controller> {
	private GUI gui;
	private FormName name;
	private String filename;
	private String title;
	private C controller;
	public Form(GUI gui, FormName formName, String title) {
		this.gui = gui;
		this.name = formName;
		this.filename = formName + ".fxml";
		this.title = title;
	}
	public Form(Form from, FormName formName, String formTitle) {
		this(from.gui(), formName, formTitle);
	}
	final public GUI gui() {
		return gui;
	}
	final public String title() {
		return title;
	}
	final public <T> T root() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/videoman/fxml/" + filename));
		T root = loader.load();
		controller = loader.getController();
		controller.init(this);
		return root;
	}
	final public C getController() {
		return controller;
	}
}
