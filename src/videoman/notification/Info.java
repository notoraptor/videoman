package videoman.notification;

public class Info<T> {
	private T element;
	public Info(T theElement) {
		assert theElement != null;
		element = theElement;
	}
	public T get() {
		return element;
	}
}
