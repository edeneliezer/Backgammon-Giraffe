package view;

import Model.GameModel;
import Model.Question;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class QuestionOverlay extends Stage {

    public QuestionOverlay(Stage parentStage) {
        // Set up the modal dialog
        initModality(Modality.APPLICATION_MODAL);
        initOwner(parentStage);

        // Load questions
        List<Question> questions = GameModel.loadQuestions();
        if (questions.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No questions available!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Select a random question
        Question currentQuestion = questions.get((int) (Math.random() * questions.size()));

        // Root container
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f0f0f0); " +
                      "-fx-border-color: #5b3924; -fx-border-width: 5; -fx-border-radius: 20; -fx-background-radius: 20;");
        root.setPrefHeight(400);

        // Add shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(5);
        dropShadow.setColor(Color.rgb(90, 90, 90, 0.5));
        root.setEffect(dropShadow);

        // Question label
        Label questionLabel = new Label("Question:\n" + currentQuestion.getQuestion());
        questionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        questionLabel.setTextFill(Color.web("#333333"));
        questionLabel.setWrapText(true);

        // Answers section
        ToggleGroup toggleGroup = new ToggleGroup();
        VBox answersBox = new VBox(10);
        answersBox.setAlignment(Pos.TOP_LEFT);
        answersBox.setPadding(new Insets(10, 20, 10, 20));

        for (String answer : currentQuestion.getAnswers()) {
            RadioButton answerButton = new RadioButton(answer);
            answerButton.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
            answerButton.setTextFill(Color.web("#5b3924"));
            answerButton.setStyle("-fx-padding: 5; -fx-background-radius: 5;");
            answerButton.setToggleGroup(toggleGroup);

            // Add hover effect
            answerButton.setOnMouseEntered(e -> answerButton.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 5; -fx-background-radius: 5;"));
            answerButton.setOnMouseExited(e -> answerButton.setStyle("-fx-padding: 5; -fx-background-radius: 5;"));

            answersBox.getChildren().add(answerButton);
        }

        // Bottom section: Level label on the left and Submit button on the right
        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(10, 20, 10, 20));
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(20);

        // Level label
        Label difficultyLabel = new Label("Level: " + currentQuestion.getDifficulty());
        difficultyLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        difficultyLabel.setTextFill(Color.web("#5b3924"));
        difficultyLabel.setAlignment(Pos.CENTER_LEFT);

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        submitButton.setStyle("-fx-background-color: #b30000; -fx-text-fill: white; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;");
        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle("-fx-background-color: #b30000; -fx-text-fill: white; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;"));

        submitButton.setOnAction(e -> {
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

        // Combine label and button into the bottom box
        HBox labelBox = new HBox(difficultyLabel);
        labelBox.setAlignment(Pos.CENTER_LEFT);
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.getChildren().addAll(labelBox, buttonBox);

        // Combine all sections into the root container
        root.getChildren().addAll(questionLabel, answersBox, bottomBox);

        // Set up the scene
        Scene scene = new Scene(root, 500, 400);
        setScene(scene);
    }
}
