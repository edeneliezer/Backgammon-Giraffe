package view;

import Model.DifficultyDice;
import Model.SysData;
import Model.Question;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class QuestionOverlay extends Stage {
    private Question currentQuestion;
    private boolean isDiceRolled = false;

    public QuestionOverlay(Stage parentStage) {
        // Set up the modal dialog
        initModality(Modality.APPLICATION_MODAL);
        initOwner(parentStage);
//        initStyle(StageStyle.TRANSPARENT); // Remove window decorations
//        setResizable(false);
        setOnCloseRequest(event -> {
            // Consume the event to prevent the window from closing
            event.consume();

            // Optional: Show a warning or message
            Alert alert = new Alert(Alert.AlertType.WARNING, "You cannot close this window without answering the question!", ButtonType.OK);
            alert.showAndWait();
        });

        DifficultyDice dice = new DifficultyDice(); // Initialize DifficultyDice

        // Load questions
        List<Question> questions = SysData.loadQuestions();
        if (questions.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No questions available!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        // Set up the root container
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #FAEBD7, #FFF8DC); " +
                      "-fx-border-color: #5B3924; -fx-border-width: 5; -fx-border-radius: 20; -fx-background-radius: 20;");
        root.setPrefSize(800, 600);
        

        // Add shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(5);
        dropShadow.setColor(Color.rgb(90, 90, 90, 0.5));
        root.setEffect(dropShadow);

        // Dice Image Section
        ImageView diceImageView = new ImageView();
        diceImageView.setFitWidth(120);
        diceImageView.setFitHeight(120);

        // Label showing the rolled difficulty level
        Label difficultyLabel = new Label();
        difficultyLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        difficultyLabel.setTextFill(Color.web("#5B3924"));

        // Placeholder for the question and answers
        VBox questionBox = new VBox(10);
        questionBox.setAlignment(Pos.TOP_LEFT);
        questionBox.setPadding(new Insets(10, 20, 10, 20));
        questionBox.setPrefSize(600, 700);

        // Roll Question Dice Button
        Button rollDiceButton = new Button("Roll Question Dice");
        rollDiceButton.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        rollDiceButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;");
        rollDiceButton.setOnMouseEntered(e -> rollDiceButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;"));
        rollDiceButton.setOnMouseExited(e -> rollDiceButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;"));

        rollDiceButton.setOnAction(e -> {
            if (!isDiceRolled) {
                // Roll the dice to get the difficulty level
                String difficulty = dice.roll();

                // Update dice image and difficulty label
                diceImageView.setImage(new Image("game/img/dices/black/" + difficulty.toLowerCase() + ".png"));
                difficultyLabel.setText("You rolled: " + difficulty);

                // Change button text to "Next"
                rollDiceButton.setText("Next");
                isDiceRolled = true;
            } else {
                // Roll the question based on difficulty
                currentQuestion = questions.get((int) (Math.random() * questions.size()));

                // Display the question and answers
                questionBox.getChildren().clear();
                root.getChildren().clear();

                Label questionLabel = new Label("Question:\n" + currentQuestion.getQuestion());
                questionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
                questionLabel.setTextFill(Color.web("#333333"));
                questionLabel.setWrapText(true);

                // Answer options
                ToggleGroup toggleGroup = new ToggleGroup();
                VBox answersBox = new VBox(10);
                answersBox.setAlignment(Pos.TOP_LEFT);
                answersBox.setPadding(new Insets(10, 20, 10, 20));

                for (String answer : currentQuestion.getAnswers()) {
                    RadioButton answerButton = new RadioButton(answer);
                    answerButton.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
                    answerButton.setTextFill(Color.web("#5B3924"));
                    answerButton.setToggleGroup(toggleGroup);

                    answersBox.getChildren().add(answerButton);
                }

                // Submit button
                Button submitButton = new Button("Submit");
                submitButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
                submitButton.setStyle("-fx-background-color: #B30000; -fx-text-fill: white; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;");
                submitButton.setOnMouseEntered(ev -> submitButton.setStyle("-fx-background-color: #FF4D4D; -fx-text-fill: white; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;"));
                submitButton.setOnMouseExited(ev -> submitButton.setStyle("-fx-background-color: #B30000; -fx-text-fill: white; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;"));

                submitButton.setOnAction(ev -> {
                    RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();
                    if (selectedButton == null) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an answer!", ButtonType.OK);
                        alert.showAndWait();
                        return;
                    }

                    String selectedAnswer = selectedButton.getText();
                    if (selectedAnswer.equals(currentQuestion.getAnswers().get(Integer.parseInt(currentQuestion.getCorrectAnswer()) - 1))) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Correct!", ButtonType.OK);
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong answer.", ButtonType.OK);
                        alert.showAndWait();
                    }
                    close(); // Close the overlay
                });
                
                // Level label
                Label diffLabel = new Label("Level: " + currentQuestion.getDifficulty());
                diffLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
                diffLabel.setTextFill(Color.web("#5b3924"));
                diffLabel.setAlignment(Pos.CENTER_LEFT);
                
                HBox labelBox = new HBox(diffLabel);
                labelBox.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(labelBox, Priority.ALWAYS);

                HBox buttonBox = new HBox(submitButton);
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                HBox.setHgrow(buttonBox, Priority.ALWAYS);

                // Add question and answers to the question box
                questionBox.getChildren().addAll(questionLabel, answersBox,labelBox, buttonBox);
                root.getChildren().add(questionBox);
            }
        });

        // Add components to the root container
        root.getChildren().addAll(diceImageView, difficultyLabel, rollDiceButton, questionBox);

        // Set up the scene
        Scene scene = new Scene(root, 500, 400);
        setScene(scene);
    }
}
