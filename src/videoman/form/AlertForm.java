package videoman.form;

import videoman.GUI;
import videoman.control.AlertController;

public class AlertForm extends Form<AlertController> {
	public String alertTitle;
	public String alertMessage;
	public String buttonTitle;
	public AlertForm(GUI gui, String alertTitle, String alertMessage, String buttonTitle) {
		super(gui, FormName.alert, null);
		this.alertTitle = alertTitle;
		this.alertMessage = alertMessage;
		this.buttonTitle = buttonTitle;
	}
}
