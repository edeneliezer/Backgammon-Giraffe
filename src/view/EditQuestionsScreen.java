package view;

import Model.SysData;
import Model.Question;
import Model.QuestionBuilder;
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
import javafx.stage.StageStyle;

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
    private Scene scene;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private ComboBox<String> correctAnswerField;


    public EditQuestionsScreen(Stage stage, Scene previousScene) {
    	
        // Set up the modal dialog
        initOwner(stage);

     // Replace the TextInputDialog with a custom password prompt
        Stage passwordStage = new Stage();
        passwordStage.initModality(Modality.APPLICATION_MODAL);
        passwordStage.initOwner(stage);
        passwordStage.setTitle("Enter Password");
//        passwordStage.initStyle(StageStyle.UNDECORATED);
//        passwordStage.setOnCloseRequest(e -> {
//            e.consume(); // Consume the event to prevent default close behavior
//            passwordStage.close(); // Close the password stage
//            // Do nothing else (prevent opening the Edit Questions Screen)
//        });

        
      
        VBox passwordRoot = new VBox(10);
        passwordRoot.setPadding(new Insets(20));
        passwordRoot.setAlignment(Pos.CENTER);

        Label passwordLabel = new Label("Enter Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button submitPasswordButton = new Button("Submit");
        final boolean[] isPasswordCorrect = {false}; 
        
        submitPasswordButton.setOnAction(e -> {
            if (passwordField.getText().equals(PASSWORD)) {
                passwordStage.close();
                isPasswordCorrect[0] = true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect password!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        passwordRoot.getChildren().addAll(passwordLabel, passwordField, submitPasswordButton);
        Scene passwordScene = new Scene(passwordRoot, 300, 150);
        passwordStage.setScene(passwordScene);
//        passwordStage.showAndWait();
        
        // Handle the close button behavior
        passwordStage.setOnCloseRequest(e -> {
            isPasswordCorrect[0] = false; // Ensure password is marked as incorrect on close
            passwordStage.close();
        });

        // Show the password stage and wait for user interaction
        passwordStage.showAndWait();

        // Only proceed if the password is correct
        if (!isPasswordCorrect[0]) {
            return; // Exit constructor if password is incorrect or dialog is closed
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
        backButton.setCursor(javafx.scene.Cursor.HAND); // Set cursor to hand on hover

        // Change style on mouse hover
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #7A0000; -fx-text-fill: white;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #b30000; -fx-text-fill: white;"));

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
        questionField.setDisable(true);
        root.getChildren().add(createLabeledField("QUESTION", questionField));
        

        // Answers fields
        answer1Field = new TextField();
        answer2Field = new TextField();
        answer3Field = new TextField();
        answer4Field = new TextField();
        answer1Field.setDisable(true);
        answer2Field.setDisable(true);
        answer3Field.setDisable(true);
        answer4Field.setDisable(true);
        
        VBox answersBox = new VBox(10,
                createLabeledField("1.", answer1Field),
                createLabeledField("2.", answer2Field),
                createLabeledField("3.", answer3Field),
                createLabeledField("4.", answer4Field));
        answersBox.setPadding(new Insets(10));
//        answersBox.setDisable(true);
        root.getChildren().add(createLabeledBox("ANSWERS:", answersBox));

        // Level and Correct Answer
        HBox levelAndCorrectAnswerBox = new HBox(20);
        levelComboBox = new ComboBox<>();
        levelComboBox.getItems().addAll("1", "2", "3");
        levelComboBox.setPromptText("Level");
        levelComboBox.setDisable(true);
        correctAnswerField = new ComboBox<>();
        correctAnswerField.getItems().addAll("1", "2", "3", "4");
        correctAnswerField.setPromptText("Correct Answer");
        correctAnswerField.setDisable(true);
        correctAnswerField.setDisable(true);
        levelAndCorrectAnswerBox.getChildren().addAll(
                createLabeledField("LEVEL", levelComboBox),
                createLabeledField("CORRECT ANSWER", correctAnswerField)
        );
        root.getChildren().add(levelAndCorrectAnswerBox);

        // Buttons: Submit, Add, Delete
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        editButton = createButton("Edit");
        editButton.setOnAction(e -> {
            if(editButton.getText().equals("Edit")) {
                int index = questionsComboBox.getSelectionModel().getSelectedIndex();
                if (index == -1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a question to edit.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                addButton.setDisable(true);
                deleteButton.setDisable(true);
                editButton.setText("Submit");
                enableFields(true);
            } else {
                if(isAnyFieldEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "All fields must be filled out.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                editQuestion();
                enableFields(false);
                addButton.setDisable(false);
                deleteButton.setDisable(false);
                editButton.setText("Edit");
            }
        });

        addButton = createButton("Add");
        addButton.setOnAction(e -> {
            if(addButton.getText().equals("Add")) {
                addButton.setText("Submit");
                editButton.setDisable(true);
                deleteButton.setDisable(true);
                clearFields();
                questionsComboBox.setPromptText("Select a question...");
                enableFields(true);
            } else {
                if (isAnyFieldEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Incomplete Fields");
                    alert.setHeaderText("Some fields are empty.");
                    alert.setContentText("Do you want to cancel the process?");

                    ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    ButtonType continueButtonType = new ButtonType("Continue Editing", ButtonBar.ButtonData.OK_DONE);

                    alert.getButtonTypes().setAll(continueButtonType, cancelButtonType);
                    ButtonType result = alert.showAndWait().orElse(cancelButtonType);

                    if (result == cancelButtonType) {
                        clearFields();
                        enableFields(false);
                        addButton.setText("Add");
                        editButton.setDisable(false);
                        deleteButton.setDisable(false);
                        return;
                    } else {
                        return; // Let the user continue editing
                    }
                }
                saveNewQuestion();
                deleteButton.setDisable(false);
                editButton.setDisable(false);
                enableFields(false);
                addButton.setText("Add");
            }
        });

        deleteButton = createButton("Delete");
        deleteButton.setOnAction(e -> deleteQuestion());

        buttonBox.getChildren().addAll(editButton, addButton, deleteButton);
        root.getChildren().add(buttonBox);

        // Initialize the 'scene' field
        this.scene = new Scene(root, 800, 650);

        // Set the scene to the stage
        stage.setScene(this.scene); // Switch to the new scene
    }
    
    // Creating a method to style buttons consistently
    private Button createButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        button.setStyle("-fx-background-color: #b30000; -fx-text-fill: white;");
        button.setCursor(javafx.scene.Cursor.HAND); // Set cursor to hand on hover
        // Hover effects
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #7A0000; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #b30000; -fx-text-fill: white;"));
        return button;
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
        correctAnswerField.setValue(question.getCorrectAnswer());
    }

    private void editQuestion() {
        int index = questionsComboBox.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a question to edit.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            Question editedQuestion = new QuestionBuilder()
                    .setQuestionText(questionField.getText())
                    .addAnswer(answer1Field.getText())
                    .addAnswer(answer2Field.getText())
                    .addAnswer(answer3Field.getText())
                    .addAnswer(answer4Field.getText())
                    .setCorrectAnswer(correctAnswerField.getValue())
                    .setDifficulty(levelComboBox.getValue())
                    .build();

            questions.set(index, editedQuestion);
            saveQuestionsToJSON();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Question edited successfully!", ButtonType.OK);
            alert.showAndWait();
        } catch (IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    
    private void saveNewQuestion() {
        try {
            Question newQuestion = new QuestionBuilder()
                    .setQuestionText(questionField.getText())
                    .addAnswer(answer1Field.getText())
                    .addAnswer(answer2Field.getText())
                    .addAnswer(answer3Field.getText())
                    .addAnswer(answer4Field.getText())
                    .setCorrectAnswer(correctAnswerField.getValue())
                    .setDifficulty(levelComboBox.getValue())
                    .build();

            SysData.saveQuestion(newQuestion);
            questions.add(newQuestion);
            questionsComboBox.getItems().add(newQuestion.getQuestion());

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Question added successfully!", ButtonType.OK);
            alert.showAndWait();
        } catch (IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    
    private void deleteQuestion() {
        int index = questionsComboBox.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a question to delete.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Remove the selected question
        questions.remove(index);
        questionsComboBox.getItems().remove(index);

        // Save to JSON
        saveQuestionsToJSON();

        // Clear fields and update ComboBox
        clearFields();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Question deleted successfully!", ButtonType.OK);
        alert.showAndWait();
    }

    private void clearFields() {
        questionField.clear();
        answer1Field.clear();
        answer2Field.clear();
        answer3Field.clear();
        answer4Field.clear();
        levelComboBox.setValue(null);
        correctAnswerField.setValue(null);;
        questionsComboBox.getSelectionModel().clearSelection();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void enableFields(boolean enable) {
        questionField.setDisable(!enable);
        answer1Field.setDisable(!enable);
        answer2Field.setDisable(!enable);
        answer3Field.setDisable(!enable);
        answer4Field.setDisable(!enable);
        levelComboBox.setDisable(!enable);
        correctAnswerField.setDisable(!enable);
    }
    
    private boolean isAnyFieldEmpty() {
        return questionField.getText().isEmpty() ||
               answer1Field.getText().isEmpty() ||
               answer2Field.getText().isEmpty() ||
               answer3Field.getText().isEmpty() ||
               answer4Field.getText().isEmpty() ||
               correctAnswerField.getValue() == null ||
               levelComboBox.getValue() == null;
    }
    
    private void addNewQuestion() {
        if (isAnyFieldEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "All fields must be filled out to add a new question.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Question newQuestion = new Question(
                questionField.getText(),
                Arrays.asList(
                        answer1Field.getText(),
                        answer2Field.getText(),
                        answer3Field.getText(),
                        answer4Field.getText()
                ),
                correctAnswerField.getValue(),
                levelComboBox.getValue()
        );

        questions.add(newQuestion);
        questionsComboBox.getItems().add(newQuestion.getQuestion());
        saveQuestionsToJSON();
        clearFields();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "New question added successfully!", ButtonType.OK);
        alert.showAndWait();
    }

}
