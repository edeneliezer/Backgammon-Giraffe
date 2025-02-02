package Model;

public interface QuestionSubject {

    void addObserver(QuestionObserver observer);
    void notifyCorrectAnswer();
    void notifyTimeExpired();

}
