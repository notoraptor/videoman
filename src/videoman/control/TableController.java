package videoman.control;

import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import videoman.core.*;
import videoman.core.database.SortColumn;
import videoman.form.TableForm;

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
		});
		colNotation.setCellFactory(ComboBoxTableCell.forTableColumn(Notation.values()));
		colNotation.setOnEditCommit((TableColumn.CellEditEvent<Video, Notation> t) -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setNotation(t.getNewValue());
		});
		tableVideos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
}
