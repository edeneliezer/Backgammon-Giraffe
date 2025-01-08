package view;

import Model.MatchTimer;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TimerDisplay extends StackPane {
    private MatchTimer matchTimer;
    private Label timerLabel;

    public TimerDisplay() {
        initTimer();
    }

    /**
     * Initializes the match timer and label.
     */
    private void initTimer() {
        matchTimer = new MatchTimer();
        timerLabel = new Label();
        timerLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-background-color: black; -fx-padding: 5px;");
        getChildren().add(timerLabel);

        // Update the label periodically with the timer value.
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                timerLabel.setText(matchTimer.getFormattedTime());
            }
        };
        animationTimer.start();
    }

    /**
     * Starts the timer.
     */
    public void startTimer() {
        matchTimer.start();
    }

    /**
     * Stops the timer.
     */
    public void stopTimer() {
        matchTimer.stop();
    }

    /**
     * Resets the timer.
     */
    public void resetTimer() {
        matchTimer.stop();
        matchTimer.start();
    }
}
