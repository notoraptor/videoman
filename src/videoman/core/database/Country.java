package videoman.core.database;

public class Country extends Property {
	Country(String name) {
		super(Type.COUNTRY, name);
	}
	Country(Property oldProperty, String newName) {
		super(oldProperty, newName);
	}
}
