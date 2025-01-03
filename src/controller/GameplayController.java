package controller;

import java.util.Optional;

import Model.DieInstance;
import Model.DieResults;
import Model.DoublingCube;
import Model.GameEndScore;
import Model.GameModel;
import controller.GameplayMovesController;
import view.Home;
import controller.MatchController;
import Model.MessageType;
import Model.Moves;
import Model.Player;
import Model.Settings;
import controller.ColorParser;
import controller.ColorPerspectiveParser;
import controller.CommandController;
import controller.IndexOffset;
import controller.InputValidator;
import controller.IntegerLettersParser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.Dialogs;
import view.GameComponentsController;
import view.InfoPanel;
import view.PlayerPanel;
import view.ScoreboardPrompt;

/**
 * This class handles the gameplay of Backgammon.
 * Sub-controller of MainController.

 */
public class GameplayController implements ColorParser, ColorPerspectiveParser, InputValidator, IndexOffset, IntegerLettersParser {
	private boolean isStarted, isRolled, isMoved, isFirstRoll, isTopPlayer, isDoubling, isDoubled, isMaxDoubling, isInTransition;
	private Player bottomPlayer, topPlayer;
	private static Player pCurrent;
	private Player pOpponent;
	
	private Stage stage;
	private MatchController root;
	private CommandController cmd;
	private GameComponentsController game;
	private InfoPanel infoPnl;
	private GameplayMovesController gameplayMoves;
	
	public GameplayController(Stage stage, MatchController root, GameComponentsController game, InfoPanel infoPnl, Player bottomPlayer, Player topPlayer) {
		this.bottomPlayer = bottomPlayer;
		this.topPlayer = topPlayer;
		this.stage = stage;
		this.root = root;
		this.game = game;
		this.infoPnl = infoPnl;
		gameplayMoves = new GameplayMovesController(game, this, infoPnl);
		reset();
	}
	
	public void reset() {
		isStarted = false;
		isRolled = false;
		isMoved = false;
		isFirstRoll = true;
		isTopPlayer = false;
		isDoubling = false;
		isDoubled = false;
		isMaxDoubling = false;
		isInTransition = false;
		if (nextPause != null) nextPause.stop();
		gameplayMoves.reset();
		//stopCurrentPlayerTimer();
	}
	
	public void setCommandController(CommandController cmd) {
		this.cmd = cmd;
		gameplayMoves.setCommandController(cmd);
	}
	
	/**
	 * Start the game by determining the first player with a dice roll panel.
	 * Called at /start.
	 */
	public void start() {
	    isStarted = true;

	    // Create a stage for the dice roll panel
	    Stage diceStage = new Stage();
	    VBox layout = new VBox(20);
	    layout.setAlignment(Pos.CENTER);

	    javafx.scene.control.Label instructionLabel = new javafx.scene.control.Label("Roll the dice to determine the first player!");
	    instructionLabel.setFont(javafx.scene.text.Font.font("Verdana", 16));

	    // Dice views
	    ImageView bottomPlayerDice = new ImageView();
	    bottomPlayerDice.setFitWidth(100);
	    bottomPlayerDice.setFitHeight(100);

	    ImageView topPlayerDice = new ImageView();
	    topPlayerDice.setFitWidth(100);
	    topPlayerDice.setFitHeight(100);

	    // Confirm button to close the diceStage
	    javafx.scene.control.Button confirmButton = new javafx.scene.control.Button("OK");
	    confirmButton.setFont(javafx.scene.text.Font.font("Verdana", 14));
	    confirmButton.setDisable(true); // Initially disabled until dice are rolled
	    confirmButton.setOnAction(e -> {
	        diceStage.close();

	        // Proceed with the game
	        handleNecessitiesOfEachTurn();

	        // Ensure the winner's turn is properly set
	        isFirstRoll = false; // Indicates the first player is already set
	        isRolled = false;    // Allow the winner to roll their actual dice
	    });

	    // Roll dice button
	    javafx.scene.control.Button rollButton = new javafx.scene.control.Button("Roll Dice");
	    rollButton.setFont(javafx.scene.text.Font.font("Verdana", 14));
	    rollButton.setOnAction(e -> {
	        // Roll dice for both players
	        int bottomPlayerRoll = rollDice(bottomPlayerDice);
	        int topPlayerRoll = rollDice(topPlayerDice);

	        // Determine the first player
	        if (bottomPlayerRoll > topPlayerRoll) {
	            setpCurrent(bottomPlayer);
	            pOpponent = topPlayer;
	            instructionLabel.setText(bottomPlayer.getName() + " goes first with a roll of " + bottomPlayerRoll + "!");
	            rollButton.setDisable(true);
	        } else if (topPlayerRoll > bottomPlayerRoll) {
	            setpCurrent(topPlayer);
	            pOpponent = bottomPlayer;
	            instructionLabel.setText(topPlayer.getName() + " goes first with a roll of " + topPlayerRoll + "!");
	            rollButton.setDisable(true);
	        } else {
	            // Handle tie by prompting to roll again
	            instructionLabel.setText("It's a tie! Roll again.");
	            return;
	        }

	        // Enable the confirm button after rolling
	        confirmButton.setDisable(false);
	    });

	    layout.getChildren().addAll(instructionLabel, bottomPlayerDice, topPlayerDice, rollButton, confirmButton);

	    Scene scene = new Scene(layout, 400, 400);
	    diceStage.setScene(scene);
	    diceStage.setTitle("Dice Roll");
	    diceStage.show();
	}

