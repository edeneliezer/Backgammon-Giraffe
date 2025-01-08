package Model;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class DoublingCube extends StackPane implements Touchable {
    private final Label timerLabel;
    private final MatchTimer matchTimer;

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
    }

	public void setNormalImage() {
		// TODO Auto-generated method stub
		
	}

	public void setUsed(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void doubleDoublingCube() {
		// TODO Auto-generated method stub
		
	}

	public boolean isMaxDoubling() {
		// TODO Auto-generated method stub
		return false;
	}

	public void resetRotation() {
		// TODO Auto-generated method stub
		
	}

	public void rotateOnBoard() {
		// TODO Auto-generated method stub
		
	}

	public void setHighlightImage() {
		// TODO Auto-generated method stub
		
	}

	public int getEndGameMultiplier() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getIntermediateGameMultiplier() {
		// TODO Auto-generated method stub
		return 0;
	}
}
