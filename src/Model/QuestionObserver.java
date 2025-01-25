package Model;

public interface QuestionObserver {
    void onCorrectAnswer();
    void onWrongAnswer();
    void onTimeExpired();
}