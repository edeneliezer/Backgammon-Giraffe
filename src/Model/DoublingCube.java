package Model;

import controller.ColorPerspectiveParser;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * This class represents the doubling cube object in Backgammon game.
 */
public class DoublingCube extends StackPane implements ColorPerspectiveParser, Touchable {
    private final int MAX_DICE_SIZE = 6;
    private Label timerLabel;
    private MatchTimer matchTimer;
    private int currentSide;
    private boolean isMaxDoubling, isUsed;

    /**
     * Constructor
     * - Initialize the cube with the timer and setup its state.
     */
    public DoublingCube() {
        super();
        initTimer();
        reset();
    }

    /**
     * Initializes the timer label and the MatchTimer.
     */
    private void initTimer() {
        timerLabel = new Label();
        timerLabel.setFont(Font.font("Arial", 16));
        timerLabel.setTextAlignment(TextAlignment.CENTER);
        timerLabel.setStyle("-fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 5px;");
        matchTimer = new MatchTimer();
        getChildren().add(timerLabel);
        setStyle("-fx-border-radius: 10px; -fx-padding: 10px;");
        setMinSize(100, 100);
    }

    /**
     * Starts the timer and updates the timer label.
     */
    public void startTimer() {
        matchTimer.start();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                timerLabel.setText(matchTimer.getFormattedTime());
            }
        }.start();
    }

    /**
     * Stops the timer.
     */
    public void stopTimer() {
        matchTimer.stop();
    }

    /**
     * Use the highlighted state (visual change).
     */
    public void setHighlightImage() {
        setStyle("-fx-background-color: yellow; -fx-border-radius: 10px; -fx-padding: 10px;");
    }

    /**
     * Use the normal state (visual reset).
     */
    public void setNormalImage() {
        setStyle("-fx-border-radius: 10px; -fx-padding: 10px;");
    }

    /**
     * Rotate the doubling cube visually (if needed).
     */
    public void rotateOnBoard() {
        // Add visual rotation if desired
    }

    /**
     * Doubling the current cube value.
     */
    public void doubleDoublingCube() {
        if (currentSide < MAX_DICE_SIZE - 1) currentSide += 1;

        if (currentSide == MAX_DICE_SIZE - 1) isMaxDoubling = true;
    }

    /**
     * For declined doubling cube multiplier.
     * @return the intermediate game multiplier.
     */
    public int getIntermediateGameMultiplier() {
        if (isUsed)
            return (int) Math.pow(2.0, currentSide);
        return 1;
    }

    /**
     * For game end multiplier.
     * @return the final multiplier value.
     */
    public int getEndGameMultiplier() {
        if (isUsed)
            return (int) Math.pow(2.0, currentSide + 1);
        return 1;
    }

    public boolean isMaxDoubling() {
        return isMaxDoubling;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
        currentSide = 0;
        setNormalImage();
    }

    public void resetRotation() {
        setRotate(0.0);
    }

    /**
     * Resets the cube to its initial state.
     */
    public void reset() {
        currentSide = MAX_DICE_SIZE - 1;
        isMaxDoubling = false;
        isUsed = false;
        setNormalImage();
        resetRotation();
        timerLabel.setText("00:00");
        matchTimer.stop();
    }
}
