package videoman.core;

import org.json.JSONArray;
import org.json.JSONObject;
import videoman.Utils;
import videoman.core.database.Database;

import java.io.File;
import java.io.IOException;

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
		extractSampleRate(this, firstVideoStream, firstAudioStream);
	}
	static public void extractSampleRate(Video video) throws InterruptedException, VideoException, IOException {
		if (video.getFrameRate() == 0 || Double.isNaN(video.getFrameRate())) {
			File filename = new File(video.getVideoPath());
			JSONObject info = ffprobe(filename);
			JSONObject firstAudioStream = null;
			JSONObject firstVideoStream = null;
			JSONArray streams = info.getJSONArray("streams");
			int streamsCount = streams.length();
			for (int i = 0; i < streamsCount; ++i) {
				JSONObject stream = streams.getJSONObject(i);
				String codecType = stream.getString("codec_type").toLowerCase();
				switch (codecType) {
					case "audio":
						if (firstAudioStream == null)
							firstAudioStream = stream;
						break;
					case "video":
						if (firstVideoStream == null)
							firstVideoStream = stream;
						break;
					default:
						break;
				}
			}
			if (firstVideoStream == null)
				throw new VideoException(filename, "Pas de flux vidéo dans ce fichier.");
			extractSampleRate(video, firstVideoStream, firstAudioStream);
		}
	}
	static private void extractSampleRate(Video video, JSONObject firstVideoStream, JSONObject firstAudioStream) throws VideoException {
		File filename = new File(video.getVideoPath());
		String f = firstVideoStream.getString("avg_frame_rate");
		if (f.equals("0") || f.equals("0/0"))
			f = firstVideoStream.getString("r_frame_rate");
		String[] fpieces = f.split("/");
		if (fpieces.length > 2) throw new VideoException(filename, "Impossible d'analyser la fréquence d'images.");
		double frameRate = Double.parseDouble(fpieces[0]);
		if (fpieces.length == 2) frameRate /= Double.parseDouble(fpieces[1]);
		if (Double.isNaN(frameRate)) frameRate = 24; // Par défaut, disons 24 images par seconde.
		video.setFrameRate(frameRate);
		if (firstAudioStream != null) {
			String s = firstAudioStream.getString("sample_rate");
			String[] spieces =s.split("/");
			if (spieces.length > 2) throw new VideoException(filename, "Impossible d'analyser la fréquence audio.");
			double sampleRate = Double.parseDouble(spieces[0]);
			if (spieces.length == 2) sampleRate /= Double.parseDouble(spieces[1]);
			if (Double.isNaN(sampleRate)) sampleRate = 44100; // Par défaut, disons 44100Hz.
			video.setSampleRate(sampleRate);
		}
	}
	private void generateCategories() {
		for (String category : Utils.explode(filename.getAbsoluteName()))
			if (!category.isEmpty()) {
				database.addCategory(this, category);
			}
	}
	static private JSONObject ffprobe(File file) throws IOException, InterruptedException, VideoException {
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
			throw new VideoException(file, "Impossible d'extraire les informations du fichier.");
		return new JSONObject(json.toString());
	}
}
