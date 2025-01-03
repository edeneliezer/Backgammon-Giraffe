package view;

import Model.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class represents the game made up of separate components in Backgammon.
 * It creates a game made out of modular panes/nodes.
 */
public class GameComponentsController extends VBox {
    private PlayerPanel topPlayerPnl, btmPlayerPnl;
    private Bars bars;
    private Board board;
    private HomePanel leftHome, rightHome, mainHome, otherHome;
    private Dice[] dices; // Array of dice objects for the game

    /**
     * Constructor to initialize the game components.
     */
    public GameComponentsController(Player bottomPlayer, Player topPlayer) {
        super();
        initGameComponents(bottomPlayer, topPlayer);
    }

    /**
     * Initializes the game by creating the components and setting them together.
     */
    public void initGameComponents(Player bottomPlayer, Player topPlayer) {
        board = new Board(this);
        bars = new Bars();
        leftHome = new HomePanel();
        rightHome = new HomePanel();

        // Set main and other homes based on the quadrant setting
        switch (Settings.getMainQuadrant()) {
            case BOTTOM_RIGHT:
            case TOP_RIGHT:
                mainHome = rightHome;
                otherHome = leftHome;
                leftHome.setAsMainHome(false);
                break;
            case BOTTOM_LEFT:
            case TOP_LEFT:
                mainHome = leftHome;
                otherHome = rightHome;
                rightHome.setAsMainHome(false);
                break;
        }
        mainHome.setAsMainHome(true);

        // Initialize the dice
        dices = new Dice[2];
        dices[0] = new Dice(Dice.Mode.REGULAR); // Default mode
        dices[1] = new Dice(Dice.Mode.REGULAR);

        HBox middlePart = board;
        middlePart.setMinWidth(GameConstants.getMiddlePartWidth());
        middlePart.getChildren().add(1, bars);
        middlePart.getChildren().add(0, leftHome);
        middlePart.getChildren().add(rightHome);

        topPlayerPnl = new PlayerPanel(middlePart.getMinWidth(), topPlayer);
        btmPlayerPnl = new PlayerPanel(middlePart.getMinWidth(), bottomPlayer);

        getChildren().addAll(topPlayerPnl, middlePart, btmPlayerPnl);
        setBackground(GameConstants.getGameImage());
        setEffect(new DropShadow(20, 0, 0, Color.BLACK));
        setMaxHeight(topPlayerPnl.getMinHeight() + middlePart.getHeight() + btmPlayerPnl.getMinHeight());
    }

    /**
     * Sets the mode of the dice.
     * 
     * @param mode The mode to set for all dice.
     */
    public void setDiceMode(Dice.Mode mode) {
        if (dices != null) {
            for (Dice dice : dices) {
                dice.setMode(mode);
            }
        } else {
            System.err.println("Error: Dices array is not initialized.");
        }
    }

    /**
     * Gets the dice array.
     * 
     * @return The array of dice.
     */
    public Dice[] getDices() {
        return dices;
    }

    public void unhighlightAll() {
        board.unhighlightPipsAndCheckers();
        mainHome.unhighlight();
        bars.unhighlight();
        unhighlightAllCubeZones();
        unhighlightCube();
    }

    public void unhighlightAllCubeZones() {
        unhighlightAllPlayersCubeHomes();
        board.unhighlightAllCubeHome();
    }

    public void unhighlightAllPlayersCubeHomes() {
        otherHome.unhighlight();
    }

    public void highlightAllPlayersCubeHomes() {
        otherHome.highlight();
    }

    public void highlightCubeZones(Color pColor) {
        unhighlightAllCubeZones();
        unhighlightCube();
        otherHome.highlight(pColor);
        getCubeHome().highlight();
        highlightCube();
    }

    public void highlightCube() {
        getCube().setHighlightImage();
    }

    public void unhighlightCube() {
        getCube().setNormalImage();
    }

    public MoveResult moveToBar(int fro) {
        MoveResult moveResult = MoveResult.NOT_MOVED;

        Pip[] pips = board.getPips();
        Bar bar = bars.getBar(pips[fro].getTopChecker().getColor());

        if (!pips[fro].isEmpty()) {
            bar.push(pips[fro].pop());
            moveResult = MoveResult.MOVED_TO_BAR;

            pips[fro].drawCheckers();
            bar.drawCheckers();
        }
        return moveResult;
    }

