import java.awt.BorderLayout;
import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class UI {
    // UI is the top level interface to the user interface
	
	//Added new prompts to the player
	//Also add getScoreLimit function to return an int for score limit or error otherwise

    private static final int FRAME_WIDTH = 1100;
    private static final int FRAME_HEIGHT = 600;

    private final BoardPanel boardPanel;
    private final InfoPanel infoPanel;
    private final CommandPanel commandPanel;

    UI (Board board, Players players) {
        infoPanel = new InfoPanel();
        commandPanel = new CommandPanel();
        JFrame frame = new JFrame();
        boardPanel = new BoardPanel(board,players);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setTitle("Backgammon");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(boardPanel, BorderLayout.LINE_START);
        frame.add(infoPanel, BorderLayout.LINE_END);
        frame.add(commandPanel, BorderLayout.PAGE_END);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public String getString() {
        return commandPanel.getString();
    }
    
    
    //returns a score limit for the game or 0 if 'quit'
    public int getScoreLimit() {
    	int score = 0;
    	boolean valid = false;
    	do {
            String scoreString = commandPanel.getString();
            if(scoreString.equals("quit")){
            	return 0;
            }
           
            else if (!isNumber(scoreString)) {
                displayString("Error: Please enter a number to play to");
            }
            else
            {
            	score = Integer.parseInt(scoreString);
            	valid = true;
            }
        } while (!valid);
        return score;
    }
    
    //Ensure value entered is a number
    public boolean isNumber(String str) { 
    	  try {  
    	    Integer.parseInt(str);  
    	    return true;
    	  } catch(NumberFormatException e){  
    	    return false;  
    	  }
    	  
    	}

    public void display() {
        boardPanel.refresh();
    }

    public void displayString(String string) {
        infoPanel.addText(string);
    }

    public void displayStartOfGame() {
        displayString("Welcome to Backgammon");
    }

    public void promptPlayerName() {
        displayString("Enter a player name:");
    }
    
    public void promptScoreLimit() {
        displayString("Enter a score you wish to play to: ");
    }
    
    public void promptDoubleOption(Player player) {
        displayString(player + " (" + player.getColorName() + ") enter 'yes' to double the current value of the game\nOr 'no' to concede this game");
    }
    
    public void promptNextMatch() {
    	displayString("Please enter 'yes' to play another match or 'no' to quit ");
    }

    public void displayPlayerColor(Player player) {
        displayString(player + " uses " + player.getColorName() + " checkers.");
    }

    public void displayRoll(Player player) {
        displayString(player + " (" + player.getColorName() + ") rolls " + player.getDice());
    }

    public void displayDiceEqual() {
        displayString("Equal. Roll again");
    }

    public void displayDiceWinner(Player player) {
        displayString(player + " wins the roll and goes first.");
    }

    public void displayGameWinner(Player player) {
        displayString(player + " WINS THIS GAME!!!");
    }
    
    public void displayMatchWinner(Player player) {
        displayString(player + " WINS THIS MATCH!!!");
    }
    
    public void displayScore(Player player1 ,Player player2) {
    	displayString("Match score: " + player1.getColorName() + ": " + player1.getScore() + " " + player2.getColorName() + ": " + player2.getScore());
    }

    public void promptCommand(Player player) {
        displayString(player + " (" + player.getColorName() + ") enter your move or quit:");
    }

    public Command getCommand(Plays possiblePlays) {
        Command command;
        do {
            String commandString = commandPanel.getString();
            command = new Command(commandString,possiblePlays);
            if (!command.isValid()) {
                displayString("Error: Command not valid.");
            }
        } while (!command.isValid());
        return command;
    }
    
    
    //Simpler getCommand can be used without possiblePlays
    public Command getCommand() {
        Command command;
        do {
            String commandString = commandPanel.getString();
            command = new Command(commandString);
            if (!command.isValid()) {
                displayString("Error: Command not valid.");
            }
        } while (!command.isValid());
        return command;
    }

    public void displayPlays(Player player, Plays plays) {
        displayString(player + " (" + player.getColorName() + ") available moves...");
        int index = 0;
        for (Play play : plays) {
            String code;
            if (index<26) {
                code = "" + (char) (index%26 + (int) 'A');
            } else {
                code = "" + (char) (index/26 - 1 + (int) 'A') + (char) (index % 26 + (int) 'A');
            }
            displayString(code + ". " + play);
            index++;
        }
    }

    public void displayNoMove(Player player) throws InterruptedException {
        displayString(player + " has no valid moves.");
        TimeUnit.SECONDS.sleep(1);
    }

    public void displayForcedMove(Player player) throws InterruptedException {
        displayString(player + " has a forced move.");
        TimeUnit.SECONDS.sleep(1);
    }
}