package videoman.control;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import videoman.DialogController;
import videoman.core.Video;
import videoman.core.database.Database;
import videoman.core.database.Property;
import videoman.core.database.Type;
import videoman.form.PropertyEditionForm;
import videoman.gui.*;

import java.util.*;

public class PropertyEditionController extends Controller<PropertyEditionForm> implements DialogController {

	@FXML
	private Label title;

	@FXML
	private ListView<Video> videoList;

	@FXML
	private Label freePropertiesLabel;

	@FXML
	private Label takenPropertiessLabel;

	@FXML
	private VBox freePropertiesContainer;

	@FXML
	private VBox takenPropertiesContainer;

	@FXML
	private TextField input;

	@FXML
	private Button addButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Button saveButton;

	@FXML
	void add(ActionEvent event) {
		String propertyName = input.getText().trim();
		if (!propertyName.isEmpty()) {
			Property property = form.gui().getDatabase().create(propertyName, form.type());
			takenProperties.add(new RemovableLabel<Property>(takenProperties, property));
			input.clear();
		}
	}

	@FXML
	void cancel(ActionEvent event) throws Exception {
		//form.gui().back();
		if (cancelAction != null)
			cancelAction.execute();
	}

	@FXML
	void save(ActionEvent event) throws Exception {
		HashSet<Property> removed = new HashSet<>();
		HashSet<Property> added = new HashSet<>();
		HashSet<Property> sharedForAll = new HashSet<>();
		HashSet<Property> sharedForSel = new HashSet<>();
		Set<Property> edited = takenProperties.getElements();
		for (Property property: properties) {
			if (!edited.contains(property))
				removed.add(property);
		}
		for (Property property: edited) {
			if (!properties.contains(property))
				added.add(property);
			else if (initialSharedProperties.contains(property)) {
				SharedLabel<Property> label = (SharedLabel<Property>) takenProperties.getItem(property);
				if (label.isForAll())
					sharedForAll.add(property);
				else
					sharedForSel.add(property);
			}
		}
		added.addAll(sharedForAll);
		form.gui().getDatabase().update(videos, removed, added);
		if (okAction != null)
			okAction.execute();
	}

	private Action cancelAction;
	private Action okAction;
	private PropertyEditionForm form;
	private ObservableList<Video> videos;
	private EditableList<Property> freeProperties;
	private EditableList<Property> takenProperties;
	private HashSet<Property> properties;
	private TreeSet<Property> initialCommonProperties;
	private TreeSet<Property> initialSharedProperties;
	static private boolean propertyIsCommon(Property property, Collection<Video> videos) {
		boolean isCommon = true;
		for (Video video: videos) if (!video.hasProperty(property)) {
			isCommon = false;
			break;
		}
		return isCommon;
	}
	static private int countShared(Property property, Collection<Video> videos) {
		int count = 0;
		for (Video video: videos) if (video.hasProperty(property)) ++count;
		return count;
	}
	@Override
	public void setCancelAction(Action action) {
		cancelAction = action;
	}
	@Override
	public void setOkAction(Action action) {
		okAction = action;
	}
	@Override
	public void init(PropertyEditionForm propertyEditionForm) {
		form = propertyEditionForm;
		freeProperties = new EditableList<>(freePropertiesContainer);
		takenProperties = new EditableList<>(takenPropertiesContainer);
		videos = form.videos();
		initialCommonProperties = new TreeSet<>();
		initialSharedProperties = new TreeSet<>();
		properties = new HashSet<>();
		for (Video video: videos) {
			video.addPropertiesTo(properties, form.type());
		}
		for (Property property: properties) {
			if (propertyIsCommon(property, videos))
				initialCommonProperties.add(property);
			else
				initialSharedProperties.add(property);
		}
		for (Property common: initialCommonProperties)
			takenProperties.add(new RemovableLabel<Property>(takenProperties, common));
		for (Property shared: initialSharedProperties)
			takenProperties.add(new SharedLabel<Property>(takenProperties, shared, countShared(shared, videos), videos.size()));
		Database.View databaseView = form.gui().getDatabase().getDatabaseView();
		Collection<Property> allProperties = null;
		switch (form.type()) {
			case PERSON:
				allProperties = databaseView.persons();
				break;
			case CATEGORY:
				allProperties = databaseView.categories();
				break;
			case COUNTRY:
				allProperties = databaseView.countries();
				break;
			default:
				break;
		}
		for (Property property: allProperties) if (!properties.contains(property)) {
			freeProperties.add(new AddableLabel<Property>(freeProperties, takenProperties, property));
		}
		String typeName = Type.getPropertyName(form.type());
		String e = form.type() == Type.COUNTRY ? "" : "e";
		title.setText("Modifier les " + typeName + " de " + videos.size() + " vidéos.");
		freePropertiesLabel.setText(typeName + " non attribué" + e + "s");
		takenPropertiessLabel.setText(typeName + "attribué" + e + "s aux vidéos");
		addButton.setText("ajouter un" + e + " " + typeName);
		videoList.setItems(videos);
		freeProperties.setInstancer(element -> new AddableLabel<Property>(freeProperties, takenProperties, element));
		takenProperties.setTrash(freeProperties);
		freeProperties.setTrash(takenProperties);
		input.setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.ENTER))
				add(null);
		});
	}
}
