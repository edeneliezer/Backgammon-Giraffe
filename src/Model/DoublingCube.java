package Model;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class DoublingCube extends StackPane implements Touchable {
    private final Label timerLabel;
    private final MatchTimer matchTimer;

    private final int MAX_DICE_SIZE = 6;
    private int currentSide;
    private boolean isMaxDoubling, isUsed;
    
    public DoublingCube() {
        super();
        timerLabel = new Label();
        matchTimer = new MatchTimer();
        initTimerLabel();
        initLayout();
        reset();
    }

    private void initTimerLabel() {
        timerLabel.setFont(Font.font("Arial", 18)); // גודל גופן קטן יותר
        timerLabel.setTextAlignment(TextAlignment.CENTER);
        timerLabel.setStyle("-fx-text-fill: white; " + // צבע טקסט צהוב
                            "-fx-padding: 5px; "); // ללא רקע
    }



    private void initLayout() {
        getChildren().add(timerLabel);
        setStyle("-fx-border-radius: 10px; -fx-padding: 10px;");
        setMinSize(100, 100); 
    }

    public void startTimer() {
        matchTimer.start();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                timerLabel.setText(matchTimer.getFormattedTime());
            }
        }.start();
    }

    public void stopTimer() {
        matchTimer.stop();
    }

    public void reset() {
        matchTimer.stop();
        matchTimer.start();
        timerLabel.setText("00:00");
        currentSide = MAX_DICE_SIZE - 1;
        isMaxDoubling = false;
        isUsed = false;
        setNormalImage();
        resetRotation();
    }

	public void setNormalImage() {
		// TODO Auto-generated method stub
		
	}

	public void setUsed(boolean b) {
		  this.isUsed = isUsed;
	        currentSide = 0;
	        setNormalImage();
	}

	public void doubleDoublingCube() {
		// Allow double if less than max_dice_size-1.
        if (currentSide < MAX_DICE_SIZE - 1) currentSide += 1;
        setNormalImage();
        if (currentSide == MAX_DICE_SIZE - 1) isMaxDoubling = true;
		
	}

	public boolean isMaxDoubling() {
		return isMaxDoubling;
	}

	public void resetRotation() {
		setRotate(0.0);
		
	}

	/**
     * Rotate the doubling cube representation only when it is on the board.
     */
	public void rotateOnBoard() {
		// Simulate a rotation range of 15 to -15.
        Random rand = new Random();
        int rotation = rand.nextInt(30) - 15 + 1;
        setRotate(rotation);
		
	}

	public void setHighlightImage() {
		// TODO Auto-generated method stub
		
	}

	 // For game end multiplier
	public int getEndGameMultiplier() {
		if (isUsed) return (int) Math.pow(2.0, currentSide + 1);
        return 1;
	}

	// For declined doubling cube multiplier.
	public int getIntermediateGameMultiplier() {
		if (isUsed) return (int) Math.pow(2.0, currentSide);
        return 1;
	}
}
