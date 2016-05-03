package videoman.core.database;

public class SpecialProperty extends Query {
	private String usedName;
	public SpecialProperty(String used) {
		super('#' + used);
		usedName = getValue();
	}
	@Override
	public int compareTo(Property other) {
		return other instanceof SpecialProperty ? super.compareTo(other) : -1;
	}
}