	/**
	 * Roll a dice and update the dice face image.
	 * @param diceView The ImageView to display the rolled dice face.
	 * @return The rolled value.
	 */
	private int rollDice(ImageView diceView) {
	    java.util.Random random = new java.util.Random();
	    int rollResult = random.nextInt(6) + 1; // Roll a dice (1-6)
	    
	    // Correct resource path relative to 'resources' directory
	    String diceImagePath = "/game/img/dices/black/" + rollResult + ".png";
	    
	    // Load the image from resources
	    try {
	        diceView.setImage(new Image(getClass().getResourceAsStream(diceImagePath)));
	    } catch (NullPointerException e) {
	        System.err.println("Image not found: " + diceImagePath);
	        diceView.setImage(null); // Handle missing image gracefully
	    }
	    return rollResult;
	}
	/**
	 * Rolls die, calculates possible moves and highlight top checkers.
	 * Called at /roll.
	 */
	public void roll() {
		// start() calls this method(),
		// we only get the first player once.
		DieResults rollResult;
		if (isFirstRoll) {
			rollResult = game.getBoard().rollDices(DieInstance.SINGLE);
			setpCurrent(getFirstPlayerToRoll(rollResult));
			pOpponent = getSecondPlayerToRoll(getpCurrent());
			infoPnl.print("First player to move is: " + getpCurrent().getName() + ".");
			isFirstRoll = false;
			handleNecessitiesOfEachTurn();	// highlight the current player's checker in his player panel.
			
			// if first player is top player, then we swap the pip number labels.
			if (getpCurrent().equals(topPlayer)) {
				game.getBoard().swapPipLabels();
				isTopPlayer = true;
			}
		} else {
			rollResult = game.getBoard().rollDices(getpCurrent().getPOV());
		}
		
		infoPnl.print("Roll dice result: " + rollResult + ".");
		isRolled = true;
		
		// calculate possible moves.
		setValidMoves(game.getBoard().calculateMoves(rollResult, getpCurrent()));
		gameplayMoves.handleEndOfMovesCalculation(getValidMoves());
	}
	
	/**
	 * Returns first player to roll based on roll die result.
	 * @param rollResult roll die result.
	 * @return first player to roll.
	 */
	private Player getFirstPlayerToRoll(DieResults rollResult) {
		int bottomPlayerRoll = rollResult.getLast().getDiceResult();
		int topPlayerRoll = rollResult.getFirst().getDiceResult();

		if (bottomPlayerRoll > topPlayerRoll) {
			return bottomPlayer;
		} else if (topPlayerRoll > bottomPlayerRoll) {
			return topPlayer;
		}
		return null;
	}
	
	/**
	 * Returns the second player to roll based on first player.
	 * i.e. its one or the other.
	 * @param firstPlayer first player to roll.
	 * @return second player to roll.
	 */
	private Player getSecondPlayerToRoll(Player firstPlayer) {
		if (firstPlayer.equals(topPlayer)) {
			return bottomPlayer;
		} else {
			return topPlayer;
		}
	}
	
