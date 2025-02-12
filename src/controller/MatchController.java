package controller;

import java.awt.Rectangle;
import java.util.Optional;

import Model.Dice;
import Model.DoublingCube;
import Model.GameConstants;
import Model.MatchTimer;
import Model.Player;
import Model.PlayerPerspectiveFrom;
import Model.Settings;
import controller.ColorPerspectiveParser;
import controller.CommandController;
import controller.EventController;
import controller.GameplayController;
import controller.InputValidator;
import controller.MusicPlayer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
//import view.CommandPanel;
import view.Dialogs;
import view.GameComponentsController;
import view.HistoryScreen;
import view.InfoPanel;
import view.RollDieButton;
import view.ScoreboardPrompt;
import view.SettingsScreen;
import view.backgammonUI;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
/**
 * This class represents the entire component of the application,
 * consisting of the game components and the UI components.
 * 
 * These components are children of this class, therefore
 * this class is the root in the layout structure/tree.
 */
public class MatchController extends GridPane implements ColorPerspectiveParser, InputValidator {
	private Player bottomPlayer;
	private Player topPlayer;
	private GameComponentsController game;
	private InfoPanel infoPnl;
	private RollDieButton rollDieBtn;
//	private CommandPanel cmdPnl;
	private CommandController cmd;
	private GameplayController gameplay;
	private EventController event;
//	private MusicPlayer musicPlayer;
	private Stage stage;
	private boolean isPlayerInfosEnteredFirstTime, isPromptCancel, hadCrawfordGame, isCrawfordGame;
	private MatchTimer gameTimer;
	private Button settingsButton;
	private Dice.Mode diceMode;
	private SoundEffectsPlayer soundEffectsPlayer;
	private SettingsScreen settingsScreen;
	private StackPane stackPane;
	private VBox settingsOverlay;
	private Button backToHomeBtn;
	
	/**
	 * Default Constructor
	 * 		- Initialize all the components (game, commandPanel, InfoPanel, RollDieButton, etc).
	 * 		- Initialize sub-controllers (Command, Gameplay, GameComponents, Event).
	 * 		- Initialize the layout of the components.
	 * 		- Style the application.
	 */
	 public MatchController(Stage stage, Dice.Mode diceMode) {
	        super();
	        this.stage = stage;
	        this.diceMode = diceMode; // Store the dice mode
	        initApplication();
	        initGame();
	        style();
	  }
	 
	 public MatchTimer getGameTimer() {
		    return gameTimer;
		}
	
	/**
	 * Initialize players and UI components.
	 */
	private void initApplication() {
		bottomPlayer = new Player(PlayerPerspectiveFrom.BOTTOM);
		topPlayer = new Player(PlayerPerspectiveFrom.TOP);
		infoPnl = new InfoPanel();
		rollDieBtn = new RollDieButton();
	//	cmdPnl = new CommandPanel();
//		musicPlayer = new MusicPlayer();
	    soundEffectsPlayer = new SoundEffectsPlayer(); // Initialize sound effects
		isPlayerInfosEnteredFirstTime = true;
		isPromptCancel = false;
		backToHomeBtn = createBTHButton();
	}
	
	
	
