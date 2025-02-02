package Model;

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class represents a specialized dice that only rolls values 0, -1, -2, and -3.
 */
public class NegativeDice extends ImageView {
    private final int MAX_DICE_SIZE = 4; // Indices for 0, -1, -2, -3
    private Image[] dices;
    private int diceRollResult;

    /**
     * Constructor
     */
    public NegativeDice() {
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
    }

    /**
     * Initializes the dice images for negative values and zero.
     */
    private void initImages() {
        String colorString = "black"; // Use black color images as per design
        String[] fileNames = {"zero.jpg", "minus1.jpg", "minus2.jpg", "minus3.jpg"};

        for (int i = 0; i < MAX_DICE_SIZE; i++) {
            try {
                InputStream input = getClass().getResourceAsStream(
                        "/game/img/dices/" + colorString + "/" + fileNames[i]);
                if (input == null) {
                    System.err.println("File not found: /game/img/dices/" + colorString + "/" + fileNames[i]);
                    throw new IOException("File not found: " + fileNames[i]);
                }
                dices[i] = new Image(input);
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
                // Fallback to a basic placeholder if image file fails to load
                dices[i] = createFallbackPlaceholderImage(-i);
            }
        }
    }

    /**
     * Creates a fallback placeholder image for unexpected issues.
     * 
     * @param value The negative value for which to create a fallback image.
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
     * Rolls the dice to get a value between 0 and -3.
     * 
     * @return The rolled value.
     */
    public int roll() {
        int index = (int) (Math.random() * MAX_DICE_SIZE);
        diceRollResult = -index;
        return diceRollResult;
    }

    /**
     * Draws the dice's visual representation based on the result.
     * 
     * @param result The rolled result to display.
     */
    public void draw(int result) {
        this.diceRollResult = result;
        int index = -result; // Adjust index to map 0 to -3
        setImage(dices[index]);
    }

    /**
     * Gets the result of the last dice roll.
     * 
     * @return The last rolled value.
     */
    public int getDiceResult() {
        return diceRollResult;
    }
}
