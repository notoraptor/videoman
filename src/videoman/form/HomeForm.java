package videoman.form;

import videoman.GUI;
import videoman.control.HomeController;

public class HomeForm extends Form<HomeController> {
	public HomeForm(GUI gui) {
		super(gui, FormName.home, "Bienvenue dans VideoMan!");
	}
}