	private Button createBTHButton() {
	    Button backToHomeBtn = new Button("Back");
	    backToHomeBtn.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

	    // עיצוב הכפתור בצבע בורדו
	    backToHomeBtn.setStyle(
	        "-fx-background-color: #800000; " +  // צבע רקע בורדו
	        "-fx-text-fill: white; " +           // טקסט בצבע לבן
	        "-fx-border-radius: 5; " +           // פינות מעוגלות
	        "-fx-background-radius: 5;" +        // רקע מעוגל
	        "-fx-cursor: hand;"                  // שינוי ה-CURSOR לכף יד
	    );

	    // שינוי עיצוב בעת ריחוף (Hover)
	    backToHomeBtn.setOnMouseEntered(e -> backToHomeBtn.setStyle(
	        "-fx-background-color: #990000; " +  // צבע כהה יותר בעת ריחוף
	        "-fx-text-fill: white; " +
	        "-fx-border-radius: 5; " +
	        "-fx-background-radius: 5;" +
	        "-fx-cursor: hand;"                  // שמירה על כף יד
	    ));

	    backToHomeBtn.setOnMouseExited(e -> backToHomeBtn.setStyle(
	        "-fx-background-color: #800000; " +  // חזרה לצבע המקורי
	        "-fx-text-fill: white; " +
	        "-fx-border-radius: 5; " +
	        "-fx-background-radius: 5;" +
	        "-fx-cursor: hand;"                  // שמירה על כף יד
	    ));

	    // פעולה שמתרחשת בעת לחיצה על הכפתור
	    backToHomeBtn.setOnAction(e -> {
	        // יצירת הודעת Alert מעוצבת
	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Exit Confirmation");
	        alert.setHeaderText(null); // אין כותרת עליונה

	        // עיצוב תוכן ההודעה
	        VBox content = new VBox(10);
	        content.setAlignment(Pos.CENTER_LEFT);

	        // טקסט גדול ובולט לחלק הראשון
	        Label mainMessage = new Label("Are you sure you want to leave?");
	        mainMessage.setFont(Font.font("Verdana", FontWeight.BOLD, 16)); // טקסט בולט
	        mainMessage.setTextFill(Color.BROWN);

	        // טקסט קטן יותר לחלק השני
	        Label subMessage = new Label("If you leave, the game will end and won't be saved.");
	        subMessage.setFont(Font.font("Verdana", FontWeight.NORMAL, 12)); // טקסט קטן יותר
	        subMessage.setTextFill(Color.DARKRED);

	        // הוספת שני חלקי הטקסט לתוכן
	        content.getChildren().addAll(mainMessage, subMessage);

	        // הגדרת התוכן המעוצב
	        alert.getDialogPane().setContent(content);

	        alert.getDialogPane().setStyle(
	            "-fx-background-color: #fefaf4; " +  // רקע בז'
	            "-fx-border-color: brown; " +       // גבול חום
	            "-fx-border-width: 2; " +           // עובי הגבול
	            "-fx-border-radius: 10; " +         // פינות מעוגלות
	            "-fx-background-radius: 10;"        // רקע מעוגל
	        );

	        // עיצוב כפתורים (כמו בקוד הקודם)
	        ButtonType yesButton = new ButtonType("Yes");
	        ButtonType cancelButton = new ButtonType("Cancel");
	        alert.getButtonTypes().setAll(yesButton, cancelButton);

	        Node yesButtonNode = alert.getDialogPane().lookupButton(yesButton);
	        if (yesButtonNode instanceof Button) {
	            Button yesBtn = (Button) yesButtonNode;
	            yesBtn.setStyle(
	                "-fx-background-color: #800000; " +  // צבע בורדו
	                "-fx-text-fill: white; " +           // טקסט בצבע לבן
	                "-fx-border-radius: 5; " +           // פינות מעוגלות
	                "-fx-background-radius: 5; " +
	                "-fx-cursor: hand;"                  // שינוי ה-CURSOR לכף יד
	            );
	            yesBtn.setOnMouseEntered(event -> yesBtn.setStyle(
	                "-fx-background-color: #990000; " +  // צבע כהה יותר בעת ריחוף
	                "-fx-text-fill: white; " +
	                "-fx-border-radius: 5; " +
	                "-fx-background-radius: 5; " +
	                "-fx-cursor: hand;"
	            ));
	            yesBtn.setOnMouseExited(event -> yesBtn.setStyle(
	                "-fx-background-color: #800000; " +
	                "-fx-text-fill: white; " +
	                "-fx-border-radius: 5; " +
	                "-fx-background-radius: 5; " +
	                "-fx-cursor: hand;"
	            ));
	        }

	        Node cancelButtonNode = alert.getDialogPane().lookupButton(cancelButton);
	        if (cancelButtonNode instanceof Button) {
	            Button cancelBtn = (Button) cancelButtonNode;
	            cancelBtn.setStyle(
	                "-fx-background-color: #444444; " +  // צבע אפור כהה
	                "-fx-text-fill: white; " +           // טקסט בצבע לבן
	                "-fx-border-radius: 5; " +           // פינות מעוגלות
	                "-fx-background-radius: 5; " +
	                "-fx-cursor: hand;"                  // שינוי ה-CURSOR לכף יד
	            );
	            cancelBtn.setOnMouseEntered(event -> cancelBtn.setStyle(
	                "-fx-background-color: #555555; " +  // צבע כהה יותר בעת ריחוף
	                "-fx-text-fill: white; " +
	                "-fx-border-radius: 5; " +
	                "-fx-background-radius: 5; " +
	                "-fx-cursor: hand;"
	            ));
	            cancelBtn.setOnMouseExited(event -> cancelBtn.setStyle(
	                "-fx-background-color: #444444; " +
	                "-fx-text-fill: white; " +
	                "-fx-border-radius: 5; " +
	                "-fx-background-radius: 5; " +
	                "-fx-cursor: hand;"
	            ));
	        }

	        // פעולה לפי בחירת המשתמש
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.isPresent() && result.get() == yesButton) {
	            resetApplication(); // איפוס המשחק
	            try {
	                backgammonUI homeScreen = new backgammonUI();
	                homeScreen.start(stage); // חזרה למסך הבית
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        } else {
	            alert.close();
	        }
	    });
	    
	    return backToHomeBtn;
	}

	
	/**
	 * Initialize game components and sub-controllers.
	 */
	
	
	private void initGame() {
		game = new GameComponentsController(bottomPlayer, topPlayer);
        gameplay = new GameplayController(stage, this, game, infoPnl, bottomPlayer, topPlayer, null);
		cmd = new CommandController(stage, this, game, gameplay, infoPnl, bottomPlayer, topPlayer);
		gameplay.setCommandController(cmd);
		event = new EventController(stage, this, game, gameplay, cmd, infoPnl, rollDieBtn);
        gameplay.setEventController(event);
		cmd.setEventController(event);
		gameTimer = new MatchTimer();
		
		  // Create the Settings screen instance
	    settingsScreen = new SettingsScreen(cmd,0);
	    settingsOverlay = settingsScreen.getSettingsBox();
	    settingsOverlay.setVisible(false); // Initially hidden

	    // Wrap the root layout in a StackPane to support overlays
//	    Scene currentScene = stage.getScene();
//	    stackPane = new StackPane(currentScene.getRoot(), settingsOverlay);
//	    stage.setScene(new Scene(stackPane, currentScene.getWidth(), currentScene.getHeight()));

	 // Create the Settings Button
	    settingsButton = new Button("Settings");
	    settingsButton.setStyle(
	        "-fx-font-size: 14px; " +
	        "-fx-font-weight: bold; " +
	        "-fx-background-color: #d2a679; " +  // צבע רקע מקורי
	        "-fx-text-fill: white; " +           // טקסט בצבע לבן
	        "-fx-border-radius: 5; " +           // פינות מעוגלות
	        "-fx-background-radius: 5;" +        // רקע מעוגל
	        "-fx-cursor: hand;"                  // שינוי ה-CURSOR לכף יד
	    );

	    // אפקט שינוי צבע בעת ריחוף עם העכבר
	    settingsButton.setOnMouseEntered(e -> settingsButton.setStyle(
	        "-fx-font-size: 14px; " +
	        "-fx-font-weight: bold; " +
	        "-fx-background-color: #b58a5c; " +  // צבע כהה יותר
	        "-fx-text-fill: white; " +
	        "-fx-border-radius: 5; " +
	        "-fx-background-radius: 5;" +
	        "-fx-cursor: hand;"
	    ));

	    settingsButton.setOnMouseExited(e -> settingsButton.setStyle(
	        "-fx-font-size: 14px; " +
	        "-fx-font-weight: bold; " +
	        "-fx-background-color: #d2a679; " +  // חזרה לצבע המקורי
	        "-fx-text-fill: white; " +
	        "-fx-border-radius: 5; " +
	        "-fx-background-radius: 5;" +
	        "-fx-cursor: hand;"
	    ));

	    // פעולה שמתרחשת בלחיצה על הכפתור
	    settingsButton.setOnAction(e -> toggleSettingsOverlay());
	    
//	    stackPane.getChildren().add(settingsButton); // Add settings button to StackPane
//	    StackPane.setAlignment(settingsButton, Pos.TOP_RIGHT); // Position button in top-right corner
	    
		initLayout();
		initializeMusic();
		settingsScreen.updateMusicButton();
	}
	

	

//	private Button createSettingsButton() {
//        Button button = new Button("Settings");
//        button.setStyle(
//            "-fx-font-size: 14px; " +
//            "-fx-font-weight: bold; " +
//            "-fx-background-color: #d2a679; " +
//            "-fx-text-fill: black; " +
//            "-fx-border-radius: 5; " +
//            "-fx-background-radius: 5;"
//        );
//
//        settingsButton.setOnAction(e -> toggleSettingsOverlay());
//        return button;
//    }
	
	
//	private void toggleSettingsOverlay() {
//	    boolean isVisible = !settingsOverlay.isVisible();
//	    settingsOverlay.setVisible(isVisible);
//	}
	
	
//	 VBox settingsOverlay = createSettingsOverlay();
	 
