package videoman;

import videoman.core.database.Database;
import videoman.core.FileDesc;
import videoman.core.FileException;
import videoman.core.VideoDuration;
import videoman.notification.Notification;
import videoman.notification.info.MessageInfo;
import videoman.notification.info.ProgressInfo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class VideoMan {
	static public Database updateDatabase(File list, Collection<File> folders) throws Exception {
		MessageInfo.inform("Lecture des vidéos. Collecte en cours, veuillez patienter ...");
		long start = System.currentTimeMillis();
		FileDesc dbDirectory = new FileDesc(new File(list.getAbsolutePath()).getParentFile(), Utils.getName(list));
		Database database = new Database(dbDirectory);
		LinkedList<File> collection = new LinkedList<>();
		for (File folder : folders)
			database.collectFiles(folder, collection);
		MessageInfo.inform(database.countBadFiles() + " fichiers ignorés.");
		MessageInfo.inform(collection.size() + " fichiers listés.");

		int before = collection.size();
		Database.checkFilesToUpdate(dbDirectory, collection);
		int after = collection.size();
		if (after != before)
			MessageInfo.inform(after + " fichiers à sauvegarder.");
		database.processFiles(collection);
		Notification.info(new ProgressInfo(collection.size()));
		int databaseSize = database.size();
		MessageInfo.inform(databaseSize + " vidéos lues au total.");
		if (databaseSize != 0)
			database.save();
		long end = System.currentTimeMillis();
		long duration = (end - start + 1L) / 1000L;
		MessageInfo.inform(databaseSize + " fichiers sauvegardés en " + new VideoDuration(duration) + ".");
		return database;
	}
	static public void loadDatabase(Database database) throws IOException, FileException {
		MessageInfo.inform("Chargement de la base de données.");
		long start = System.currentTimeMillis();
		database.load();
		long end = System.currentTimeMillis();
		long duration = (end - start + 1L) / 1000L;
		MessageInfo.inform(database.countBadFiles() + " entrées ignorées." + Utils.endl +
				database.size() + " vidéos lues en " + new VideoDuration(duration) + ".");
	}
	static public void main(String[] args) throws Exception {
		System.out.println("Hello, Video Man !");
	}
}
