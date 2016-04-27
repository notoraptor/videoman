package videoman.core.database;

public enum Type {
	FOLDER, PERSON, CATEGORY, COUNTRY,
	QUERY;
	static public String getPropertyName(Type type) {
		switch (type) {
			case PERSON:
				return "personnes";
			case CATEGORY:
				return "catégories";
			case COUNTRY:
				return "lieux";
			case FOLDER:
			case QUERY:
				break;
		}
		return null;
	}
}
