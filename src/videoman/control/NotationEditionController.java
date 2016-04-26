package videoman.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import videoman.core.Notation;
import videoman.core.Video;
import videoman.form.NotationEditionForm;

import java.util.HashSet;

public class NotationEditionController extends Controller<NotationEditionForm> {

	@FXML
	private Label videoLabel;

	@FXML
	private ListView<Video> videoList;

	@FXML
	private ChoiceBox<Notation> notationChoice;

	@FXML
	void cancel(ActionEvent event) throws Exception {
		form.gui().back();
	}

	@FXML
	void edit(ActionEvent event) throws Exception {
		for(Video video: videos)
			video.setNotation(notationChoice.getValue());
		form.gui().back();
	}

	private NotationEditionForm form;
	private ObservableList<Video> videos;
	@Override
	public void init(NotationEditionForm element) {
		form = element;
		HashSet<Notation> notations = new HashSet<>();
		videos = form.videos();
		for(Video video: videos) notations.add(video.getNotation());
		Notation defaultNotation = notations.size() == 1 ? notations.iterator().next() : Notation.ZERO;
		videoLabel.setText(videos.size() == 1 ? "Vidéo:" : videos.size() + " videos sélectionnées");
		videoList.setItems(videos);
		notationChoice.setItems(FXCollections.observableArrayList(Notation.values()));
		notationChoice.setValue(defaultNotation);
	}
}
