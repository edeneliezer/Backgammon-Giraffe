package Model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class represents the dice object with support for negative values and zero.
 */
public class Dice extends ImageView {
    private final int MAX_DICE_SIZE = 10; // Range: -3 to 6
    private Image[] dices;
    private int diceRollResult;
    private ColorAdjust colorAdjust;
    public static Mode currentMode = Mode.REGULAR;

    public enum Mode {
        REGULAR, HARD
    }

    /**
     * Constructors
     */
    public Dice(Mode mode) {
        this();
    }

    public Dice(Dice otherDice) {
        this();
        this.diceRollResult = otherDice.getDiceResult();
    }

    public Dice(int diceRollResult) {
        this();
        this.diceRollResult = diceRollResult;
    }

    public Dice() {
    	 super();
         dices = new Image[MAX_DICE_SIZE];
         initImages();

         // Set fixed size for dice images
         setFitWidth(60);  // Adjust as needed
         setFitHeight(60);

         // Add rounded corners
         Rectangle clip = new Rectangle(60, 60);
         clip.setArcWidth(15);  // Adjust rounding size as needed
         clip.setArcHeight(15);
         setClip(clip);

         colorAdjust = new ColorAdjust();
         colorAdjust.setBrightness(-0.5);
    }

    /**
     * Initializes the dice images for both Regular and Hard modes.
     * Includes placeholder images for negative values and zero.
     */
    private void initImages() {
    	String colorString = "black"; // Always use black images
        for (int i = -3; i <= 6; i++) {
            try {
                if (i > 0) {
                    InputStream input = getClass().getResourceAsStream(
                            "/game/img/dices/" + colorString + "/" + i + ".png");
                    dices[i + 3] = new Image(input);
                    input.close();
                } else {
                    dices[i + 3] = createPlaceholderImage(i);
                }
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                dices[i + 3] = createPlaceholderImage(i);
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
        String colorString = "black"; // Always use "black"
        String fileName;

        switch (value) {
            case 0:
                fileName = "zero.jpg";
                break;
            case -1:
                fileName = "minus1.jpg";
                break;
            case -2:
                fileName = "minus2.jpg";
                break;
            case -3:
                fileName = "minus3.jpg";
                break;
            default:
                throw new IllegalArgumentException("Invalid dice value: " + value);
        }

        try {
            InputStream input = getClass().getResourceAsStream(
                    "/game/img/dices/" + colorString + "/" + fileName);
            if (input == null) {
                System.err.println("File not found: /game/img/dices/" + colorString + "/" + fileName);
                throw new IOException("File not found: " + fileName);
            }
            return new Image(input);
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback in case of an issue
            return createFallbackPlaceholderImage(value);
        }
    }

    /**
     * Creates a fallback placeholder image for unexpected issues.
     * 
     * @param value The value for which to create a fallback image.
     * @return A fallback placeholder Image.
     */
    private Image createFallbackPlaceholderImage(int value) {
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
//    public Color getColor() {
//        return getColor();
//    }

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
