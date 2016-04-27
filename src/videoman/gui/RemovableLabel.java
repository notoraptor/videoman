package videoman.gui;

public class RemovableLabel<E> extends BasicLabel<E> {
	public RemovableLabel(EditableList<E> parent, E object) {
		super(parent, object, "supprimer");
		setAction(event -> parent.remove(this));
	}
}