	 private void toggleSettingsOverlay() {
	      
	        Scene currentScene = stage.getScene();
	        StackPane stackPane;

	        if (currentScene.getRoot() instanceof StackPane) {
	            stackPane = (StackPane) currentScene.getRoot();
	        } else {
	            stackPane = new StackPane(currentScene.getRoot());
	            stage.setScene(new Scene(stackPane, currentScene.getWidth(), currentScene.getHeight()));
	        }

	        if (stackPane.getChildren().contains(settingsOverlay)) {
	            settingsOverlay.setVisible(!settingsOverlay.isVisible());
	        } else {
	            stackPane.getChildren().add(settingsOverlay);
	        }
	        
	        StackPane.setMargin(settingsOverlay, new Insets(500, 500, 250, 250)); // Margin from top and right edges
	    }
	
	public void resetApplication() {
	//	cmdPnl.reset();
//		musicPlayer.reset();
		bottomPlayer.reset();
		topPlayer.reset();
		infoPnl.reset();
		resetGame();
		//game.resetTimers();
		
		isPlayerInfosEnteredFirstTime = true;
		isPromptCancel = false;
		hadCrawfordGame = false;
		isCrawfordGame = false;
		//Settings.setTotalGames(Settings.DEFAULT_TOTAL_GAMES);
	}
	
