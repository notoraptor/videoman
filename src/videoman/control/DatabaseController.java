package videoman.control;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import videoman.core.*;
import videoman.core.database.*;
import videoman.form.*;
import videoman.gui.Alert;
import videoman.gui.FormDialog;
import videoman.gui.Question;
import videoman.notification.Info;
import videoman.notification.Informer;
import videoman.notification.Notification;
import videoman.notification.info.MessageInfo;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DatabaseController extends Controller<DatabaseForm> {
	@FXML private TextField queryField;
	@FXML private Button queryButton;
	@FXML private Label statusInfo;
	@FXML private Label statusMessage;
	@FXML private TitledPane folderPane;
	@FXML private TitledPane personPane;
	@FXML private TitledPane categoryPane;
	@FXML private TitledPane countryPane;
	@FXML private TableView<Property> tableFolders;
	@FXML private TableColumn<Property, String> colFolderName;
	@FXML private TableColumn<?, ?> colFolderSize;
	@FXML private TableView<Property> tablePersons;
	@FXML private TableColumn<Property, String> colPersonName;
	@FXML private TableColumn<?, ?> colPersonSize;
	@FXML private TableView<Property> tableCategories;
	@FXML private TableColumn<Property, String> colCategoryName;
	@FXML private TableColumn<?, ?> colCategorySize;
	@FXML private TableView<Property> tableCountries;
	@FXML private TableColumn<Property, String> colCountryName;
	@FXML private TableColumn<?, ?> colCountrySize;
	@FXML private Pagination pagination;
	@FXML private Button editionDeletion;
	@FXML private Hyperlink labelName;
	@FXML private Hyperlink labelOpenFolder;
	@FXML private Label labelDuration;
	@FXML private Label labelFormat;
	@FXML private Label labelSize;
	@FXML private Label labelNotation;
	@FXML private Button editionNotation;
	@FXML private Button editionPersons;
	@FXML private TreeView<String> labelPersons;
	@FXML private Button editionCategories;
	@FXML private TreeView<String> labelCategories;
	@FXML private Button editionCountries;
	@FXML private TreeView<String> labelCountries;
	@FXML private SplitPane rightSplitPane;
	@FXML private ScrollPane detailsPane;
	@FXML private GridPane thumbnailPane;
	@FXML private ImageView imageView;
	@FXML void editCategories(ActionEvent event) throws Exception {
		synchronized (selected) {
			if(!selected.isEmpty()) {
				//form.gui().load(new CategoryEditionForm(form.gui(), selected));
				new FormDialog(new PropertyEditionForm(form.gui(), selected, Type.CATEGORY));
				updateCategoryTable();
				tableVideos.refresh();
				setLabelsFrom(selected);
			}
		}
	}
	@FXML void editCountries(ActionEvent event) throws Exception {
		synchronized (selected) {
			if(!selected.isEmpty()) {
				//form.gui().load(new CountryEditionForm(form.gui(), selected));
				new FormDialog(new PropertyEditionForm(form.gui(), selected, Type.COUNTRY));
				updateCountryTable();
				tableVideos.refresh();
				setLabelsFrom(selected);
			}
		}
	}
	@FXML void editPersons(ActionEvent event) throws Exception {
		synchronized (selected) {
			if(!selected.isEmpty()) {
				//form.gui().load(new PersonEditionForm(form.gui(), selected));
				new FormDialog(new PropertyEditionForm(form.gui(), selected, Type.PERSON));
				updatePersonTable();
				tableVideos.refresh();
				setLabelsFrom(selected);
			}
		}
	}
	@FXML void editNotation(ActionEvent event) throws Exception {
		synchronized (selected) {
			if(!selected.isEmpty()) {
				//form.gui().load(new NotationEditionForm(form.gui(), selected));
				new FormDialog(new NotationEditionForm(form.gui(), selected));
				tableVideos.refresh();
				setLabelsFrom(selected);
			}
		}
	}
	@FXML void requestVideoDeletion(ActionEvent event) {
		synchronized (selected) {
			LinkedList<Video> toDelete = new LinkedList<>(selected);
			int size = toDelete.size();
			if (size > 0) {
				Question question = new Question();
				if(size == 1) {
					question.setTitle("Supprimer définitivement une vidéo.");
					question.setQuestion("Voulez-vous vraiment supprimer DÉFINITIVEMENT cette vidéos ?\n" +
							toDelete.iterator().next() + "\n\nNB: LA VIDÉO SERA AUSSI SUPPRIMÉE DU DISQUE DUR!");
				} else {
					question.setTitle("Supprimer définitivement plusieurs vidéos.");
					question.setQuestion("Voulez-vous vraiment supprimer DÉFINITIVEMENT " + size + " vidéos ?" +
							"\n\nNB: LES VIDÉOS SERONT AUSSI SUPPRIMÉES DU DISQUE DUR !");
				}
				question.setPositiveLabel("SUPPRIMER DÉFINITIVEMENT LES FICHIERS VIDÉOS");
				question.setPositiveAction(() -> {
					try {
						database.deletePermanently(toDelete);
						new Alert(null, "Fichier ssupprimés", "Fichiers supprimés.");
						// TODO
					} catch (FileException e) {
						new Alert(null, "Erreur pendant la suppression de vidéos", e.getMessage());
					}
					update();
				});
				try {
					question.show();
				} catch (Exception ignored) {}
			}
		}
	}
	@FXML void showAll(ActionEvent event) {
		tableFolders.getSelectionModel().clearSelection();
		tablePersons.getSelectionModel().clearSelection();
		tableCategories.getSelectionModel().clearSelection();
		tableCountries.getSelectionModel().clearSelection();
		view.showAll();
		resetPagination();
		setDefaultStatus();
	}
	@FXML void search(ActionEvent event) {
		String query = queryField.getText().trim();
		if (!query.isEmpty()) {
			int found = database.find(query);
			resetPagination();
			statusInfo.setText(found + " vidéos trouvés sur " + database.size() + " vidéos.");
		}
	}
	@FXML void searchDirect(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			search(null);
		}
	}
	@FXML void deleteCategory(ActionEvent event) {
		deleteProperty(tableCategories);
	}
	@FXML void deleteCountry(ActionEvent event) {
		deleteProperty(tableCountries);
	}
	@FXML void deletePerson(ActionEvent event) {
		deleteProperty(tablePersons);
	}
	@FXML void transformCategoryToCountry(ActionEvent event) {
		transformPropertyTo(tableCategories, Type.COUNTRY);
		updateCategoryTable();
		updateCountryTable();
		tableVideos.refresh();
		synchronized (selected) {
			setLabelsFrom(selected);
		}
	}
	@FXML void transformCategoryToPerson(ActionEvent event) {
		transformPropertyTo(tableCategories, Type.PERSON);
		updateCategoryTable();
		updatePersonTable();
		tableVideos.refresh();
		synchronized (selected) {
			setLabelsFrom(selected);
		}
	}
	@FXML void transformCountryToCategory(ActionEvent event) {
		transformPropertyTo(tableCountries, Type.CATEGORY);
		updateCountryTable();
		updateCategoryTable();
		tableVideos.refresh();
		synchronized (selected) {
			setLabelsFrom(selected);
		}
	}
	@FXML void transformCountryToPerson(ActionEvent event) {
		transformPropertyTo(tableCountries, Type.PERSON);
		updateCountryTable();
		updatePersonTable();
		tableVideos.refresh();
		synchronized (selected) {
			setLabelsFrom(selected);
		}
	}
	@FXML void transformPersonToCategory(ActionEvent event) {
		transformPropertyTo(tablePersons, Type.CATEGORY);
		updatePersonTable();
		updateCategoryTable();
		tableVideos.refresh();
		synchronized (selected) {
			setLabelsFrom(selected);
		}
	}
	@FXML void transformPersonToCountry(ActionEvent event) {
		transformPropertyTo(tablePersons, Type.COUNTRY);
		updatePersonTable();
		updateCountryTable();
		tableVideos.refresh();
		synchronized (selected) {
			setLabelsFrom(selected);
		}
	}

	private void deleteProperty(TableView<Property> table) {
		Property property = table.getSelectionModel().getSelectedItem();
		if (property == null) return;
		String typeName = (property.type() == Type.COUNTRY ? "le " : "la ") + Type.getPropertyName(property.type());
		Question question = new Question();
		question.setQuestion("Supprimer " + typeName + " \"" + property.getValue() + "\"");
		question.setQuestion("Voulez-vous vraiment supprimer " + typeName + " \"" + property.getValue() + "\" ?");
		question.setPositiveLabel("supprimer " + typeName);
		question.setPositiveAction(() -> {
			database.deleteProperty(property);
			switch (property.type()) {
				case PERSON:
					updatePersonTable();
					break;
				case CATEGORY:
					updateCategoryTable();
					break;
				case COUNTRY:
					updateCountryTable();
					break;
				default:break;
			}
			tableVideos.refresh();
			synchronized (selected) {
				setLabelsFrom(selected);
			}
		});
		try {
			question.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void transformPropertyTo(TableView<Property> table, Type outType) {
		Property property = table.getSelectionModel().getSelectedItem();
		if (property == null) return;
		Property newProperty = null;
		switch (outType) {
			case FOLDER:
				break;
			case PERSON:
				newProperty = database.createPerson(property.getValue());
				break;
			case CATEGORY:
				newProperty = database.createCategory(property.getValue());
				break;
			case COUNTRY:
				newProperty = database.createCountry(property.getValue());
				break;
			case QUERY:
				break;
		}
		property.transferTo(newProperty);
		database.deleteProperty(property);
	}
	// todo synchroniser totalement "selected".

	private DatabaseForm form;
	private Database database;
	private Database.View view;
	private TableForm tableForm;
	private TableView<Video> tableVideos;
	private boolean tableVideosUpdated;
	private ObservableList<Property> folders;
	private ObservableList<Property> persons;
	private ObservableList<Property> categories;
	private ObservableList<Property> countries;
	final private ObservableList<Video> videos = FXCollections.observableArrayList();
	final private HashSet<Video> selected = new HashSet<>();
	final private TreeItem<String> rootPersons = new TreeItem<>("");
	final private TreeItem<String> rootCategories = new TreeItem<>("");
	final private TreeItem<String> rootCountries = new TreeItem<>("");
	final private Label labelRootPersons = new Label();
	final private Label labelRootCategories = new Label();
	final private Label labelRootCountries = new Label();

	private void chooseDeletion(Type type, ActionEvent event) {
		switch (type) {
			case PERSON:
				deletePerson(event);
				break;
			case CATEGORY:
				deleteCategory(event);
				break;
			case COUNTRY:
				deleteCountry(event);
				break;
			default:
				break;
		}
	}
	private void chooseTransformation(Type from, Type to, ActionEvent event) {
		switch (from) {
			case PERSON:
				switch (to) {
					case CATEGORY:
						transformPersonToCategory(event);
						break;
					case COUNTRY:
						transformPersonToCountry(event);
						break;
					default:break;
				}
				break;
			case CATEGORY:
				switch (to) {
					case PERSON:
						transformCategoryToPerson(event);
						break;
					case COUNTRY:
						transformCategoryToCountry(event);
						break;
					default:break;
				}
				break;
			case COUNTRY:
				switch (to) {
					case PERSON:
						transformCountryToPerson(event);
						break;
					case CATEGORY:
						transformCountryToCategory(event);
						break;
					default:break;
				}
				break;
			default:break;
		}
	}
	private class DatabaseInformer implements Informer {
		@Override
		public void inform(Info info) {
			if (info instanceof MessageInfo) {
				statusMessage.setText(((MessageInfo)info).get());
			}
		}
	}
	private class PropertyTableRowFactory implements Callback<TableView<Property>, TableRow<Property>> {
		private Type typeFrom;
		private Type typeTo1;
		private Type typeTo2;
		public PropertyTableRowFactory(Type from) {
			typeFrom = from;
			switch (typeFrom) {
				case PERSON:
					typeTo1 = Type.CATEGORY;
					typeTo2 = Type.COUNTRY;
					break;
				case CATEGORY:
					typeTo1 = Type.PERSON;
					typeTo2 = Type.COUNTRY;
					break;
				case COUNTRY:
					typeTo1 = Type.PERSON;
					typeTo2 = Type.CATEGORY;
					break;
				default:break;
			}
		}
		@Override
		public TableRow<Property> call(TableView<Property> param) {
			final TableRow<Property> tableRow = new TableRow<>();
			final ContextMenu contextMenu = new ContextMenu();
			final MenuItem menuDelete =
					new MenuItem("Supprimer " + (typeFrom == Type.COUNTRY ? "le " : "la ") + Type.getPropertyName(typeFrom));
			final MenuItem menuTo1 =
					new MenuItem("Transformer en " + Type.getPropertyName(typeTo1));
			final MenuItem menuTo2 =
					new MenuItem("Transformer en " + Type.getPropertyName(typeTo2));
			menuDelete.setOnAction(event -> chooseDeletion(typeFrom, event));
			menuTo1.setOnAction(event -> chooseTransformation(typeFrom, typeTo1, event));
			menuTo2.setOnAction(event -> chooseTransformation(typeFrom, typeTo2, event));
			contextMenu.getItems().addAll(menuDelete, menuTo1, menuTo2);
			tableRow.contextMenuProperty().bind(
					Bindings.when(tableRow.emptyProperty()).then((ContextMenu)null).otherwise(contextMenu)
			);
			tableRow.getTooltip();
			return tableRow;
		}
	}
	private class PropertyTableSelector implements ListChangeListener<Property> {
		private TableView<Property> focus;
		public PropertyTableSelector(TableView<Property> focused) {
			focus = focused;
		}
		@Override
		public void onChanged(Change<? extends Property> c) {
			//System.err.println("SELECTION");
			Property property = focus.getSelectionModel().getSelectedItem();
			if (property != null) {
				//System.err.println("SELECTION OK");
				if (tableFolders != focus) tableFolders.getSelectionModel().clearSelection();
				if (tablePersons != focus) tablePersons.getSelectionModel().clearSelection();
				if (tableCategories != focus) tableCategories.getSelectionModel().clearSelection();
				if (tableCountries != focus) tableCountries.getSelectionModel().clearSelection();
				view.show(property);
				resetPagination();
			}
		}
	}
	private void update() {
		updateFolderTable();
		updatePersonTable();
		updateCategoryTable();
		updateCountryTable();
		updatePagination();
	}
	private void updateFolderTable() {
		ObservableList<TableColumn<Property, ?>> sortOrder = FXCollections.observableArrayList(tableFolders.getSortOrder());
		folders.clear();
		folders.addAll(view.folders());
		if (tableFolders.getSortOrder().isEmpty())
			tableFolders.getSortOrder().addAll(sortOrder);
		tableFolders.sort();
	}
	private void updatePersonTable() {
		ObservableList<TableColumn<Property, ?>> sortOrder = FXCollections.observableArrayList(tablePersons.getSortOrder());
		persons.clear();
		persons.addAll(view.persons());
		if (tablePersons.getSortOrder().isEmpty())
			tablePersons.getSortOrder().addAll(sortOrder);
		tablePersons.sort();
	}
	private void updateCategoryTable() {
		ObservableList<TableColumn<Property, ?>> sortOrder = FXCollections.observableArrayList(tableCategories.getSortOrder());
		categories.clear();
		categories.addAll(view.categories());
		if (tableCategories.getSortOrder().isEmpty())
			tableCategories.getSortOrder().addAll(sortOrder);
		tableCategories.sort();
	}
	private void updateCountryTable() {
		ObservableList<TableColumn<Property, ?>> sortOrder = FXCollections.observableArrayList(tableCountries.getSortOrder());
		countries.clear();
		countries.addAll(view.countries());
		if (tableCountries.getSortOrder().isEmpty())
			tableCountries.getSortOrder().addAll(sortOrder);
		tableCountries.sort();
	}
	private void resetPagination() {
		setPageCountFromView();
		if (pagination.getCurrentPageIndex() == 0) {
			int from = 0;
			int to = Const.PAGESIZE - 1;
			videos.clear();
			videos.addAll(view.get(from, to));
		} else {
			pagination.setCurrentPageIndex(0);
		}
	}
	private void updatePagination() {
		int currentPageIndex = pagination.getCurrentPageIndex();
		setPageCountFromView();
		if(currentPageIndex < pagination.getPageCount()) {
			int from = currentPageIndex * Const.PAGESIZE;
			int to = from + Const.PAGESIZE - 1;
			videos.clear();
			videos.addAll(view.get(from, to));
		} else {
			pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
		}
	}
	private void setDefaultStatus() {
		statusInfo.setText(database.size() + " vidéos.");
	}
	private void setDefaultLabels() {
		setEditor(false);
		labelName.setText(Const.defaultName);
		labelName.setUserData(null);
		labelOpenFolder.setText(Const.defaultOpenFolder);
		labelDuration.setText(Const.defaultDuration);
		labelFormat.setText(Const.defaultFormat);
		labelSize.setText(Const.defaultSize);
		labelNotation.setText(Const.defaultNotation.toString());
		//labelPersons.setText(Const.defaultPersons);
		rootPersons.getChildren().clear();
		labelRootPersons.setText("Aucune personne");
		//labelCategories.setText(Const.defaultCategories);
		rootCategories.getChildren().clear();
		labelRootCategories.setText("Aucune catégorie");
		//labelCountries.setText(Const.defaultCountries);
		rootCountries.getChildren().clear();
		labelRootCountries.setText("Aucun lieu");
		setThumbnail(null);
		setDefaultStatus();
	}
	private void setLabels(Video video) {
		setEditor(true);
		labelName.setText(video.getName());
		labelName.setUserData(video.getVideoPath());
		labelOpenFolder.setText("(ouvrir le dossier parent)");
		labelDuration.setText(video.getDuration().toString());
		labelFormat.setText(video.concatenateFormat());
		labelSize.setText(video.getHumanSize().toString());
		labelNotation.setText(video.getNotation().toString());
		//labelPersons.setText(video.getPersons().toString());
		Collection<Property> persons = new LinkedList<>();
		video.addPersonsTo(persons);
		rootPersons.getChildren().clear();
		labelRootPersons.setText(persons.size() + " personnes");
		for (Property person: persons)
			rootPersons.getChildren().add(new TreeItem<>(person.getValue()));
		//labelCategories.setText(video.getCategories().toString());
		Collection<Property> categories = new LinkedList<>();
		video.addCategoriesTo(categories);
		rootCategories.getChildren().clear();
		labelRootCategories.setText(categories.size() + " catégories");
		for (Property category: categories)
				rootCategories.getChildren().add(new TreeItem<>(category.getValue()));
		//labelCountries.setText(video.getCountries().toString());
		Collection<Property> countries = new LinkedList<>();
		video.addCountriesTo(countries);
		rootCountries.getChildren().clear();
		labelRootCountries.setText(countries.size() + " lieux");
		for (Property country: countries)
			rootCountries.getChildren().add(new TreeItem<>(country.getValue()));
		//
		String path = video.getThumbnailPath();
		if (path != null)
			setThumbnail(getImage(path));
		statusInfo.setText("1 vidéo sélectionnée sur " + database.size() + " vidéos.");
	}
	private void setLabelsFrom(Collection<Video> selected) {
		setEditor(!selected.isEmpty());
		if (selected.isEmpty()) {
			setDefaultLabels();
			return;
		}
		TreeSet<String> listNames = new TreeSet<>();
		TreeSet<VideoDuration> listDurations = new TreeSet<>();
		TreeSet<String> listFormats = new TreeSet<>();
		TreeSet<VideoSize> listSizes = new TreeSet<>();
		TreeSet<Notation> listNotations = new TreeSet<>();
		TreeSet<Property> listPersons = new TreeSet<>();
		TreeSet<Property> listCategories = new TreeSet<>();
		TreeSet<Property> listCountries = new TreeSet<>();
		for (Video video : selected) {
			listNames.add(video.getName());
			listDurations.add(video.getDuration());
			listFormats.add(video.concatenateFormat());
			listSizes.add(video.getHumanSize());
			listNotations.add(video.getNotation());
			video.addPersonsTo(listPersons);
			video.addCategoriesTo(listCategories);
			video.addCountriesTo(listCountries);
		}
		String stringNames = listNames.size() == 1 ? listNames.iterator().next() : "...";
		String stringDurations = listDurations.size() == 1 ?
				listDurations.iterator().next().toString() :
				"De " + listDurations.first() + " à " + listDurations.last();
		String stringFormats = listFormats.size() == 1 ? listFormats.iterator().next() : "...";
		String stringSizes = listSizes.size() == 1 ?
				listSizes.iterator().next().toString() :
				"De " + listSizes.first() + " à " + listSizes.last();
		String stringNotations = listNotations.size() == 1 ?
				listNotations.iterator().next().toString() :
				"De " + listNotations.first().ordinal() + " à " + listNotations.last().ordinal();
		labelName.setText(stringNames);
		labelName.setUserData(null);
		labelOpenFolder.setText("...");
		labelDuration.setText(stringDurations);
		labelFormat.setText(stringFormats);
		labelSize.setText(stringSizes);
		labelNotation.setText(stringNotations);
		//labelPersons.setText(stringPersons.toString());
		labelRootPersons.setText(listPersons.size() + " personnes dans " + selected.size() + " vidéos");
		rootPersons.getChildren().clear();
		for (Property person: listPersons)
			rootPersons.getChildren().add(new TreeItem<>(person.getValue()));
		//labelCategories.setText(stringCategories.toString());
		labelRootCategories.setText(listCategories.size() + " catégories dans " + selected.size() + " vidéos");
		rootCategories.getChildren().clear();
		for (Property category: listCategories)
				rootCategories.getChildren().add(new TreeItem<>(category.getValue()));
		//labelCountries.setText(stringCountries.toString());
		labelRootCountries.setText(listCountries.size() + " lieux dans " + selected.size() + " vidéos");
		rootCountries.getChildren().clear();
		for (Property country: listCountries)
			rootCountries.getChildren().add(new TreeItem<>(country.getValue()));
		//
		setThumbnail(null);
		statusInfo.setText(selected.size() + " vidéos sélectionnées sur " + database.size() + " vidéos..");
	}
	private void setEditor(boolean editable) {
		boolean disabled = !editable;
		editionDeletion.setDisable(disabled);
		editionNotation.setDisable(disabled);
		editionPersons.setDisable(disabled);
		editionCategories.setDisable(disabled);
		editionCountries.setDisable(disabled);
	}
	public Image getImage(String path) {
		if (path == null)
			return null;
		//Image image = new Image(new FileInputStream(path));
		return new Image("file:" + path);
	}
	private void setThumbnail(Image image) {
		imageView.setImage(image);
		if (image == null)
			return;
		setImageSize(thumbnailPane.getWidth(), thumbnailPane.getHeight(), image.getWidth(), image.getHeight());
	}
	private void setImageSize(double frameWidth, double frameHeight, double thumbnailWidth, double thumbnailHeight) {
		double frameRatio = frameWidth / frameHeight;
		double thumbnailRatio = thumbnailWidth / thumbnailHeight;
		double imageWidth;
		double imageHeight;
		if(frameRatio == thumbnailRatio) {
			imageWidth = frameWidth;
			imageHeight = frameHeight;
		} else if(frameRatio > thumbnailRatio) {
			imageWidth = Math.round(frameHeight * thumbnailRatio);
			imageHeight = frameHeight;
		} else {
			imageWidth = frameWidth;
			imageHeight = Math.round(frameWidth / thumbnailRatio);
		}
		imageView.setFitWidth(imageWidth);
		imageView.setFitHeight(imageHeight);
	}
	private void setPageCountFromView() {
		int rest = view.size() % Const.PAGESIZE;
		int count = view.size() / Const.PAGESIZE;
		if (rest > 0) ++count;
		pagination.setPageCount(count);
		statusInfo.setText(view.infoToString());
	}
	private void listenRightSplitPane() {
		int lastDividerPosition = rightSplitPane.getDividers().size() - 1;
		rightSplitPane.getDividers().get(lastDividerPosition).positionProperty().addListener((obs, old, newValue) -> {
			Image image = imageView.getImage();
			if (image == null)
				return;
			double totalHeight = rightSplitPane.getHeight();
			double dividerHeight =  totalHeight - detailsPane.getHeight() - thumbnailPane.getHeight();
			double currentPosition = newValue.doubleValue();
			double rest = totalHeight - (totalHeight * currentPosition) - (dividerHeight/2);
			if(rest > 0) {
				double frameWidth = thumbnailPane.getWidth();
				imageView.setImage(null);
				imageView.setImage(image);
				//System.err.println("(" + image.getWidth() + " ; " + image.getHeight() + ")");
				setImageSize(frameWidth, rest, image.getWidth(), image.getHeight());
			}
		});
		rightSplitPane.widthProperty().addListener((obs, oldValue, newValue) -> {
			Image image = imageView.getImage();
			if (image == null)
				return;
			double totalHeight = rightSplitPane.getHeight();
			double dividerHeight =  totalHeight - detailsPane.getHeight() - thumbnailPane.getHeight();
			double currentPosition = rightSplitPane.getDividers().get(lastDividerPosition).getPosition();
			double rest = totalHeight - (totalHeight * currentPosition) - (dividerHeight/2);
			if (rest > 0) {
				double frameWidth = newValue.doubleValue();
				//double frameHeight = thumbnailPane.getHeight();
				imageView.setImage(null);
				imageView.setImage(image);
				//System.err.println("(" + image.getWidth() + " ; " + image.getHeight() + ")");
				setImageSize(frameWidth, rest, image.getWidth(), image.getHeight());
			}
		});
	}
	private void listenVideoTableSelection() {
		tableVideos.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Video>) c -> {
			synchronized (selected) {
				selected.clear();
				for (Video video : c.getList()) if (video != null) selected.add(video);
				int selectedSize = selected.size();
				if (selectedSize == 0) {
					setDefaultLabels();
				} else if (selectedSize == 1) {
					setLabels(selected.iterator().next());
				} else {
					setLabelsFrom(selected);
				}
			}
		});
	}
	private void listenVideoTableSorting() {
		tableVideos.setOnSort(event -> {
			int currentPageIndex = pagination.getCurrentPageIndex();
			ObservableList<TableColumn<Video, ?>> sortOrder = FXCollections.observableArrayList(tableVideos.getSortOrder());
			if (sortOrder.isEmpty())
				return;
			LinkedList<Sort> sort = new LinkedList<>();
			for (TableColumn<Video, ?> tableColumn : sortOrder) {
				SortType sortType = tableColumn.getSortType() == TableColumn.SortType.ASCENDING ?
						SortType.ASCENDING : SortType.DESCENDING;
				SortColumn sortColumn = tableForm.getController().getSortColumn(tableColumn);
				if (sortColumn == null) {
					new Exception("Impossible de trouver une colonne dans le tableau des vidéos.").printStackTrace();
					return;
				}
				sort.add(new Sort(sortColumn, sortType));
			}
			view.sort(sort);
			int from = currentPageIndex * Const.PAGESIZE;
			int to = from + Const.PAGESIZE - 1;
			videos.clear();
			videos.addAll(view.get(from, to));
		});
	}
	private void listenPropertyTablesSelection() {
		tableFolders.getSelectionModel().getSelectedItems().addListener(new PropertyTableSelector(tableFolders));
		tablePersons.getSelectionModel().getSelectedItems().addListener(new PropertyTableSelector(tablePersons));
		tableCategories.getSelectionModel().getSelectedItems().addListener(new PropertyTableSelector(tableCategories));
		tableCountries.getSelectionModel().getSelectedItems().addListener(new PropertyTableSelector(tableCountries));
	}
	@Override
	public void init(DatabaseForm databaseForm) throws Exception {
		form = databaseForm;
		database = form.getDatabase();
		view = database.getDatabaseView();
		tableForm = new TableForm();
		tableVideos = tableForm.root();
		folders = FXCollections.observableArrayList();
		persons = FXCollections.observableArrayList();
		categories = FXCollections.observableArrayList();
		countries = FXCollections.observableArrayList();
		form.gui().setInformer(new DatabaseInformer());
		queryButton.setText(Const.magnifierSymbol);
		folders.addListener((ListChangeListener<? super Property>) c -> {
			folderPane.setText("Dossiers" + (folders.isEmpty() ? "" : " (" + folders.size() + ")"));
		});
		persons.addListener((ListChangeListener<? super Property>) c -> {
			personPane.setText("Personnes" + (persons.isEmpty() ? "" : " (" + persons.size() + ")"));
		});
		categories.addListener((ListChangeListener<? super Property>) c -> {
			categoryPane.setText("Catégories" + (categories.isEmpty() ? "" : " (" + categories.size() + ")"));
		});
		countries.addListener((ListChangeListener<? super Property>) c -> {
			countryPane.setText("Lieux" + (countries.isEmpty() ? "" : " (" + countries.size() + ")"));
		});
		folders.addAll(view.folders());
		persons.addAll(view.persons());
		categories.addAll(view.categories());
		countries.addAll(view.countries());
		//
		if(Desktop.isDesktopSupported()) {
			labelName.setOnAction(event -> {
				Object userData = labelName.getUserData();
				if (userData != null) {
					File file = new File((String) userData);
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException e) {
						Notification.bad("Impossible d'ouvrir le chemin " + userData);
					}
				}
			});
			labelOpenFolder.setOnAction(event -> {
				Object userData = labelName.getUserData();
				if (userData != null) {
					File file = new File((String) userData);
					File parent = file.getParentFile();
					try {
						Desktop.getDesktop().open(parent);
					} catch (IOException e) {
						Notification.bad("Impossible d'ouvrir le chemin" + parent);
					}
				}
			});
		}
		//
		labelRootPersons.setStyle("-fx-font-weight:bold;");
		labelRootCategories.setStyle("-fx-font-weight:bold;");
		labelRootCountries.setStyle("-fx-font-weight:bold;");
		rootPersons.setGraphic(labelRootPersons);
		rootCategories.setGraphic(labelRootCategories);
		rootCountries.setGraphic(labelRootCountries);
		rootPersons.setExpanded(true);
		rootCategories.setExpanded(true);
		rootCountries.setExpanded(true);
		labelPersons.setRoot(rootPersons);
		labelCategories.setRoot(rootCategories);
		labelCountries.setRoot(rootCountries);
		listenRightSplitPane();
		setDefaultLabels();
		// folders
		colFolderSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		colFolderName.setCellValueFactory(new PropertyValueFactory<>("value"));
		tableFolders.setItems(folders);
		// persons
		colPersonSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		colPersonName.setCellValueFactory(new PropertyValueFactory<>("value"));
		colPersonName.setCellFactory(TextFieldTableCell.forTableColumn());
		colPersonName.setOnEditCommit((TableColumn.CellEditEvent<Property, String > t) -> {
			boolean modified = database.renamePerson(
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getValue(), t.getNewValue());
			if(modified) {
				updatePersonTable();
				tableVideos.refresh();
			}
		});
		tablePersons.setItems(persons);
		// categories
		colCategorySize.setCellValueFactory(new PropertyValueFactory<>("size"));
		colCategoryName.setCellValueFactory(new PropertyValueFactory<>("value"));
		colCategoryName.setCellFactory(TextFieldTableCell.forTableColumn());
		colCategoryName.setOnEditCommit((TableColumn.CellEditEvent<Property, String > t) -> {
			boolean modified = database.renameCategory(
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getValue(), t.getNewValue());
			if(modified) {
				updateCategoryTable();
				tableVideos.refresh();
			}
		});
		tableCategories.setItems(categories);
		// countries
		colCountrySize.setCellValueFactory(new PropertyValueFactory<>("size"));
		colCountryName.setCellValueFactory(new PropertyValueFactory<>("value"));
		colCountryName.setCellFactory(TextFieldTableCell.forTableColumn());
		colCountryName.setOnEditCommit((TableColumn.CellEditEvent<Property, String > t) -> {
			boolean modified = database.renameCountry(
					t.getTableView().getItems().get(t.getTablePosition().getRow()).getValue(), t.getNewValue());
			if(modified) {
				updateCountryTable();
				tableVideos.refresh();
			}
		});
		tableCountries.setItems(countries);
		// videos @
		tableVideos.setItems(videos);
		listenVideoTableSelection();
		listenVideoTableSorting();
		listenPropertyTablesSelection();
		//
		pagination.setPageFactory(pageIndex -> {
			int from = pageIndex * Const.PAGESIZE;
			int to = from + Const.PAGESIZE - 1;
			videos.clear();
			videos.addAll(view.get(from, to));
			//tableVideos.setItems(videos);
			//statusInfo.setText(view.infoToString());
			return tableVideos;
		});
		setPageCountFromView();
		statusInfo.setText(database.size() + " vidéos.");
		tablePersons.setRowFactory(new PropertyTableRowFactory(Type.PERSON));
		tableCategories.setRowFactory(new PropertyTableRowFactory(Type.CATEGORY));
		tableCountries.setRowFactory(new PropertyTableRowFactory(Type.COUNTRY));
	}
}
