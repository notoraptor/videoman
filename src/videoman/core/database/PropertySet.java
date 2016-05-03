package videoman.core.database;

import videoman.Utils;
import videoman.core.Video;
import videoman.core.database.Property;
import videoman.core.database.Type;

import java.util.*;

public class PropertySet {
	static private Collection<Property> EMPTY = new LinkedList<>();
	private EnumMap<Type, TreeMap<String, Property>> set;
	public PropertySet() {
		set = new EnumMap<>(Type.class);
	}
	public boolean add(Property property) {
		TreeMap<String, Property> properties;
		if (set.containsKey(property.type()))
			properties = set.get(property.type());
		else {
			properties = new TreeMap<>();
			set.put(property.type(), properties);
		}
		return properties.put(property.getValue(), property) == null;
	}
	public boolean remove(Property property) {
		boolean removed = false;
		if (set.containsKey(property.type())) {
			TreeMap<String, Property> properties = set.get(property.type());
			removed = properties.remove(property.getValue()) != null;
			if (properties.isEmpty())
				set.remove(property.type());
		}
		return removed;
	}
	private void addTo(Type type, Collection<Property> collector) {
		if (set.containsKey(type))
			collector.addAll(set.get(type).values());
	}
	public void addPersonsTo(Collection<Property> collector) {
		addTo(Type.PERSON, collector);
	}
	public void addCategoriesTo(Collection<Property> collector) {
		addTo(Type.CATEGORY, collector);
	}
	public void addCountriesTo(Collection<Property> collector) {
		addTo(Type.COUNTRY, collector);
	}
	private StringBuilder implode(String delimiter, Type type) {
		if (set.containsKey(type))
			return Utils.implode(delimiter, set.get(type).values());
		return new StringBuilder();
	}
	public StringBuilder implodePersons(String delimiter) {
		return implode(delimiter, Type.PERSON);
	}
	public StringBuilder implodeCategories(String delimiter) {
		return implode(delimiter, Type.CATEGORY);
	}
	public StringBuilder implodeCountries(String delimiter) {
		return implode(delimiter, Type.COUNTRY);
	}
	public void clear() {
		set.clear();
	}
	public Property find(Type type, String name) {
		if (set.containsKey(type)) {
			return set.get(type).get(name);
		}
		return null;
	}
	public int sizeOf(Type type) {
		return set.containsKey(type) ? set.get(type).size() : 0;
	}
	public void remove(Video video) {
		int totalSize = 0;
		for (TreeMap<String, Property> properties: set.values())
			totalSize += properties.size();
		ArrayList<Property> all = new ArrayList<>(totalSize);
		for (TreeMap<String, Property> properties: set.values())
			all.addAll(properties.values());
		for (Property property: all)
			property.remove(video);
	}
	public void refresh() {
		Iterator<TreeMap<String, Property>> propertiesIterator = set.values().iterator();
		while (propertiesIterator.hasNext()) {
			TreeMap<String, Property> properties = propertiesIterator.next();
			Iterator<Property> propertyIterator = properties.values().iterator();
			while (propertyIterator.hasNext()) {
				Property property = propertyIterator.next();
				if (property.isEmpty())
					propertyIterator.remove();
			}
			if (properties.isEmpty())
				propertiesIterator.remove();
		}
	}
	public Collection<Property> get(Type type) {
		Collection<Property> properties;
		if (set.containsKey(type))
			properties = set.get(type).values();
		else
			properties = EMPTY;
		return properties;
	}
	public boolean contains(Property property) {
		return set.containsKey(property.type()) && set.get(property.type()).containsKey(property.getValue());
	}
	public boolean hasPersons() {
		return set.containsKey(Type.PERSON);
	}
	public boolean hasCategories() {
		return set.containsKey(Type.CATEGORY);
	}
	public boolean hasCountries() {
		return set.containsKey(Type.COUNTRY);
	}
	public boolean has(Type type) {
		return set.containsKey(type);
	}
}