	/**
	 * Called at /move.
	 */
	public void move() {
		gameplayMoves.setStalemateCount(0);
		
		if (isGameOver()) {
			handleGameOver();
		// else, proceed to gameplay.
		} else {
			updateMovesAfterMoving();
			
			boolean moveMadeCausedPlayerAbleBearOff = !getValidMoves().isEmpty() && game.getBoard().isAllCheckersInHomeBoard(getpCurrent());
			if (moveMadeCausedPlayerAbleBearOff || getValidMoves().hasDiceResultsLeft()) {
				recalculateMoves();
			} else if (getValidMoves().isEmpty()) {
				isMoved = true;
				infoPnl.print("Move over.");
				next();
			} else {
				gameplayMoves.handleCharacterMapping();
				gameplayMoves.printMoves();
			}
		}
	}
	
	private void updateMovesAfterMoving() {
		game.getBoard().updateIsHit(getValidMoves(), getpCurrent());
	}
	
	public void recalculateMoves() {
		if (isRolled()) {
			infoPnl.print("Recalculating moves.", MessageType.DEBUG);
			setValidMoves(game.getBoard().recalculateMoves(getValidMoves(), getpCurrent()));
			gameplayMoves.handleEndOfMovesCalculation(getValidMoves());
		}
	}
	
	/**
	 * Starts the timer for the respective player's turn.
	 * If the safe timer runs out (15 secs),
	 * it will start decrementing the player's individual timer per sec.
	 */
/*	private void startCurrentPlayerTimer() {
		if (getpCurrent() != null) game.getPlayerPanel(getpCurrent().getColor()).getTimer().start();
	}*/
	
	/**
	 * Stops the timer for the respective player's turn.
	 * If timer is stopped within the safe timer's limit, then nothing is decremented,
	 * else, update the player's individual timer
	 */
/*	public void stopCurrentPlayerTimer() {
		if (getpCurrent() != null) game.getPlayerPanel(getpCurrent().getColor()).getTimer().stop();
	}*/
	
