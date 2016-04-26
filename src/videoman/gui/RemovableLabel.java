package videoman.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class RemovableLabel<E> extends BasicLabel<E> {
	public RemovableLabel(EditableList<E> parent, E object) {
		super(parent, object, "supprimer");
		setAction(event -> parent.remove(this));
	}
}
