package Model;

import java.util.Random;

import controller.ColorPerspectiveParser;
import javafx.scene.image.ImageView;

/**
 * This class represents the doubling cube object in Backgammon game.
 * 
 * @teamname TeaCup
 * @author Bryan
 */
public class DoublingCube extends ImageView implements ColorPerspectiveParser, Touchable {
    private final int MAX_DICE_SIZE = 6;
    private int currentSide;
    private boolean isMaxDoubling, isUsed;

    /**
     * Constructor - Initializes the doubling cube.
     */
    public DoublingCube() {
        super();
        reset();
    }

    /**
     * Use the highlighted image. (Dummy implementation, no images loaded)
     */
    public void setHighlightImage() {
        // Dummy implementation since images are not used
    }

    /**
     * Use the normal image. (Dummy implementation, no images loaded)
     */
    public void setNormalImage() {
        // Dummy implementation since images are not used
    }

    /**
     * Rotate the doubling cube representation only when it is on the board.
     */
    public void rotateOnBoard() {
        // Simulate a rotation range of 15 to -15.
        Random rand = new Random();
        int rotation = rand.nextInt(30) - 15 + 1;
        setRotate(rotation);
    }

    /**
     * Doubles the value of the doubling cube.
     */
    public void doubleDoublingCube() {
        // Allow double if less than max_dice_size-1.
        if (currentSide < MAX_DICE_SIZE - 1) currentSide += 1;
        setNormalImage();

        if (currentSide == MAX_DICE_SIZE - 1) isMaxDoubling = true;
    }

    // For declined doubling cube multiplier.
    public int getIntermediateGameMultiplier() {
        if (isUsed) return (int) Math.pow(2.0, currentSide);
        return 1;
    }

    // For game end multiplier.
    public int getEndGameMultiplier() {
        if (isUsed) return (int) Math.pow(2.0, currentSide + 1);
        return 1;
    }

    public boolean isMaxDoubling() {
        return isMaxDoubling;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
        currentSide = 0;
        setNormalImage();
    }

    public void resetRotation() {
        setRotate(0.0);
    }

    public void reset() {
        currentSide = MAX_DICE_SIZE - 1;
        isMaxDoubling = false;
        isUsed = false;
        setNormalImage();
        resetRotation();
    }
}
