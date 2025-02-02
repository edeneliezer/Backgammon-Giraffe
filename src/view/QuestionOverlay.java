package view;

import Model.DifficultyDice;
import Model.SysData;
import Model.Question;
import Model.QuestionObserver;
import Model.QuestionSubject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class QuestionOverlay extends Stage implements QuestionSubject {
    private Question currentQuestion;
    private boolean isDiceRolled = false;
    private Label timerLabel;
    private Timeline timer;
	private List<QuestionObserver> observers = new ArrayList<>();

    public QuestionOverlay(Stage parentStage) {
        initModality(Modality.APPLICATION_MODAL);
        initOwner(parentStage);

        DifficultyDice dice = new DifficultyDice();

        List<Question> questions = SysData.loadQuestions();
        if (questions.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No questions available!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #FAEBD7, #FFF8DC); " +
                      "-fx-border-color: #5B3924; -fx-border-width: 5; -fx-border-radius: 20; -fx-background-radius: 20;");
        root.setPrefSize(800, 600);

        ImageView diceImageView = new ImageView();
        diceImageView.setFitWidth(120);
        diceImageView.setFitHeight(120);

        Label difficultyLabel = new Label();
        difficultyLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        difficultyLabel.setTextFill(Color.web("#5B3924"));

        VBox questionBox = new VBox(10);
        questionBox.setAlignment(Pos.TOP_LEFT);

        Button rollDiceButton = createButton("Roll Question Dice", "#4CAF50", "#367B3A");

        rollDiceButton.setOnAction(e -> {
            if (!isDiceRolled) {
                String difficulty = dice.roll();
                diceImageView.setImage(new Image("game/img/dices/black/" + difficulty.toLowerCase() + ".png"));
                difficultyLabel.setText("You rolled: " + difficulty);
                rollDiceButton.setText("Next");
                isDiceRolled = true;
            } else {
                currentQuestion = questions.get((int) (Math.random() * questions.size()));

                questionBox.getChildren().clear();
                root.getChildren().clear();

                handleQuestionDisplay(questionBox, root);
            }
        });

        root.getChildren().addAll(diceImageView, difficultyLabel, rollDiceButton, questionBox);
        Scene scene = new Scene(root, 1000, 400);
        setScene(scene);

        // Prevent window from closing with the "X" button
        setOnCloseRequest(event -> event.consume());
    }

    private Button createButton(String text, String normalColor, String hoverColor) {
        Button button = new Button(text);
        button.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        button.setStyle("-fx-background-color: " + normalColor + "; -fx-text-fill: white;");
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + normalColor + "; -fx-text-fill: white;"));
        return button;
    }

    private void handleQuestionDisplay(VBox questionBox, VBox root) {
        Label questionLabel = new Label("Question:\n" + currentQuestion.getQuestion());
        questionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        questionLabel.setTextFill(Color.web("#333333"));

        ToggleGroup toggleGroup = new ToggleGroup();
        VBox answersBox = new VBox(10);
        for (String answer : currentQuestion.getAnswers()) {
            RadioButton answerButton = new RadioButton(answer);
            answerButton.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
            answerButton.setTextFill(Color.web("#5B3924"));
            answerButton.setToggleGroup(toggleGroup);
            answersBox.getChildren().add(answerButton);
        }

        Button submitButton = createButton("Submit", "#B30000", "#7A0000");
        submitButton.setOnAction(ev -> submitAnswer(toggleGroup));

        timerLabel = new Label("Time left: 30 seconds");
        timerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        timerLabel.setTextFill(Color.web("#B30000"));

        startCountdown();

        questionBox.getChildren().addAll(timerLabel, questionLabel, answersBox, submitButton);
        root.getChildren().add(questionBox);
    }

    private void submitAnswer(ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an answer!", ButtonType.OK).showAndWait();
            return;
        }

        // Get the selected answer
        RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();
        String selectedAnswer = selectedButton.getText().trim();

        // Get the correct answer text using the correct answer index
        int correctAnswerIndex = Integer.parseInt(currentQuestion.getCorrectAnswer().trim()) - 1;
        String correctAnswer = currentQuestion.getAnswers().get(correctAnswerIndex).trim();

        // Compare the selected answer with the correct answer
        if (selectedAnswer.equals(correctAnswer)) {
            Alert correctAlert = new Alert(Alert.AlertType.INFORMATION, "Correct answer! Well done.", ButtonType.OK);
            correctAlert.showAndWait();
            notifyCorrectAnswer();
        } else {
            Alert wrongAlert = new Alert(Alert.AlertType.ERROR, "Wrong answer!  Opponent gets your turn.", ButtonType.OK);
            wrongAlert.showAndWait();
        }
        close();
    }
    private void startCountdown() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timer.setCycleCount(30);
        timer.play();
    }

    private void updateTimer() {
        int timeLeft = Integer.parseInt(timerLabel.getText().replaceAll("[^0-9]", "")) - 1;
        timerLabel.setText("Time left: " + timeLeft + " seconds");
        if (timeLeft <= 0) {
            notifyTimeExpired();
            close();
        }
    }

    @Override
    public void addObserver(QuestionObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyCorrectAnswer() {
        stopTimer();
        for (QuestionObserver observer : observers) {
            observer.onCorrectAnswer();
        }
    }

   

    @Override
    public void notifyTimeExpired() {
        stopTimer();
        for (QuestionObserver observer : observers) {
            observer.onTimeExpired();
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
    
}
