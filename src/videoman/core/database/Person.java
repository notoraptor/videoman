package videoman.core.database;

public class Person extends Property {
	Person(String name) {
		super(Type.PERSON, name);
	}
	Person(Property oldProperty, String newName) {
		super(oldProperty, newName);
	}
}
