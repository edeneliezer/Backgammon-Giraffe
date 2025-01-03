package Model;
import Model.Dice;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DiceTestApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a Dice in HARD mode
        Dice hardModeDice = new Dice(Dice.Mode.HARD);

        // Text to display the dice roll result
        Text resultText = new Text("Press 'Roll Dice' to start!");
        resultText.setFont(Font.font("Verdana", 20));
        resultText.setFill(Color.BLACK);

        // Button to roll the dice
        Button rollButton = new Button("Roll Dice");
        rollButton.setFont(Font.font("Verdana", 16));
        rollButton.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: white;");

        rollButton.setOnAction(e -> {
            int rollResult = hardModeDice.roll(); // Roll the dice
            hardModeDice.draw(rollResult);       // Update the dice's visual
            resultText.setText("Rolled: " + rollResult);
        });

        // VBox layout
        VBox root = new VBox(20, hardModeDice, rollButton, resultText);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #fefaf4;");

        // Scene and Stage
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Dice Test - HARD Mode");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}