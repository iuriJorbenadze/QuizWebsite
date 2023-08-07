package model;

public class Question {
    private Long questionId;
    private Long quizId;
    private String content;
    private String correctAnswer;

    // Constructor
    public Question(Long questionId, Long quizId, String content, String correctAnswer) {
        this.questionId = questionId;
        this.quizId = quizId;
        this.content = content;
        this.correctAnswer = correctAnswer;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", quizId=" + quizId +
                ", content='" + content + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
