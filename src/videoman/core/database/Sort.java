package videoman.core.database;

import videoman.core.Video;

import java.util.Comparator;

public class Sort {
	final public SortColumn column;
	final public SortType type;
	public Sort(SortColumn sortColumn, SortType sortType) {
		column = sortColumn;
		type = sortType;
	}
	@Override
	public String toString() {
		return column + " (" + type + ")";
	}
	static private int compare(Video v1, Video v2, Sort sort) {
		int t = 0;
		switch (sort.column) {
			case name:
				t = v1.getName().toLowerCase().compareTo(v2.getName().toLowerCase());
				break;
			case dateModified:
				t = v1.getVideoDateModified().compareTo(v2.getVideoDateModified());
				break;
			case extension:
				t = v1.getExtension().compareTo(v2.getExtension());
				break;
			case format:
				t = v1.getFormat().compareTo(v2.getFormat());
				break;
			case size:
				long c = v1.getSize() - v2.getSize();
				t = c == 0 ? 0 : (c < 0 ? -1 : +1);
				break;
			case duration:
				t = v1.getDuration().compareTo(v2.getDuration());
				break;
			case audioCodec:
				t = v1.getAudioCodec().compareTo(v2.getAudioCodec());
				break;
			case videoCodec:
				t = v1.getVideoCodec().compareTo(v2.getVideoCodec());
				break;
			case width:
				t = v1.getWidth() - v2.getWidth();
				break;
			case height:
				t = v1.getHeight() - v2.getHeight();
				break;
			case quality:
				t = v1.getVideoQuality().compareTo(v2.getVideoQuality());
				break;
			case notation:
				t = v1.getNotation().compareTo(v2.getNotation());
				break;
			case persons:
				t = v1.getPersons().toString().compareTo(v2.getPersons().toString());
				break;
			case categories:
				t = v1.getCategories().toString().compareTo(v2.getCategories().toString());
				break;
			case countries:
				t = v1.getCountries().toString().compareTo(v2.getCountries().toString());
				break;
			case path:
				t = v1.getVideoPath().compareTo(v2.getVideoPath());
				break;
		}
		if (sort.type == SortType.DESCENDING)
			t = -t;
		return t;
	}
	static class VideoComparator implements Comparator<Video> {
		private Iterable<Sort> order;
		public VideoComparator(Iterable<Sort> sortOrder) {
			order = sortOrder;
		}
		@Override
		public int compare(Video v1, Video v2) {
			int t = 0;
			for (Sort sort: order) {
				if (t != 0)
					break;
				t = Sort.compare(v1, v2, sort);
			}
			return t;
		}
	}
}
