package view;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import controller.CommandController;
import controller.MusicPlayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SettingsScreen {

	private VBox settingsBox;
	private CommandController cmd;
	private Button soundEffectsButton;
	private Integer back;
	private Button historyButton;
	private Button musicButton;
	private Button homeButten;
	private Button editQuestion;

	
	private static boolean isMusicEnabled = true; // Default state: music is on

    public static boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    public static void setMusicEnabled(boolean enabled) {
        isMusicEnabled = enabled;
    }

    public SettingsScreen(CommandController cmd, int back) {
        settingsBox = createSettingsOverlay();
    	historyButton.setDisable(true);
        if(cmd != null) {
            this.cmd = cmd;
        }
        else {
        	soundEffectsButton.setDisable(true);
        }
        if(back == 1) {
        	historyButton.setDisable(false);
        }
//        startMusicByDefault();
    }

    /**
     * Creates the settings box (overlay container).
     *
     * @return VBox containing the settings UI.
     */
    private VBox createSettingsOverlay() {
        VBox settingsBox = new VBox(20); // Add smaller spacing between elements
        settingsBox.setPadding(new Insets(20)); // Reduce padding
        settingsBox.setAlignment(Pos.TOP_CENTER);
        settingsBox.setStyle(
            "-fx-background-color: #fefaf4; " + // Match the beige background
            "-fx-border-color: brown; " +       // Dark brown border
            "-fx-border-width: 3; " +           // Border thickness
            "-fx-border-radius: 10; " +         // Rounded corners
            "-fx-background-radius: 10;"        // Rounded background
        );

        // Make the box smaller
        settingsBox.setPrefSize(150, 180); // Adjust width and height to make it smaller

        // Settings Title
        Label settingsTitle = new Label("SETTINGS");
        settingsTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); 
        settingsTitle.setTextFill(Color.BROWN);

        // Buttons for settings options with icons
        musicButton = createIconButton("Music", "sound.png");
        musicButton.setOnAction(e -> {
            ImageView icon;
            if (isMusicEnabled()) {
                setMusicEnabled(false); // Disable music globally
                MusicPlayer.getInstance().pause();
                icon = new ImageView(new Image("mute.png"));
                musicButton.setText("Unmute");
            } else {
                setMusicEnabled(true); // Enable music globally
                MusicPlayer.getInstance().play();
                icon = new ImageView(new Image("sound.png"));
                musicButton.setText("Music");
            }            // Set the size of the icon
            icon.setFitWidth(20); // Set the width
            icon.setFitHeight(20); // Set the height
            musicButton.setGraphic(icon);
        });
        
        soundEffectsButton = createIconButton("disable Sound", "sound.png");
        soundEffectsButton.setOnAction(e -> {
    	ImageView pic;
    	 if (cmd.getSoundFXPlayer().isWorking()) {
    	     cmd.getSoundFXPlayer().disableEffects();
    	     soundEffectsButton.setText("enable Sound");
             pic = new ImageView(new Image("mute.png"));
        } else {
        	cmd.getSoundFXPlayer().enableEffects();
            pic = new ImageView(new Image("sound.png"));
            soundEffectsButton.setText("disable sound");
        }
        // Set the size of the icon
        pic.setFitWidth(20); // Set the width
        pic.setFitHeight(20); // Set the height
        soundEffectsButton.setGraphic(pic);
    });

        historyButton = createIconButton("History", "history.png");
        Button infoButton = createIconButton("Information", "info.png");
        infoButton.setOnAction(e -> openPdfFile("BackgammonRules.pdf"));
        
        editQuestion = createIconButton("Edit Questions", "history.png");
        editQuestion.setOnAction(e -> {
      	  // Switch to History Screen and pass this SettingsScreen
        	  Stage stage = (Stage) settingsBox.getScene().getWindow(); // Get the current stage
        	  Scene previousScene = stage.getScene();                  // Save the current scene
        	  new EditQuestionsScreen(stage, previousScene); 
      });

        // Close Button
        Button closeButton = new Button("Close");
        closeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        closeButton.setStyle(
        	    "-fx-background-color: #d11e1e; " +  // רקע אדום
        	    "-fx-text-fill: white; " +           // טקסט בצבע לבן
        	    "-fx-border-radius: 5; " +
        	    "-fx-background-radius: 5; " +
        	    "-fx-cursor: hand;"                // שינוי ה-Cursor לכף יד
        	);

    	// אפקט ריחוף
    	closeButton.setOnMouseEntered(e -> closeButton.setStyle(
    	    "-fx-background-color: #b30000; " +  // רקע אדום כהה יותר בעת ריחוף
    	    "-fx-text-fill: white; " +
    	    "-fx-border-radius: 5; " +
    	    "-fx-background-radius: 5; " +
    	    "-fx-cursor: hand;"
    	));

    	closeButton.setOnMouseExited(e -> closeButton.setStyle(
    	    "-fx-background-color: #d11e1e; " +  // חזרה לצבע המקורי
    	    "-fx-text-fill: white; " +
    	    "-fx-border-radius: 5; " +
    	    "-fx-background-radius: 5; " +
    	    "-fx-cursor: hand;"
    	));

        closeButton.setOnAction(e -> {
            settingsBox.setVisible(false); // Hide settings box
            StackPane stackPane = (StackPane) settingsBox.getParent();
            Node fadeBackground = stackPane.getChildren().get(1); // Access the fade rectangle
            fadeBackground.setVisible(false); // Hide fade background
        });

        // Add Action to the History Button
        historyButton.setOnAction(e -> {
        	  // Switch to History Screen and pass this SettingsScreen
            Stage stage = (Stage) settingsBox.getScene().getWindow();
            HistoryScreen historyScreen = new HistoryScreen(); // Pass the current SettingsScreen
            historyScreen.start(stage);
        });
        

        // Add all elements to the settings box
        settingsBox.getChildren().addAll(settingsTitle, musicButton,soundEffectsButton, historyButton, infoButton,editQuestion, closeButton);

        return settingsBox;
    }

    /**
     * Creates a button with an icon.
     *
     * @param text The button text.
     * @param iconPath The path to the icon image.
     * @return Button with the specified icon and text.
     */
    private Button createIconButton(String text, String iconPath) {
        ImageView icon = new ImageView(new Image(iconPath));
        icon.setFitWidth(20); // Set the width
        icon.setFitHeight(20); // Set the height
        Button button = new Button(text, icon);
        button.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        button.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        // סגנון ברירת מחדל
        button.setStyle(
            "-fx-background-color: #fefaf4; " + // רקע רגיל
            "-fx-border-color: brown; " +       // גבול בצבע חום
            "-fx-border-width: 1; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"                // שינוי ה-Cursor לכף יד
        );

        // אפקט ריחוף
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #d2a679; " + // רקע חום בהיר יותר בעת ריחוף
            "-fx-border-color: brown; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"                // שמירה על כף יד
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #fefaf4; " + // חזרה לצבע המקורי
            "-fx-border-color: brown; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"                // שמירה על כף יד
        ));

        return button;
    }


    /**
     * Opens a PDF file in the default PDF viewer.
     *
     * @param fileName The name of the PDF file.
     */
    public void openPdfFile(String resourcePath) { try {
      // Load the resource as an InputStream
      InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
      if (resourceStream == null) {
          throw new IllegalArgumentException("Resource not found: " + resourcePath);
      }

      // Create a temporary file
      File tempFile = File.createTempFile("BackgammonRules", ".pdf");
      tempFile.deleteOnExit(); // Ensure the file is deleted on JVM exit

      // Write the resource contents to the temporary file
      try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
          byte[] buffer = new byte[1024];
          int bytesRead;
          while ((bytesRead = resourceStream.read(buffer)) != -1) {
              outputStream.write(buffer, 0, bytesRead);
          }
      }

      // Open the PDF file using the default desktop application
      if (Desktop.isDesktopSupported()) {
          Desktop.getDesktop().open(tempFile);
      } else {
          System.err.println("Desktop is not supported on this platform.");
      }
  } catch (IOException e) {
      e.printStackTrace();
  }
  }
    
    private void startMusicByDefault() {
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (!musicPlayer.isPlaying()) {
            musicPlayer.play();
        }
    }
    
    public void updateMusicButton() {
        ImageView icon;
        if (isMusicEnabled()) {
            icon = new ImageView(new Image("sound.png"));
            musicButton.setText("Music");
        } else {
            icon = new ImageView(new Image("mute.png"));
            musicButton.setText("Unmute");
        }
        icon.setFitWidth(20); // Set the width
        icon.setFitHeight(20); // Set the height
        musicButton.setGraphic(icon);
    }


    /**
     * Returns the settings box.
     *
     * @return VBox containing the settings.
     */
    public VBox getSettingsBox() {
        return settingsBox;
    }
    
    
}
