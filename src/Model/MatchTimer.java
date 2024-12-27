package Model;

import java.awt.Label;

import javafx.animation.AnimationTimer;



public class MatchTimer {
    private long startTime;
    private long elapsedTime; // בשניות
    private AnimationTimer timer;
    

    public MatchTimer() {
        elapsedTime = 0;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long currentTime = (System.currentTimeMillis() - startTime) / 1000;
                elapsedTime = currentTime;
            }
        };
    }

    public void start() {
        startTime = System.currentTimeMillis();
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
    
    public String getFormattedTime() {
        long minutes = elapsedTime / 60;
        long seconds = elapsedTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
