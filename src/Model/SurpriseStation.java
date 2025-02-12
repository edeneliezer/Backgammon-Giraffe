package Model;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SurpriseStation extends SpecialStation {

    /*public void activate(Player player) {
        System.out.println(player.getName() + " landed on a Surprise Station and gets an extra turn!");
    }
   */
	@Override
	 public void performAction(Player player) {
	        System.out.println(player.getName() + " gets an extra turn!");
	     // יצירת Stage חדש עבור החלון
		    Stage dialogStage = new Stage();
		    dialogStage.setTitle("Surprise Station");

		    // קביעת פריסת החלון
		    VBox layout = new VBox(20);
		    layout.setAlignment(Pos.CENTER);
		    layout.setStyle("-fx-background-color: #FDF5E6; -fx-padding: 20; -fx-border-color: #8B4513; -fx-border-width: 5;");

		    // יצירת טקסט
		    javafx.scene.control.Label messageLabel = new javafx.scene.control.Label("🎉 Congrats! 🎉\nYou've landed on a Surprise Station.\nYou earned one more turn!");
		    messageLabel.setFont(javafx.scene.text.Font.font("Verdana", 16));
		    messageLabel.setStyle("-fx-text-fill: #8B4513;"); // צבע טקסט חום
		    messageLabel.setWrapText(true);
		    messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

		    // הוספת אייקון (אופציונלי)
		    ImageView surpriseIcon = new ImageView("/game/img/board/surprise_icon.png");
		    surpriseIcon.setFitWidth(100);
		    surpriseIcon.setFitHeight(100);

		    // כפתור לסגירת החלון
		    javafx.scene.control.Button closeButton = new javafx.scene.control.Button("Perfect!");
		    closeButton.setFont(javafx.scene.text.Font.font("Verdana", 14));
		    closeButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: #FDF5E6; -fx-font-weight: bold;");

		    // שינוי סגנון על מעבר עכבר
		    closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #A0522D; -fx-text-fill: #FDF5E6; -fx-font-weight: bold; -fx-cursor: hand;"));
		    closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: #FDF5E6; -fx-font-weight: bold;"));

		    // פעולה ללחיצה
		    closeButton.setOnAction(e -> dialogStage.close());

		    // הוספת רכיבים לפריסה
		    layout.getChildren().addAll(surpriseIcon, messageLabel, closeButton);

		    // יצירת Scene והוספתו ל-Stage
		    Scene scene = new Scene(layout, 400, 300);
		    dialogStage.setScene(scene);

		    // מניעת אינטראקציה עם החלון הראשי עד שהדיאלוג ייסגר
		    dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

		    // הצגת החלון
		    dialogStage.showAndWait();
	    }
	
}
