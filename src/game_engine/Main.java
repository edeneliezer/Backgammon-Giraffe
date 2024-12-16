package game_engine;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.backgammonUI;

	public class Main extends Application {
		@Override
		public void start(Stage stage) throws Exception {
		// יצירת מופע של מסך הפתיחה
			backgammonUI openingScreen = new backgammonUI();
		// קריאה לשיטת 'start' של מסך הפתיחה
			openingScreen.start(stage);
		// הוספת מאזין לכפתור ה-"Let's play!" במסך הפתיחה
			openingScreen.getPlayButton().setOnAction(e -> showGame(stage));
		}
		// מתודת עזר שתציג את מסך המשחק
		private void showGame(Stage stage) {
			try {
				MatchController root = new MatchController(stage);
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.setTitle("Backgammon");
				stage.show();
			// הגדרות נוספות למסך המשחק
				root.setRollDiceAccelarator();
				root.requestFocus();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public static void main(String[] args) {
		launch(args); // calls start method.
	}
		@Override
	public void start(Stage stage) throws Exception {
	MatchController root = new MatchController(stage);
	Scene scene = new Scene(root);
	stage.setScene(scene);
	stage.setTitle("Backgammon");
	stage.show();
	setStageIcon(stage);
	// these must be set only after stage is shown.
	root.setRollDiceAccelarator();
	root.requestFocus();
	}
	/**
	* Set the application's icon.
	* @param stage, the stage of the application.
	*/
	public void setStageIcon(Stage stage) {
	try {
	InputStream input = Main.class.getResourceAsStream("/game/img/icon/icon.png");
	stage.getIcons().add(new Image(input));
	input.close();
	} catch (IOException e) {
	e.printStackTrace();
	}
	}
	}
	
