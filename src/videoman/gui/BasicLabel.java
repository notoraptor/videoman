package videoman.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

abstract public class BasicLabel<E> {
	private EditableList<E> parent;
	private E element;
	private Label label;
	private Button button;
	private BorderPane node;
	private boolean centerSet;
	public BasicLabel(EditableList<E> parent, E object, String actionName) {
		this.parent = parent;
		element = object;
		label = new Label(element.toString());
		button = new Button(actionName);
		node = new BorderPane();
		label.setMaxWidth(Double.MAX_VALUE);
		label.setTextAlignment(TextAlignment.LEFT);
		//label.setWrapText(true);
		node.setPadding(new Insets(2, 5, 2, 5));

		node.setRight(button);
	}
	public void setAction(EventHandler<ActionEvent> eventHandler) {
		button.setOnAction(eventHandler);
	}
	public EditableList<E> getParent() {
		return parent;
	}
	public E getElement() {
		return element;
	}
	public Node getNode() {
		if (!centerSet) {
			node.setCenter(buildContent(label));
			centerSet = true;
		}
		return node;
	}
	public Node buildContent(Label label) {
		return label;
	}
}
