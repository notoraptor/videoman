package videoman.core;

public class Const {
	// Pour VideoDuration.
	static public final String ROW_EXTENSION = ".row";
	static public final String THUMBNAIL_EXTENSION = ".jpg";
	static public final int HOURS = 0;
	static public final int MINUTES = 1;
	static public final int SECONDS = 2;
	static public final int DURATION_COUNT = 3;
	static public final String[] durationNames = new String[]{"h", "min", "sec"};
	static public final String delimiter = ", ";
	// Pour DatabaseController.
	static private final String stringSortName = "Trier par nom ";
	static private final String stringSortSize = "Trier par taille ";
	static private final String ascendingSymbol = "\u25b2";
	static private final String descendingSymbol = "\u25bc";
	static public final String magnifierSymbol = "\ud83d\udd0d";
	//static private final String raw = "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum";
	static private final String raw = null;
	public static final String defaultName = raw;
	public static final String defaultOpenFolder = raw;
	public static final String defaultDuration = raw;
	public static final String defaultFormat = raw;
	public static final String defaultSize = raw;
	public static final Notation defaultNotation = Notation.ZERO;
	public static final String defaultPersons = null;
	public static final String defaultCategories = null;
	public static final String defaultCountries = null;
	public static final int PAGESIZE = 500;
}