	public void resetGame() {
		bottomPlayer.setHasCube(false);
		topPlayer.setHasCube(false);
		game.reset();
		gameplay.reset();
		cmd.reset();
		event.reset();
	}
	
	/**
	 * Remove previous event listeners and start game.
	 * Called every /start.
	 */
	public void restartGame() {
		resetGame();
		startGame();
	}
	
	

	public void startGame() {
	    if (isPlayerInfosEnteredFirstTime) {
	    	
	        // הגדרת מספר משחקים ברירת מחדל (לדוגמה, 1)
	        Settings.setTotalGames(1);
	        game.getPlayerPanel(Settings.getTopPerspectiveColor()).updateTotalGames();
	        game.getPlayerPanel(Settings.getBottomPerspectiveColor()).updateTotalGames();

	        isPromptCancel = false;
	        isPlayerInfosEnteredFirstTime = false;
	    }

	    // הפעלת המשחק
	    if (!isPromptCancel) {
	        gameplay.start();
	    }
	    gameTimer.start();
	  //  DoublingCube doublingCube = new DoublingCube();
	 //   doublingCube.startTimer(); // מפעיל את הטיימר


	}
	
	/* public void startGame() {
	    if (isPlayerInfosEnteredFirstTime) {
	        // הגדרת מספר משחקים ברירת מחדל (לדוגמה, 1)
	        Settings.setTotalGames(1);
	        game.getPlayerPanel(Settings.getTopPerspectiveColor()).updateTotalGames();
	        game.getPlayerPanel(Settings.getBottomPerspectiveColor()).updateTotalGames();

	        isPromptCancel = false;
	        isPlayerInfosEnteredFirstTime = false;
	    }

	    // הפעלת המשחק
	    if (!isPromptCancel) {
	        gameplay.start();

	        // סימולציה של ניצחון שחקן תחתון לצורך הצגת מסך ההכרזה
	        bottomPlayer.setScore(Settings.TOTAL_GAMES_IN_A_MATCH); // סימון שהשחקן התחתון ניצח
	        handleMatchOver(false); // הצגת ההודעה על המנצח
	    }
	}*/


