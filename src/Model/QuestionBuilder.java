package Model;

import java.util.ArrayList;
import java.util.List;

public class QuestionBuilder {
    private String questionText;
    private List<String> answers = new ArrayList<>();
    private String correctAnswer;
    private String difficulty;

    public QuestionBuilder setQuestionText(String questionText) {
        this.questionText = questionText;
        return this;
    }

    public QuestionBuilder addAnswer(String answer) {
        answers.add(answer);
        return this;
    }

    public QuestionBuilder setAnswers(List<String> answers) {
        this.answers = new ArrayList<>(answers);
        return this;
    }

    public QuestionBuilder setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }

    public QuestionBuilder setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public Question build() {
        // Validation logic can go here
        if (questionText == null || questionText.isEmpty()) {
            throw new IllegalStateException("Question text cannot be empty.");
        }
        if (answers.size() < 2) {
            throw new IllegalStateException("A question must have at least two answers.");
        }
        if (correctAnswer == null || correctAnswer.isEmpty()) {
            throw new IllegalStateException("Correct answer must be one of the provided answers.");
        }
        if (difficulty == null || difficulty.isEmpty()) {
            throw new IllegalStateException("Difficulty level must be specified.");
        }
        return new Question(questionText, answers, correctAnswer, difficulty);
    }
}
