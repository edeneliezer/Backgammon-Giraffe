package Model;
import controller.GameplayController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.QuestionOverlay;
import javafx.scene.control.Alert.AlertType;

public class QuestionStation extends SpecialStation implements QuestionObserver{
   

	@Override
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
       // 
    }
}
