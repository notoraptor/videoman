package videoman.form;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import videoman.GUI;
import videoman.control.PersonEditionController;
import videoman.core.Video;

import java.util.Collection;
import java.util.LinkedList;

public class PersonEditionForm extends Form<PersonEditionController> {
	private LinkedList<Video> videosToEdit;
	public PersonEditionForm(GUI gui, Collection<Video> toEdit) {
		super(gui, FormName.personEdition, "Modifier les catégories");
		videosToEdit = new LinkedList<>();
		for(Video video: toEdit) if(video != null) videosToEdit.add(video);
	}
	public ObservableList<Video> videos() {
		return FXCollections.observableArrayList(videosToEdit);
	}
}
