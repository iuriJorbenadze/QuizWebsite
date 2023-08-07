package model;

import java.time.LocalDateTime;

public class TakenQuiz {
    private Long takenQuizId;
    private Long userId;
    private Long quizId;
    private int score;
    private LocalDateTime attemptDate;

    // Constructor
    public TakenQuiz(Long takenQuizId, Long userId, Long quizId, int score, LocalDateTime attemptDate) {
        this.takenQuizId = takenQuizId;
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.attemptDate = attemptDate;
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

    @Override
    public String toString() {
        return "TakenQuiz{" +
                "takenQuizId=" + takenQuizId +
                ", userId=" + userId +
                ", quizId=" + quizId +
                ", score=" + score +
                ", attemptDate=" + attemptDate +
                '}';
    }
}
