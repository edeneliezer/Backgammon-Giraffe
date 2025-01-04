package controller;

import javafx.scene.media.AudioClip;

public class SoundEffectsPlayer {
    private double volume = 0.1;
    private AudioClip checker;
    private AudioClip dice;
    private AudioClip bearOff;
    private AudioClip bearOn;
    private AudioClip hit;
    private boolean isWorking = true;

    public SoundEffectsPlayer() {
        initCheckerSound();
        initDiceSound();
        initBearOffSound();
        initBearOnSound();
        initHitCheckerSound();
    }

    public void enableEffects() {
        isWorking = true;
    }

    public void disableEffects() {
        isWorking = false;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void playCheckerSound() {
        if (isWorking && checker != null) {
            checker.setVolume(volume);
            checker.play();
        }
    }

    public void playDiceSound() {
        if (isWorking && dice != null) {
            dice.setVolume(volume);
            dice.play();
        }
    }

    public void playBearOffSound() {
        if (isWorking && bearOff != null) {
            bearOff.setVolume(volume);
            bearOff.play();
        }
    }

    public void playBearOnSound() {
        if (isWorking && bearOn != null) {
            bearOn.setVolume(volume);
            bearOn.play();
        }
    }

    public void playCheckerHitSound() {
        if (isWorking && hit != null) {
            hit.setVolume(volume);
            hit.play();
        }
    }

    private void initCheckerSound() {
        checker = loadAudioClip("checker.aiff");
    }

    private void initDiceSound() {
        dice = loadAudioClip("dice.aiff");
    }

    private void initBearOffSound() {
        bearOff = loadAudioClip("bearoff.aiff");
    }

    private void initBearOnSound() {
        bearOn = loadAudioClip("bearon.aiff");
    }

    private void initHitCheckerSound() {
        hit = loadAudioClip("hit.aiff");
    }

    private AudioClip loadAudioClip(String fileName) {
        try {
            return new AudioClip(getClass().getResource("/musicplayer/songs/" + fileName).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Failed to load sound effect: " + fileName);
            return null;
        }
    }

    public void setVolume(double newVolume) {
        volume = Math.max(0, Math.min(1, newVolume)); // Clamp volume between 0 and 1
    }
}
