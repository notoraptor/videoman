package videoman.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class FreeLabel<E> extends BasicLabel<E> {
	public FreeLabel(EditableList<E> parent, EditableList<E> recipient, E element) {
		super(parent, element, "ajouter");
		setAction(event -> recipient.add(this));
	}
	@Override
	public Node buildContent(Label label) {
		BorderPane content = new BorderPane();
		Button removeButton = new Button("supprimer");
		removeButton.setOnAction(event -> getParent().remove(this));
		removeButton.setPadding(new Insets(0, 5, 0, 5));
		content.setCenter(label);
		content.setRight(removeButton);
		return content;
	}
}
