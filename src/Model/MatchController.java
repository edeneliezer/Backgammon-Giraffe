package Model;

import java.util.Optional;

import controller.ColorPerspectiveParser;
import controller.InputValidator;
import controller.MusicPlayer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.CommandPanel;
import view.Dialogs;
import view.InfoPanel;
import view.RollDieButton;
import view.ScoreboardPrompt;
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
	private CommandPanel cmdPnl;
	private CommandController cmd;
	private GameplayController gameplay;
	private EventController event;
	private MusicPlayer musicPlayer;
	private Stage stage;
	private boolean isPlayerInfosEnteredFirstTime, isPromptCancel, hadCrawfordGame, isCrawfordGame;
	
	/**
	 * Default Constructor
	 * 		- Initialize all the components (game, commandPanel, InfoPanel, RollDieButton, etc).
	 * 		- Initialize sub-controllers (Command, Gameplay, GameComponents, Event).
	 * 		- Initialize the layout of the components.
	 * 		- Style the application.
	 */
	public MatchController(Stage stage) {
		super();
		this.stage = stage;
		initApplication();
		initGame();
		style();
	}
	
	/**
	 * Initialize players and UI components.
	 */
	private void initApplication() {
		bottomPlayer = new Player(PlayerPerspectiveFrom.BOTTOM);
		topPlayer = new Player(PlayerPerspectiveFrom.TOP);
		infoPnl = new InfoPanel();
		rollDieBtn = new RollDieButton();
		cmdPnl = new CommandPanel();
		musicPlayer = new MusicPlayer();
		isPlayerInfosEnteredFirstTime = true;
		isPromptCancel = false;
	}
	
	
	/**
	 * Initialize game components and sub-controllers.
	 */
	private void initGame() {
		game = new GameComponentsController(bottomPlayer, topPlayer);
		gameplay = new GameplayController(stage, this, game, infoPnl, bottomPlayer, topPlayer);
		cmd = new CommandController(stage, this, game, gameplay, infoPnl, cmdPnl, bottomPlayer, topPlayer, musicPlayer);
		gameplay.setCommandController(cmd);
		event = new EventController(stage, this, game, gameplay, cmdPnl, cmd, infoPnl, rollDieBtn);
		cmd.setEventController(event);
		initLayout();
	}
	
	public void resetApplication() {
		cmdPnl.reset();
		musicPlayer.reset();
		bottomPlayer.reset();
		topPlayer.reset();
		infoPnl.reset();
		resetGame();
		game.resetTimers();
		
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
	    String winnerMessage;
	        winnerMessage = "🏆 Congratulations, " + GameplayController.getpCurrent().getShortName() + " wins the match! 🏆";

	    // יצירת דיאלוג מותאם אישית
	    Dialogs<ButtonType> dialog = new Dialogs<>("Match Over", stage, "Match Options");

	    // הוספת כפתורים Play Again ו-Home
	    ButtonType playAgainButton = new ButtonType("Play Again");
	    ButtonType homeButton = new ButtonType("Home");

	    // הוספת הכפתורים לדיאלוג
	    dialog.getDialogPane().getButtonTypes().setAll(playAgainButton, homeButton);

	    // יצירת כיתוב גדול וברור להכרזה
	    Text winnerText = new Text(winnerMessage);
	    winnerText.setStyle("-fx-fill: linear-gradient(to bottom, #8B4513, #B8860B); " +
	                        "-fx-font-size: 42px; " +
	                        "-fx-font-weight: bold; " +
	                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0.3, 2, 2); " +
	                        "-fx-text-alignment: center;");
	    winnerText.setWrappingWidth(600);

	    // יצירת אזור עיצוב נוסף
	    VBox contentBox = new VBox();
	    contentBox.setAlignment(Pos.CENTER);
	    contentBox.setSpacing(30);
	    contentBox.setStyle("-fx-background-color: #FAEBD7; " +
	                        "-fx-border-color: #8B4513; " +
	                        "-fx-border-width: 8px; " +
	                        "-fx-border-radius: 25px; " +
	                        "-fx-padding: 30px;");
	    contentBox.getChildren().add(winnerText);

	    // הוספת תוכן לדיאלוג
	    dialog.getDialogPane().setContent(contentBox);

	    // הגדלת גודל החלון
	    dialog.getDialogPane().setPrefSize(850, 450);

	    // הצגת הדיאלוג
	    Platform.runLater(() -> {
	        Optional<ButtonType> result = dialog.showAndWait();

	        if (result.isPresent()) {
	            if (result.get().equals(playAgainButton)) {
	                // סגירת הדיאלוג לפני התחלת המשחק
	                dialog.hide(); // סגירת הדיאלוג
	                resetApplication(); // איפוס המשחק
	                cmd.runCommand("/start"); // התחלת משחק חדש
	            } else if (result.get().equals(homeButton)) {
	                // סגירת הדיאלוג וחזרה למסך הפתיחה
	                dialog.hide(); // סגירת הדיאלוג
	                resetApplication(); // איפוס נתוני המשחק
	                Platform.runLater(() -> {
	                    try {
	                        view.backgammonUI openingScreen = new view.backgammonUI();
	                        openingScreen.start(stage); // מעבר למסך הפתיחה
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                });
	            }
	        }
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
		terminal.getChildren().addAll(infoPnl, cmdPnl);
		terminal.setAlignment(Pos.CENTER);
		terminal.setEffect(new DropShadow(20, 0, 0, Color.BLACK));
		
		getChildren().clear();
		add(game, 0, 0, 1, 3);
		add(terminal, 1, 0);
		add(rollDieBtn, 1, 2);
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
	
	
}
