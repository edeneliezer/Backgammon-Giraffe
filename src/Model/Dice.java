package Model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import controller.ColorParser;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * This class represents the dice object with support for negative values and zero.
 */
public class Dice extends ImageView implements ColorParser {
    private final int MAX_DICE_SIZE = 10; // Range: -3 to 6
    private Image[] dices;
    private int diceRollResult;
    private ColorAdjust colorAdjust;
    private Color color;
    public static Mode currentMode = Mode.REGULAR;

    public enum Mode {
        REGULAR, HARD
    }

    /**
     * Constructors
     */
    
    public Dice(Mode mode) {
        this(Color.RED);
    	//this.currentMode = mode;
    }
    
    public Dice(Dice otherDice) {
        this(otherDice.getColor());
        this.diceRollResult = otherDice.getDiceResult();
        //this.currentMode = otherDice.getMode();
    }

    public Dice(int diceRollResult) {
        this(Color.RED);
        this.diceRollResult = diceRollResult;
       // this.currentMode = Mode.REGULAR;
    }

    public Dice(Color color) {
        super();
        this.color = color;
       // this.currentMode = Mode.REGULAR;
        dices = new Image[MAX_DICE_SIZE];
        initImages();

        colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
    }

    /**
     * Initializes the dice images for both Regular and Hard modes.
     * Includes placeholder images for negative values and zero.
     */
    private void initImages() {
        String colorString = parseColor(color);
        for (int i = -3; i <= 6; i++) {
            try {
                if (i > 0) {
                    // Use existing images for positive values
                    InputStream input = getClass().getResourceAsStream(
                        "/game/img/dices/" + colorString + "/" + i + ".png");
                    dices[i + 3] = new Image(input);
                    input.close();
                } else {
                    // Create placeholder images for negative values and zero
                    dices[i + 3] = createPlaceholderImage(i);
                }
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                dices[i + 3] = createPlaceholderImage(i); // Fallback to placeholder
            }
        }
    }

    /**
     * Creates a placeholder image for negative values and zero.
     * 
     * @param value The value for which to create a placeholder.
     * @return A placeholder Image.
     */
    private Image createPlaceholderImage(int value) {
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(100, 100);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();

        // Background
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, 100, 100);

        // Text
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Verdana", 24));
        gc.fillText(String.valueOf(value), 40, 55);

        return canvas.snapshot(null, null);
    }

    /**
     * Sets the mode of the dice.
     * 
     * @param mode The mode to set (REGULAR or HARD).
     */
    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    /**
     * Rolls the dice based on the current mode.
     * 
     * @return The rolled value.
     */
    public int roll() {
        Random rand = new Random();
        if (currentMode == Mode.REGULAR) {
            diceRollResult = rand.nextInt(6) + 1; // Regular mode: 1 to 6
        } else if (currentMode == Mode.HARD) {
            diceRollResult = rand.nextInt(10) - 3; // Hard mode: -3 to 6
        }
        return diceRollResult;
    }

    /**
     * Draws the dice's visual representation based on the result.
     * 
     * @param result The rolled result to display.
     * @return The updated Dice object.
     */
    public Dice draw(int result) {
        this.diceRollResult = result;
        int index = result + 3; // Adjust index to map -3 to 6
        setImage(dices[index]);
        setEffect(null);
        rotate();
        return this;
    }

    /**
     * Rotates the dice image randomly within a range.
     */
    private void rotate() {
        Random rand = new Random();
        int rotation = rand.nextInt(30) - 15; // Rotation range: -15 to 15
        setRotate(rotation);
    }

    /**
     * Marks the dice as used (darkens the image).
     */
    public void setUsed() {
        setEffect(colorAdjust);
    }

    /**
     * Marks the dice as not used (removes the effect).
     */
    public void setNotUsed() {
        setEffect(null);
    }

    /**
     * Gets the result of the last dice roll.
     * 
     * @return The last rolled value.
     */
    public int getDiceResult() {
        return diceRollResult;
    }

    /**
     * Gets the current color of the dice.
     * 
     * @return The color of the dice.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Checks if the dice result equals the value of another dice.
     * 
     * @param otherDice The other dice to compare.
     * @return True if the values are equal; otherwise, false.
     */
    public boolean equalsValueOf(Dice otherDice) {
        return diceRollResult == otherDice.getDiceResult();
    }

    /**
     * Gets the current mode of the dice.
     * 
     * @return The current mode (REGULAR or HARD).
     */
    public Mode getMode() {
        return currentMode;
    }
}