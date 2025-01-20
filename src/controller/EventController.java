package controller;

import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Model.Bar;
import Model.DoublingCubeHome;
import view.GameComponentsController;
import Model.GameConstants;
import controller.GameplayController;
import view.Home;
import controller.MatchController;
import Model.Pip;
import Model.Settings;
import Model.TouchablesStorer;
import controller.ColorParser;
import controller.ColorPerspectiveParser;
import controller.CommandController;
import controller.InputValidator;
import controller.OutOfTimeHandler;
import controller.OutOfTimeSelectedEvent;
import controller.TouchablesStorerHandler;
import controller.TouchablesStorerSelectedEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.CommandPanel;
import view.InfoPanel;
import view.QuestionOverlay;
import view.RollDieButton;
import view.backgammonUI;

/**
 * This class handles all the events that is triggered by the user.
 * Sub-controller of MainController.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
public class EventController implements ColorParser, ColorPerspectiveParser, InputValidator {
	private Stage stage;
	private MatchController root;
	private GameComponentsController game;
	private GameplayController gameplay;
	private InfoPanel infoPnl;
	private RollDieButton rollDieBtn;
	//private CommandPanel cmdPnl;
	private CommandController cmd;
	private List<Stage> childStages = new ArrayList<Stage>(); // Track child stages

	public EventController(Stage stage, MatchController root, GameComponentsController game, GameplayController gameplay,
			 CommandController cmd, InfoPanel infoPnl, RollDieButton rollDieBtn) {
		this.stage = stage;
		this.root = root;
		this.game = game;
		this.gameplay = gameplay;
		//this.cmdPnl = cmdPnl;
		this.cmd = cmd;
		this.infoPnl = infoPnl;
		this.rollDieBtn = rollDieBtn;
		initGameListeners();
		initUIListeners();
	}

	private void initGameListeners() {
		// Exit selection mode when any part of the game board is clicked.
		game.setOnMouseClicked((MouseEvent event) -> {
			resetSelections();
		});
		
		initTouchableListeners();
		initTouchablesStorersListeners();
	}
	public void resetSelections() {
		// reshow the dices on the board,
		// if player selected cube in its box or player's home
		// but unselected it.
		if (gameplay.isStarted()) {
			if (!gameplay.isRolled() && !game.getBoard().isCubeInBoard()) {
				// if box or player's home clicked, 
				if (isCubeHomeSelectionMode || isHomeSelectionMode) {
					game.getBoard().redrawDices(gameplay.getCurrent().getColor());
				}
			}
		} else {
			if (!game.getBoard().isCubeInBoard()) game.getBoard().redrawDices();
		}
		
		game.unhighlightAll();
		isPipSelectionMode = false;
		isBarSelectionMode = false;
		isHomeSelectionMode = false;
		isCubeHomeSelectionMode = false;
		
		if (gameplay.isStarted()) {
			if (!gameplay.isRolled()) {
				if (gameplay.mustHighlightCube()) {
					game.highlightCube();
				}
			// highlight the possible moves if player hasn't move.
			} else if (!gameplay.isMoved()) {
				game.getBoard().highlightFromPipsAndFromBarChecker(gameplay.getValidMoves());
			}
		}
	}
	
	private void initTouchableListeners() {
		//root.addEventHandler(TouchableSelectedEvent.TOUCHABLE_SELECTED, touchableHandler);
	}
	
	/*
	TouchableHandler touchableHandler = new TouchableHandler() {
		@Override
		public void onClicked(Touchable object) {
			if (object instanceof DoublingCube) {
				infoPnl.print("Doubling Cube selected.", MessageType.DEBUG);

				// if cube at its box.
				if (!game.getOtherHome().getCubeHome().isEmpty()) {
					
				} else if (!game) {
					
				}
				
				if (gameplay.isStarted()) {
					//  
					if (!gameplay.isDoubling()) {
						// highlight cube box.
						game.getOtherHome().getCubeHome().highlight();
					}
					
					game.getOtherHome().getHome(gameplay.getCurrent().getColor()).highlight();
				} else {
					// highlight player's other homes.
					game.getOtherHome().getHome(Settings.getBottomPerspectiveColor()).highlight();
					game.getOtherHome().getHome(Settings.getTopPerspectiveColor()).highlight();
				}
				isCubeSelectionMode = true;
			}
		}
	};
	*/
	
	private void initTouchablesStorersListeners() {
		root.addEventHandler(TouchablesStorerSelectedEvent.STORER_SELECTED, touchablesStorerHandler);
	}

	/**
	 * Event handler for all checker storers (pips, bars, homes).
	 * Separated from initTouchablesStorersListeners() for easier removal.
	 */
	private boolean isPipSelectionMode = false;
	private boolean isBarSelectionMode = false;
	private boolean isHomeSelectionMode = false;
	private boolean isCubeHomeSelectionMode = false;
	private TouchablesStorer storerSelected;
	TouchablesStorerHandler touchablesStorerHandler = new TouchablesStorerHandler() {
		@Override
		public void onClicked(TouchablesStorer object) {
			// pip selected, basis for fromPip or toPip selection.
			if (object instanceof Pip) {
				// nothing selected, basis for fromPip selection.
				if (!isInSelectionMode()) {
					storerSelected = object;
					int fromPip = ((Pip) storerSelected).getPipNumber();
					// same as ((gameplay.isStarted() && gameplay.isValidFro(fromPip)) || (!gameplay.isStarted()))
					if (!gameplay.isStarted() || gameplay.getValidMoves().isValidFro(fromPip)) {
						gameplay.highlightPips(fromPip);
						isPipSelectionMode = true;
						infoPnl.print("Pip clicked is: " + gameplay.correct(fromPip) + ".", Model.MessageType.DEBUG);
					} else {
						if (!gameplay.isRolled()) infoPnl.print("You can only move after rolling.", Model.MessageType.ERROR);
						else infoPnl.print("You can only move from highlighted objects.", Model.MessageType.ERROR);
					}
				// either pip or bar selected, basis for toPip selection.
				} else if (isPipSelectionMode || isBarSelectionMode) {
					int toPip = ((Pip) object).getPipNumber();
					
					if (isPipSelectionMode) {
						int fromPip = ((Pip) storerSelected).getPipNumber();
						cmd.runCommand("/move " + fromPip + " " + toPip);
					} else if (isBarSelectionMode) {
						String fromBar = parseColor(((Bar) storerSelected).getColor());
						cmd.runCommand("/move " + fromBar + " " + toPip);
					}
					gameplay.unhighlightPips();
					isPipSelectionMode = false;
					isBarSelectionMode = false;
				}
			// bar selected, basis for fromBar selection.
			} else if (object instanceof Bar) {
				// prevent entering into both pip and bar selection mode.
				if (!isInSelectionMode()) {
					storerSelected = object;
					String fromBar = parseColor(((Bar) storerSelected).getColor());
					int fromBarPipNum = Settings.getPipBearOnBoundary(getPOV(parseColor(fromBar)));
					// same as ((gameplay.isStarted() && gameplay.isValidFro(fromPip)) || (!gameplay.isStarted()))
					if (!gameplay.isStarted() || gameplay.getValidMoves().isValidFro(fromBarPipNum)) {
						gameplay.highlightPips(fromBar);
						isBarSelectionMode = true;
						infoPnl.print("Bar clicked.", Model.MessageType.DEBUG);
					} else {
						if (!gameplay.isRolled()) infoPnl.print("You can only move after rolling.", Model.MessageType.ERROR);
						else infoPnl.print("You can only move from highlighted objects.", Model.MessageType.ERROR);
					}
				}
			// home selected, basis for fromHome or toHome selection.
			} else if (object instanceof Home) {
				// some storer selected, basis for toHome selection.
				if (isPipSelectionMode || isBarSelectionMode || isCubeHomeSelectionMode) {
					String toHome = parseColor(((Home) object).getColor());
					if (isPipSelectionMode) {
						int fromPip = ((Pip) storerSelected).getPipNumber();
						cmd.runCommand("/move " + fromPip + " " + toHome);
					} else if (isBarSelectionMode) {
						String fromBar = parseColor(((Bar) storerSelected).getColor());
						cmd.runCommand("/move " + fromBar + " " + toHome);
					} else if (isCubeHomeSelectionMode) {
						if (gameplay.isStarted()) {
							cmd.runCommand("/accept");
						} else {
							DoublingCubeHome fromCubeHome = (DoublingCubeHome) storerSelected;
							cmd.runCommand("/movecube " + parseColor(fromCubeHome.getColor()) + " " + toHome);
						}
					}
					gameplay.unhighlightPips();
					game.unhighlightAllPlayersCubeHomes();
					isPipSelectionMode = false;
					isBarSelectionMode = false;
					isCubeHomeSelectionMode = false;
				// home not selected, basis for fromHome selection.
				// used to select the doubling cube.
				} else {
					if (!isInSelectionMode()) {
						if (!gameplay.isStarted() || (!gameplay.isRolled() && !root.isCrawfordGame() && gameplay.mustHighlightCube())) {
							// fromHome consideration only if its a doubling cube.
							storerSelected = object;
							Home fromHome = (Home) storerSelected;
							if (fromHome.getTopCube() != null) {
								gameplay.highlightBoardCubeZones();
								isHomeSelectionMode = true;
							}
						}
					}
				}
			// doubling cube home selected, basis for (from box to Board) or (from Board to box/home) selection.
			} else if (object instanceof DoublingCubeHome) {
				// player's home selected, basis for toBoard selection. 
				if (isHomeSelectionMode) {
					DoublingCubeHome toCubeHome = (DoublingCubeHome) object;
					if (toCubeHome.isOnBoard()) {
						Home fromHome = (Home) storerSelected;
						if (gameplay.isStarted()) {
							cmd.runCommand("/double");
						} else {
							cmd.runCommand("/movecube " + parseColor(fromHome.getColor()) + " " + parseColor(toCubeHome.getColor()));
						}
					}
					game.getBoard().unhighlightAllCubeHome();
					isHomeSelectionMode = false;
				// no cube home selected, basis for fromCubeHome selection.
				} else if (!isInSelectionMode()) {
					if (!gameplay.isStarted() || (!root.isCrawfordGame() && gameplay.mustHighlightCube())) {
						storerSelected = object;
						DoublingCubeHome fromCubeHome = (DoublingCubeHome) storerSelected;
						
						// check if the doubling cube home has the doubling cube.
						if (!fromCubeHome.isEmpty()) {
							// cube home selected is on board.
							if (fromCubeHome.isOnBoard()) {
								gameplay.highlightOtherHomeCubeZones();
							// cube home selected is in its box.
							} else {
								// highlight board.
								gameplay.highlightBoardCubeZones();
							}
							isCubeHomeSelectionMode = true;
						}
					}
				// cube home selected, basis for toCubeHome selection.
				// i.e. cube box to board's cube home, or vice versa.
				} else if (isCubeHomeSelectionMode) {
					DoublingCubeHome fromCubeHome = (DoublingCubeHome) storerSelected;
					DoublingCubeHome toCubeHome = (DoublingCubeHome) object;

					// check if it clicked on the same cube home,
					// if so, we ignore the selection.
					//
					// NOTE: if we don't check this, the code below will execute
					// and the cube will be unhighlighted when it should be.
					if (!fromCubeHome.equals(toCubeHome)) {
						// cube box to board.
						if (!fromCubeHome.isOnBoard() && toCubeHome.isOnBoard()) {
							if (gameplay.isStarted()) {
								cmd.runCommand("/double");
							} else {
								cmd.runCommand("/movecube box " + parseColor(toCubeHome.getColor()));
							}
						// board to cube box.
						} else if (fromCubeHome.isOnBoard() && !toCubeHome.isOnBoard()) {
							if (gameplay.isStarted()) {
								cmd.runCommand("/decline");
							} else {
								cmd.runCommand("/movecube " + parseColor(fromCubeHome.getColor()) + " box");
							}
						}
						game.unhighlightAllPlayersCubeHomes();
						game.getBoard().unhighlightAllCubeHome();
						isCubeHomeSelectionMode = false;
					}
				}
			} else {
				infoPnl.print("Other instances of checkersStorer were clicked.", Model.MessageType.DEBUG);
			}
		}
	};
	public boolean isInSelectionMode() {
		return isPipSelectionMode || isBarSelectionMode || isCubeHomeSelectionMode || isHomeSelectionMode;
	}
	

	/**
	 * Manages all the UI (infoPnl, cmdPnl, rollDieBtn) listeners.
	 */
	private void initUIListeners() {
		//initCommandPanelListeners();
		initRollDieButtonListeners();

		if (!GameConstants.DEBUG_MODE)
			initStageListeners();
	}

	/**
	 * Manages command panel listeners.
	 * 		- if its a command (i.e. start with '/'), run it.
	 * 		- echoes player input to infoPanel.
	 * 		- does not echo empty strings/whitespace.
	 */