	// Checks if next game is crawford game.
	// is crawford game either winner match score, i.e. TOTAL_GAMES_IN_A_MATCH-1.
	private boolean checkIsCrawfordGame() {
		return topPlayer.getScore() == Settings.TOTAL_GAMES_IN_A_MATCH-1 || bottomPlayer.getScore() == Settings.TOTAL_GAMES_IN_A_MATCH-1;
	}
	
	/**
	 * Returns true if match over.
	 * Match over if player score greater or equal than total games.
	 * @return boolean value indicating if match is over.
	 */
	public boolean isMatchOver() {
		return (topPlayer.getScore() >= Settings.TOTAL_GAMES_IN_A_MATCH) || (bottomPlayer.getScore() >= Settings.TOTAL_GAMES_IN_A_MATCH);
	}
	
	/**
	 * If a player has a score equal to the maximum score,
	 * then announce the winner and ask if they want to play again.
	 */
	/*public void handleMatchOver(boolean isOutOfTime) {
		Player winner;
		if (isOutOfTime) winner = gameplay.getOpponent();
		else winner = gameplay.getCurrent();
		
		Dialogs<ButtonType> dialog = new Dialogs<ButtonType>("Congratulations, " + winner.getShortName() + " wins the match!", stage, "Play again");
		
		ScoreboardPrompt contents = new ScoreboardPrompt(topPlayer, bottomPlayer);
		
		dialog.getDialogPane().setContent(contents);
		
		// Run later because this method will be called from a running animation.
		// This animation resides in GameplayTimer.java.
		//
		// Animations are handled via events, showAndWait() below relies on
		// events too.
		//
		// So, calling showAndWait() from a running animations means there is a
		// nested event loop. This cannot happen, so the code below will have
		// to run later.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Optional<ButtonType> result = dialog.showAndWait();
				
				// Restart game if player wishes,
				// else exit gameplay mode and enter free-for-all mode.
				if (ButtonType.OK.equals(result.get())) {
					resetApplication();
					cmd.runCommand("/start");
				} else {
					resetApplication();
					infoPnl.print("Enter /start if you wish to play again.", MessageType.ANNOUNCEMENT);
					infoPnl.print("Enter /quit if you wish to quit.", MessageType.ANNOUNCEMENT);
				}
			}
		});
	}*/
	
