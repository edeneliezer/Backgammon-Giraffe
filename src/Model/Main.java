package Model;
import java.io.IOException;
import java.io.InputStream;

import controller.MatchController;
import controller.MusicPlayer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.EditQuestionsScreen;
import view.QuestionOverlay;
import view.SettingsScreen;
import view.backgammonUI;

	public class Main extends Application {
		@Override
		public void start(Stage stage) throws Exception {
		// יצירת מופע של מסך הפתיחה
			backgammonUI openingScreen = new backgammonUI();
//			 EditQuestionsScreen editScreen = new EditQuestionsScreen(null);
//		     editScreen.showAndWait();
//			QuestionOverlay questionOverlay = new QuestionOverlay(null);
//			questionOverlay.showAndWait();
			MusicPlayer.getInstance().play();
		// קריאה לשיטת 'start' של מסך הפתיחה
			openingScreen.start(stage);
		// הוספת מאזין לכפתור ה-"Let's play!" במסך הפתיחה
			openingScreen.getPlayButton().setOnAction(e -> showGame(stage));
		}
		// מתודת עזר שתציג את מסך המשחק
		private void showGame(Stage stage) {
			try {
				
				// Map chosen difficulty to Dice.Mode
		        Dice.Mode mode = Dice.Mode.REGULAR; // Default mode
		        String chosenDifficulty = backgammonUI.getChosenDiffficulty();
		        if (chosenDifficulty.equals("Hard")) {
		            mode = Dice.Mode.HARD;
		        }
				MatchController root = new MatchController(stage, mode);
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
		launch(args); // calls start method.
	}
	
	}


