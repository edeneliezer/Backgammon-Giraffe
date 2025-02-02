package Model;
import controller.GameplayController;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.QuestionOverlay;
import javafx.scene.control.Alert.AlertType;

public class QuestionStation extends SpecialStation implements QuestionObserver{
   

<<<<<<< HEAD
	AudioClip diceSound = new AudioClip(getClass().getResource("/musicplayer/songs/dice.aiff").toExternalForm());
    @Override
=======
	@Override
>>>>>>> parent of 2cb8ad2 (BONUS - Negative dice when wrong answer on Question Station)
    public void performAction(Player player) {
        // יצירת Stage חדש עבור החלון
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Question Station");

        // קביעת פריסת החלון
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FDF5E6; -fx-padding: 20; -fx-border-color: #8B4513; -fx-border-width: 5;");

        // יצירת טקסט
        Label messageLabel = new Label("You've landed on a Question Station.\nClick next to see the question");
        messageLabel.setFont(Font.font("Verdana", 16));
        messageLabel.setStyle("-fx-text-fill: #8B4513;");
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // כפתור "Next"
        Button nextButton = new Button("Next");
        nextButton.setFont(Font.font("Verdana", 14));
        nextButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: #FDF5E6;");
        nextButton.setCursor(javafx.scene.Cursor.HAND); // הוספת סמן יד
        // עיצוב תגובתי להעברת העכבר
        nextButton.setOnMouseEntered(e -> nextButton.setStyle("-fx-background-color: #7A5A2F; -fx-text-fill: #FDF5E6;"));
        nextButton.setOnMouseExited(e -> nextButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: #FDF5E6;"));

        nextButton.setOnAction(e -> {
            // פתיחת חלון השאלה
            QuestionOverlay questionOverlay = new QuestionOverlay(dialogStage);
            questionOverlay.addObserver(this);
            questionOverlay.showAndWait();

            dialogStage.close(); // סגירת חלון התחנה
        });

        // הוספת רכיבים לפריסה
        layout.getChildren().addAll(messageLabel, nextButton);

        // Scene והוספתו ל-Stage
        Scene scene = new Scene(layout, 400, 300);
        dialogStage.setScene(scene);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.showAndWait();
    }
	
	@Override
    public void onCorrectAnswer() {
        // המשחק ממשיך כרגיל, תור לא משתנה
    }

    @Override
    public void onWrongAnswer() {
        //
    }

    @Override
    public void onTimeExpired() {
<<<<<<< HEAD
    	Platform.runLater(() -> {
    	    try {
    	        showAlert("Time Expired", "Time's up!\nRoll the negative dice.");
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	});

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
        	diceSound.play();
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


=======
       // 
    }
>>>>>>> parent of 2cb8ad2 (BONUS - Negative dice when wrong answer on Question Station)
}
