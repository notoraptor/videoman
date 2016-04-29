package videoman.core.database;

import videoman.core.Video;

import java.util.Collection;
import java.util.HashSet;

abstract public class Property implements Comparable<Property> {
	private Type type;
	private String name;
	private HashSet<Video> content;
	private int hashCode;
	Property(Type theType, String theName) {
		assert theType != null && theName != null;
		type = theType;
		name = theName;
		content = new HashSet<>();
		hashCode = (type.name() + '/' + name).hashCode();
	}
	Property(Property old, String newName) {
		type = old.type();
		name = newName;
		content = new HashSet<>();
		hashCode = (type.name() + '/' + name).hashCode();
		old.transferTo(this);
	}
	public boolean add(Video video) {
		if(!content.contains(video)) {
			content.add(video);
			video.add(this);
			return true;
		}
		return false;
	}
	public boolean remove(Video video) {
		if(content.contains(video)) {
			content.remove(video);
			video.remove(this);
			return true;
		}
		return false;
	}
	public Type type() {
		return type;
	}
	public String getValue() {
		return name;
	}
	public int getSize() {
		return content.size();
	}
	@Override
	public int compareTo(Property other) {
		int t = type.compareTo(other.type);
		if (t == 0) t = name.compareTo(other.name);
		return t;
	}
	@Override
	public int hashCode() {
		return hashCode;
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof Property) {
			Property other = (Property) o;
			return type.equals(other.type) && name.equals(other.name);
		}
		return false;
	}
	@Override
	public String toString() {
		return name;
	}
	public void transferTo(Property newProperty) {
		HashSet<Video> copy = new HashSet<>(content);
		for (Video video: copy) {
			video.remove(this);
			video.add(newProperty);
		}
	}
	void clear() {
		HashSet<Video> copy = new HashSet<>(content);
		for (Video video: copy)
			video.remove(this);
	}
	public void copyVideosTo(Collection<Video> copy) {
		copy.addAll(content);
	}
	public boolean isEmpty() {
		return content.isEmpty();
	}
	public boolean isQuery() {
		return type == Type.QUERY;
	}
}
