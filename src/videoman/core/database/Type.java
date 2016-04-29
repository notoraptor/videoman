package videoman.core.database;

public enum Type {
	FOLDER, PERSON, CATEGORY, COUNTRY,
	QUERY;
	static public String getPropertyPluralName(Type type) {
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
	static public String getPropertyName(Type type) {
		switch (type) {
			case PERSON:
				return "personne";
			case CATEGORY:
				return "catégorie";
			case COUNTRY:
				return "lieu";
			case FOLDER:
			case QUERY:
				break;
		}
		return null;
	}
}
