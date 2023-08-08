package model;

public class Option {
    private Long optionId;
    private Long questionId;
    private String content;

    // Constructor with ID, mainly for retrieval from DB
    public Option(Long optionId, Long questionId, String content) {
        this.optionId = optionId;
        this.questionId = questionId;
        this.content = content;
    }

    // Constructor without ID, for creating new options
    public Option(Long questionId, String content) {
        this.questionId = questionId;
        this.content = content;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Option{" +
                "optionId=" + optionId +
                ", questionId=" + questionId +
                ", content='" + content + '\'' +
                '}';
    }
}
