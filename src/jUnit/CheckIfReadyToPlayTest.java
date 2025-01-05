package jUnit;

import static org.junit.Assert.*;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import org.junit.Test;
import view.backgammonUI;

public class CheckIfReadyToPlayTest {

    @Test
    public void testCheckIfReadyToPlay() {
        Platform.startup(() -> {
            backgammonUI ui = new backgammonUI();

            // Set up TextFields and PlayButton
            TextField player1Field = new TextField();
            TextField player2Field = new TextField();
            Button playButton = new Button();
            playButton.setDisable(true);

            // Set up the UI with mock fields and button
            ui.setPlayer1Field(player1Field);
            ui.setPlayer2Field(player2Field);
            ui.setChosenDiffficulty("Easy");
            ui.getPlayButton().setDisable(true);

            // Simulate entering player names
            player1Field.setText("Player 1");
            player2Field.setText("Player 2");

            // Call the method
            ui.checkIfReadyToPlay();

            // Assert: Play button should be enabled
            assertFalse("Play button should be enabled", ui.getPlayButton().isDisable());
        });
    }
}
