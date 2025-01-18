package view;

import Model.GameModel;
import Model.Question;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
        root.setStyle("-fx-background-color: #fefaf4; -fx-border-color: #5b3924; -fx-border-width: 5; -fx-border-radius: 20; -fx-background-radius: 20;");
        root.setPrefHeight(400);

        // Question label
        Label questionLabel = new Label("Question:\n" + currentQuestion.getQuestion());
        questionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        questionLabel.setTextFill(Color.web("#5b3924"));
        questionLabel.setWrapText(true);

        // Answers section
        ToggleGroup toggleGroup = new ToggleGroup();
        VBox answersBox = new VBox(10);
        answersBox.setAlignment(Pos.CENTER_LEFT);
        answersBox.setPadding(new Insets(10, 20, 10, 20));

        for (String answer : currentQuestion.getAnswers()) {
            RadioButton answerButton = new RadioButton(answer);
            answerButton.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
            answerButton.setTextFill(Color.web("#5b3924"));
            answerButton.setToggleGroup(toggleGroup);
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
        submitButton.setStyle("-fx-background-color: #b30000; -fx-text-fill: white; -fx-padding: 5 20 5 20; -fx-border-radius: 10; -fx-background-radius: 10;");
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
        
        HBox submit = new HBox(submitButton);
        submit.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(submit, Priority.ALWAYS);

        HBox label = new HBox(difficultyLabel);
        label.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(label, Priority.ALWAYS);

        // Aligning the elements
        HBox.setHgrow(difficultyLabel, Priority.ALWAYS); // Push label to the left
        HBox.setHgrow(submitButton, Priority.ALWAYS); // Push button to the right

        // Add label and button to the bottom box
        bottomBox.getChildren().addAll(label, submit);

        // Combine all sections into the root container
        root.getChildren().addAll(questionLabel, answersBox, bottomBox);

        // Set up the scene
        Scene scene = new Scene(root, 500, 400);
        setScene(scene);
    }
}