	/*public void handleMatchOver(boolean isOutOfTime) {
	    Player winner;
	    String winnerMessage;
	    
	    if (isOutOfTime) {
	        winner = gameplay.getOpponent();
	        winnerMessage = "The game ended. The winner is: " + winner.getShortName();
	    } else {
	        winner = gameplay.getCurrent();
	        winnerMessage = "Congratulations, " + winner.getShortName() + " wins the match!";
	    }
	    
	    // יצירת דיאלוג מותאם אישית
	    Dialogs<ButtonType> dialog = new Dialogs<ButtonType>(winnerMessage, stage, "Play again");

	    // יצירת אובייקט Text מותאם אישית עם צבעים ואימוג'ים
	    Text winnerText = new Text(winnerMessage);
	    if (winner == bottomPlayer) {
	        winnerText.setStyle("-fx-fill: green; -fx-font-size: 18px; -fx-font-weight: bold;");
	        winnerText.setText(winnerText.getText() + " 🎉");
	    } else {
	        winnerText.setStyle("-fx-fill: blue; -fx-font-size: 18px; -fx-font-weight: bold;");
	        winnerText.setText(winnerText.getText() + " 🏆");
	    }
	 // יצירת התוכן המותאם אישית לדיאלוג
	    dialog.getDialogPane().setContent(winnerText);

	    // הרצת הדיאלוג
	    Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	            Optional<ButtonType> result = dialog.showAndWait();
	            
	            if (ButtonType.OK.equals(result.get())) {
	                resetApplication();
	                cmd.runCommand("/start");
	            } else {
	                resetApplication();
	                infoPnl.print("Enter /start if you wish to play again.", MessageType.ANNOUNCEMENT);
	                infoPnl.print("Enter /quit if you wish to quit.", MessageType.ANNOUNCEMENT);
	            }
	        }
	    });
	}*/
	
	
	public void handleMatchOver() {
	    gameTimer.stop();
	    System.out.println("Game over! Total time: " + gameTimer.getFormattedTime());

	    String winnerMessage;
	    winnerMessage = "🏆 Congratulations, " + GameplayController.getpCurrent().getShortName() + " wins the match! 🏆";

	    // יצירת דיאלוג מותאם אישית
	    Dialog dialog = new Dialog();
	    dialog.setTitle("Match Over");

	    // הגדרת שלב הדיאלוג
	    dialog.initOwner(stage);

	    // יצירת כפתור "Play Again"
	    Button playAgainButton = new Button("Play Again");
	    playAgainButton.setStyle(
	            "-fx-font-size: 24px; " +
	            "-fx-font-weight: bold; " +
	            "-fx-text-fill: linear-gradient(to bottom, #8B4513, #B8860B); " +
	            "-fx-background-color: #d2a679; " +
	            "-fx-border-color: #8B4513; " +
	            "-fx-border-width: 2px; " +
	            "-fx-border-radius: 15px; " +
	            "-fx-background-radius: 15px;" +
	            "-fx-cursor: hand;"
	    );

	    // הוספת אפקט hover לכפתור Play Again
	    playAgainButton.setOnMouseEntered(e -> playAgainButton.setStyle(
	            "-fx-font-size: 24px; " +
	            "-fx-font-weight: bold; " +
	            "-fx-text-fill: linear-gradient(to bottom, #8B4513, #B8860B); " +
	            "-fx-background-color: #e0b689; " + // צבע מודגש
	            "-fx-border-color: #8B4513; " +
	            "-fx-border-width: 2px; " +
	            "-fx-border-radius: 15px; " +
	            "-fx-background-radius: 15px;" +
	            "-fx-cursor: hand;"
	    ));
	    playAgainButton.setOnMouseExited(e -> playAgainButton.setStyle(
	            "-fx-font-size: 24px; " +
	            "-fx-font-weight: bold; " +
	            "-fx-text-fill: linear-gradient(to bottom, #8B4513, #B8860B); " +
	            "-fx-background-color: #d2a679; " +
	            "-fx-border-color: #8B4513; " +
	            "-fx-border-width: 2px; " +
	            "-fx-border-radius: 15px; " +
	            "-fx-background-radius: 15px;" +
	            "-fx-cursor: hand;"
	    ));

	    // יצירת כפתור "Home"
	    Button homeButton = new Button("Home");
	    homeButton.setStyle(
	            "-fx-font-size: 18px; " +
	            "-fx-font-weight: bold; " +
	            "-fx-text-fill: linear-gradient(to bottom, #8B4513, #B8860B); " +
	            "-fx-background-color: #d2a679; " +
	            "-fx-border-color: #8B4513; " +
	            "-fx-border-width: 2px; " +
	            "-fx-border-radius: 15px; " +
	            "-fx-background-radius: 15px;" +
	            "-fx-cursor: hand;"
	    );

	    // הוספת אפקט hover לכפתור Home
	    homeButton.setOnMouseEntered(e -> homeButton.setStyle(
	            "-fx-font-size: 18px; " +
	            "-fx-font-weight: bold; " +
	            "-fx-text-fill: linear-gradient(to bottom, #8B4513, #B8860B); " +
	            "-fx-background-color: #e0b689; " + // צבע מודגש
	            "-fx-border-color: #8B4513; " +
	            "-fx-border-width: 2px; " +
	            "-fx-border-radius: 15px; " +
	            "-fx-background-radius: 15px;" +
	            "-fx-cursor: hand;"
	    ));
	    homeButton.setOnMouseExited(e -> homeButton.setStyle(
	            "-fx-font-size: 18px; " +
	            "-fx-font-weight: bold; " +
	            "-fx-text-fill: linear-gradient(to bottom, #8B4513, #B8860B); " +
	            "-fx-background-color: #d2a679; " +
	            "-fx-border-color: #8B4513; " +
	            "-fx-border-width: 2px; " +
	            "-fx-border-radius: 15px; " +
	            "-fx-background-radius: 15px;" +
	            "-fx-cursor: hand;"
	    ));

	    // יצירת כיתוב גדול וברור להכרזה
	    Text winnerText = new Text(winnerMessage);
	    winnerText.setStyle(
	            "-fx-fill: linear-gradient(to bottom, #8B4513, #B8860B); " +
	            "-fx-font-size: 42px; " +
	            "-fx-font-weight: bold; " +
	            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0.3, 2, 2); " +
	            "-fx-text-alignment: center;"
	    );
	    winnerText.setWrappingWidth(800); // קביעת רוחב ההודעה כך שיתאים לרקע

