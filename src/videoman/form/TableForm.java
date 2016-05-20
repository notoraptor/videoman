package videoman.form;

import videoman.control.DatabaseController;
import videoman.control.TableController;

public class TableForm extends Form<TableController> {
	private DatabaseController databaseController;
	public TableForm(DatabaseController databaseController) {
		super(databaseController.getForm().gui(), FormName.table, null);
		this.databaseController = databaseController;
	}
	public DatabaseController getDatabaseController() {
		return databaseController;
	}
}
