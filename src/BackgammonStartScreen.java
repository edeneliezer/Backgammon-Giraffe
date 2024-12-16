import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BackgammonUI extends Application {

    private Button selectedButton; // To track the selected difficulty button

    @Override
    public void start(Stage primaryStage) {
        // Main layout - Centered VBox
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #fefaf4;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPrefHeight(600);

        // --- MENU BAR ---
        HBox menuBar = new HBox();
        menuBar.setPadding(new Insets(10));
        menuBar.setSpacing(20);
        menuBar.setStyle("-fx-background-color: #fefaf4;"); // Match page color
        menuBar.setAlignment(Pos.CENTER_LEFT);

        // Add DropShadow for Shading Effect
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(4.0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.2)); // Subtle shadow with 20% opacity
        menuBar.setEffect(shadow);

        // Gradient Title on the Left
        Text menuTitle = new Text("BACKGAMMON");
        menuTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        menuTitle.setFill(new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#d11e1e")), // Start of gradient
                new Stop(1, Color.web("#a07f17"))  // End of gradient
        ));

        // Buttons with Placeholder Images on the Right
        HBox menuButtons = new HBox(15);
        menuButtons.setAlignment(Pos.CENTER_RIGHT);

        Button homeButton = createImageButton("path/to/home.png", 30, 30);
        Button settingsButton = createImageButton("path/to/settings.png", 30, 30);
        Button pauseButton = createImageButton("path/to/pause.png", 30, 30);

        menuButtons.getChildren().addAll(homeButton, settingsButton, pauseButton);

        // Add title and buttons to menu bar
        HBox.setHgrow(menuButtons, Priority.ALWAYS); // Push buttons to the right
        menuBar.getChildren().addAll(menuTitle, menuButtons);

        // --- TITLE BOX (With Images) ---
        HBox titleBox = new HBox(20);
        titleBox.setAlignment(Pos.CENTER);

        ImageView leftImage = createImageView("path/to/left_image.png", 80, 80);
        Label title = new Label("BACKGAMMON");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        title.setTextFill(Color.web("#4d2d00"));
        title.setAlignment(Pos.CENTER);
        ImageView rightImage = createImageView("path/to/right_image.png", 80, 80);

        titleBox.getChildren().addAll(leftImage, title, rightImage);

        // --- PLAYERS SECTION ---
        HBox playersBox = new HBox(50);
        playersBox.setAlignment(Pos.CENTER);

        VBox player1Box = createPlayerBox("player 1 name");
        VBox player2Box = createPlayerBox("player 2 name");
        playersBox.getChildren().addAll(player1Box, player2Box);

        // --- DIFFICULTY BUTTONS ---
        HBox difficultyBox = new HBox(15);
        difficultyBox.setAlignment(Pos.CENTER);

        Label difficultyLabel = new Label("Select Difficulty Level:");
        difficultyLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        difficultyLabel.setTextFill(Color.BLACK);

        Button easyButton = createStyledButton("Easy");
        Button mediumButton = createStyledButton("Medium");
        Button hardButton = createStyledButton("Hard");

        addHighlightEffect(easyButton);
        addHighlightEffect(mediumButton);
        addHighlightEffect(hardButton);

        difficultyBox.getChildren().addAll(difficultyLabel, easyButton, mediumButton, hardButton);

        // --- PLAY BUTTON ONLY (Centered) ---
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER);

        Button playButton = new Button("Let's play!");
        playButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        playButton.setStyle("-fx-background-color: #d11e1e; -fx-text-fill: white;");
        playButton.setPrefSize(200, 60);

        bottomBox.getChildren().add(playButton);

        // Combine everything into the root
        root.getChildren().addAll(menuBar, titleBox, playersBox, difficultyBox, bottomBox);

        // Scene and Stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Backgammon Game");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
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
        playerBox.setStyle("-fx-background-color: #b30000; -fx-padding: 15; -fx-border-color: black;");
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

    private void addHighlightEffect(Button button) {
        button.setOnAction(e -> {
            if (selectedButton != null) {
                selectedButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
            }
            button.setStyle("-fx-background-color: #ffcc66; -fx-text-fill: black;");
            selectedButton = button;
        });
    }

    private ImageView createImageView(String imagePath, int width, int height) {
        ImageView imageView = new ImageView();
        try {
            imageView.setImage(new Image(imagePath));
        } catch (Exception e) {
            System.out.println("Image not found: " + imagePath);
        }
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    private Button createImageButton(String imagePath, int width, int height) {
        ImageView icon = createImageView(imagePath, width, height);
        Button button = new Button();
        button.setGraphic(icon);
        button.setStyle("-fx-background-color: transparent;");
        return button;
    }
}
