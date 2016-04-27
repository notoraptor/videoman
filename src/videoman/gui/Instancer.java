package videoman.gui;

public interface Instancer<FROM, TO> {
	TO get(FROM from);
}
