package videoman.control;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import videoman.core.*;
import videoman.core.database.SortColumn;
import videoman.form.TableForm;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TableController extends Controller<TableForm> {

	@FXML
	private TableView<Video> tableVideos;

	@FXML
	private TableColumn colEmpty;

	@FXML
	private TableColumn<Video, String> colName;

	@FXML
	private TableColumn<?, ?> colVideoDateModified;

	@FXML
	private TableColumn<Video, ?> colExtension;

	@FXML
	private TableColumn<Video, ?> colFormat;

	@FXML
	private TableColumn<Video, VideoSize> colSize;

	@FXML
	private TableColumn<Video, VideoDuration> colDuration;

	@FXML
	private TableColumn<Video, ?> colAudioCodec;

	@FXML
	private TableColumn<Video, ?> colVideoCodec;

	@FXML
	private TableColumn<Video, ?> colWidth;

	@FXML
	private TableColumn<Video, ?> colHeight;

	@FXML
	private TableColumn<Video, VideoQuality> colQuality;

	@FXML
	private TableColumn<Video, Notation> colNotation;

	@FXML
	private TableColumn<Video, ?> colPersons;

	@FXML
	private TableColumn<Video, ?> colCategories;

	@FXML
	private TableColumn<Video, ?> colCountries;

	@FXML
	private TableColumn<Video, ?> colPath;

	private TableForm form;
	public SortColumn getSortColumn(TableColumn<Video, ?> column) {
		if (column == colName) return SortColumn.name;
		if (column == colVideoDateModified) return SortColumn.dateModified;
		if (column == colExtension) return SortColumn.extension;
		if (column == colFormat) return SortColumn.format;
		if (column == colSize) return SortColumn.size;
		if (column == colDuration) return SortColumn.duration;
		if (column == colAudioCodec) return SortColumn.audioCodec;
		if (column == colVideoCodec) return SortColumn.videoCodec;
		if (column == colWidth) return SortColumn.width;
		if (column == colHeight) return SortColumn.height;
		if (column == colQuality) return SortColumn.quality;
		if (column == colNotation) return SortColumn.notation;
		if (column == colPersons) return SortColumn.persons;
		if (column == colCategories) return SortColumn.categories;
		if (column == colCountries) return SortColumn.countries;
		if (column == colPath) return SortColumn.path;
		return null;
	}
	@Override
	public void init(TableForm tableForm) {
		form = tableForm;
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colVideoDateModified.setCellValueFactory(new PropertyValueFactory<>("videoDateModified"));
		colExtension.setCellValueFactory(new PropertyValueFactory<>("extension"));
		colFormat.setCellValueFactory(new PropertyValueFactory<>("format"));
		colSize.setCellValueFactory(new PropertyValueFactory<>("humanSize"));
		colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
		colAudioCodec.setCellValueFactory(new PropertyValueFactory<>("audioCodec"));
		colVideoCodec.setCellValueFactory(new PropertyValueFactory<>("videoCodec"));
		colWidth.setCellValueFactory(new PropertyValueFactory<>("width"));
		colHeight.setCellValueFactory(new PropertyValueFactory<>("height"));
		colQuality.setCellValueFactory(new PropertyValueFactory<>("videoQuality"));
		colNotation.setCellValueFactory(new PropertyValueFactory<>("notation"));
		colPersons.setCellValueFactory(new PropertyValueFactory<>("persons"));
		colCategories.setCellValueFactory(new PropertyValueFactory<>("categories"));
		colCountries.setCellValueFactory(new PropertyValueFactory<>("countries"));
		colPath.setCellValueFactory(new PropertyValueFactory<>("videoPath"));
		// vidéos.
		colName.setCellFactory(TextFieldTableCell.forTableColumn());
		colName.setOnEditCommit((TableColumn.CellEditEvent<Video, String> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
			tableVideos.refresh();
		});
		colNotation.setCellFactory(ComboBoxTableCell.forTableColumn(Notation.values()));
		colNotation.setOnEditCommit((TableColumn.CellEditEvent<Video, Notation> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setNotation(t.getNewValue());
			tableVideos.refresh();
		});
		tableVideos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableVideos.setRowFactory(param -> {
			TableRow<Video> tableRow = new TableRow<>();
			tableRow.itemProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue == null) {
					tableRow.setTooltip(null);
				} else {
					tableRow.setTooltip(new Tooltip(newValue.toString()));
					if (Desktop.isDesktopSupported()) {
						ContextMenu contextMenu = new ContextMenu();
						MenuItem menuItem = new MenuItem("Ouvrir le fichier");
						menuItem.setOnAction(event -> {
							try {
								Desktop.getDesktop().open(new File(tableRow.getItem().getVideoPath()));
							} catch (IOException ignored) {}
						});
						contextMenu.getItems().add(menuItem);
						tableRow.contextMenuProperty().bind(
							Bindings.when(tableRow.emptyProperty()).then((ContextMenu)null).otherwise(contextMenu)
						);
					}
				}
			});
			return tableRow;
		});
	}
}
