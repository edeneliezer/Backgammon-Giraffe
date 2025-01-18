package Model;

import java.util.List;

public class Question {
    private String question;
    private List<String> answers;
    private String correctAnswer;
    private String difficulty;

    public Question(String question, List<String> answers, String correctAnswer, String difficulty) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
