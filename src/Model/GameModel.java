package Model;


import org.json.JSONObject;

import java.io.*;
import java.util.Random;

public class GameModel {
    private static final String FILE_NAME = "gameInfo.json";

    public static void saveGameInfo(String player1, String player2, String difficulty) {
        JSONObject gameInfo = new JSONObject();
        gameInfo.put("gameNumber", new Random().nextInt(100000)); // Generate a random game number
        gameInfo.put("player1", player1);
        gameInfo.put("player2", player2);
        gameInfo.put("difficulty", difficulty);

        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile(); // Create the file if it doesn't exist
            }
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(gameInfo.toString() + System.lineSeparator());
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
