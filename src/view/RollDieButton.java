package view;

import Model.GameConstants;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Cursor;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This class represents the roll die button in Backgammon.
 */
public class RollDieButton extends Button {
    public RollDieButton () {
        super("Roll Dice");
        setMaxWidth(Double.MAX_VALUE);
        setMinHeight(GameConstants.getUIHeight());

        // Set bold font explicitly
        setFont(Font.font("Verdana", FontWeight.BOLD, 16)); 

        // Set button style
        setStyle("-fx-background-color: #4CAF50; -fx-text-fill: black; -fx-border-width: 2px;");

        // Add shadow effect
        setEffect(new DropShadow(10, 0, 0, Color.BLACK));

        // Initialize hover and click effects
        initEventEffects();
    }
    
    public void initEventEffects() {
        // Make button distinct on click with shadow on click
        setOnMousePressed((MouseEvent event) -> {
            setEffect(new DropShadow(20, 0, 0, Color.BLACK));
        });

        // Remove shadow when click is released
        setOnMouseReleased((MouseEvent event) -> {
            setEffect(new DropShadow(10, 0, 0, Color.BLACK));
        });

        // Change cursor to hand and background color on hover
        setOnMouseEntered((MouseEvent event) -> {
            setCursor(Cursor.HAND); // Change cursor to hand
            setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-border-width: 2px;"); // Lighter green
        });

        // Return cursor and color to default when mouse exits
        setOnMouseExited((MouseEvent event) -> {
            setCursor(Cursor.DEFAULT); // Change cursor back to default
            setStyle("-fx-background-color: #4CAF50; -fx-text-fill: black; -fx-border-width: 2px;"); // Original green
        });
    }
}
