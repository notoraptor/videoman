package videoman.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

abstract public class BasicLabel<E> {
	static private final String[] colors = new String[] {
			"white", "rgb(245,245,248)"
	};
	private E element;
	private Label label;
	private Button button;
	private BorderPane node;
	public BasicLabel(EditableList<E> parent, E object, String actionName) {
		element = object;
		label = new Label(element.toString());
		button = new Button(actionName);
		node = new BorderPane();
		String color = colors[parent.getSize() % colors.length];
		node.setPadding(new Insets(2, 5, 2, 5));
		node.setStyle("-fx-background-color:" + color + ";");
		node.setOnMouseEntered(event -> node.setStyle("-fx-background-color:rgb(220,220,235);"));
		node.setOnMouseExited (event -> node.setStyle("-fx-background-color:" + color + ";"));
		node.setCenter(buildContent(label));
		node.setRight(button);
	}
	public void setAction(EventHandler<ActionEvent> eventHandler) {
		button.setOnAction(eventHandler);
	}
	public E getElement() {
		return element;
	}
	public Node getNode() {
		return node;
	}
	public Node buildContent(Label label) {
		return label;
	};
}
