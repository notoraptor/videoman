package videoman.core;

import org.json.JSONArray;
import org.json.JSONObject;
import videoman.Utils;
import videoman.core.database.Database;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class VideoFile extends Video {
	//@modifiable
	public VideoFile(Database db, FileDesc filepath) throws FileException, IOException, InterruptedException {
		super(db, generateID(filepath));
		filename = filepath;
		extractInfos();
		generateCategories();
		dateAdded = System.currentTimeMillis();
		database.addVideoFolder(this);
		resetModifications();
		modify();
		checkName();
	}
	static public boolean isSaved(File folder, File file) {
		String id = generateID(file);
		File safeguard = safeguard(folder, id);
		File thumbguard = thumbguard(folder, id);
		return safeguard.exists() && thumbguard.exists();
	}
	@Override
	public int save() throws VideoException {
		return modifications() > 1 ? super.save() : (wellSaved() ? 0 : super.save());
	}
	private void extractInfos() throws InterruptedException, VideoException, IOException {
		JSONObject info = ffprobe(filename);
		JSONObject firstAudioStream = null;
		JSONObject firstVideoStream = null;
		int audioStreamCount = 0;
		int videoStreamCount = 0;
		JSONArray streams = info.getJSONArray("streams");
		int streamsCount = streams.length();
		for (int i = 0; i < streamsCount; ++i) {
			JSONObject stream = streams.getJSONObject(i);
			String codecType = stream.getString("codec_type").toLowerCase();
			switch (codecType) {
				case "audio":
					++audioStreamCount;
					if (firstAudioStream == null)
						firstAudioStream = stream;
					break;
				case "video":
					++videoStreamCount;
					if (firstVideoStream == null)
						firstVideoStream = stream;
					break;
				default:
					break;
			}
		}
		if (firstVideoStream == null)
			throw new VideoException(filename, "Pas de flux vidéo dans ce fichier.");
		JSONObject infoFormat = info.getJSONObject("format");
		format = infoFormat.getString("format_long_name");
		size = infoFormat.getLong("size");
		if (!infoFormat.has("duration"))
			throw new VideoException(filename, "Pas de durée pour ce fichier.");
		duration = infoFormat.getDouble("duration");
		videoCodec = firstVideoStream.getString("codec_name");
		width = firstVideoStream.getInt("width");
		height = firstVideoStream.getInt("height");
		if (firstAudioStream != null)
			audioCodec = firstAudioStream.getString("codec_name");
	}
	private void generateCategories() {
		String pattern = "(?U)[^\\p{Alpha}0-9']+";
		for (String category : filename.getAbsolutePath().split(pattern))
			if (!category.isEmpty()) {
				database.addCategory(this, category);
			}
	}
	private JSONObject ffprobe(File file) throws IOException, InterruptedException, VideoException {
		//String command = "ffprobe -v quiet -print_format json -show_error -show_format -show_streams \"" + file + "\"";
		String[] commands = new String[]{
				"ffprobe",
				"-v",
				"quiet",
				"-print_format",
				"json",
				"-show_error",
				"-show_format",
				"-show_streams",
				file.getAbsolutePath()
		};
		StringBuilder json = Utils.execute(commands);
		if (json.length() == 0)
			throw new VideoException(filename, "Impossible d'extraire les informations du fichier.");
		return new JSONObject(json.toString());
	}
}
