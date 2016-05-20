package videoman.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import videoman.Utils;
import videoman.VideoMan;
import videoman.core.database.Database;
import videoman.form.DatabaseForm;
import videoman.form.HomeForm;
import videoman.notification.Info;
import videoman.notification.Informer;
import videoman.notification.Notification;
import videoman.notification.info.MessageInfo;
import videoman.notification.info.ProgressInfo;

import java.io.*;
import java.util.LinkedList;

public class HomeController extends Controller<HomeForm> {

	@FXML
	private TextField listPath;
	@FXML
	private Button listChooser;
	@FXML
	private Button listCreator;
	@FXML
	private TextArea list;
	@FXML
	private Button databaseMaker;
	@FXML
	private ProgressBar progress;
	@FXML
	private Button next;
	@FXML
	private Label message;
	@FXML
	private Button folderChooser;

	@FXML
	void chooseList(ActionEvent event) throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charger une liste de dossiers ...");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier texte", "*.txt", "*.list", "*.ls"));
		File file = homeForm.gui().getFile(fileChooser);
		if (file != null) {
			if (!file.exists() || !file.isFile())
				Notification.bad("Le chemin choisi n'existe pas ou n'est pas un fichier.");
			else {
				listPath.setText(file.getAbsolutePath());
				created = true;
				readList(file);
			}
		}
	}
	@FXML
	void createList(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Créer une liste de dossiers ...");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier texte", "*.txt", "*.list", "*.ls"));
		File file = homeForm.gui().createFile(fileChooser);
		if (file != null) {
			listPath.setText(file.getAbsolutePath());
			created = true;
		}
	}
	@FXML
	void addFolder(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Choisir un dossier contenant des vidéos ...");
		if (lastDirectoryAdded != null)
			directoryChooser.setInitialDirectory(lastDirectoryAdded);
		File file = homeForm.gui().getDirectory(directoryChooser);
		if (file != null) {
			if (!file.exists() || !file.isDirectory())
				Notification.bad("Le chemin choisi n'existe pas ou n'est pas un dossier.");
			else {
				String text = list.getText();
				if (text == null)
					text = "";
				else
					text = text.trim();
				if (!text.isEmpty())
					text += Utils.endl;
				text += file.getAbsolutePath() + Utils.endl;
				lastDirectoryAdded = file;
				list.setText(text);
			}
		}
	}
	@FXML
	void makeDatabase(ActionEvent event) {
		if (listPath.getText().isEmpty() || list.getText().isEmpty())
			return;
		disableAll();
		File listFile = new File(listPath.getText());
		LinkedList<File> folders = new LinkedList<>();
		if (created) try {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(listFile))) {
				writer.write(list.getText());
			}
		} catch (IOException e) {
			enableAll();
			Notification.bad("Impossible de sauvegarder la liste." + Utils.endl + e.getMessage());
			e.printStackTrace();
			return;
		}
		try {
			try (BufferedReader reader = new BufferedReader(new StringReader(list.getText()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (!line.isEmpty() && line.charAt(0) != '#') {
						File path = new File(line);
						if (!path.exists() || !path.isDirectory())
							Notification.bad("Un des chemins n'existe pas ou n'est pas un dossier:" + Utils.endl + path.getAbsolutePath());
						folders.add(path);
					}
				}
			}
		} catch (IOException e) {
			enableAll();
			e.printStackTrace();
			Notification.bad("Impossible d'analyser les dossiers." + Utils.endl + e.getMessage());
			return;
		}
		new Thread(() -> {
			try {
				database = VideoMan.updateDatabase(listFile, folders);
				VideoMan.loadDatabase(database);
				setDatabaseUpdated();
			} catch (Exception e) {
				Notification.bad("Erreur pendant la création/lecture de la base de données:" + Utils.endl + e.getMessage());
				e.printStackTrace();
				enableAll();
			}
		}).start();
	}
	@FXML
	void showDatabase(ActionEvent event) throws Exception {
		// ...
		if(database != null) {
			homeForm.gui().setDatabase(database);
			homeForm.gui().load(new DatabaseForm(homeForm.gui(), database));
		}
	}

	class HomeInformer implements Informer {
		@Override
		public void inform(Info info) throws Exception {
			if (info instanceof ProgressInfo) {
				ProgressInfo pi = (ProgressInfo) info;
				message.setText(pi.current() + "/" + pi.length() + " vidéos enregistrées.");
				progress.setProgress(pi.current() / (double) pi.length());
				return;
			}
			if (info instanceof MessageInfo) {
				MessageInfo mi = (MessageInfo) info;
				message.setText(mi.get());
			}
		}
	}
	private HomeForm homeForm;
	private boolean created;
	private Database database;
	private File lastDirectoryAdded;
	private int readList(File file) throws IOException {
		list.clear();
		int count = 0;
		StringBuilder listContent = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!line.isEmpty() && line.charAt(0) != '#') {
					File path = new File(line);
					if (path.exists() && path.isDirectory()) {
						++count;
						listContent.append(path.getAbsoluteFile()).append(Utils.endl);
					}
				}
			}
		}
		list.setText(listContent.toString());
		return count;
	}
	private void disableAll() {
		listPath.setDisable(true);
		listChooser.setDisable(true);
		listCreator.setDisable(true);
		list.setDisable(true);
		folderChooser.setDisable(true);
		databaseMaker.setDisable(true);
		next.setDisable(true);
	}
	private void enableAll() {
		listPath.setDisable(false);
		listChooser.setDisable(false);
		listCreator.setDisable(false);
		list.setDisable(false);
		folderChooser.setDisable(false);
		databaseMaker.setDisable(false);
		next.setDisable(false);
	}
	private void setDatabaseUpdated() {
		next.setDisable(false);
	}
	@Override
	public void init(HomeForm form) {
		homeForm = form;
		homeForm.gui().setInformer(new HomeInformer());
		databaseMaker.setDisable(true);
		next.setDisable(true);
		list.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				newValue = newValue.trim();
				if (newValue.isEmpty())
					databaseMaker.setDisable(true);
				else
					databaseMaker.setDisable(false);
			}
		});
	}
}
