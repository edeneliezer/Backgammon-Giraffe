package jUnit;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import Model.GameModel;

import org.json.JSONObject;

import java.io.*;

public class GameInfoTest {

    private static final String FILE_NAME = "gameInfo.json";
    private static final String GAME_NUMBER_TRACKER = "gameNumberTracker.txt";

    @Before
    public void setup() throws IOException {
        // Ensure the tracker file exists and is initialized
        File trackerFile = new File(GAME_NUMBER_TRACKER);
        if (!trackerFile.exists()) {
            try (FileWriter trackerWriter = new FileWriter(trackerFile)) {
                trackerWriter.write("0"); // Initialize game number to 0
            }
        }
    }

    @Test
    public void testSaveGameInfo() {
        // Arrange
        String player1 = "Alice";
        String player2 = "Bob";
        String difficulty = "Medium";
        String winner = "Alice";

        // Act
        GameModel.saveGameInfo(player1, player2, difficulty, winner);

        // Assert
        // Verify the game info file
        File gameInfoFile = new File(FILE_NAME);
        assertTrue("Game info file should exist", gameInfoFile.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(gameInfoFile))) {
            String lastLine = null;
            String line;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }

            assertNotNull("Game info file should not be empty", lastLine);
            JSONObject gameInfo = new JSONObject(lastLine);
            assertEquals(player1, gameInfo.getString("player1"));
            assertEquals(player2, gameInfo.getString("player2"));
            assertEquals(difficulty, gameInfo.getString("difficulty"));
            assertEquals(winner, gameInfo.getString("winner"));
        } catch (IOException e) {
            fail("Error reading game info file: " + e.getMessage());
        }

        // Verify the tracker file
        File trackerFile = new File(GAME_NUMBER_TRACKER);
        assertTrue("Game number tracker file should exist", trackerFile.exists());

        try (BufferedReader trackerReader = new BufferedReader(new FileReader(trackerFile))) {
            String gameNumber = trackerReader.readLine();
            assertNotNull("Game number tracker should not be empty", gameNumber);

            // Verify that the game number was incremented
            int gameNumberInt = Integer.parseInt(gameNumber);
            assertTrue("Game number should be greater than 0", gameNumberInt > 0);
        } catch (IOException e) {
            fail("Error reading tracker file: " + e.getMessage());
        }
    }

    @After
    public void cleanup() {
        // Cleanup is not required since we want to retain the existing files
        // However, you can uncomment the following if you want to reset the files after testing
         new File(FILE_NAME).delete();
         new File(GAME_NUMBER_TRACKER).delete();
    }
}
