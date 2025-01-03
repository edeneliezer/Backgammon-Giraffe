package view;

import Model.DifficultyDice;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Pane for displaying and interacting with the DifficultyDice.
 */
public class DifficultyDicePane extends VBox {
    private final DifficultyDice difficultyDice;
    private final Text diceText;
    private String currentResult;
    private final Button rollButton;

    public DifficultyDicePane() {
        difficultyDice = new DifficultyDice();
        currentResult = "Roll First!"; // Default text before rolling

        // Outer VBox Background (light beige)
        setStyle("-fx-background-color: #fefaf4;"); // Light beige background
        setAlignment(Pos.CENTER);
        setSpacing(20);

        // Dice Background (brown with black border)
        Rectangle diceShape = new Rectangle(150, 150);
        diceShape.setFill(Color.web("#8b5e3c")); // Dark brown for the dice
        diceShape.setArcWidth(20);
        diceShape.setArcHeight(20);
        diceShape.setStroke(Color.BLACK);
        diceShape.setStrokeWidth(2);

        // Dice Text (centered on the dice)
        diceText = new Text(currentResult);
        diceText.setFont(Font.font("Verdana", 24));
        diceText.setFill(Color.BEIGE);

        // StackPane for the dice (text + background)
        StackPane dicePane = new StackPane();
        dicePane.getChildren().addAll(diceShape, diceText);
        dicePane.setAlignment(Pos.CENTER);

        // Roll Button (red with white text)
        rollButton = new Button("Roll Dice");
        rollButton.setFont(Font.font("Verdana", 14));
        rollButton.setStyle(
            "-fx-background-color: #d11e1e; " +  // Red background
            "-fx-text-fill: white; " +          // White text
            "-fx-border-radius: 5; " +          // Rounded border
            "-fx-background-radius: 5;"         // Rounded background
        );
        rollButton.setOnAction(e -> rollDice());

        // Add components to the main VBox
        getChildren().addAll(dicePane, rollButton);
    }

    private void rollDice() {
        // Perform the dice roll
        currentResult = difficultyDice.roll();
        diceText.setText(currentResult);

        // Update button text and action
        rollButton.setText("Get Question");
        rollButton.setOnAction(e -> getQuestion());
    }

    /**
     * Placeholder for the "Get Question" button action.
     */
    private void getQuestion() {
        // Add the action for getting a question here
        System.out.println("Get Question button clicked!");
    }

    /**
     * Gets the result of the last dice roll.
     * 
     * @return The current difficulty level.
     */
    public String getCurrentResult() {
        return currentResult;
    }
}