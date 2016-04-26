package videoman.form;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import videoman.GUI;
import videoman.control.CategoryEditionController;
import videoman.core.Video;

import java.util.Collection;
import java.util.LinkedList;

public class CategoryEditionForm extends Form<CategoryEditionController> {
	private LinkedList<Video> videosToEdit;
	public CategoryEditionForm(GUI gui, Collection<Video> toEdit) {
		super(gui, FormName.categoryEdition, "Modifier les catégories");
		videosToEdit = new LinkedList<>();
		for(Video video: toEdit) if(video != null) videosToEdit.add(video);
	}
	public ObservableList<Video> videos() {
		return FXCollections.observableArrayList(videosToEdit);
	}
}
