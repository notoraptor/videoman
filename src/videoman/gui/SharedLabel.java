package videoman.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SharedLabel<E> extends BasicLabel<E> {
	private int sharedSize;
	private int totalSize;
	private boolean forAll;
	public SharedLabel(EditableList<E> parent, E element, int sharedCount, int totalCount) {
		super(parent, element, "supprimer");
		sharedSize = sharedCount;
		totalSize = totalCount;
		setAction(event -> parent.remove(this));
	}
	@Override
	public Node buildContent(Label label) {
		BorderPane content = new BorderPane();
		content.setCenter(label);
		ToggleButton toggleButtonSel = new ToggleButton("Pour " + sharedSize + "/" + totalSize);
		ToggleButton toggleButtonAll = new ToggleButton("Pour tout (" + totalSize + ")");
		ToggleGroup toggleGroup = new ToggleGroup();
		toggleButtonSel.setToggleGroup(toggleGroup);
		toggleButtonAll.setToggleGroup(toggleGroup);
		toggleButtonSel.setUserData(false);
		toggleButtonAll.setUserData(true);
		toggleButtonSel.setSelected(true);
		Font fontSel = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize());
		Font fontNot = Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, Font.getDefault().getSize());
		Color colSel = Color.BLACK;
		Color colNot = Color.rgb(150, 150, 150);
		toggleButtonSel.setFont(fontSel);
		toggleButtonSel.setTextFill(colSel);
		toggleButtonAll.setFont(fontNot);
		toggleButtonAll.setTextFill(colNot);
		toggleGroup.selectedToggleProperty().addListener((ov, toggle, new_toggle) -> {
			if (new_toggle == null)
				forAll = false;
			else {
				forAll = (boolean) toggleGroup.getSelectedToggle().getUserData();
				if (new_toggle == toggleButtonSel) {
					toggleButtonSel.setFont(fontSel);
					toggleButtonSel.setTextFill(colSel);
					toggleButtonAll.setFont(fontNot);
					toggleButtonAll.setTextFill(colNot);
				} else {
					toggleButtonAll.setFont(fontSel);
					toggleButtonAll.setTextFill(colSel);
					toggleButtonSel.setFont(fontNot);
					toggleButtonSel.setTextFill(colNot);
				}
			}
		});
		HBox container = new HBox(5, toggleButtonSel, toggleButtonAll);
		container.setPadding(new Insets(0, 5, 0, 5));
		content.setRight(container);
		return content;
	}
	public boolean isForAll() {
		return forAll;
	}
}
