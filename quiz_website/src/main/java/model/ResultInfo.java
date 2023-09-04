package model;

public class ResultInfo {
    private Long questionId;
    private String submittedAnswer;
    private String correctAnswer;
    private boolean isCorrect;


    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getSubmittedAnswer() {
        return submittedAnswer;
    }

    public void setSubmittedAnswer(String submittedAnswer) {
        this.submittedAnswer = submittedAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "questionId=" + questionId +
                ", submittedAnswer='" + submittedAnswer + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
