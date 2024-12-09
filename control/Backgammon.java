package control;
import java.util.concurrent.TimeUnit;

import model.Board;
import model.Command;
import model.Dice;
import model.Player;
import model.Players;
import model.Plays;
import view.UI;

public class Backgammon {
    // This is the main class for the Backgammon game. It orchestrates the running of the game.

    public static final int NUM_PLAYERS = 2;

    private final Players players = new Players();
    private final Board board;
    private final UI ui;

    // Constructor to initialize dependencies properly
    public Backgammon() {
        this.board = new Board(players); // Ensure this matches the actual Board constructor
        this.ui = new UI(board, players);
    }

    private void getPlayerNames() {
        for (Player player : players) {
            ui.promptPlayerName();
            String name = ui.getString();
            ui.displayString("> " + name);
            player.setName(name);
            ui.displayPlayerColor(player);
        }
    }

    private int getScoreLimit() {
        ui.promptScoreLimit();
        int scorelimit = ui.getScoreLimit();
        ui.displayString("You are playing up to a score of: " + scorelimit);
        return scorelimit;
    }

    private void rollToStart() {
        do {
            for (Player player : players) {
                player.getDice().rollDie();
                ui.displayRoll(player);
            }
            if (players.isEqualDice()) {
                ui.displayDiceEqual();
            }
        } while (players.isEqualDice());
        players.setCurrentAccordingToDieRoll();
        ui.displayDiceWinner(players.getCurrent());
        ui.display();
    }

    private int takeTurns() throws InterruptedException {
        Command command = new Command();
        boolean firstMove = true;
        int score = 1;
        int doubleCube = 1;

        do {
            Player currentPlayer = players.getCurrent();
            Dice currentDice;

            if (firstMove) {
                currentDice = new Dice(players.get(0).getDice().getDie(), players.get(1).getDice().getDie());
                firstMove = false;
            } else {
                currentPlayer.getDice().rollDice();
                ui.displayRoll(currentPlayer);
                currentDice = currentPlayer.getDice();
            }
            Plays possiblePlays = board.getPossiblePlays(currentPlayer, currentDice);

            ui.displayString("Enter 'double' to double game score or any other command to continue");
            String eqDouble = ui.getString();

            if (eqDouble.equals("double") && currentPlayer.hasDouble()) {
                currentPlayer.useDouble();
                players.advanceCurrentPlayer();
                players.getCurrent().receiveDouble();
                do {
                    ui.promptDoubleOption(players.getCurrent());
                    command = ui.getCommand();
                } while (!command.isNo() && !command.isYes());

                if (command.isYes()) {
                    score *= 2;
                    doubleCube *= 2;
                    board.setDoubleValue(doubleCube);
                    ui.display();
                }

                players.advanceCurrentPlayer();
            } else if (eqDouble.equals("double")) {
                ui.displayString("You do not currently have the doubling cube");
            }

            if (!command.isNo() && !command.isQuit()) {
                if (possiblePlays.number() == 0) {
                    ui.displayNoMove(currentPlayer);
                } else if (possiblePlays.number() == 1) {
                    ui.displayForcedMove(currentPlayer);
                    board.move(currentPlayer, possiblePlays.get(0));
                } else {
                    ui.displayPlays(currentPlayer, possiblePlays);
                    ui.promptCommand(currentPlayer);
                    command = ui.getCommand(possiblePlays);
                    ui.displayString("> " + command);
                    if (command.isMove()) {
                        board.move(currentPlayer, command.getPlay());
                    } else if (command.isCheat()) {
                        board.cheat();
                    }
                }
                ui.display();
                TimeUnit.SECONDS.sleep(2);
                players.advanceCurrentPlayer();
                ui.display();
            }

        } while (!command.isQuit() && !board.isGameOver() && !command.isNo());

        if (command.isQuit()) {
            return 0;
        } else if (board.isGameOver()) {
            ui.displayGameWinner(board.getWinner());
            players.setWinner(board.getWinner());
            board.getWinner().addWin(score);
            TimeUnit.SECONDS.sleep(2);
            return score;
        } else if (command.isNo()) {
            ui.displayGameWinner(players.getCurrent());
            players.setWinner(players.getCurrent());
            players.getCurrent().addWin(score);
            TimeUnit.SECONDS.sleep(2);
            return score;
        } else {
            return 0;
        }
    }

    private void play() throws InterruptedException {
        ui.display();
        ui.displayStartOfGame();
        boolean end = false;
        int limit = getScoreLimit();

        if (limit != 0) {
            getPlayerNames();
            while (players.get(0).getScore() < limit && players.get(1).getScore() < limit) {
                rollToStart();
                int win = takeTurns();

                if (win == 0) {
                    end = true;
                    break;
                }
                ui.displayString("Please enter any key to continue!");
                ui.getString();
                board.reset();
            }

            if (!end) {
                ui.displayMatchWinner(players.getWinner());
                ui.displayScore(players.getCurrent(), players.getOther());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Backgammon game = new Backgammon();
        game.play();
        System.exit(0);
    }
}