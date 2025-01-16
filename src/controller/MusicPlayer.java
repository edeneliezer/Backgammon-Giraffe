package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import view.SettingsScreen;

import java.net.URL;

import Model.Settings;

/**
 * Singleton class to control the music functionalities of the game.
 */
public class MusicPlayer {
    private static MusicPlayer instance;
    private MediaPlayer mediaPlayer;
    private String currentTrack = "jazz.aiff"; // Default track
    private static final double DEFAULT_VOLUME = 0.1;

    // Private constructor to enforce Singleton pattern
    private MusicPlayer() {
        initializePlayer();
    }

    /**
     * Retrieves the single instance of MusicPlayer.
     *
     * @return the MusicPlayer instance.
     */
    public static synchronized MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    /**
     * Initializes the MediaPlayer with the current track.
     */
    private void initializePlayer() {
        try {
            String resourcePath = "/musicplayer/songs/" + currentTrack;
            URL resource = getClass().getResource(resourcePath);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setVolume(DEFAULT_VOLUME);
                mediaPlayer.setOnReady(() -> {
                    if (SettingsScreen.isMusicEnabled()) {
                        mediaPlayer.play();
                    }
                });            } else {
                throw new RuntimeException("Music resource not found: " + resourcePath);
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize music player: " + e.getMessage());
        }
    }

    /**
     * Plays the music.
     */
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    /**
     * Pauses the music.
     */
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * Stops the music.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Toggles between playing and pausing the music.
     */
    public void togglePlayPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                pause();
            } else {
                play();
            }
        }
    }

    /**
     * Checks if the music is currently playing.
     *
     * @return true if playing, false otherwise.
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Adjusts the music volume.
     *
     * @param volume the new volume level (0.0 to 1.0).
     */
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    /**
     * Mutes or unmutes the music.
     *
     * @param mute true to mute, false to unmute.
     */
    public void setMute(boolean mute) {
        if (mediaPlayer != null) {
            mediaPlayer.setMute(mute);
        }
    }
}
