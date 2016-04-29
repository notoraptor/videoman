package videoman.form;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import videoman.GUI;
import videoman.control.PropertyEditionController;
import videoman.core.Video;
import videoman.core.database.Type;

import java.util.Collection;
import java.util.LinkedList;

public class PropertyEditionForm extends Form<PropertyEditionController> {
	private LinkedList<Video> videosToEdit;
	private Type type;
	public PropertyEditionForm(GUI gui, Collection<Video> toEdit, Type propertyType) {
		super(gui, FormName.propertyEdition, "Modifier les " + Type.getPropertyPluralName(propertyType));
		type = propertyType;
		videosToEdit = new LinkedList<>();
		for (Video video: toEdit) if (video != null) videosToEdit.add(video);
	}
	public Type type() {
		return type;
	}
	public ObservableList<Video> videos() {
		return FXCollections.observableArrayList(videosToEdit);
	}
}