    public MoveResult moveFromBar(Color fromBar, int toPip) {
        MoveResult moveResult = board.isBarToPipMove(fromBar, toPip);

        switch (moveResult) {
            case MOVED_FROM_BAR:
                Pip[] pips = board.getPips();
                Bar bar = getBars().getBar(fromBar);
                pips[toPip].push(bar.pop());
                pips[toPip].drawCheckers();
                bar.drawCheckers();
                break;
            case MOVE_TO_BAR:
                break;
            default:
        }
        return moveResult;
    }

    public MoveResult moveToHome(int fromPip) {
        MoveResult moveResult = board.isPipToHomeMove(fromPip, null);

        switch (moveResult) {
            case MOVED_TO_HOME_FROM_PIP:
                Pip[] pips = board.getPips();
                Home home = mainHome.getHome(pips[fromPip].getTopChecker().getColor());
                home.push(pips[fromPip].pop());

                pips[fromPip].drawCheckers();
                home.drawCheckers();
                break;
            default:
        }
        return moveResult;
    }

    public MoveResult moveToHome(Color fromBar) {
        MoveResult moveResult = MoveResult.NOT_MOVED;

        Bar bar = bars.getBar(fromBar);
        if (!bar.isEmpty()) {
            Home home = mainHome.getHome(bar.getTopChecker().getColor());
            home.push(bar.pop());
            moveResult = MoveResult.MOVED_TO_HOME_FROM_BAR;

            bar.drawCheckers();
            home.drawCheckers();
        }
        return moveResult;
    }

    public PlayerPanel getPlayerPanel(Color color) {
        PlayerPanel playerPnl = null;
        if (color.equals(Settings.getTopPerspectiveColor())) {
            playerPnl = topPlayerPnl;
        } else if (color.equals(Settings.getBottomPerspectiveColor())) {
            playerPnl = btmPlayerPnl;
        }
        return playerPnl;
    }

    public Emoji getEmojiOfPlayer(Color pColor) {
        return getPlayerPanel(pColor).getEmoji();
    }

    public Board getBoard() {
        return board;
    }

    public HomePanel getMainHome() {
        return mainHome;
    }

    public HomePanel getOtherHome() {
        return otherHome;
    }

    public DoublingCubeHome getCubeHome() {
        return otherHome.getCubeHome();
    }

    public DoublingCube getCube() {
        DoublingCube cube = null;
        if (board.isCubeInBoard()) {
            cube = board.getHomeCubeIsIn().getTopCube();
        } else if (isCubeInHome()) {
            cube = this.getPlayerHomeCubeIsIn().getTopCube();
        } else {
            cube = getCubeHome().getTopCube();
        }
        return cube;
    }

    public Bars getBars() {
        return bars;
    }

    public Home getPlayerHomeCubeIsIn() {
        Home theHome = null;
        if (!otherHome.getHome(Settings.getBottomPerspectiveColor()).isEmpty()) {
            theHome = otherHome.getHome(Settings.getBottomPerspectiveColor());
        } else if (!otherHome.getHome(Settings.getTopPerspectiveColor()).isEmpty()) {
            theHome = otherHome.getHome(Settings.getTopPerspectiveColor());
        }
        return theHome;
    }

    public boolean isCubeInHome() {
        return getPlayerHomeCubeIsIn() != null;
    }

    public void removeCheckers() {
        board.removeCheckers();
        bars.getBar(Settings.getTopPerspectiveColor()).removeCheckers();
        bars.getBar(Settings.getBottomPerspectiveColor()).removeCheckers();
        mainHome.getHome(Settings.getTopPerspectiveColor()).removeCheckers();
        mainHome.getHome(Settings.getBottomPerspectiveColor()).removeCheckers();
    }

    public void reset() {
        board.reset();
        leftHome.reset();
        rightHome.reset();
        bars.reset();
        topPlayerPnl.reset();
        btmPlayerPnl.reset();
    }
}