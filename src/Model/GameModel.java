package Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameModel {
    private static final String FILE_NAME = "gameInfo.json";
    private static final String GAME_NUMBER_TRACKER = "gameNumberTracker.txt"; // File to track the last game number
    private static AtomicInteger gameNumberCounter = new AtomicInteger(0);
    private static final String QUESTIONS_FILE_NAME = "questions.json";

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

    public static void saveGameInfo(String player1, String player2, String difficulty, String winner, String gameTime) {
        int gameNumber = gameNumberCounter.incrementAndGet(); // Increment the game number

        JSONObject gameInfo = new JSONObject();
        gameInfo.put("gameNumber", gameNumber);
        gameInfo.put("player1", player1);
        gameInfo.put("player2", player2);
        gameInfo.put("difficulty", difficulty);
        gameInfo.put("winner", winner); // Save the winner's name
        gameInfo.put("gameTime", gameTime); // Save the game time

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
    
    public static void saveQuestion(Question question) {
        try {
            // Load existing data or create a new structure
            JSONObject questionsData;
            File file = new File(QUESTIONS_FILE_NAME);
            if (file.exists()) {
                // Load existing questions
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    questionsData = new JSONObject(content.toString());
                }
            } else {
                // Create a new structure if the file doesn't exist
                questionsData = new JSONObject();
                questionsData.put("questions", new JSONArray());
            }

            // Get the "questions" array
            JSONArray questionsArray = questionsData.getJSONArray("questions");

            // Create a JSONObject for the new question
            JSONObject questionObject = new JSONObject();
            questionObject.put("question", question.getQuestion());
            questionObject.put("answers", question.getAnswers());
            questionObject.put("correct_ans", question.getCorrectAnswer());
            questionObject.put("difficulty", question.getDifficulty());

            // Add the new question to the array
            questionsArray.put(questionObject);

            // Save the updated JSON to the file
            try (FileWriter writer = new FileWriter(QUESTIONS_FILE_NAME)) {
                writer.write(questionsData.toString(4)); // Pretty print
            }

        } catch (IOException e) {
            System.out.println("Error saving question: " + e.getMessage());
        }
    }
    
    // Load questions from a JSON file
    public static List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(QUESTIONS_FILE_NAME))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            JSONObject questionsData = new JSONObject(content.toString());
            JSONArray questionsArray = questionsData.getJSONArray("questions");

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObject = questionsArray.getJSONObject(i);
                String questionText = questionObject.getString("question");
                JSONArray answersArray = questionObject.getJSONArray("answers");
                List<String> answers = new ArrayList<>();
                for (int j = 0; j < answersArray.length(); j++) {
                    answers.add(answersArray.getString(j));
                }
                String correctAnswer = questionObject.getString("correct_ans");
                String difficulty = questionObject.getString("difficulty");

                questions.add(new Question(questionText, answers, correctAnswer, difficulty));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Questions file not found.");
        } catch (IOException e) {
            System.out.println("Error reading questions: " + e.getMessage());
        }

        return questions;
    }

    
    public static void printGameInfo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(QUESTIONS_FILE_NAME))) {
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
