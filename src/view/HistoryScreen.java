package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

     // Title
        Label title = new Label("HISTORY");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 36)); // Added FontWeight.BOLD
        title.setTextFill(Color.BROWN);
        root.getChildren().add(title);


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

                root.getChildren().add(createGameRow(player1, player2, difficulty, winner));
            }
        } catch (IOException e) {
            throw new IOException("Error reading game info from file.", e);
        }
    }

    private HBox createGameRow(String player1, String player2, String difficulty, String winner) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER);
        row.setSpacing(20);
        row.setStyle("-fx-background-color: #b30000; -fx-padding: 10; -fx-border-color: black; -fx-border-width: 2;");
        row.setPrefHeight(50);

        // Player 1 on the far left
        Label player1Label = new Label(player1);
        player1Label.setFont(Font.font("Verdana", 18));
        player1Label.setTextFill(player1.equals(winner) ? Color.LIGHTGREEN : Color.WHITE);
        HBox.setHgrow(player1Label, Priority.ALWAYS);
        player1Label.setAlignment(Pos.CENTER_LEFT);

        // Difficulty in the center
        Label difficultyLabel = new Label(difficulty);
        difficultyLabel.setFont(Font.font("Verdana", 18));
        difficultyLabel.setTextFill(Color.BEIGE);
        difficultyLabel.setStyle("-fx-background-color: #8b5e3c; -fx-padding: 5; -fx-border-color: black;");

        // Player 2 on the far right
        Label player2Label = new Label(player2);
        player2Label.setFont(Font.font("Verdana", 18));
        player2Label.setTextFill(player2.equals(winner) ? Color.LIGHTGREEN : Color.WHITE);
        HBox.setHgrow(player2Label, Priority.ALWAYS);
        player2Label.setAlignment(Pos.CENTER_RIGHT);

        // Adjust alignment by using spacers
        Region spacerLeft = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        row.getChildren().addAll(player1Label, spacerLeft, difficultyLabel, spacerRight, player2Label);
        return row;
    }


}
