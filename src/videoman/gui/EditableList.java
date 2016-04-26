package videoman.gui;

import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EditableList<E> {
	private VBox container;
	private HashMap<E, BasicLabel<E>> elements;
	public EditableList(VBox vBox) {
		container = vBox;
		elements = new HashMap<>();
	}
	public void add(E element) {
		if(!elements.containsKey(element))
			add(new RemovableLabel<>(this, element));
	}
	public void add(BasicLabel<E> removableLabel) {
		if (!elements.containsKey(removableLabel.getElement())) {
			container.getChildren().add(removableLabel.getNode());
			elements.put(removableLabel.getElement(), removableLabel);
		}
	}
	public void remove(BasicLabel<E> removableLabel) {
		container.getChildren().remove(removableLabel.getNode());
		elements.remove(removableLabel.getElement());
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
}
