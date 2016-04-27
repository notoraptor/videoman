package videoman.gui;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import videoman.core.Notation;

public class NotationChooser {
	static private final int width = 40;
	static private final int height = 30;
	static private final String mouseEnteredStyle = "-fx-base: salmon;";
	static private final String selectedStyle = "-fx-base: lightgreen;";
	static private final String unselectedStyle = "-fx-base: gray;";
	private ToggleGroup group;
	private ToggleButton[] toggleButtons;
	private HBox container;
	private Notation currentNotation;
	public NotationChooser(Notation base) {
		group = new ToggleGroup();
		toggleButtons = new ToggleButton[Notation.values().length];
		currentNotation = base;
		for (int i = 0; i < toggleButtons.length; ++i) {
			ToggleButton toggleButton = new ToggleButton(String.valueOf(i));
			toggleButton.setToggleGroup(group);
			toggleButton.setStyle(unselectedStyle);
			toggleButton.setUserData(i);
			toggleButton.setOnMouseEntered(event -> {
				int position = (int) toggleButton.getUserData();
				for (int p = 0; p <= position; ++p)
					toggleButtons[p].setStyle(mouseEnteredStyle);
				for (int p = position + 1; p < toggleButtons.length; ++p)
					toggleButtons[p].setStyle(p <= currentNotation.ordinal() ? selectedStyle : unselectedStyle);
			});
			toggleButton.setOnMouseExited(event -> {
				for (int p = 0; p < toggleButtons.length; ++p)
					toggleButtons[p].setStyle(p <= currentNotation.ordinal() ? selectedStyle : unselectedStyle);
			});
			toggleButton.setMinWidth(width);
			toggleButton.setMaxWidth(width);
			toggleButton.setMinHeight(height);
			toggleButton.setMaxHeight(height);
			toggleButton.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize()));
			toggleButtons[i] = toggleButton;
		}
		group.selectedToggleProperty().addListener((ov, old, new_toggle) -> {
			if (new_toggle == null) {
				for (ToggleButton toggleButton : toggleButtons)
					toggleButton.setStyle(unselectedStyle);
			} else {
				ToggleButton toggleButton = (ToggleButton) new_toggle;
				int position = (int) toggleButton.getUserData();
				currentNotation = Notation.values()[position];
				for (int p = 0; p <= position; ++p)
					toggleButtons[p].setStyle(selectedStyle);
				for (int p = position + 1; p < toggleButtons.length; ++p)
					toggleButtons[p].setStyle(unselectedStyle);
			}
		});
		container = new HBox();
		container.getChildren().addAll(toggleButtons);
		toggleButtons[currentNotation.ordinal()].setSelected(true);
		//container.setSpacing(5);
	}
	public Notation getCurrentNotation() {
		return currentNotation;
	}
	public HBox getNode() {
		return container;
	}
}
