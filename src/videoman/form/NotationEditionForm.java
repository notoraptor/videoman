package videoman.form;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import videoman.GUI;
import videoman.control.NotationEditionController;
import videoman.core.Video;

import java.util.Collection;
import java.util.LinkedList;

public class NotationEditionForm extends Form<NotationEditionController> {
	private LinkedList<Video> videosToEdit;
	public NotationEditionForm(GUI gui, Collection<Video> toEdit) {
		super(gui, FormName.notationEdition, "Modifier la notation");
		videosToEdit = new LinkedList<>();
		for(Video video: toEdit) if(video != null) videosToEdit.add(video);
	}
	public ObservableList<Video> videos() {
		return FXCollections.observableArrayList(videosToEdit);
	}
}
