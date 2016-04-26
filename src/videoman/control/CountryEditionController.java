package videoman.control;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import videoman.core.database.Country;
import videoman.core.database.Property;
import videoman.core.Video;
import videoman.form.CountryEditionForm;
import videoman.gui.EditableList;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CountryEditionController extends Controller<CountryEditionForm> {

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
		String countryName = newElement.getText().trim();
		if (!countryName.isEmpty()) {
			Country country = form.gui().getDatabase().createCountry(countryName);
			editor.add(country);
			newElement.clear();
		}
	}

	@FXML
	void cancel(ActionEvent event) throws Exception {
		form.gui().back();
	}

	@FXML
	void save(ActionEvent event) throws Exception {
		Set<Country> newCountries = editor.getElements();
		HashSet<Country> toAdd = new HashSet<>();
		HashSet<Country> toRemove = new HashSet<>();
		for(Property properties: countries) {
			Country country = (Country) properties;
			if(!newCountries.contains(country))
				toRemove.add(country);
		}
		for(Country country: newCountries) {
			if(!countries.contains(country))
				toAdd.add(country);
		}
		form.gui().getDatabase().updateCountries(videos, toRemove, toAdd);
		form.gui().back();
	}

	private CountryEditionForm form;
	private ObservableList<Video> videos;
	private EditableList<Country> editor;
	private TreeSet<Property> countries;
	@Override public void init(CountryEditionForm countryEditionForm) {
		form = countryEditionForm;
		editor = new EditableList<>(list);
		videos = form.videos();
		countries = new TreeSet<>();
		newElement.setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.ENTER))
				addElement(null);
		});
		for(Video video: videos) {
			video.addCountriesTo(countries);
		}
		for(Property country: countries) {
			editor.add((Country) country);
		}
		videoLabel.setText(videos.size() == 1 ? "Vidéo:" : videos.size() + " videos sélectionnées");
		videoList.setItems(videos);
	}
}
