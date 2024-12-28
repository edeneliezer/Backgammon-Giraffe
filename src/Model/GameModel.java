package Model;

import org.json.JSONObject;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameModel {
    private static final String FILE_NAME = "gameInfo.json";
    private static final String GAME_NUMBER_TRACKER = "gameNumberTracker.txt"; // File to track the last game number
    private static AtomicInteger gameNumberCounter = new AtomicInteger(0);

    static {
        // Initialize the game number counter from the tracker file
        try {
            File trackerFile = new File(GAME_NUMBER_TRACKER);
            if (trackerFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(trackerFile))) {
                    String lastGameNumber = reader.readLine();
                    if (lastGameNumber != null) {
                        gameNumberCounter.set(Integer.parseInt(lastGameNumber));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error initializing game number tracker: " + e.getMessage());
        }
    }

    public static void saveGameInfo(String player1, String player2, String difficulty, String winner) {
        int gameNumber = gameNumberCounter.incrementAndGet(); // Increment the game number

        JSONObject gameInfo = new JSONObject();
        gameInfo.put("gameNumber", gameNumber);
        gameInfo.put("player1", player1);
        gameInfo.put("player2", player2);
        gameInfo.put("difficulty", difficulty);
        gameInfo.put("winner", winner); // Save the winner's name

        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile(); // Create the file if it doesn't exist
            }
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(gameInfo.toString() + System.lineSeparator());
            }

            // Update the tracker file with the new game number
            try (FileWriter trackerWriter = new FileWriter(GAME_NUMBER_TRACKER)) {
                trackerWriter.write(String.valueOf(gameNumber));
            }

        } catch (IOException e) {
            System.out.println("Error saving game info: " + e.getMessage());
        }
    }

    public static void printGameInfo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved game data found.");
        } catch (IOException e) {
            System.out.println("Error reading game info: " + e.getMessage());
        }
    }
}
