package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HistoryScreen {

    private static final String FILE_NAME = "gameInfo.json";


    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #fefaf4;");
        root.setAlignment(Pos.TOP_CENTER);
      
     // Home Button (Top-Left Corner)
        Button homeButton = new Button("Back");
        homeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        homeButton.setStyle(
            "-fx-background-color: #d11e1e; " +  // רקע אדום
            "-fx-text-fill: white; " +          // טקסט בצבע לבן
            "-fx-border-radius: 5; " +          // גבול מעוגל
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"                // שינוי הסמן לכף יד
        );

        // אפקט שינוי צבע בעת ריחוף
        homeButton.setOnMouseEntered(e -> homeButton.setStyle(
            "-fx-background-color: #b30000; " +  // רקע כהה יותר בעת ריחוף
            "-fx-text-fill: white; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"
        ));

        homeButton.setOnMouseExited(e -> homeButton.setStyle(
            "-fx-background-color: #d11e1e; " +  // חזרה לצבע המקורי
            "-fx-text-fill: white; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"
        ));

        // פעולה בעת לחיצה על הכפתור
        homeButton.setOnAction(e -> {
            // חזרה למסך הראשי של המשחק
            backgammonUI backgammonScreen = new backgammonUI();
            backgammonScreen.start(primaryStage);
        });


        // Add Home Button to a HBox for alignment
        HBox homeButtonContainer = new HBox(homeButton);
        homeButtonContainer.setAlignment(Pos.TOP_LEFT);
        homeButtonContainer.setPadding(new Insets(10));

        // Title
        Label title = new Label("HISTORY");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 36));
        title.setTextFill(Color.BROWN);

        VBox titleContainer = new VBox(title);
        titleContainer.setAlignment(Pos.TOP_CENTER);
        titleContainer.setPadding(new Insets(10));
        
        // Add elements to BorderPane
        root.getChildren().add(homeButtonContainer);
        root.getChildren().add(titleContainer);


        // Read games from JSON file and add rows
        try {
            readGamesFromJson(root);
        } catch (IOException e) {
            e.printStackTrace();
            Label error = new Label("Error loading history.");
            error.setTextFill(Color.RED);
            root.getChildren().add(error);
        }

        // Scene and Stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game History");
        primaryStage.show();
    }

    private void readGamesFromJson(VBox root) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject gameInfo = new JSONObject(line);
                String player1 = gameInfo.getString("player1");
                String player2 = gameInfo.getString("player2");
                String difficulty = gameInfo.getString("difficulty");
                String winner = gameInfo.getString("winner");
                String gameTime = gameInfo.optString("gameTime", "N/A"); // Get gameTime or default to "N/A"

                root.getChildren().add(createGameRow(player1, player2, difficulty, winner, gameTime));
            }
        } catch (IOException e) {
            throw new IOException("Error reading game info from file.", e);
        }
    }

    private HBox createGameRow(String player1, String player2, String difficulty, String winner, String gameTime) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER);
        row.setSpacing(10);
        row.setStyle("-fx-background-color: #b30000; -fx-padding: 10; -fx-border-color: black; -fx-border-width: 2;");
        row.setPrefHeight(50);

        // Player 1 or Winner Label
        Label player1Label = new Label(player1.equals(winner) ? "Winner: " + player1 : player1);
        player1Label.setFont(Font.font("Verdana", 18));
        player1Label.setTextFill(player1.equals(winner) ? Color.LIGHTGREEN : Color.WHITE);

        // Difficulty Label
        Label difficultyLabel = new Label(difficulty);
        difficultyLabel.setFont(Font.font("Verdana", 18));
        difficultyLabel.setTextFill(Color.BEIGE);
        difficultyLabel.setStyle(
            "-fx-background-color: #8b5e3c; " +
            "-fx-padding: 5; " +
            "-fx-border-color: black; " +
            "-fx-border-width: 2;"
        );
        HBox.setHgrow(difficultyLabel, Priority.ALWAYS);

        // Player 2 or Winner Label
        Label player2Label = new Label(player2.equals(winner) ? "Winner: " + player2 : player2);
        player2Label.setFont(Font.font("Verdana", 18));
        player2Label.setTextFill(player2.equals(winner) ? Color.LIGHTGREEN : Color.WHITE);

        // Game Time Label (Updated Styling)
        Label gameTimeLabel = new Label("Time: " + gameTime);
        gameTimeLabel.setFont(Font.font("Verdana", 18));
        gameTimeLabel.setTextFill(Color.BEIGE);
        gameTimeLabel.setStyle(
            "-fx-background-color: #8b5e3c; " +
            "-fx-padding: 5; " +
            "-fx-border-color: black; " +
            "-fx-border-width: 2;"
        );

        // Create containers to force proper alignment
        HBox player1Container = new HBox(player1Label);
        player1Container.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(player1Container, Priority.ALWAYS);

        HBox player2Container = new HBox(player2Label);
        player2Container.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(player2Container, Priority.ALWAYS);

        // Add elements to the row
        row.getChildren().addAll(player1Container, difficultyLabel, gameTimeLabel, player2Container);
        return row;
    }





}
