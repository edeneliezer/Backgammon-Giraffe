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
	        "-fx-background-color: #d2a679; " +
	        "-fx-text-fill: black; " +
	        "-fx-border-radius: 5; " +
	        "-fx-background-radius: 5;"
	    );
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
		add(settingsButton, 1, 1);
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
	
	
	@SuppressWarnings("static-access")
//	private VBox createSettingsOverlay() {
//	        // Create the settings box (overlay container)
//	        VBox settingsBox = new VBox(20); // Add smaller spacing between elements
//	        settingsBox.setPadding(new Insets(20)); // Reduce padding
//	        settingsBox.setAlignment(Pos.TOP_CENTER);
//	        settingsBox.setStyle(
//	            "-fx-background-color: #fefaf4; " + // Match the beige background
//	            "-fx-border-color: brown; " +       // Dark brown border
//	            "-fx-border-width: 3; " +           // Border thickness
//	            "-fx-border-radius: 10; " +         // Rounded corners
//	            "-fx-background-radius: 10;"        // Rounded background
//	        );
//
//	        // Make the box smaller
//	        settingsBox.setPrefSize(150, 180); // Adjust width and height to make it smaller
//
//	        // Settings Title
//	        Label settingsTitle = new Label("SETTINGS");
//	        settingsTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14)); // Slightly smaller font
//	        settingsTitle.setTextFill(Color.BROWN);
//
//	        // Buttons for settings options with icons
//	        Button musicButton = createIconButton("Music", "music.png");
//	        musicButton.setOnAction(e -> {
//	        	ImageView icon;
//	            if (musicPlayer.isPlaying()) {
//	            	musicPlayer.pause();
//	            	icon = new ImageView(new Image("musicOff.png"));
//	                musicButton.setText("Unmute");
//	            } else {
//	            	 musicPlayer.play();
//	            	 icon = new ImageView(new Image("music.png"));
//	            	 musicButton.setText("Music");
//	            }
//	            // Set the size of the icon
//	            icon.setFitWidth(20); // Set the width
//	            icon.setFitHeight(20); // Set the height
//	            musicButton.setGraphic(icon);
//	        });
//	        
//	        Button soundEffectsButton = createIconButton("disable Sound", "sound.png");
////	        soundEffectsButton.setOnAction(e -> toggleSoundEffects(soundEffectsButton));
//	        soundEffectsButton.setOnAction(e -> {
//	        	ImageView pic;
//	        	 if (cmd.getSoundFXPlayer().isWorking()) {
//	        	     cmd.getSoundFXPlayer().disableEffects();
//	        	     soundEffectsButton.setText("enable Sound");
//	                 pic = new ImageView(new Image("mute.png"));
//	            } else {
//	            	cmd.getSoundFXPlayer().enableEffects();
//	                pic = new ImageView(new Image("sound.png"));
//	                soundEffectsButton.setText("disable sound");
//	            }
//	            // Set the size of the icon
//	            pic.setFitWidth(20); // Set the width
//	            pic.setFitHeight(20); // Set the height
//	            soundEffectsButton.setGraphic(pic);
//	        });
//	       
////	        Button historyButton = createIconButton("History", "history.png");
//	        Button infoButton = createIconButton("Information", "info.png");
//
//	        // Close Button
//	        Button closeButton = new Button("Close");
//	        closeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
//	        closeButton.setStyle(
//	            "-fx-background-color: #d11e1e; " +  // Red button background
//	            "-fx-text-fill: white; " +           // White text
//	            "-fx-border-radius: 5; " +           // Rounded border
//	            "-fx-background-radius: 5;"         // Rounded background
//	        );
//	        closeButton.setOnAction(e -> {
//	            settingsBox.setVisible(false); // Hide settings box
//	            StackPane stackPane = (StackPane) settingsBox.getParent();
//	            Node fadeBackground = stackPane.getChildren().get(1); // Access the fade rectangle
//	            fadeBackground.setVisible(false); // Hide fade background
//	        });
//	        
//	        // Add Action to the History Button
////	        historyButton.setOnAction(e -> {
////	            // Switch to History Screen
////	            Stage stage = (Stage) settingsBox.getScene().getWindow();
////	            HistoryScreen historyScreen = new HistoryScreen();
////	            historyScreen.start(stage); // Navigate to History Screen
////	        });
////	        
//	        // Add all elements to the settings box
//	        settingsBox.getChildren().addAll(settingsTitle, musicButton, soundEffectsButton, infoButton,closeButton);
//
//	        return settingsBox;
//	    }
//	
////	private void toggleSoundEffects(Button soundEffectsButton) {
////	    ImageView icon;
////	    if (soundEffectsPlayer.isEnabled()) {
////	        soundEffectsPlayer.disable();
////	        icon = new ImageView(new Image("mute.png"));
////	        soundEffectsButton.setText("Unmute");
////	    } else {
////	        soundEffectsPlayer.enable();
////	        icon = new ImageView(new Image("sound.png"));
////	        soundEffectsButton.setText("Sound Effects");
////	    }
////	    icon.setFitWidth(20);
////	    icon.setFitHeight(20);
////	    soundEffectsButton.setGraphic(icon);
////	}
//	  
//	  private Button createIconButton(String text, String iconUrl) {
//	        // Create an icon for the button
//	        ImageView icon = new ImageView(new Image(iconUrl));
//	        icon.setFitWidth(20); // Icon size
//	        icon.setFitHeight(20);
//
//	        // Create the button with the icon and text
//	        Button button = new Button(text, icon);
//	        button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
//	        button.setStyle(
//	            "-fx-background-color: #d2a679; " + // Light brown background
//	            "-fx-text-fill: black; " +          // Black text
//	            "-fx-border-color: brown; " +       // Dark brown border
//	            "-fx-border-width: 1.5; " +         // Thin border
//	            "-fx-background-radius: 10; " +    // Rounded background
//	            "-fx-border-radius: 10;"           // Rounded border
//	        );
//	        button.setContentDisplay(ContentDisplay.LEFT); // Icon on the left of text
//	        button.setPrefWidth(160); // Consistent button width
//	        return button;
//	    }

	


	
	public boolean isCrawfordGame() {
		return isCrawfordGame;
	}
	
	private void initializeMusic() {
	    if (SettingsScreen.isMusicEnabled() && !MusicPlayer.getInstance().isPlaying()) {
	    	MusicPlayer.getInstance().play();
	    }

	}
	
	
}