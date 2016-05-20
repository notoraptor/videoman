package videoman.control;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import videoman.DialogController;
import videoman.core.database.Database;
import videoman.core.database.Property;
import videoman.form.PropertyDeletionForm;
import videoman.gui.Action;

import java.util.Collection;

public class PropertyDeletionController extends Controller<PropertyDeletionForm> implements DialogController {

	@FXML
	private Label label;

	@FXML
	private TableView<ObservableProperty> table;


	@FXML
	private TableColumn<ObservableProperty, Boolean> colCheckbox;

	@FXML
	private TableColumn<ObservableProperty, String> colName;

	@FXML
	private TableColumn<ObservableProperty, Integer> colLength;

	@FXML
	private TableColumn<ObservableProperty, Integer> colSize;

	@FXML
	void cancel(ActionEvent event) throws Exception {
		if (cancelAction != null) cancelAction.execute();
	}

	@FXML
	void delete(ActionEvent event) throws Exception {
		for (ObservableProperty observableProperty: propertyObservableList) if (observableProperty.property.isSelected())
			form.gui().getDatabase().remove(observableProperty.property);
		if (okAction != null) okAction.execute();
	}

	static public class ObservableProperty {
		private Property property;
		private SimpleStringProperty value;
		private SimpleIntegerProperty valueLength;
		private SimpleIntegerProperty size;
		private SimpleBooleanProperty selected;
		public ObservableProperty(Property property) {
			this.property = property;
			value = new SimpleStringProperty(property.getValue());
			valueLength = new SimpleIntegerProperty(property.getValueLength());
			size = new SimpleIntegerProperty(property.getSize());
			selected = new SimpleBooleanProperty(property.isSelected());
			selected.addListener((observable, oldValue, newValue) -> property.setSelected(newValue));
		}
		public StringProperty valueProperty() {
			return value;
		}
		public IntegerProperty valueLengthProperty() {
			return valueLength;
		}
		public IntegerProperty sizeProperty() {
			return size;
		}
		public BooleanProperty selectedProperty() {
			return selected;
		}
	}
	Action cancelAction;
	Action okAction;
	PropertyDeletionForm form;
	ObservableList<ObservableProperty> propertyObservableList;
	@Override
	public void init(PropertyDeletionForm propertyDeletionForm) throws Exception {
		form = propertyDeletionForm;
		Database.View view = form.gui().getDatabase().getDatabaseView();
		label.setText(form.title());
		Collection<Property> properties = null;
		switch (form.type()) {
			case PERSON:
				properties = view.persons();
				break;
			case CATEGORY:
				properties = view.categories();
				break;
			case COUNTRY:
				properties = view.countries();
				break;
			case FOLDER:break;
			case QUERY:break;
		}
		if (properties == null) {
			cancel(null);
			return;
		}
		colCheckbox.setCellValueFactory(new PropertyValueFactory<>("selected"));
		colName.setCellValueFactory(new PropertyValueFactory<>("value"));
		colLength.setCellValueFactory(new PropertyValueFactory<>("valueLength"));
		colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
		colCheckbox.setCellFactory(CheckBoxTableCell.forTableColumn(colCheckbox));
		propertyObservableList = FXCollections.observableArrayList();
		for (Property property: properties) if (!property.isQuery())
			propertyObservableList.add(new ObservableProperty(property));
		table.setItems(propertyObservableList);
	}
	@Override
	public void setCancelAction(Action action) {
		cancelAction = action;
	}
	@Override
	public void setOkAction(Action action) {
		okAction = action;
	}
}
