package videoman.form;

import videoman.GUI;
import videoman.control.PropertyDeletionController;
import videoman.core.database.Type;

public class PropertyDeletionForm extends Form<PropertyDeletionController> {
	private Type type;
	public PropertyDeletionForm(GUI gui, Type propertyType) {
		super(gui, FormName.propertyDeletion, "Supprimer des " + Type.getPropertyPluralName(propertyType));
		type = propertyType;
	}
	public Type type() {
		return type;
	}
}
