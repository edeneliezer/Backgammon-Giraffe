package Model;

import controller.GameplayController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.QuestionOverlay;

/**
 * This class extends SpecialStation and implements QuestionObserver.
 * It manages the interactions when a player lands on a Question Station.
 */
public class QuestionStation extends SpecialStation implements QuestionObserver {

    @Override
    public void performAction(Player player) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Question Station");

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FDF5E6; -fx-padding: 20; -fx-border-color: #8B4513; -fx-border-width: 5;");

        Label messageLabel = new Label("You've landed on a Question Station.\nClick next to see the question");
        messageLabel.setFont(Font.font("Verdana", 16));
        messageLabel.setStyle("-fx-text-fill: #8B4513;");
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button nextButton = new Button("Next");
        nextButton.setFont(Font.font("Verdana", 14));
        nextButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: #FDF5E6;");
        nextButton.setCursor(javafx.scene.Cursor.HAND);
        nextButton.setOnMouseEntered(e -> nextButton.setStyle("-fx-background-color: #7A5A2F; -fx-text-fill: #FDF5E6;"));
        nextButton.setOnMouseExited(e -> nextButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: #FDF5E6;"));

        nextButton.setOnAction(e -> {
            QuestionOverlay questionOverlay = new QuestionOverlay(dialogStage);
            questionOverlay.addObserver(this);
            questionOverlay.showAndWait();

            dialogStage.close();
        });

        layout.getChildren().addAll(messageLabel, nextButton);

        Scene scene = new Scene(layout, 400, 300);
        dialogStage.setScene(scene);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.showAndWait();
    }

    @Override
    public void onCorrectAnswer() {
        // The game continues normally; the player's turn does not change.
    }

    @Override
    public void onWrongAnswer() {
        showAlert("Wrong Answer", "You answered incorrectly!\nRoll the negative dice.");
    }

    @Override
    public void onTimeExpired() {
        showAlert("Time Expired", "Time's up!\nRoll the negative dice.");
    }

    private void showAlert(String title, String message) {
        Stage alertStage = new Stage();
        alertStage.setTitle(title);

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FDF5E6; -fx-padding: 20;");

        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Verdana", 16));
        messageLabel.setStyle("-fx-text-fill: #8B4513;");
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button rollButton = new Button("Roll Dice");
        rollButton.setFont(Font.font("Verdana", 14));
        rollButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: #FDF5E6;");
        rollButton.setCursor(javafx.scene.Cursor.HAND);

        rollButton.setOnAction(e -> {
            Dice dice = new Dice(Dice.Mode.NEGATIVE); // קובייה שלילית
            int rollResult = dice.roll();
            dice.draw(rollResult);

            ImageView diceView = new ImageView(dice.getImage());
            diceView.setFitWidth(60);
            diceView.setFitHeight(60);
            layout.getChildren().add(diceView);
            alertStage.sizeToScene();

            rollButton.setDisable(true); // ביטול הפעילות של הכפתור לאחר ההטלה

            Button closeButton = new Button("Close and Move");
            closeButton.setFont(Font.font("Verdana", 14));
            closeButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: #FDF5E6;");
            closeButton.setCursor(javafx.scene.Cursor.HAND);
            closeButton.setOnAction(closeEvent -> {
                alertStage.close(); // סגירת החלון והמשך המשחק
            });

            layout.getChildren().add(closeButton);
        });

        layout.getChildren().addAll(messageLabel, rollButton);

        Scene scene = new Scene(layout, 300, 200);
        alertStage.setScene(scene);
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.showAndWait();
    }


}
