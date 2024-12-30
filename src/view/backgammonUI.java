package view;
import Model.GameModel;
import controller.MatchController;
import controller.jsonController;
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

public class backgammonUI extends Application {

    private Button selectedButton; // To track the selected difficulty button
    private Button playButton; // Button for playing the game
    private boolean arePlayersReady = false;
    private boolean isDifficultySelected = false;
    private static TextField player1Field; // TextField for player 1
    private static TextField player2Field;
    private static Button easyButton;
    private static Button mediumButton;
    private static Button hardButton;

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
        menuTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
        menuTitle.setFill(new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#d11e1e")), // Start of gradient
                new Stop(1, Color.web("#a07f17"))  // End of gradient
        ));

        // Buttons with Placeholder Images on the Right
        HBox menuButtons = new HBox(15);
        menuButtons.setAlignment(Pos.CENTER_RIGHT);

        Button homeButton = createImageButton("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQjH7pqz2CjdDdATbWPhlUt3DNjZh6mhjQ0YA&s", 25, 30);
        Button settingsButton = createImageButton("https://www.iconpacks.net/icons/2/free-settings-icon-3110-thumb.png", 25, 25);
        Button pauseButton = createImageButton("https://png.pngtree.com/png-vector/20220124/ourmid/pngtree-pause-icon-pause-pause-icon-sign-vector-png-image_26215894.jpg", 30, 25);

        menuButtons.getChildren().addAll(pauseButton,settingsButton,homeButton);

        // Add title and buttons to menu bar
        HBox.setHgrow(menuButtons, Priority.ALWAYS); // Push buttons to the right
        menuBar.getChildren().addAll(menuTitle, menuButtons);
        
     // --- TITLE BOX (With Gradient Colors) ---
        HBox titleBox = new HBox(20);
        titleBox.setAlignment(Pos.CENTER);

        // Create the big title with gradient colors
        Text bigTitle = new Text("BACKGAMMON");
        bigTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        bigTitle.setFill(new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#d11e1e")), // Start of gradient
                new Stop(1, Color.web("#a07f17"))  // End of gradient
        ));

        titleBox.getChildren().addAll(bigTitle);


        // --- PLAYERS SECTION ---
        HBox playersBox = new HBox(50);
        playersBox.setAlignment(Pos.CENTER);

        VBox player1Box = createPlayerBox("player 1 name");
        VBox player2Box = createPlayerBox("player 2 name");
        playersBox.getChildren().addAll(player1Box, player2Box);
        player1Field = (TextField) player1Box.getChildren().get(1); // עכשיו הם שדות מחלקה
        player2Field = (TextField) player2Box.getChildren().get(1);
        
        
        
        // מאזיני טקסט לשדות השחקנים
        player1Field.textProperty().addListener((observable, oldValue, newValue) -> {
            checkIfReadyToPlay();
        });

        player2Field.textProperty().addListener((observable, oldValue, newValue) -> {
            checkIfReadyToPlay();
        });
        // --- DIFFICULTY BUTTONS ---
        HBox difficultyBox = new HBox(15);
        difficultyBox.setAlignment(Pos.CENTER);

        Label difficultyLabel = new Label("Select Difficulty Level:");
        difficultyLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        difficultyLabel.setTextFill(Color.BLACK);

         easyButton = createStyledButton("Easy");
         mediumButton = createStyledButton("Medium");
         hardButton = createStyledButton("Hard");

        addHighlightEffect(easyButton);
        addHighlightEffect(mediumButton);
        addHighlightEffect(hardButton);
        
        easyButton.setOnAction(e -> {
            if (!isDifficultySelected) {
                isDifficultySelected = true;  // בחר קושי
                checkIfReadyToPlay();  // בודק אם אפשר להפעיל את כפתור המשחק
            }
            // משבית את שאר הכפתורים
            easyButton.setStyle("-fx-background-color: #ffcc66; -fx-text-fill: black;");
            mediumButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
            hardButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
        });

        mediumButton.setOnAction(e -> {
            if (!isDifficultySelected) {
                isDifficultySelected = true;
                checkIfReadyToPlay();
            }
            // משבית את שאר הכפתורים
            easyButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
            mediumButton.setStyle("-fx-background-color: #ffcc66; -fx-text-fill: black;");
            hardButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
        });

        hardButton.setOnAction(e -> {
            if (!isDifficultySelected) {
                isDifficultySelected = true;
                checkIfReadyToPlay();
            }
            // משבית את שאר הכפתורים
            easyButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
            mediumButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
            hardButton.setStyle("-fx-background-color: #ffcc66; -fx-text-fill: black;");
        });



        root.setSpacing(50); 
        difficultyBox.getChildren().addAll(difficultyLabel, easyButton, mediumButton, hardButton);

        // --- PLAY BUTTON ONLY (Centered) ---
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER);

        playButton = new Button("Let's play!");
        playButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        playButton.setStyle("-fx-background-color: #d11e1e; -fx-text-fill: white;");
        playButton.setPrefSize(200, 60);
        playButton.setDisable(true);
        playButton.setOnAction(e -> {
            // Create a new MatchController instance
            MatchController matchController = new MatchController(primaryStage); // assuming primaryStage is available here
            Scene gameScene = new Scene(matchController, 1000, 800); // adjust size as needed
            primaryStage.setScene(gameScene); // Switch to the game screen
            matchController.startGame();
        });
        
        
        

       
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
    private void checkIfReadyToPlay() {
        // Enable "Let's play!" only if all conditions are met
        boolean isPlayer1NameEntered = !player1Field.getText().trim().isEmpty();
        boolean isPlayer2NameEntered = !player2Field.getText().trim().isEmpty();
        boolean isDifficultySelected = this.isDifficultySelected;

        if (isPlayer1NameEntered && isPlayer2NameEntered && isDifficultySelected) {
            playButton.setDisable(false); // Enable the play button
        } else {
            playButton.setDisable(true); // Keep it disabled if conditions are not met
        }
        
    }
    
    

    private VBox createPlayerBox(String playerName) {
        Label nameLabel = new Label(playerName);
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.WHITE);

        TextField textField = new TextField();
        textField.setPromptText("write here");
        textField.setPrefWidth(150);

        VBox playerBox = new VBox(10);
        playerBox.setAlignment(Pos.CENTER);
        playerBox.setStyle("-fx-background-color: #b30000; -fx-padding: 15; -fx-border-color: black;");
        playerBox.getChildren().addAll(nameLabel, textField);

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
    private static String getSelectedDifficulty() {
        if (easyButton != null && easyButton.getStyle().contains("ffcc66")) return "Easy";
        if (mediumButton != null && mediumButton.getStyle().contains("ffcc66")) return "Medium";
        if (hardButton != null && hardButton.getStyle().contains("ffcc66")) return "Hard";
        return "Unknown";
    }
    
    private static String chosenDifficulty = getSelectedDifficulty();
    

    public static String getChosenDifficulty() {
		return chosenDifficulty;
	}
	public void setChosenDifficulty(String chosenDifficulty) {
		this.chosenDifficulty = chosenDifficulty;
	}
	public Button getPlayButton() {
    	Button playButton = new Button("Play");
        return playButton;
    }
	public static TextField getPlayer1Field() {
		return player1Field;
	}
	public void setPlayer1Field(TextField player1Field) {
		this.player1Field = player1Field;
	}
	public static TextField getPlayer2Field() {
		return player2Field;
	}
	public void setPlayer2Field(TextField player2Field) {
		this.player2Field = player2Field;
	}
}