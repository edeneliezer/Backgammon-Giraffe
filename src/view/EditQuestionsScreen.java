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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditQuestionsScreen extends Stage {

    private static final String PASSWORD = "admin123"; // Password to access the screen
    private List<Question> questions;
    private ComboBox<String> questionsComboBox;
    private TextField questionField;
    private TextField answer1Field, answer2Field, answer3Field, answer4Field;
    private ComboBox<String> levelComboBox;
    private TextField correctAnswerField;
    private  Scene scene;

    public EditQuestionsScreen(Stage stage, Scene previousScene) {
		// Set up the modal dialog
        initOwner(stage);

        // Prompt for password
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setHeaderText("Enter Password");
        passwordDialog.setContentText("Password:");
        passwordDialog.initModality(Modality.APPLICATION_MODAL);
        passwordDialog.initOwner(stage);

        String password = passwordDialog.showAndWait().orElse("");
        if (!password.equals(PASSWORD)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect password!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Load questions from JSON
        questions = loadQuestions();

        // Root layout
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #fefaf4;");
        root.setAlignment(Pos.TOP_CENTER);

        // Back button
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        backButton.setStyle("-fx-background-color: #b30000; -fx-text-fill: white;");
        backButton.setOnAction(e -> {
            stage.setScene(previousScene); // Switch back to the previous scene
        });
        HBox label = new HBox(backButton);
        label.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(label, Priority.ALWAYS);
        
        root.getChildren().add(label);

        // Title
        Label titleLabel = new Label("QUESTIONS:");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#5b3924"));
        root.getChildren().add(titleLabel);

        // Questions ComboBox
        questionsComboBox = new ComboBox<>();
        questionsComboBox.getItems().addAll(getQuestionsText());
        questionsComboBox.setPromptText("Select a question...");
        questionsComboBox.setOnAction(e -> populateFields());
        root.getChildren().add(questionsComboBox);

        // Question field
        questionField = new TextField();
        questionField.setPromptText("Question...");
        root.getChildren().add(createLabeledField("QUESTION", questionField));

        // Answers fields
        answer1Field = new TextField();
        answer2Field = new TextField();
        answer3Field = new TextField();
        answer4Field = new TextField();
        VBox answersBox = new VBox(10,
                createLabeledField("1.", answer1Field),
                createLabeledField("2.", answer2Field),
                createLabeledField("3.", answer3Field),
                createLabeledField("4.", answer4Field));
        answersBox.setPadding(new Insets(10));
        root.getChildren().add(createLabeledBox("ANSWERS:", answersBox));

        // Level and Correct Answer
        HBox levelAndCorrectAnswerBox = new HBox(20);
        levelComboBox = new ComboBox<>();
        levelComboBox.getItems().addAll("1", "2", "3");
        levelComboBox.setPromptText("Level");
        correctAnswerField = new TextField();
        correctAnswerField.setPromptText("Correct Answer");
        levelAndCorrectAnswerBox.getChildren().addAll(
                createLabeledField("LEVEL", levelComboBox),
                createLabeledField("CORRECT ANSWER", correctAnswerField)
        );
        root.getChildren().add(levelAndCorrectAnswerBox);

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        submitButton.setStyle("-fx-background-color: #b30000; -fx-text-fill: white;");
        submitButton.setOnAction(e -> saveQuestion());
        root.getChildren().add(submitButton);

        // Initialize the 'scene' field
        this.scene = new Scene(root, 800, 600);

        // Set the scene to the stage
        stage.setScene(this.scene); // Switch to the new scene
    }

    private VBox createLabeledField(String labelText, TextField textField) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#5b3924"));
        VBox box = new VBox(5, label, textField);
        return box;
    }

    private VBox createLabeledField(String labelText, ComboBox<String> comboBox) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#5b3924"));
        VBox box = new VBox(5, label, comboBox);
        return box;
    }

    private VBox createLabeledBox(String labelText, VBox innerBox) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#5b3924"));
        VBox box = new VBox(10, label, innerBox);
        return box;
    }

    private List<String> getQuestionsText() {
        List<String> questionTexts = new ArrayList<>();
        for (Question q : questions) {
            questionTexts.add(q.getQuestion());
        }
        return questionTexts;
    }

    private void populateFields() {
        int index = questionsComboBox.getSelectionModel().getSelectedIndex();
        if (index == -1) return;

        Question question = questions.get(index);
        questionField.setText(question.getQuestion());
        List<String> answers = question.getAnswers();
        answer1Field.setText(answers.size() > 0 ? answers.get(0) : "");
        answer2Field.setText(answers.size() > 1 ? answers.get(1) : "");
        answer3Field.setText(answers.size() > 2 ? answers.get(2) : "");
        answer4Field.setText(answers.size() > 3 ? answers.get(3) : "");
        levelComboBox.setValue(question.getDifficulty());
        correctAnswerField.setText(question.getCorrectAnswer());
    }

    private void saveQuestion() {
        int index = questionsComboBox.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a question to edit.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Get the selected question
        Question question = questions.get(index);

        // Update the question details
        question.setQuestion(questionField.getText());
        question.setAnswers(Arrays.asList(
                answer1Field.getText(),
                answer2Field.getText(),
                answer3Field.getText(),
                answer4Field.getText()
        ));
        question.setDifficulty(levelComboBox.getValue());
        question.setCorrectAnswer(correctAnswerField.getText());

        // Save updated questions to JSON
        saveQuestionsToJSON();

        // Show success alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Question saved successfully!", ButtonType.OK);
        alert.showAndWait();
    }

    private List<Question> loadQuestions() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("questions.json"))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            JSONObject jsonObject = new JSONObject(content.toString());
            JSONArray questionsArray = jsonObject.getJSONArray("questions");

            List<Question> questionList = new ArrayList<>();
            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject qObject = questionsArray.getJSONObject(i);

                // Parse question and answers correctly
                String questionText = qObject.getString("question");
                JSONArray answersArray = qObject.getJSONArray("answers");
                List<String> answers = new ArrayList<>();
                for (int j = 0; j < answersArray.length(); j++) {
                    answers.add(answersArray.getString(j)); // Extract as String
                }
                String correctAnswer = qObject.getString("correct_ans");
                String difficulty = qObject.getString("difficulty");

                // Create Question object
                Question question = new Question(questionText, answers, correctAnswer, difficulty);
                questionList.add(question);
            }
            return questionList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    private void saveQuestionsToJSON() {
        try {
            JSONArray questionsArray = new JSONArray();
            for (Question question : questions) {
                JSONObject qObject = new JSONObject();
                qObject.put("question", question.getQuestion());
                qObject.put("answers", question.getAnswers());
                qObject.put("correct_ans", question.getCorrectAnswer());
                qObject.put("difficulty", question.getDifficulty());
                questionsArray.put(qObject);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("questions", questionsArray);

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("questions.json"))) {
                writer.write(jsonObject.toString(4));
            }        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 
}
