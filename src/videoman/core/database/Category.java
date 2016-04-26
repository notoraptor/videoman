package videoman.core.database;

public class Category extends Property {
	Category(String name) {
		super(Type.CATEGORY, name);
	}
	Category(Property oldProperty, String newName) {
		super(oldProperty, newName);
	}
}
