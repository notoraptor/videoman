package videoman.gui;

public class AddableLabel<E> extends BasicLabel<E> {
	public AddableLabel(EditableList<E> parent, EditableList<E> recipient, E element) {
		super(parent, element, "ajouter");
		setAction(event -> parent.remove(this));
	}
}