	    // יצירת BorderPane עבור מיקום כפתורים
	    BorderPane mainPane = new BorderPane();
	    mainPane.setStyle("-fx-background-color: #FAEBD7; " +
	                      "-fx-border-color: #8B4513; " +
	                      "-fx-border-width: 8px; " +
	                      "-fx-border-radius: 25px; " +
	                      "-fx-padding: 30px;");

	    // מיקום כפתור "Home" בחלק העליון הימני
	    BorderPane.setAlignment(homeButton, Pos.TOP_RIGHT);
	    mainPane.setTop(homeButton);

	    // מיקום כיתוב המנצח במרכז
	    BorderPane.setAlignment(winnerText, Pos.CENTER);
	    mainPane.setCenter(winnerText);

	    // מיקום כפתור "Play Again" בתחתית
	    BorderPane.setAlignment(playAgainButton, Pos.BOTTOM_CENTER);
	    mainPane.setBottom(playAgainButton);

	    // הגדרת תוכן הדיאלוג
	    dialog.getDialogPane().setContent(mainPane);
	    dialog.getDialogPane().setPrefSize(850, 450);

	    // הסרת כפתורי ברירת מחדל
	    dialog.getDialogPane().getButtonTypes().clear();

	    // הצגת הדיאלוג
	    Platform.runLater(() -> {
	        // טיפול בלחיצה על כפתורים
	        playAgainButton.setOnAction(e -> {
	            dialog.hide(); // סגירת הדיאלוג
	            resetApplication(); // איפוס המשחק
	            cmd.runCommand("/start"); // התחלת משחק חדש
	        });

	        homeButton.setOnAction(e -> {
	            dialog.hide(); // סגירת הדיאלוג
	            resetApplication(); // איפוס נתוני המשחק
	            Platform.runLater(() -> {
	                try {
	                	 // סגירת המסך הנוכחי
	                    stage.close();
	                	
	                	view.backgammonUI openingScreen = new view.backgammonUI();
	                    openingScreen.start(stage); // מעבר למסך הפתיחה
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            });
	        });

	        dialog.showAndWait();
	    });
	}


	

