package model;

import java.util.List;

public class Question {

    //TODO come up with a solution for different variations of questions, fill in question, open ended and so on

    private Long questionId;
    private Long quizId;
    private String content;
    private List<String> options;  // List of answer options
    private String correctAnswer;  // Assuming single correct answer for now

    // Constructor without questionId
    public Question(Long quizId, String content, List<String> options, String correctAnswer) {
        this.quizId = quizId;
        this.content = content;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }



    // Constructor
    public Question(Long questionId, Long quizId, String content, List<String> options, String correctAnswer) {
        this.questionId = questionId;
        this.quizId = quizId;
        this.content = content;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }



    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }



    // Getter and Setter for questionId
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
                ", options=" + options.toString() +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