/*	private void initCommandPanelListeners() {
		cmdPnl.setOnAction((ActionEvent event) -> {
			String text = cmdPnl.getText();
			cmdPnl.addHistory(text);
			String[] args = text.split(" ");
			
			if (cmdPnl.isCommand(text)) {
				cmd.runCommand(cmdPnl.getText(), true);
			} else if (args.length == 2 && isPip(args[0]) && isPip(args[1])) {
				cmd.runCommand("/move " + text, true);
			} else if (gameplay.getGameplayMoves().isMapped() && gameplay.getGameplayMoves().isKey(text.toUpperCase().trim())) {
				cmd.runCommand(gameplay.getGameplayMoves().getMapping(text.toUpperCase().trim()));
			} else if (text.equals("double")) {
				cmd.runCommand("/double");
			} else if (text.equals("yes") && gameplay.isDoubling()) {
				cmd.runCommand("/accept");
			} else if (text.equals("no") && gameplay.isDoubling()) {
				cmd.runCommand("/decline");
			} else if (text.equals("start")) {
				cmd.runCommand("/start");
			} else if (text.equals("roll")) {
				cmd.runCommand("/roll");
			} else if (text.equals("next")) {
				cmd.runCommand("/next");
			} else if (text.equals("cheat")) {
				cmd.runCommand("/cheat");
			} else if (text.equals("save")) {
				cmd.runCommand("/save");
			} else if (text.equals("quit")) {
				cmd.runCommand("/quit");
			} else if (text.trim().isEmpty()) {
				// ignores if string empty or whitespace only.
			} else {
				infoPnl.print(text, MessageType.CHAT);
			}
			cmdPnl.setText("");
		});
	}*/

	private int dieState = 2;
	private void initRollDieButtonListeners() {
	    rollDieBtn.setOnAction((ActionEvent event) -> {
	    	
	    	if(backgammonUI.getChosenDiffficulty()!="Easy") {
	    		QuestionOverlay questionOverlay = new QuestionOverlay(null);
	    		questionOverlay.showAndWait();
	    	}

	   
	       

	        // Continue with the action logic after the popup is closed
	        if (dieState == 1) {
	            dieState = 2;
	        } else {
	            dieState = 1;
	        }
	        cmd.runCommand("/roll " + Integer.toString(dieState));
	    });
	}
	
	// Register child stages
	public void registerChildStage(Stage childStage) {
	    childStages.add(childStage);
	    childStage.setOnCloseRequest(event -> childStages.remove(childStage)); // Deregister when child closes
	}

	private void initStageListeners() {
	    // Check if player really wants to exit game, prevents accidental exits
	    stage.setOnCloseRequest((WindowEvent event) -> {
	        // Alert settings
	        Alert exitCheck = new Alert(Alert.AlertType.CONFIRMATION);
	        exitCheck.setHeaderText("Do you really want to exit Backgammon?");
	        exitCheck.initModality(Modality.APPLICATION_MODAL);
	        exitCheck.initOwner(stage);

	        infoPnl.print("Trying to quit game.");
	        cmd.runSaveCommand();

	        // Exit button
	        Button exitBtn = (Button) exitCheck.getDialogPane().lookupButton(ButtonType.OK);
	        exitBtn.setText("Exit");

	        // Exit application
	        Optional<ButtonType> closeResponse = exitCheck.showAndWait();
	        if (!ButtonType.OK.equals(closeResponse.get())) {
	            event.consume(); // Cancel close event
	        } else {
	            // Close all child stages
	            for (Stage childStage : childStages) {
	                if (childStage.isShowing()) {
	                    childStage.close();
	                }
	            }
	        }
	    });
	}

	public void reset() {
		dieState = 2;
		storerSelected = null;
		resetSelections();
	}
}