	/**
	 * Swap players and pip number labels, used to change turns.
	 * Called at /next.
	 * @return the next player to roll.
	 */
	private Timeline nextPause;
	public Player next() {
		// this needs to be set first,
		// if not during wait, players can /next more than once.
		isRolled = false;
		isMoved = false;
		//stopCurrentPlayerTimer();
		
		infoPnl.print("Swapping turns...", MessageType.ANNOUNCEMENT);
		
		// pause for 2 seconds before "next-ing".
		if (Settings.ENABLE_NEXT_PAUSE) {
			nextPause = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
				isInTransition = false;
				nextFunction();
			}));
			nextPause.setCycleCount(1);
			nextPause.play();
			isInTransition = true;
		} else nextFunction();
		return getpCurrent();
	}
	public void nextFunction() {
		//if (isDoubling()) stopCurrentPlayerTimer();
		
		infoPnl.print("It is now " + pOpponent.getName() + "'s (" + parseColor(pOpponent.getColor()) + ") move.");
		swapPlayers();
		game.getBoard().swapPipLabels();
		
		handleNecessitiesOfEachTurn();
		
		// if doubling cube can be highlighted,
		// then player can choose to roll or play double.
		if (mustHighlightCube()) {
			game.highlightCube();
			infoPnl.print("You may now roll the dice or play the double.");
		} else {
			if (Settings.ENABLE_AUTO_ROLL) {
				infoPnl.print("Cannot play double, auto rolling...");
				roll();
			} else infoPnl.print("You can only roll the dice.");
		}
	}
	private void swapPlayers() {
		Player temp = getpCurrent();
		setpCurrent(pOpponent);
		pOpponent = temp;
		if (getpCurrent().equals(topPlayer)) {
			isTopPlayer = true;
		} else {
			isTopPlayer = false;
		}
	}
	
	private void handleNecessitiesOfEachTurn() {
		//startCurrentPlayerTimer();
		// highlight the current player's checker in his player panel,
		// and unhighlight opponent's.
		game.getPlayerPanel(getpCurrent().getColor()).highlightChecker();
		game.getPlayerPanel(pOpponent.getColor()).unhighlightChecker();
	}
	
	public boolean mustHighlightCube() {
		boolean mustHighlightCube = false;
		if (!root.isCrawfordGame() && !isInTransition() && !isMaxDoubling() || isDoubling()) {
			// if cube in player's home,
			// then highlight only when it is that player's turn.
			if (game.isCubeInHome() && !getpCurrent().hasCube()) {
				mustHighlightCube = false;
			} else {
				mustHighlightCube = true;
			}
			
			// dont highlight cube if player's score
			// is already capped with current stakes.
			//
			// highlight only if doubling stakes hasn't been proposed.
			if (isCurrentPlayerScoreCapped() && !isDoubling()) {
				mustHighlightCube = false;
				infoPnl.print("Cube not highlighted, player's score is capped.", MessageType.DEBUG);
			}
		}
		return mustHighlightCube;
	}
	
	// checks if current player's score added with current cube multiplier
	// causes player to reach total matches,
	// i.e. player wins the match if player wins this game.
	// i.e. currentPlayer.score + 1*cubeMultiplier >= totalGames.
	public boolean isCurrentPlayerScoreCapped() {
		return getpCurrent().getScore() + game.getCube().getEndGameMultiplier() >= Settings.TOTAL_GAMES_IN_A_MATCH;
	}
	
	/**
	 * Highlight pips and checkers based on mode.
	 * Used by EventController.
	 * @param fromPip
	 */
	public void highlightPips(int fromPip) {
		// gameplay mode.
		if (isRolled()) {
			game.getBoard().highlightToPipsAndToHome(getValidMoves(), fromPip);
		// free for all mode, i.e. before /start.
		} else {
			game.getBoard().highlightAllPipsExcept(fromPip);
		}
	}
	public void highlightPips(String fromBar) {
		// gameplay mode.
		if (isRolled()) {
			game.getBoard().highlightToPipsAndToHome(getValidMoves(), fromBar);
		// free for all mode, i.e. before /start.
		} else {
			game.getBoard().highlightAllPipsExcept(-1);
		}
	}
	
	/**
	 * Unhighlight pips based on mode.
	 * Used by EventController.
	 */
	public void unhighlightPips() {
		// gameplay mode.
		if (isStarted()) {
			if (isMoved()) game.unhighlightAll();
			else game.getBoard().highlightFromPipsAndFromBarChecker(getValidMoves());
		// free for all mode, i.e. before /start.
		} else {
			game.unhighlightAll();
		}
	}
	
	public void highlightOtherHomeCubeZones() {
		if (isStarted()) {
			game.highlightCubeZones(getpCurrent().getColor());
		} else {
			game.highlightAllPlayersCubeHomes();
		}
	}
	
	public void highlightBoardCubeZones() {
		if (isStarted()) {
			game.getBoard().highlightCubeHome(getpCurrent().getColor());
		} else {
			game.getBoard().highlightAllCubeHome();
		}
	}
	
	public void doubling() {
		if (isDoubling()) {
			isDoubling = false;
			isDoubled = true;
		} else {
			isDoubling = true;
			isDoubled = false;
		}
	}
	
	/**
	 * If either player's score is not equal to the max score per match,
	 * announces game over on infoPnl and dialog prompt,
	 * then ask if player wants another game.
	 * 
	 * Else announce the winner and ask if the players want to play another match.
	 */
	public void handleGameOver(boolean isIntermediate) {
		// Output to infoPnl.
		infoPnl.print("Game over.", MessageType.ANNOUNCEMENT);
		
		//stopCurrentPlayerTimer();
		if (isIntermediate) swapPlayers();
		handleGameOverScore(isIntermediate);
		
		if (root.isMatchOver())
			root.handleMatchOver();
		else {
			int remainingScore = Settings.TOTAL_GAMES_IN_A_MATCH - getpCurrent().getScore();
			String playerResult = getpCurrent().getScore() + " down, " + remainingScore + " to go.";
			
			// Create dialog prompt.
			Dialogs<ButtonType> dialog = new Dialogs<ButtonType>("Winner Winner Chicken Dinner! " + getpCurrent().getShortName() + " wins! " + playerResult, stage, "Next game");
			
			// Add score board to dialog prompt
			ScoreboardPrompt contents = new ScoreboardPrompt(topPlayer, bottomPlayer);
			dialog.getDialogPane().setContent(contents);
			
			// Auto save game log.
			infoPnl.saveToFile();
			
			// Output to dialog prompt.
			Optional<ButtonType> result = dialog.showAndWait();
			
			// Restart game if player wishes,
			// else exit gameplay mode and enter free-for-all mode.
			reset();
			if (result.get().equals(dialog.getButton())) {
				cmd.runCommand("/start");
			} else {
				infoPnl.print("Enter /start if you wish to resume the game.", MessageType.ANNOUNCEMENT);
				infoPnl.print("Enter /quit if you wish to quit.", MessageType.ANNOUNCEMENT);
			}
		}
	}
	public void handleGameOver() {
		handleGameOver(false);
	}
	
	private void handleGameOverScore(boolean isIntermediate) {
		Player winner = getpCurrent();
		String chosenDifficulty = view.backgammonUI.getChosenDiffficulty();
	    GameModel.saveGameInfo(bottomPlayer.getName(), topPlayer.getName(), chosenDifficulty, winner.getName());
	    GameModel.printGameInfo();
        // Print all saved games to verify
		if (isIntermediate) {
			// round end, allocate points as required.
			PlayerPanel winnerPnl = game.getPlayerPanel(winner.getColor());
			winnerPnl.setPlayerScore(winner, getIntermediateScore());
		} else {
			Home filledHome = game.getMainHome().getFilledHome();
			if (filledHome.getColor().equals(winner.getColor())) {
				PlayerPanel winnerPnl = game.getPlayerPanel(winner.getColor());
				winnerPnl.setPlayerScore(winner, getGameOverScore());
			} else {
				infoPnl.print("[ERROR] FilledHome is not the expected winner's (i.e. pCurrent).", MessageType.DEBUG);
			}
		}
		// facial expressions.
		game.getEmojiOfPlayer(getpCurrent().getColor()).setWinFace();
		game.getEmojiOfPlayer(pOpponent.getColor()).setLoseFace();
		infoPnl.print("Congratulations, " + winner.getName() + " won.");
		infoPnl.print(topPlayer.getName() + ": " + getScoreFormat(topPlayer.getScore()) + " vs " + bottomPlayer.getName() + ": " + getScoreFormat(bottomPlayer.getScore()));
	}
	private String getScoreFormat(int score) {
		return score + "/" + Settings.TOTAL_GAMES_IN_A_MATCH;
	}
	
	/**
	 * Check if any homes are filled.
	 * Game over when one of the player has all 15 checkers at their home.
	 * @return boolean value indicating if game is over.
	 */
	private boolean isGameOver() {
		return game.getMainHome().getFilledHome() != null;
	}
	
	// score if a player wins, i.e. 15 checkers in their home.
	public int getGameOverScore() {
		int score;
		// since current player is the one that made the winning move,
		// the opponent the loser.
		Player winner = getpCurrent();
		Player loser = pOpponent;
		DoublingCube cube = game.getCube();
		score = winner.getScore() + game.getBoard().getGameScore(loser.getColor())*cube.getEndGameMultiplier();
		return score;
	}
	
	// score if a player rejects/declines a doubling of stakes.
	public int getIntermediateScore() {
		int score;
		// current player must be the proposer, hence the winner.
		// so turns must be swapped to get back to the proposer.
		Player winner = getpCurrent();
		DoublingCube cube = game.getCube();
		score = winner.getScore() + GameEndScore.SINGLE.ordinal()*cube.getIntermediateGameMultiplier();
		return score;
	}
	
	public String correct(int pipNum) {
		return gameplayMoves.correct(pipNum);
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	public boolean isRolled() {
		return isRolled;
	}
	public boolean isMoved() {
		return isMoved;
	}
	public boolean isDoubling() {
		return isDoubling;
	}
	public boolean isDoubled() {
		return isDoubled;
	}
	public boolean isMaxDoubling() {
		return isMaxDoubling;
	}
	public boolean isTopPlayer() {
		return isTopPlayer;
	}
	public boolean isInTransition() {
		return isInTransition;
	}
	public GameplayMovesController getGameplayMoves() {
		return gameplayMoves;
	}
	public Moves getValidMoves() {
		return gameplayMoves.getValidMoves();
	}
	public Player getCurrent() {
		return getpCurrent();
	}
	public Player getOpponent() {
		return pOpponent;
	}
	public void setValidMoves(Moves moves) {
		gameplayMoves.setValidMoves(moves);
	}
	public void setIsMaxDoubling(boolean isMaxDoubling) {
		this.isMaxDoubling = isMaxDoubling;
	}

	public static Player getpCurrent() {
		return pCurrent;
	}

	public void setpCurrent(Player pCurrent) {
		this.pCurrent = pCurrent;
	}
}
