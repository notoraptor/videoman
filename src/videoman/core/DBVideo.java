package videoman.core;

import videoman.Utils;
import videoman.core.database.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;
import java.util.HashSet;

// TODO: "thumbnail" ne doit jamais être null.

public class DBVideo extends Video {
	public DBVideo(Database db, FileDesc filepath) throws IOException, FileException, InterruptedException {
		super(db, filepath.getName());
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
			String line;
			while ((line = reader.readLine()) != null)
				content.append(line);
		}
		BufferedReader reader = new BufferedReader(new StringReader(new String(
			Base64.getDecoder().decode(content.toString()),
			Utils.UTF8
		)));
		String line;
		HashSet<Field> read = new HashSet<>();
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (!line.isEmpty()) {
				String[] pieces = line.split("\t", 2);
				String key = pieces[0].trim();
				String value = pieces.length == 2 ? pieces[1].trim() : "";
				Field field = Field.valueOf(key);
				if (field != Field.categories && field != Field.countries && field != Field.persons && value.isEmpty())
					throw new FileException(filepath, field + " indisponible.");
				read.add(field);
				switch (field) {
					case id:
						if (!id.equals(value))
							throw new FileException(filepath, "Les IDs déduits et lus sont incompatibles.");
						break;
					case path:
						filename = new FileDesc(value);
						String readId = generateID(filename);
						if (!id.equals(readId))
							throw new FileException(filepath, "L'ID ne correspond pas au fichier vidéo." +
											"\nDéduit: " + id + "\nLu:     " + readId + "\nDepuis: " + value);
						break;
					case dateAdded:
						dateAdded = Long.parseLong(value);
						break;
					case format:
						format = value;
						break;
					case size:
						size = Long.parseLong(value);
						break;
					case duration:
						duration = Double.parseDouble(value);
						break;
					case audioCodec:
						audioCodec = value;
						break;
					case videoCodec:
						videoCodec = value;
						break;
					case width:
						width = Integer.parseInt(value);
						break;
					case height:
						height = Integer.parseInt(value);
						break;
					case notation:
						notation = Notation.valueOf(value);
						break;
					case countries:
						for (String piece : value.split(", ")) if (!piece.isEmpty())
								database.addCountry(this, piece);
						break;
					case persons:
						for (String piece : value.split(", ")) if (!piece.isEmpty())
								database.addPerson(this, piece);
						break;
					case categories:
						for (String piece : value.split(", ")) if (!piece.isEmpty())
								database.addCategory(this, piece);
						break;
					case thumbnail:
						if (!thumbnail.getAbsolutePath().equals(value))
							throw new FileException(thumbnail, "Les chemins de miniature déduits et lus sont incompatibles.\n" + value);
						break;
					case frameRate:
						frameRate = Double.parseDouble(value);
						break;
					case sampleRate:
						sampleRate = Double.parseDouble(value);
						break;
				}
			}
		}
		reader.close();
		if (read.size() != Field.values().length)
			throw new FileException(filepath, "Clés manquantes.");
		if (!filename.exists() || !filename.isFile())
			throw new FileException(filename, "Impossible de trouver cette vidéo.");
		database.addVideoFolder(this);
		resetModifications();
		checkName();
	}
}
