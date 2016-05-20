package videoman.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.*;

public class EditableList<E> {
	static private final String[] colors = new String[] {
			"white", "rgb(245,245,248)"
	};
	private SimpleIntegerProperty simpleIntegerProperty;
	private VBox container;
	private Instancer<E, BasicLabel<E>> instancer;
	private TreeMap<E, BasicLabel<E>> elements;
	private EditableList<E> trash;
	public EditableList(VBox vBox) {
		simpleIntegerProperty = new SimpleIntegerProperty(0);
		container = vBox;
		elements = new TreeMap<>();
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
			simpleIntegerProperty.set(simpleIntegerProperty.get() + 1);
		} else {
			BasicLabel<E> basicLabel = elements.get(removableLabel.getElement());
			if (basicLabel instanceof SharedLabel) {
				((SharedLabel) basicLabel).setForAll();
			}
		}
	}
	public void remove(BasicLabel<E> removableLabel) {
		boolean removed = container.getChildren().remove(removableLabel.getNode());
		elements.remove(removableLabel.getElement());
		if (removed && trash != null)
			trash.add(removableLabel.getElement());
		if (removed)
			simpleIntegerProperty.set(simpleIntegerProperty.get() - 1);
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
	public void filter(String filter) {
		if (filter == null || (filter = filter.trim()).isEmpty()) {
			showAllElements();
		} else {
			filter = filter.toLowerCase();
			LinkedList<Node> nodes = new LinkedList<>();
			for (Map.Entry<E, BasicLabel<E>> entry: elements.entrySet()) {
				if (entry.getKey().toString().contains(filter))
					nodes.add(entry.getValue().getNode());
			}
			showNodes(nodes);
		}
	}
	public SimpleIntegerProperty simpleIntegerProperty() {
		return simpleIntegerProperty;
	}
	private void showAllElements() {
		LinkedList<Node> nodes = new LinkedList<>();
		for(BasicLabel<E> label: elements.values())
			nodes.add(label.getNode());
		showNodes(nodes);
	}
	private void showNodes(Collection<Node> nodes) {
		int position = 0;
		for (Node node: nodes) {
			String color = colors[position % colors.length];
			node.setStyle("-fx-background-color:" + color + ";");
			node.setOnMouseEntered(event -> node.setStyle("-fx-background-color:rgb(220,220,235);"));
			node.setOnMouseExited (event -> node.setStyle("-fx-background-color:" + color + ";"));
			++position;
		}
		container.getChildren().clear();
		container.getChildren().addAll(nodes);
		simpleIntegerProperty.set(nodes.size());
	}
}
