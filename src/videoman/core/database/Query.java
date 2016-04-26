package videoman.core.database;

import videoman.core.Video;

public class Query extends Property {
	public Query(String query) {
		super(Type.QUERY, query);
	}
}
