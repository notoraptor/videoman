package videoman.core;

import javafx.geometry.Orientation;
import videoman.Utils;
import videoman.core.database.*;
import videoman.notification.Notification;
import videoman.notification.info.MessageInfo;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.Set;

abstract public class Video {
	protected FileDesc filename;
	protected long dateAdded;
	protected String format;
	protected long size;
	protected double duration;
	protected String audioCodec;
	protected String videoCodec;
	protected int width;
	protected int height;
	// Initialisés.
	private int modifications;
	protected Database database;
	protected String id;
	private FileDesc databaseFolder;
	protected FileDesc thumbnail;
	protected Notation notation;
	protected PropertySet videoPropertySet;
	public Video(Database db, String outputId) {
		assert db != null && outputId != null;
		database = db;
		id = outputId;
		databaseFolder = db.getDirectory();
		thumbnail = thumbguard();
		notation = Notation.ZERO;
		videoPropertySet = new PropertySet();
	}
	static protected String generateID(File file) {
		try {
			return Utils.hash(file.getAbsolutePath());
		} catch (NoSuchAlgorithmException ignored) {
			return null;
		}
	}
	static protected File safeguard(File databaseFolder, String id) {
		return new File(databaseFolder, id + Const.ROW_EXTENSION);
	}
	static protected FileDesc thumbguard(File databaseFolder, String id) {
		return new FileDesc(databaseFolder, id + Const.THUMBNAIL_EXTENSION);
	}
	private File safeguard() {
		return new File(databaseFolder, id + Const.ROW_EXTENSION);
	}
	private FileDesc thumbguard() {
		return new FileDesc(databaseFolder, id + Const.THUMBNAIL_EXTENSION);
	}
	private FileDesc getThumbnail() throws VideoException {
		if (!thumbnail.exists()) {
			// Générer la miniature.
			String[] commands = new String[] {
					"ffmpeg", "-y", "-ss", String.valueOf(((int) (duration / 2))),
					"-i", filename.getAbsolutePath(), "-vframes", "1",
					thumbnail.getAbsolutePath() //,"2>&1"
			};
			try {
				Utils.execute(commands);
			} catch (IOException | InterruptedException e) {
				throw new VideoException(filename, "Miniature non générée avec la commande:" + Utils.endl + Utils.implode(" ", commands));
			}
			if (!thumbnail.exists() || !thumbnail.isFile())
				throw new VideoException(filename, "Miniature introuvable après la commande:" + Utils.endl + Utils.implode(" ", commands));
		}
		return thumbnail;
	}
	protected void checkName() {
		String name = getName();
		String trim = name.trim();
		if (!name.equals(trim))
			setName(trim);
	}
	protected boolean wellSaved() {
		return safeguard().exists() && thumbnail.exists();
	}
	protected int modifications() {
		return modifications;
	}
	protected void resetModifications() {
		modifications = 0;
	}
	public String getVideoFolder() {
		return filename.getParentFile().getAbsolutePath();
	}
	public String getVideoPath() {
		return filename.getAbsolutePath();
	}
	public VideoDate getDatabaseDateAdded() {
		return new VideoDate(dateAdded);
	}
	public VideoDate getVideoDateModified() {
		return new VideoDate(filename.lastModified());
	}
	public String getName() {
		return filename.getName();
	}
	public String getExtension() {
		return filename.getExtension();
	}
	public String getFormat() {
		return format;
	}
	public long getSize() {
		return size;
	}
	public VideoSize getHumanSize() {
		return new VideoSize(size);
	}
	public VideoDuration getDuration() {
		return new VideoDuration(duration);
	}
	public String getAudioCodec() {
		return audioCodec;
	}
	public String getVideoCodec() {
		return videoCodec;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public String getThumbnailPath() {
		return thumbnail.getAbsolutePath();
	}
	public VideoQuality getVideoQuality() {
		return new VideoQuality(this);
	}
	public Notation getNotation() {
		return notation;
	}
	public Orientation getOrientation() {
		return width > height ? Orientation.HORIZONTAL : Orientation.VERTICAL;
	}
	public StringBuilder getPersons() {
		return videoPropertySet.implodePersons(Const.delimiter);
	}
	public StringBuilder getCategories() {
		return videoPropertySet.implodeCategories(Const.delimiter);
	}
	public StringBuilder getCountries() {
		return videoPropertySet.implodeCountries(Const.delimiter);
	}
	public StringBuilder getSuperInfo() {
		StringBuilder s = new StringBuilder();
		s.append(filename.getAbsolutePath().toLowerCase());
		s.append('/').append(getVideoDateModified().toString().toLowerCase());
		s.append('/').append(getDatabaseDateAdded().toString().toLowerCase());
		s.append('/').append(format.toLowerCase());
		s.append('/').append(getHumanSize().toString().toLowerCase());
		s.append('/').append(getDuration().toString().toLowerCase());
		s.append('/').append(audioCodec.toLowerCase());
		s.append('/').append(videoCodec.toLowerCase());
		s.append('/').append(width);
		s.append('/').append(height);
		s.append('/').append(getVideoQuality());
		s.append('/').append(notation.ordinal());
		s.append('/').append(getOrientation().toString().toLowerCase());
		s.append('/').append(getPersons());
		s.append('/').append(getCategories());
		s.append('/').append(getCountries());
		return s;
	}
	public String concatenateFormat() {
		return filename.getExtension() + " / " + format + " / " + videoCodec + " / " + audioCodec;
	}
	public boolean hasProperty(Property property) {
		return videoPropertySet.contains(property);
	}
	public void addPropertiesTo(Collection<Property> collection, Type type) {
		switch (type) {
			case FOLDER:
				break;
			case PERSON:
				addPersonsTo(collection);
				break;
			case CATEGORY:
				addCategoriesTo(collection);
				break;
			case COUNTRY:
				addCountriesTo(collection);
				break;
			case QUERY:
				break;
		}
	}
	public void addPersonsTo(Collection<Property> collection) {
		videoPropertySet.addPersonsTo(collection);
	}
	public void addCategoriesTo(Collection<Property> collection) {
		videoPropertySet.addCategoriesTo(collection);
	}
	public void addCountriesTo(Collection<Property> collection) {
		videoPropertySet.addCountriesTo(collection);
	}
	public void removeFromProperties() {
		videoPropertySet.remove(this);
	}
	public void deletePermanently() throws FileException {
		if (!filename.delete())
			throw new FileException(filename, "Impossible de supprimer le fichier vidéo.");
		File sg = safeguard();
		if (sg.exists() && !sg.delete())
			throw new FileException(sg, "Impossible de supprimer cette entrée de la base de données.");
		if (thumbnail.exists() && !thumbnail.delete())
			throw new FileException(thumbnail, "Impossible de supprimer cette miniature de la base de données.");
		System.out.println("Vidéo supprimée: " + filename.getAbsolutePath());
	}
	@Override
	public String toString() {
		return filename.getName() + " (" + filename.getExtension() + ", " + getHumanSize() + ')';
	}
	// @modifier
	public void modify() {
		//new Exception("modifié.").printStackTrace();
		++modifications;
	}
	//@save
	public int save() throws VideoException {
		if (modifications <= 0)
			return 0;
		VideoManStringWriter videoManStringWriter = new VideoManStringWriter();
		videoManStringWriter.write(Field.id + "\t" + id);
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.path + "\t" + filename.getAbsolutePath());
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.dateAdded + "\t" + dateAdded);
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.format + "\t" + format);
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.size + "\t" + size);
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.duration + "\t" + duration);
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.audioCodec + "\t" + audioCodec);
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.videoCodec + "\t" + videoCodec);
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.width + "\t" + width);
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.height + "\t" + height);
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.notation + "\t" + notation.name());
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.countries + "\t" + getCountries());
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.categories + "\t" + getCategories());
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.persons + "\t" + getPersons());
		videoManStringWriter.newLine();
		videoManStringWriter.write(Field.thumbnail + "\t" + getThumbnail());
		videoManStringWriter.newLine();
		try {
			try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(safeguard()))) {
				bufferedWriter.write(
						Base64.getEncoder().encodeToString(
								videoManStringWriter.toString().getBytes(Utils.UTF8)
						)
				);
			}
		} catch (IOException e) {
			throw new VideoException(filename, "Problème pendant la sauvegarde de l'entrée." + Utils.endl + e.getMessage());
		}
		return 1;
	}
	//@modifiable
	public void setName(String newName) {
		newName = newName.trim();
		if (!filename.getName().equals(newName)) {
			FileDesc newFilename = new FileDesc(filename.getParentFile(), newName + '.' + filename.getExtension());
			if (!newFilename.exists()) {
				if (filename.renameTo(newFilename)) {
					modify();
					MessageInfo.inform("Nouveau nom attribué.");
					safeguard().delete();
					id = generateID(newFilename);
					FileDesc newThumbnailName = thumbguard();
					if (thumbnail.exists()) {
						thumbnail.renameTo(newThumbnailName);
					}
					filename = newFilename;
					thumbnail = newThumbnailName;
					return;
				}
			}
		}
		Notification.bad("Impossible de renommer le fichier . Nouveau nom invalide ou déjà existant.");
	}
	//@modifiable
	public void setNotation(Notation newNotation) {
		if (notation != newNotation) {
			modify();
			notation = newNotation;
		}
	}
	//@modifiable
	public void add(Property property) {
		if (videoPropertySet.add(property)) {
			property.add(this);
			if(!property.isQuery()) modify();
		}
	}
	//@modifiable
	public void remove(Property property) {
		if (videoPropertySet.remove(property)) {
			property.remove(this);
			if(!property.isQuery()) modify();
		}
	}
	//@modifiable
	public void updateCategories(Set<Category> toRemove, Set<Category> toAdd) {
		for (Category c : toRemove)
			if (c.remove(this)) modify();
		for (Category c : toAdd)
			if (c.add(this)) modify();
	}
	//@modifiable
	public void updatePersons(Set<Person> toRemove, Set<Person> toAdd) {
		for (Person p : toRemove)
			if (p.remove(this)) modify();
		for (Person p : toAdd)
			if (p.add(this)) modify();
	}
	//@modifiable
	public void updateCountries(Set<Country> toRemove, Set<Country> toAdd) {
		for (Country c : toRemove)
			if (c.remove(this)) modify();
		for (Country c : toAdd)
			if (c.add(this)) modify();
	}
	//@modifiable
	public void update(Set<Property> toRemove, Set<Property> toAdd) {
		for (Property p: toRemove)
			if (p.remove(this)) modify();
		for (Property p: toAdd)
			if (p.add(this)) modify();
	}
}
