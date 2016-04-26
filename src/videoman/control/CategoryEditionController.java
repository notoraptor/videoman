package videoman.control;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import videoman.core.database.Category;
import videoman.core.database.Property;
import videoman.core.Video;
import videoman.form.CategoryEditionForm;
import videoman.gui.EditableList;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CategoryEditionController extends Controller<CategoryEditionForm> {

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
		String categoryName = newElement.getText().trim();
		if (!categoryName.isEmpty()) {
			Category category = form.gui().getDatabase().createCategory(categoryName);
			editor.add(category);
			newElement.clear();
		}
	}

	@FXML
	void cancel(ActionEvent event) throws Exception {
		form.gui().back();
	}

	@FXML
	void save(ActionEvent event) throws Exception {
		Set<Category> newCategories = editor.getElements();
		HashSet<Category> toAdd = new HashSet<>();
		HashSet<Category> toRemove = new HashSet<>();
		for(Property property: categories) {
			Category category = (Category) property;
			if(!newCategories.contains(category))
				toRemove.add(category);
		}
		for(Category category: newCategories) {
			if(!categories.contains(category))
				toAdd.add(category);
		}
		form.gui().getDatabase().updateCategories(videos, toRemove, toAdd);
		form.gui().back();
	}

	private CategoryEditionForm form;
	private ObservableList<Video> videos;
	private EditableList<Category> editor;
	private TreeSet<Property> categories;
	@Override public void init(CategoryEditionForm categoryEditionForm) {
		form = categoryEditionForm;
		editor = new EditableList<>(list);
		videos = form.videos();
		categories = new TreeSet<>();
		newElement.setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.ENTER))
				addElement(null);
		});
		for(Video video: videos)
			video.addCategoriesTo(categories);
		for(Property category: categories)
			editor.add((Category) category);
		videoLabel.setText(videos.size() == 1 ? "Vidéo:" : videos.size() + " videos sélectionnées");
		videoList.setItems(videos);
	}
}
