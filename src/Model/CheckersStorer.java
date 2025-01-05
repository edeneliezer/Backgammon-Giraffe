package Model;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import view.Checker;

public class CheckersStorer extends TouchablesStorer {
	public CheckersStorer() {
		super();
	}
	
	/**
	 * Initialize num number of checkers with the checkerColor and pushes them to the stack.
	 * Then draw the checkers (i.e. add them to the point object that will be drawn on the stage).
	 * @param num number of checkers.
	 * @param checkerColor color of the checkers.
	 */
	public void initCheckers(int num, Color checkerColor) {
		removeCheckers();
		
		for (int i = 0; i < num; i++) {
			push(new Checker(checkerColor));
		}
		drawCheckers();
	}
	
	/**
	 * Handles how the checkers are positioned in the point object.
	 * (i.e. how it will be drawn eventually on the stage).
	 */
	public void drawCheckers() {
	    // Clear the point object of any children.
	    getChildren().clear();

	    int numCheckers = size();
	    double checkerHeight = GameConstants.getCheckerSize().getHeight();
	    double pipHeight = GameConstants.getPipSize().getHeight();
	    double overlapOffset = checkerHeight * 0.5; // Overlap by half the height
	    double startOffset = 0;

	    if ((numCheckers - 1) * overlapOffset + checkerHeight > pipHeight) {
	        // Adjust overlap dynamically to fit within the pip
	        overlapOffset = (pipHeight - checkerHeight) / (numCheckers - 1);
	    }

	    int index = numCheckers - 1; // Start from the last checker
	    for (Touchable touchable : this) {
	        if (touchable instanceof Checker) {
	            Checker checker = (Checker) touchable;
	            checker.setTranslateY(startOffset + index * overlapOffset); // Adjust position for reverse order
	            getChildren().add(checker); // Add each checker in normal order for reversed z-order
	            index--;
	        }
	    }
	}
	
	/**
	 * Returns a boolean value indicating if the two checkers storer's top checkers are of the same color.
	 * If the other object is empty, return true as well.
	 * @param object, the other checker storer to be compared with.
	 * @return the boolean value.
	 */
	public boolean topCheckerColorEquals(CheckersStorer otherObject) {
		if (otherObject.isEmpty()) {
			return true;
		}
		return getTopChecker().getColor().equals(otherObject.getTopChecker().getColor());
	}
	
	/**
	 * Returns a boolean value indicating if the checkers storer's top checkers has the same color as given color.
	 * @param color given color to check.
	 * @return the boolean value.
	 */
	public boolean topCheckerColorEquals(Color color) {
		return getTopChecker().getColor().equals(color);
	}
	
	/**
	 * Removes all checkers in the storer (pop off stack).
	 */
	public void removeCheckers() {
		getChildren().clear();
		clear();
		drawCheckers();
	}
}
