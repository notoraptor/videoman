package videoman.form;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import videoman.GUI;
import videoman.control.CountryEditionController;
import videoman.core.Video;

import java.util.Collection;
import java.util.LinkedList;

public class CountryEditionForm extends Form<CountryEditionController> {
	private LinkedList<Video> videosToEdit;
	public CountryEditionForm(GUI gui, Collection<Video> toEdit) {
		super(gui, FormName.countryEdition, "Modifier les catégories");
		videosToEdit = new LinkedList<>();
		for(Video video: toEdit) if(video != null) videosToEdit.add(video);
	}
	public ObservableList<Video> videos() {
		return FXCollections.observableArrayList(videosToEdit);
	}
}
