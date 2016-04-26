package videoman.control;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import videoman.core.database.Person;
import videoman.core.database.Property;
import videoman.core.Video;
import videoman.form.PersonEditionForm;
import videoman.gui.EditableList;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class PersonEditionController extends Controller<PersonEditionForm> {

	@FXML
	private Label title;

	@FXML
	private Label videoLabel;

	@FXML
	private ListView<Video> videoList;

	@FXML
	private Label label;

	@FXML
	private VBox list;

	@FXML
	private TextField newElement;

	@FXML
	void addElement(ActionEvent event) {
		String personName = newElement.getText().trim();
		if (!personName.isEmpty()) {
			Person person = form.gui().getDatabase().createPerson(personName);
			editor.add(person);
			newElement.clear();
		}
	}

	@FXML
	void cancel(ActionEvent event) throws Exception {
		form.gui().back();
	}

	@FXML
	void save(ActionEvent event) throws Exception {
		Set<Person> newPersons = editor.getElements();
		HashSet<Person> toAdd = new HashSet<>();
		HashSet<Person> toRemove = new HashSet<>();
		for(Property property: persons) {
			Person person = (Person) property;
			if(!newPersons.contains(person))
				toRemove.add(person);
		}
		for(Person person: newPersons) {
			if(!persons.contains(person))
				toAdd.add(person);
		}
		form.gui().getDatabase().updatePersons(videos, toRemove, toAdd);
		form.gui().back();
	}

	private PersonEditionForm form;
	private ObservableList<Video> videos;
	private EditableList<Person> editor;
	private TreeSet<Property> persons;
	@Override public void init(PersonEditionForm personEditionForm) {
		form = personEditionForm;
		editor = new EditableList<>(list);
		videos = form.videos();
		persons = new TreeSet<>();
		newElement.setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.ENTER))
				addElement(null);
		});
		for(Video video: videos) {
			video.addPersonsTo(persons);
		}
		for(Property person: persons) {
			editor.add((Person) person);
		}
		videoLabel.setText(videos.size() == 1 ? "Vidéo:" : videos.size() + " videos sélectionnées");
		videoList.setItems(videos);
	}
}
