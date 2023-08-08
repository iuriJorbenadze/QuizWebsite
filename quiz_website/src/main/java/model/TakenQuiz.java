package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class TakenQuiz {
    private Long takenQuizId;
    private Long userId;
    private Long quizId;
    private int score;
    private LocalDateTime attemptDate;
    private Duration timeTaken;
    private String feedback;
    private String status;

    // Default constructor
    public TakenQuiz() {
    }

    // Constructor
    public TakenQuiz(Long takenQuizId, Long userId, Long quizId, int score,
                     LocalDateTime attemptDate, Duration timeTaken,
                     String feedback, String status) {
        this.takenQuizId = takenQuizId;
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.attemptDate = attemptDate;
        this.timeTaken = timeTaken;
        this.feedback = feedback;
        this.status = status;
    }

    public Long getTakenQuizId() {
        return takenQuizId;
    }

    public void setTakenQuizId(Long takenQuizId) {
        this.takenQuizId = takenQuizId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(LocalDateTime attemptDate) {
        this.attemptDate = attemptDate;
    }

    public boolean hasPassed(int passingScore) {
        return this.score >= passingScore;
    }

    public Duration getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Duration timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TakenQuiz{" +
                "takenQuizId=" + takenQuizId +
                ", userId=" + userId +
                ", quizId=" + quizId +
                ", score=" + score +
                ", attemptDate=" + attemptDate +
                ", timeTaken=" + timeTaken +
                ", feedback='" + feedback + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
