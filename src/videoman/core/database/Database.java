package videoman.core.database;

import videoman.Utils;
import videoman.core.*;
import videoman.notification.Notification;
import videoman.notification.info.MessageInfo;
import videoman.notification.info.ProgressInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Database {
	private View databaseView;
	private FileDesc directory;
	private LinkedList<File> badFiles;
	private ArrayList<Video> database;
	private PropertySet properties;
	private HashSet<String> extensions;
	private HashSet<String> readExtensions() throws IOException {
		HashSet<String> extensions = new HashSet<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				Database.class.getResourceAsStream("/videoman/resource/extensions")
		))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!line.isEmpty() && line.charAt(0) != '#')
					extensions.add(line);
			}
		}
		return extensions;
	}
	private boolean hasExtension(File file) {
		return extensions.isEmpty() || extensions.contains(Utils.getExtension(file));
	}
	public Database(FileDesc dbDirectory) throws FileException, IOException {
		validateDatabaseFolder(dbDirectory);
		directory = dbDirectory;
		badFiles = new LinkedList<>();
		database = new ArrayList<>();
		properties = new PropertySet();
		extensions = readExtensions();
		databaseView = new View();
	}
	private void clear() {
		badFiles.clear();
		database.clear();
		properties.clear();
	}
	private void refreshProperties() {
		properties.refresh();
	}
	private void updateView() {
		if (databaseView.showed == null || (!databaseView.showed.isQuery() && !properties.contains(databaseView.showed)))
			databaseView.showAll();
		else
			databaseView.update();
	}
	public FileDesc getDirectory() {
		return directory;
	}
	public Property create(String name, Type type) {
		switch (type) {
			case FOLDER:
				break;
			case PERSON:
				return createPerson(name);
			case CATEGORY:
				return createCategory(name);
			case COUNTRY:
				return createCountry(name);
			case QUERY:
				break;
		}
		return null;
	}
	private SpecialProperty createSpecialProperty(String name) {
		SpecialProperty specialProperty = (SpecialProperty) properties.find(Type.QUERY, name);
		if (specialProperty == null) {
			specialProperty = new SpecialProperty(name);
			properties.add(specialProperty);
		}
		return specialProperty;
	}
	public Person createPerson(String name) {
		name = name.trim().toLowerCase();
		Person p = (Person) properties.find(Type.PERSON, name);
		if (p == null) {
			p = new Person(name);
			properties.add(p);
		}
		return p;
	}
	public Category createCategory(String name) {
		name = name.trim().toLowerCase();
		Category c = (Category) properties.find(Type.CATEGORY, name);
		if (c == null) {
			c = new Category(name);
			properties.add(c);
		}
		return c;
	}
	public Country createCountry(String name) {
		name = name.trim().toLowerCase();
		Country c = (Country) properties.find(Type.COUNTRY, name);
		if (c == null) {
			c = new Country(name);
			properties.add(c);
		}
		return c;
	}
	public void addVideoFolder(Video video) {
		String name = video.getVideoFolder();
		Property folder = properties.find(Type.FOLDER, name);
		if (folder == null) {
			folder = new Folder(name);
			properties.add(folder);
		}
		folder.add(video);
	}
	public void addCategory(Video video, String name) {
		name = name.trim().toLowerCase();
		Property category = properties.find(Type.CATEGORY, name);
		if (category == null) {
			category = new Category(name);
			properties.add(category);
		}
		video.add(category);
	}
	public void addCountry(Video video, String name) {
		name = name.trim().toLowerCase();
		Property country = properties.find(Type.COUNTRY, name);
		if (country == null) {
			country = new Country(name);
			properties.add(country);
		}
		video.add(country);
	}
	public void addPerson(Video video, String name) {
		name = name.trim().toLowerCase();
		Property person = properties.find(Type.PERSON, name);
		if (person == null) {
			person = new Person(name);
			properties.add(person);
		}
		video.add(person);
	}
	public boolean renamePerson(String oldName, String newName) {
		boolean modified = false;
		oldName = oldName.trim().toLowerCase();
		newName = newName.trim().toLowerCase();
		Property oldProperty = properties.find(Type.PERSON, oldName);
		Property newProperty = properties.find(Type.PERSON, newName);
		if (oldProperty != null) {
			modified = true;
			properties.remove(oldProperty);
			if (newProperty == null) {
				newProperty = new Person(oldProperty, newName);
				properties.add(newProperty);
			} else {
				oldProperty.transferTo(newProperty);
			}
		}
		return modified;
	}
	public boolean renameCategory(String oldName, String newName) {
		boolean modified = false;
		oldName = oldName.trim().toLowerCase();
		newName = newName.trim().toLowerCase();
		Property oldProperty = properties.find(Type.CATEGORY, oldName);
		Property newProperty = properties.find(Type.CATEGORY, newName);
		if (oldProperty != null) {
			modified = true;
			properties.remove(oldProperty);
			if (newProperty == null) {
				newProperty = new Category(oldProperty, newName);
				properties.add(newProperty);
			} else {
				oldProperty.transferTo(newProperty);
			}
		}
		return modified;
	}
	public boolean renameCountry(String oldName, String newName) {
		boolean modified = false;
		oldName = oldName.trim().toLowerCase();
		newName = newName.trim().toLowerCase();
		Property oldProperty = properties.find(Type.COUNTRY, oldName);
		Property newProperty = properties.find(Type.COUNTRY, newName);
		if (oldProperty != null) {
			modified = true;
			properties.remove(oldProperty);
			if (newProperty == null) {
				newProperty = new Country(oldProperty, newName);
				properties.add(newProperty);
			} else {
				oldProperty.transferTo(newProperty);
			}
		}
		return modified;
	}
	private void add(Video video) {
		database.add(video);
		//addVideoFolder(video);
	}
	public void addVideo(VideoFile video, int videoCount) {
		add(video);
		int count = database.size();
		if (count % 5 == 0) {
			// System.err.println(count + " vidéos lues.");
			Notification.info(new ProgressInfo(count, videoCount));
		}
	}
	public void addVideo(DBVideo video) {
		add(video);
		int count = database.size();
		if (count % 100 == 0) MessageInfo.inform(count + " vidéos chargées.");
	}
	public void addBadFile(File badFile) {
		badFiles.add(badFile);
	}
	public int countCountries() {
		return properties.sizeOf(Type.COUNTRY);
	}
	public int countCategories() {
		return properties.sizeOf(Type.CATEGORY);
	}
	public int countPersons() {
		return properties.sizeOf(Type.PERSON);
	}
	public int countBadFiles() {
		return badFiles.size();
	}
	public long getVideosTotalSize() {
		long total = 0;
		for (Video video: database) total += video.getSize();
		return total;
	}
	public int size() {
		return database.size();
	}
	public void load(int from, int to) {
		clear();
		File[] files = directory.listFiles();
		if (files == null)
			return;
		if (from < 1) from = 1;
		if (to < from) to = files.length;
		for (int i = from; i <= to; ++i) {
			File file = files[i - 1];
			if (('.' + Utils.getExtension(file)).equals(Const.ROW_EXTENSION) && file.isFile()) {
				try {
					addVideo(new DBVideo(this, new FileDesc(file)));
				} catch (Exception e) {
					//System.err.println("Exception: " + e);
					e.printStackTrace();
					addBadFile(file);
					if(!file.delete())
						System.err.println("Impossible de supprimer l'entrée " + file);
					System.err.println("Entrée supprimée: " + file);
				}
			}
		}
		databaseView.update();
	}
	public void load() {
		load(-1, -1);
	}
	public void save() throws FileException {
		MessageInfo.inform("Génération de la base de données.");
		int saved = 0;
		for (Video video : database) {
			saved += video.save();
			if (saved != 0 && saved % 5 == 0) {
				Notification.info(new ProgressInfo(saved, database.size()));
			}
		}
		Notification.info(new ProgressInfo(database.size()));
		MessageInfo.inform(saved + " vidéos enregistrées au total dans le dossier " + directory);
	}
	public void collectFiles(File folder, Collection<File> collector) {
		File[] files = folder.listFiles();
		if (files == null)
			return;
		for (File path : files) {
			if (path.isFile()) {
				if (hasExtension(path))
					collector.add(path);
				else
					addBadFile(path);
			} else if (path.isDirectory()) {
				collectFiles(path, collector);
			}
		}
	}
	public void processFiles(Collection<File> files) {
		for (File file : files) try {
			addVideo(new VideoFile(this, new FileDesc(file)), files.size());
		} catch (Exception e) {
			System.err.println("Exception: " + e);
			e.printStackTrace();
			addBadFile(file);
		}
		databaseView.update();
	}
	public void clearVideosFromPersonsToCategories() {
		for (Video video: database) video.clearFromPersonsToCategories();
		refreshProperties();
		updateView();
	}
	@Override
	public String toString() {
		return Utils.orthograph(database.size(), "vidéo") + " (" + new VideoSize(getVideosTotalSize())  + ").";
	}
	public String asString() {
		return toString();
	}
	//@unused
	public void remove(Collection<Video> toRemove) {
		for(Video video: toRemove)
			video.removeFromProperties();
		database.removeAll(toRemove);
		refreshProperties();
		updateView();
	}
	public void deletePermanently(LinkedList<Video> toDelete) throws FileException {
		for(Video video: toDelete) {
			video.removeFromProperties();
		}
		database.removeAll(toDelete);
		refreshProperties();
		updateView();
		for(Video video: toDelete) {
			video.deletePermanently();
		}
	}
	public void update(List<Video> videos, Set<Property> toRemove, Set<Property> toAdd) {
		HashSet<Property> propertiesToAdd = new HashSet<>();
		for (Property propertyToAdd: toAdd) {
			Property p = properties.find(propertyToAdd.type(), propertyToAdd.getValue());
			if (p == null) {
				p = propertyToAdd;
				properties.add(p);
			}
			propertiesToAdd.add(p);
		}
		for(Video video: videos)
			video.update(toRemove, propertiesToAdd);
		refreshProperties();
		updateView();
	}
	public void updateCategories(List<Video> videos, Set<Category> toRemove, Set<Category> toAdd) {
		HashSet<Category> propertiesToAdd = new HashSet<>();
		for (Category propertyToAdd: toAdd) {
			Category p = (Category) properties.find(propertyToAdd.type(), propertyToAdd.getValue());
			if (p == null) {
				p = propertyToAdd;
				properties.add(p);
			}
			propertiesToAdd.add(p);
		}
		for(Video video: videos)
			video.updateCategories(toRemove, propertiesToAdd);
		refreshProperties();
		updateView();
	}
	public void updatePersons(List<Video> videos, Set<Person> toRemove, Set<Person> toAdd) {
		HashSet<Person> propertiesToAdd = new HashSet<>();
		for (Person propertyToAdd: toAdd) {
			Person p = (Person) properties.find(propertyToAdd.type(), propertyToAdd.getValue());
			if (p == null) {
				p = propertyToAdd;
				properties.add(p);
			}
			propertiesToAdd.add(p);
		}
		for(Video video: videos)
			video.updatePersons(toRemove, propertiesToAdd);
		refreshProperties();
		updateView();
	}
	public void updateCountries(List<Video> videos, Set<Country> toRemove, Set<Country> toAdd) {
		HashSet<Country> propertiesToAdd = new HashSet<>();
		for (Country propertyToAdd: toAdd) {
			Country p = (Country) properties.find(propertyToAdd.type(), propertyToAdd.getValue());
			if (p == null) {
				p = propertyToAdd;
				properties.add(p);
			}
			propertiesToAdd.add(p);
		}
		for(Video video: videos)
			video.updateCountries(toRemove, propertiesToAdd);
		refreshProperties();
		updateView();
	}
	public int find(String query) {
		query = query.toLowerCase();
		String[] pieces = Utils.explode(query);
		LinkedList<Video> found = new LinkedList<>();
		for (Video video: database) {
			StringBuilder superInfo = video.getSuperInfo();
			boolean run = true;
			for (int i = 0; run && i < pieces.length; ++i) {
				if (superInfo.indexOf(pieces[i]) < 0)
					run = false;
			}
			if (run) found.add(video);
		}
		Query queryProperty = new Query(query);
		for (Video video: found) queryProperty.add(video);
		databaseView.show(queryProperty);
		return queryProperty.getSize();
	}
	static private void validateDatabaseFolder(File directory) throws FileException {
		if (directory.exists()) {
			if (!directory.isDirectory())
				throw new FileException(directory, "Le chemin vers la base de données est un fichier, pas un dossier.");
		} else if (!directory.mkdir())
			throw new FileException(directory, "Impossible de créer le dossier de la base de données.");
	}
	static public void checkFilesToUpdate(File folder, Collection<File> collection) throws NoSuchAlgorithmException {
		Iterator<File> iterator = collection.iterator();
		while (iterator.hasNext())
			if (VideoFile.isSaved(folder, iterator.next()))
				iterator.remove();
	}
	public View getDatabaseView() {
		return databaseView;
	}
	public void deleteProperty(Property property) {
		if (properties.contains(property)) {
			property.clear();
			refreshProperties();
			updateView();
		}
	}
	public void searchSameDuration() {
		TreeMap<Long, TreeSet<Video>> cluster = new TreeMap<>();
		for (Video video: database) {
			TreeSet<Video> list;
			long videoDuration = video.getDuration().toLong();
			if (cluster.containsKey(videoDuration))
				list = cluster.get(videoDuration);
			else {
				list = new TreeSet<>((v1, v2) -> v1.getName().compareTo(v2.getName()));
				cluster.put(videoDuration, list);
			}
			list.add(video);
		}
		Iterator<Long> videoDurationIterator = cluster.keySet().iterator();
		while (videoDurationIterator.hasNext()) {
			if (cluster.get(videoDurationIterator.next()).size() < 2)
				videoDurationIterator.remove();
		}
		Query query = new Query("vidéos aux durées identiques");
		for (TreeSet<Video> list: cluster.values()) for (Video video: list)
			query.add(video);
		databaseView.show(query);
	}
	public class View {
		private ArrayList<Video> view;
		private Property showed;
		public View() {
			view = database;
			showed = null;
		}
		private void allocate() {
			if (showed == null)
				view = new ArrayList<>();
			else
				view.clear();
		}
		private void update() {
			if (showed == null)
				showAll();
			else
				show(showed);
		}
		public void showAll() {
			if (showed != null) {
				view.clear();
				view = null;
			}
			view = database;
			showed = null;
		}
		public void show(Property property) {
			allocate();
			property.copyVideosTo(view);
			showed = property;
		}
		public int size() {
			return view.size();
		}
		private List<Video> copy(int from, int to) {
			int size = view.size();
			if (from < 0) from = 0;
			if (from >= size) from = size - 1;
			if (to < from) to = from;
			if (to >= size) to = size - 1;
			int length = to - from + 1;
			ArrayList<Video> list = new ArrayList<>(length);
			for (int i = from; i <= to; ++i)
				list.add(view.get(i));
			return list;
		}
		public List<Video> get(int from, int to) {
			int size = view.size();
			if (from >= size) from = size - 1;
			if (from < 0) from = 0;
			if (to < from) to = from;
			if (to >= size) to = size - 1;
			List<Video> list = view.subList(from, to + 1);
			return list;
		}
		public void sort(Collection<Sort> sortOrder) {
			//System.err.println("==== " + Math.abs(new Random().nextLong()) + " ====");
			//System.err.println("Before");
			//System.err.println(view.get(0).getName());
			//System.err.println(view.get(view.size()-1).getName());
			Collections.sort(view, new Sort.VideoComparator(sortOrder));
			//System.err.println("After");
			//System.err.println(view.get(0).getName());
			//System.err.println(view.get(view.size()-1).getName());
		}
		private Collection<Property> getProperties(Type type) {
			LinkedList<Property> list = new LinkedList<>();
			String e = type == Type.COUNTRY ? "" : "e";
			SpecialProperty specialProperty = createSpecialProperty("aucun" + e + " " + Type.getPropertyName(type));
			for (Video video: database) {
				if (video.has(type))
					specialProperty.remove(video);
				else
					specialProperty.add(video);
			}
			if(!specialProperty.isEmpty())
				list.add(specialProperty);
			list.addAll(properties.get(type));
			return list;
		}
		public Collection<Property> folders() {
			return getProperties(Type.FOLDER);
		}
		public Collection<Property> persons() {
			return getProperties(Type.PERSON);
		}
		public Collection<Property> categories() {
			return getProperties(Type.CATEGORY);
		}
		public Collection<Property> countries() {
			return getProperties(Type.COUNTRY);
		}
		public String infoToString() {
			if (showed == null)
				return asString();
			else
				return showed.getSize() + " sélectionnées (\"" + showed.getValue() + "\") sur " + asString();
		}
	}
}
