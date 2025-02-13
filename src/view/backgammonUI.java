package view;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import Model.Dice;
import Model.SysData;
import controller.MatchController;
import controller.SysDataController;
import controller.MusicPlayer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
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
import javafx.scene.shape.Rectangle;
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
    private  Button easyButton;
    private  Button mediumButton;
    private  Button hardButton;
    private static String chosenDiffficulty = "unknown";

    @Override
    public void start(Stage primaryStage) {
    	
    	    
        // Main layout - Centered VBox
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #fefaf4;");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPrefHeight(600);
        
     // Create settings
        SettingsScreen settings = new SettingsScreen(null,1);
        VBox settingsOverlay = settings.getSettingsBox();
        settingsOverlay.setVisible(false);

        // Fade background
        Rectangle fadeBackground = new Rectangle(800, 600);
        fadeBackground.setFill(Color.rgb(0, 0, 0, 0.5));
        fadeBackground.setVisible(false);

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

        Button settingsButton = createImageButton("settings.jpg", 25, 25);
        settingsButton.setId("settingsButton"); // Add an ID for lookup

        // הוספת Hand Cursor
        settingsButton.setStyle(
            "-fx-background-color: transparent; " + // רקע שקוף
            "-fx-cursor: hand;"                     // שינוי הסמן לכף יד
        );

        // אפקט שינוי צבע בעת ריחוף
        settingsButton.setOnMouseEntered(e -> settingsButton.setStyle(
            "-fx-background-color: #8b5e3c; " + // רקע בהיר יותר בעת ריחוף
            "-fx-cursor: hand;"                // שמירה על כף יד
        ));

        settingsButton.setOnMouseExited(e -> settingsButton.setStyle(
            "-fx-background-color: transparent; " + // חזרה לרקע המקורי
            "-fx-cursor: hand;"                     // שמירה על כף יד
        ));

        // הוספת פעולה בעת לחיצה
        settingsButton.setOnAction(e -> {
            boolean isVisible = !settingsOverlay.isVisible();
            settingsOverlay.setVisible(isVisible);
            fadeBackground.setVisible(isVisible);
        });



        menuButtons.getChildren().addAll(settingsButton);

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

        VBox player1Box = createPlayerBox(true);
        VBox player2Box = createPlayerBox(false);
        playersBox.getChildren().addAll(player1Box, player2Box);



        
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

 

         easyButton = createStyledButton("Easy");
         mediumButton = createStyledButton("Medium");
         hardButton = createStyledButton("Hard");

        addHighlightEffect(easyButton);
        addHighlightEffect(mediumButton);
        addHighlightEffect(hardButton);
        
        easyButton.setOnAction(e -> {
            if (!isDifficultySelected) {
                isDifficultySelected = true;  // בחר קושי
                chosenDiffficulty = "Easy";
                checkIfReadyToPlay();  // בודק אם אפשר להפעיל את כפתור המשחק
            }
            // משבית את שאר הכפתורים
            easyButton.setStyle("-fx-background-color: #d2a679; -fx-text-fill: black;");
            mediumButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
            hardButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
        });

        mediumButton.setOnAction(e -> {
            if (!isDifficultySelected) {
                isDifficultySelected = true;
                chosenDiffficulty = "Medium";
                checkIfReadyToPlay();
            }
            // משבית את שאר הכפתורים
            easyButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
            mediumButton.setStyle("-fx-background-color: #d2a679; -fx-text-fill: black;");
            hardButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
        });

        hardButton.setOnAction(e -> {
            if (!isDifficultySelected) {
                isDifficultySelected = true;
                chosenDiffficulty = "Hard";
                checkIfReadyToPlay();
            }
            // משבית את שאר הכפתורים
            easyButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
            mediumButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
            hardButton.setStyle("-fx-background-color: #d2a679; -fx-text-fill: black;");
        });

        // הגדרת גודל הכפתורים
        easyButton.setPrefSize(120, 50); // רוחב 120, גובה 50
        mediumButton.setPrefSize(120, 50);
        hardButton.setPrefSize(120, 50);

        root.setSpacing(50); 
        difficultyBox.getChildren().addAll(easyButton, mediumButton, hardButton);

        // --- PLAY BUTTON ONLY (Centered) ---
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER);

        playButton = new Button("Let's play!");
        playButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        playButton.setStyle("-fx-background-color: #d11e1e; -fx-text-fill: white; -fx-cursor: hand;");
        playButton.setPrefSize(200, 60);
        playButton.setDisable(true);
        playButton.setOnAction(e -> {        	
        	// Map difficulty string to Dice.Mode
            Dice.Mode mode = Dice.Mode.REGULAR; // Default
            Dice.currentMode = Dice.Mode.REGULAR;
            if (chosenDiffficulty.equals("Hard")) {
                mode = Dice.Mode.HARD;
                Dice.currentMode = Dice.Mode.HARD;
            }
        	
            // Create a new MatchController instance
        	MatchController matchController = new MatchController(primaryStage, mode); // assuming primaryStage is available here
            Scene gameScene = new Scene(matchController, 1400, 800); // adjust size as needed
            
            primaryStage.setScene(gameScene); // Switch to the game screen
            primaryStage.centerOnScreen();
            matchController.startGame();
        });
        bottomBox.getChildren().add(playButton);
        

        // Combine everything into the root
        root.getChildren().addAll(menuBar, titleBox, playersBox, difficultyBox, bottomBox);
