package videoman.form;

import videoman.GUI;
import videoman.control.DatabaseController;
import videoman.core.database.Database;

public class DatabaseForm extends Form<DatabaseController> {
	private Database database;
	public DatabaseForm(GUI gui, Database db) {
		super(gui, FormName.database, "Collection \"" + db.getDirectory().getName() + "\" (" + db.size() + " vidéos)");
		database = db;
	}
	public Database getDatabase() {
		return database;
	}
}