	/**
	 * Inner class that stores results of promptStartGame() so we can process user input.
	 * 		- Player names + total games they play to
	 * NOTE: totalGames is a string here so we can validate player input before processing it
	 */
	private static class promptResults {
		String bName;
		String wName;
		String totalGames;
		
		promptResults(String bName, String wName, String totalGames) {
			this.bName = bName;
			this.wName = wName;
			this.totalGames = "1";
		}
	}
	// valid input if:
	// - is a number.
	// - positive.
	// - odd.
	private boolean isValidInput(String userInput) {
		boolean isValidInput = false;
		// is a number.
		if (isNumber(userInput)) {
			int num = Integer.parseInt(userInput);
			
			// positive and odd.
			if (num > 0 && num % 2 != 0 && num < 100) {
				isValidInput = true;
			}
		}
		return isValidInput;
	}
	
	/**
	 * Style MainController (i.e. root).
	 */
	public void style() {
		//setStyle("-fx-font-size: " + GameConstants.FONT_SIZE + "px; -fx-font-family: '" + GameConstants.FONT_FAMILY + "';");
	
		setBackground(GameConstants.getTableImage());
		setPadding(new Insets(10));
		setVgap(GameConstants.getUIVGap());
		setHgap(5);
		setAlignment(Pos.CENTER);
		setMaxSize(GameConstants.getScreenSize().getWidth(), GameConstants.getScreenSize().getHeight());
	}

	/**
	 * Manages the layout of the children, then adds them as the child of MainController (i.e. root).
	 */
	public void initLayout() {
		VBox terminal = new VBox();
		terminal.getChildren().addAll(infoPnl);
		terminal.setAlignment(Pos.CENTER);
		terminal.setEffect(new DropShadow(20, 0, 0, Color.BLACK));
		
		getChildren().clear();
		add(game, 0, 0, 1, 3);
		add(terminal, 1, 0);
		add(rollDieBtn, 1, 2);
		
		// הגדרת גדלים זהים לכפתורי Settings ו-Back
	    double buttonWidth = 80; // רוחב קבוע לכפתורים
	    double buttonHeight = 40; // גובה קבוע לכפתורים
	    settingsButton.setPrefSize(buttonWidth, buttonHeight);
	    backToHomeBtn.setPrefSize(buttonWidth, buttonHeight);

	    // יצירת HBox עבור הכפתורים Settings ו-Back
	    HBox buttonContainer = new HBox(10); // ריווח של 10 פיקסלים בין הכפתורים
	    buttonContainer.setAlignment(Pos.CENTER_RIGHT);
	    buttonContainer.getChildren().addAll(settingsButton, backToHomeBtn);

	    // הוספת ה-HBox לפריסה
	    add(buttonContainer, 1, 1); // מיקום ה-HBox בשורה 1, עמודה 1
	}
	
	/**
	 * DO NOT TOUCH THIS OR ADD THIS ANYWHERE ELSE,
	 * KEEP IN MIND THIS METHOD IS CALLED AFTER THE STAGE IS DONE SHOWING.
	 * ALTERNATIVE METHOD WHERE I DON'T HAVE TO DO THE ABOVE IS PREFERRED.
	 * 
	 * Binds shortcut CTRL+R key combination to the roll dice button.
	 */
	public void setRollDiceAccelarator() {
		Scene scene = rollDieBtn.getScene();
		if (scene == null) {
			throw new IllegalArgumentException("Roll Dice Button not attached to a scene.");
		}
		
		scene.getAccelerators().put(
			new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN),
			new Runnable() {
				@Override
				public void run() {
					rollDieBtn.fire();
				}
			}
		);
		
	}
	
	


	


	
	public boolean isCrawfordGame() {
		return isCrawfordGame;
	}
	
	private void initializeMusic() {
	    if (SettingsScreen.isMusicEnabled() && !MusicPlayer.getInstance().isPlaying()) {
	    	MusicPlayer.getInstance().play();
	    }

	}
	
	
}