//
//        // Create a semi-transparent rectangle for background fading
//        Rectangle fadeBackground = new Rectangle(800, 600); // Match the scene size
//        fadeBackground.setFill(Color.rgb(0, 0, 0, 0.5)); // Black with 50% opacity
//        fadeBackground.setVisible(false); // Initially hidden
//        
//        // Create settings overlay
//        VBox settingsOverlay = createSettingsOverlay();
//        settingsOverlay.setVisible(false); // Initially hidden
//
//        // Wrap everything in a StackPane
//        
//        StackPane stackPane = new StackPane(root, fadeBackground, settingsOverlay);
//        StackPane.setMargin(settingsOverlay, new Insets(250, 250, 250, 250)); // Margin from top and right edges
//        
       
        StackPane stackPane = new StackPane(root, fadeBackground, settingsOverlay);
        StackPane.setMargin(settingsOverlay, new Insets(250, 250, 250, 250)); // Margin from top and right edges

        // Scene and Stage
        Scene scene = new Scene(stackPane, 800, 600);
        primaryStage.setTitle("Backgammon Game");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
  
       }
    
    
    public void checkIfReadyToPlay() {
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
    
//    private VBox createSettingsOverlay() {
//        // Create the settings box (overlay container)
//        VBox settingsBox = new VBox(20); // Add smaller spacing between elements
//        settingsBox.setPadding(new Insets(20)); // Reduce padding
//        settingsBox.setAlignment(Pos.TOP_CENTER);
//        settingsBox.setStyle(
//            "-fx-background-color: #fefaf4; " + // Match the beige background
//            "-fx-border-color: brown; " +       // Dark brown border
//            "-fx-border-width: 3; " +           // Border thickness
//            "-fx-border-radius: 10; " +         // Rounded corners
//            "-fx-background-radius: 10;"        // Rounded background
//        );
//
//        // Make the box smaller
//        settingsBox.setPrefSize(150, 180); // Adjust width and height to make it smaller
//
//        // Settings Title
//        Label settingsTitle = new Label("SETTINGS");
//        settingsTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14)); // Slightly smaller font
//        settingsTitle.setTextFill(Color.BROWN);
//
//        // Buttons for settings options with icons
//        Button musicButton = createIconButton("Music", "sound.png");
//        musicButton.setOnAction(e -> {
//        	ImageView icon;
//            if (controller.MusicPlayer.isPlaying()) {
//            	controller.MusicPlayer.pause();
//            	icon = new ImageView(new Image("mute.png"));
//                musicButton.setText("Unmute");
//            } else {
//            	controller.MusicPlayer.play();
//            	 icon = new ImageView(new Image("sound.png"));
//            	 musicButton.setText("Music");
//            }
//            // Set the size of the icon
//            icon.setFitWidth(20); // Set the width
//            icon.setFitHeight(20); // Set the height
//            musicButton.setGraphic(icon);
//        });
//       
//        Button historyButton = createIconButton("History", "history.png");
//        Button infoButton = createIconButton("Information", "info.png");
//        infoButton.setOnAction(e -> openPdfFile("BackgammonRules.pdf"));
//
//        // Close Button
//        Button closeButton = new Button("Close");
//        closeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
//        closeButton.setStyle(
//            "-fx-background-color: #d11e1e; " +  // Red button background
//            "-fx-text-fill: white; " +           // White text
//            "-fx-border-radius: 5; " +           // Rounded border
//            "-fx-background-radius: 5;"         // Rounded background
//        );
//        closeButton.setOnAction(e -> {
//            settingsBox.setVisible(false); // Hide settings box
//            StackPane stackPane = (StackPane) settingsBox.getParent();
//            Node fadeBackground = stackPane.getChildren().get(1); // Access the fade rectangle
//            fadeBackground.setVisible(false); // Hide fade background
//        });
//        
//        // Add Action to the History Button
//        historyButton.setOnAction(e -> {
//            // Switch to History Screen
//            Stage stage = (Stage) settingsBox.getScene().getWindow();
//            HistoryScreen historyScreen = new HistoryScreen();
//            historyScreen.start(stage); // Navigate to History Screen
//        });
//        
//        // Add all elements to the settings box
//        settingsBox.getChildren().addAll(settingsTitle, musicButton, historyButton, infoButton,closeButton);
//
//        return settingsBox;
//    }
//
//
//    public void openPdfFile(String resourcePath) {
//        try {
//            // Load the resource as an InputStream
//            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
//            if (resourceStream == null) {
//                throw new IllegalArgumentException("Resource not found: " + resourcePath);
//            }
//
//            // Create a temporary file
//            File tempFile = File.createTempFile("BackgammonRules", ".pdf");
//            tempFile.deleteOnExit(); // Ensure the file is deleted on JVM exit
//
//            // Write the resource contents to the temporary file
//            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = resourceStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//            }
//
//            // Open the PDF file using the default desktop application
//            if (Desktop.isDesktopSupported()) {
//                Desktop.getDesktop().open(tempFile);
//            } else {
//                System.err.println("Desktop is not supported on this platform.");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    private Button createIconButton(String text, String iconUrl) {
//        // Create an icon for the button
//        ImageView icon = new ImageView(new Image(iconUrl));
//        icon.setFitWidth(20); // Icon size
//        icon.setFitHeight(20);
//
//        // Create the button with the icon and text
//        Button button = new Button(text, icon);
//        button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
//        button.setStyle(
//            "-fx-background-color: #d2a679; " + // Light brown background
//            "-fx-text-fill: black; " +          // Black text
//            "-fx-border-color: brown; " +       // Dark brown border
//            "-fx-border-width: 1.5; " +         // Thin border
//            "-fx-background-radius: 10; " +    // Rounded background
//            "-fx-border-radius: 10;"           // Rounded border
//        );
//        button.setContentDisplay(ContentDisplay.LEFT); // Icon on the left of text
//        button.setPrefWidth(160); // Consistent button width
//        return button;
//    }




    private VBox createPlayerBox(boolean isPlayer1) {
        // יצירת תמונה
        ImageView playerImage = new ImageView();
        playerImage.setFitWidth(30);
        playerImage.setFitHeight(30);

        // טוען את התמונה המתאימה
        String imagePath = isPlayer1 
            ? "game/img/checkers/white_checkers.png" 
            : "game/img/checkers/black_checkers.png";
        try {
            playerImage.setImage(new Image(imagePath));
        } catch (Exception e) {
            System.out.println("Error loading image: " + imagePath);
        }

        // יצירת שדה טקסט
        TextField textField = new TextField();
        textField.setPromptText("insert your name here");

        if (isPlayer1) {
            player1Field = textField;
        } else {
            player2Field = textField;
        }

        // יצירת תיבה עם תמונה ושדה טקסט
        HBox playerBox = new HBox(10);
        playerBox.setAlignment(Pos.CENTER);
        playerBox.getChildren().addAll(playerImage, textField);

        // עטיפת התיבה בתיבת VBox
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: #b30000; -fx-padding: 15; -fx-border-color: black;");
        container.getChildren().add(playerBox);

        return container;
    }



    

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        button.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");
        button.setPrefSize(80, 30);
        
        // שינוי סמן העכבר לצורת כף יד
        button.setOnMouseEntered(e -> button.setCursor(javafx.scene.Cursor.HAND));
        button.setOnMouseExited(e -> button.setCursor(javafx.scene.Cursor.DEFAULT));

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
//    private static String getSelectedDifficulty() {
//        if (easyButton != null && easyButton.getStyle().contains("ffcc66")) return "Easy";
//        if (mediumButton != null && mediumButton.getStyle().contains("ffcc66")) return "Medium";
//        if (hardButton != null && hardButton.getStyle().contains("ffcc66")) return "Hard";
//        return "Unknown";
//    }
        


	public Button getPlayButton() {
    	Button playButton = new Button("Play");
        return playButton;
    }
	public static String getChosenDiffficulty() {
		return chosenDiffficulty;
	}
	public static void setChosenDiffficulty(String chosenDiffficulty) {
		backgammonUI.chosenDiffficulty = chosenDiffficulty;
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