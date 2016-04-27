package videoman.gui;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Set;

public class EditableList<E> {
	static private final String[] colors = new String[] {
			"white", "rgb(245,245,248)"
	};
	private VBox container;
	private Instancer<E, BasicLabel<E>> instancer;
	private HashMap<E, BasicLabel<E>> elements;
	private EditableList<E> trash;
	public EditableList(VBox vBox) {
		container = vBox;
		elements = new HashMap<>();
	}
	public void setInstancer(Instancer<E, BasicLabel<E>> theInstancer) {
		instancer = theInstancer;
	}
	public void add(E element) {
		if(!elements.containsKey(element)) {
			if (instancer != null)
				add(instancer.get(element));
			else
				add(new RemovableLabel<>(this, element));
		}
	}
	public void add(BasicLabel<E> removableLabel) {
		if (!elements.containsKey(removableLabel.getElement())) {
			String color = colors[getSize() % colors.length];
			Node node = removableLabel.getNode();
			node.setStyle("-fx-background-color:" + color + ";");
			node.setOnMouseEntered(event -> node.setStyle("-fx-background-color:rgb(220,220,235);"));
			node.setOnMouseExited (event -> node.setStyle("-fx-background-color:" + color + ";"));
			container.getChildren().add(node);
			elements.put(removableLabel.getElement(), removableLabel);
		}
	}
	public void remove(BasicLabel<E> removableLabel) {
		boolean removed = container.getChildren().remove(removableLabel.getNode());
		elements.remove(removableLabel.getElement());
		if (removed && trash != null)
			trash.add(removableLabel.getElement());
	}
	public Set<E> getElements() {
		return elements.keySet();
	}
	public BasicLabel<E> getItem(E element) {
		return elements.get(element);
	}
	public int getSize() {
		return elements.size();
	}
	public void setTrash(EditableList<E> theTrash) {
		trash = theTrash;
	}
}
