package model;

import java.time.LocalDateTime;

public class Quiz {
    private Long quizId;
    private String title;
    private String description;
    private Long createdByUserId;
    private LocalDateTime createdDate;

    // Constructor
    //TODO might need to remove
    public Quiz() {
    }

    public Quiz(String title, String description, Long createdByUserId, LocalDateTime createdDate) {
        this.title = title;
        this.description = description;
        this.createdByUserId = createdByUserId;
        this.createdDate = createdDate;
    }

    public Quiz(Long quizId, String title, String description, Long createdByUserId, LocalDateTime createdDate) {
        this.quizId = quizId;
        this.title = title;
        this.description = description;
        this.createdByUserId = createdByUserId;
        this.createdDate = createdDate;
    }



    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }


    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "quizId=" + quizId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdByUserId=" + createdByUserId +
                ", createdDate=" + createdDate +
                '}';
    }
}
