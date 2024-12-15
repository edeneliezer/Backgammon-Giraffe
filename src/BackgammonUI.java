import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BackgammonUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main layout (VBox)
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #fefaf4;");

        // Title
        Label title = new Label("BACKGAMMON");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        title.setTextFill(Color.web("#4d2d00"));
        title.setAlignment(Pos.CENTER);

        // Subheading
        HBox playersBox = new HBox(50);
        playersBox.setAlignment(Pos.CENTER);

        // Player 1 Box
        VBox player1Box = createPlayerBox("player 1 name");

        // Player 2 Box
        VBox player2Box = createPlayerBox("player 2 name");

        playersBox.getChildren().addAll(player1Box, player2Box);

        // Difficulty Buttons
        HBox difficultyBox = new HBox(15);
        difficultyBox.setAlignment(Pos.CENTER);

        Label difficultyLabel = new Label("Select Difficulty Level:");
        difficultyLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        difficultyLabel.setTextFill(Color.BLACK);

        Button easyButton = createStyledButton("Easy");
        Button mediumButton = createStyledButton("Medium");
        Button hardButton = createStyledButton("Hard");

        difficultyBox.getChildren().addAll(difficultyLabel, easyButton, mediumButton, hardButton);

        // Let's Play Button
        Button playButton = new Button("Let's play!");
        playButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        playButton.setStyle("-fx-background-color: #d11e1e; -fx-text-fill: white;");
        playButton.setPrefSize(200, 60);

        // History Button
        Button historyButton = new Button("History");
        historyButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        historyButton.setStyle("-fx-background-color: #d11e1e; -fx-text-fill: white;");
        historyButton.setPrefSize(100, 40);

        // Bottom Layout for Play and History Buttons
        HBox bottomBox = new HBox(20);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().addAll(playButton, historyButton);

        // Combine all into the main layout
        root.getChildren().addAll(title, playersBox, difficultyBox, bottomBox);

        // Scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Backgammon Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createPlayerBox(String playerName) {
        Label nameLabel = new Label(playerName);
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.WHITE);

        TextField textField = new TextField();
        textField.setPromptText("write here");
        textField.setPrefWidth(150);

        Button submitButton = new Button("submit");
        submitButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        submitButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");

        VBox playerBox = new VBox(10);
        playerBox.setAlignment(Pos.CENTER);
        playerBox.setStyle("-fx-background-color: #b30000; -fx-padding: 15; -fx-border-color: black; -fx-border-width: 3px;");
        playerBox.getChildren().addAll(nameLabel, textField, submitButton);

        return playerBox;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        button.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
        button.setPrefSize(80, 30);
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
