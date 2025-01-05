package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class controls the music functionalities of the game.
 */
public class MusicPlayer {
    private final String defaultMusic = "jazz.aiff";
    private static MediaPlayer mediaPlayer;
    private Media media;
    private ArrayList<String> playlist;
    private String currentMusic;
    private static boolean isPlaying = false;

    public MusicPlayer() {
        initPlaylist();
        try {
            initMediaPlayer(defaultMusic);
            currentMusic = defaultMusic;
        } catch (RuntimeException e) {
            // Handle initialization error silently
        }
    }

    /**
     * Method to load the music file into the mediaPlayer object to be played.
     * 
     * @param fileName The music file to be loaded.
     */
    private void initMediaPlayer(String fileName) {
        String resourcePath = "/musicplayer/songs/" + fileName;
        URL resource = getClass().getResource(resourcePath);

        if (resource == null) {
            throw new RuntimeException("Resource not found: " + resourcePath);
        }

        media = new Media(resource.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.setOnReady(() -> mediaPlayer.setAutoPlay(true));
    }

    /**
     * Initializes the playlist with available songs.
     */
    private void initPlaylist() {
        playlist = new ArrayList<>();
        playlist.add("jazz.aiff");
        playlist.add("classical.aiff");
    }

    /**
     * Plays a random track from the playlist.
     */
    public void random() {
        stop();

        Random random = new Random();
        int indexOfSong = random.nextInt(playlist.size());
        currentMusic = playlist.get(indexOfSong);

        try {
            initMediaPlayer(currentMusic);
        } catch (RuntimeException e) {
            // Handle random song error silently
        }
    }

    /**
     * Enables the repeat functionality for the current track.
     */
    public void repeat() {
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.play());
    }

    /**
     * Plays the current track.
     */
    public static void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            isPlaying = true;
        }
    }

    /**
     * Plays the next track in the playlist.
     */
    public void next() {
        stop();

        int indexOfSong = playlist.indexOf(currentMusic);

        if (indexOfSong == playlist.size() - 1) {
            currentMusic = playlist.get(0); // Loop to the first song
        } else {
            currentMusic = playlist.get(indexOfSong + 1);
        }

        try {
            initMediaPlayer(currentMusic);
        } catch (RuntimeException e) {
            // Handle next song error silently
        }
    }

    /**
     * Plays the previous track in the playlist.
     */
    public void prev() {
        stop();

        int indexOfSong = playlist.indexOf(currentMusic);

        if (indexOfSong == 0) {
            currentMusic = playlist.get(playlist.size() - 1); // Loop to the last song
        } else {
            currentMusic = playlist.get(indexOfSong - 1);
        }

        try {
            initMediaPlayer(currentMusic);
        } catch (RuntimeException e) {
            // Handle previous song error silently
        }
    }

    /**
     * Returns the status of the media player as a string.
     * 
     * @param option The command to describe.
     * @return The status message.
     */
    public static String getStatus(String option) {
        switch (option) {
            case "play":
                return "Playing music..";
            case "next":
                return "Next track..";
            case "prev":
                return "Previous track..";
            case "pause":
                return "Pausing music..";
            case "stop":
                return "Stopping music..";
            case "random":
                return "Playing random song..";
            case "mute":
                return "Muting..";
            case "unmute":
                return "Unmuting..";
            default:
                return "Invalid command.";
        }
    }

    /**
     * Pauses the current track.
     */
    public static void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    /**
     * Stops the current track.
     */
    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Mutes or unmutes the volume.
     * 
     * @param toggle True to mute, false to unmute.
     */
    public void muteVolume(boolean toggle) {
        if (mediaPlayer != null) {
            mediaPlayer.setMute(toggle);
        }
    }

    /**
     * Resets the music player by playing a random track.
     */
    public void reset() {
        random();
    }

    /**
     * Checks if music is currently playing.
     * 
     * @return True if music is playing, false otherwise.
     */
    public static boolean isPlaying() {
        return isPlaying;
    }
}