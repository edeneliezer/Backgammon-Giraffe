package Model;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.QuestionOverlay;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class QuestionStation extends SpecialStation {
	@Override
	public void performAction(Player player) {
	    // יצירת Stage חדש עבור החלון
	    Stage dialogStage = new Stage();
	    dialogStage.setTitle("Surprise Station");

	    // קביעת פריסת החלון
	    VBox layout = new VBox(20);
	    layout.setAlignment(Pos.CENTER);
	    layout.setStyle("-fx-background-color: #FDF5E6; -fx-padding: 20; -fx-border-color: #8B4513; -fx-border-width: 5;");

	    // יצירת טקסט
	    javafx.scene.control.Label messageLabel = new javafx.scene.control.Label("You've landed on a Question Station.\nClick next to see the question");
	    messageLabel.setFont(javafx.scene.text.Font.font("Verdana", 16));
	    messageLabel.setStyle("-fx-text-fill: #8B4513;");
	    messageLabel.setWrapText(true);
	    messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

	    // כפתור "Next"
	    javafx.scene.control.Button nextButton = new javafx.scene.control.Button("Next");
	    nextButton.setFont(javafx.scene.text.Font.font("Verdana", 14));
	    nextButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: #FDF5E6; -fx-font-weight: bold;");

	    nextButton.setOnAction(e -> {
	        // פתיחת חלון השאלה
	        QuestionOverlay questionOverlay = new QuestionOverlay(dialogStage);
	        questionOverlay.showAndWait();


	        dialogStage.close(); // סגירת חלון התחנה
	    });

	    // הוספת רכיבים לפריסה
	    layout.getChildren().addAll(messageLabel, nextButton);

	    // Scene והוספתו ל-Stage
	    Scene scene = new Scene(layout, 400, 300);
	    dialogStage.setScene(scene);
	    dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
	    dialogStage.showAndWait();
	}


}
