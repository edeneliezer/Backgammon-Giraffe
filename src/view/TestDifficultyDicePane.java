package view;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestDifficultyDicePane extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the DifficultyDicePane
        DifficultyDicePane dicePane = new DifficultyDicePane();

        // Button to get the rolled result
        Button getResultButton = new Button("Get Dice Result");
        getResultButton.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px;");
        getResultButton.setOnAction(e -> {
            String result = dicePane.getCurrentResult();
            System.out.println("Current Dice Result: " + result); // Output the result to the console
        });

        // Layout
        VBox layout = new VBox(20, dicePane, getResultButton);
        layout.setStyle("-fx-padding: 20;");
        layout.setAlignment(javafx.geometry.Pos.CENTER);

        // Scene setup
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setTitle("Test Difficulty Dice");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}