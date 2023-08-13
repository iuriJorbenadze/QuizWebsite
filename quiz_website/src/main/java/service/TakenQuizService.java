package service;


import Dao.TakenQuizDAO;
import model.TakenQuiz;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TakenQuizService {

    private final TakenQuizDAO takenQuizDAO;
    private static final int PASSING_SCORE = 5;  // you can set a threshold for passing score

    public TakenQuizService() {
        this.takenQuizDAO = new TakenQuizDAO();
    }

    public Long createTakenQuiz(TakenQuiz takenQuiz) {
        return takenQuizDAO.createTakenQuiz(takenQuiz);
    }

    public TakenQuiz getTakenQuizById(Long id) {
        return takenQuizDAO.getTakenQuizById(id);
    }

    public List<TakenQuiz> getAllTakenQuizzesForUser(Long userId) {
        return takenQuizDAO.getAllTakenQuizzesForUser(userId);
    }

    public boolean deleteTakenQuizById(Long takenQuizId) {
        return takenQuizDAO.deleteTakenQuizById(takenQuizId);
    }

    public List<TakenQuiz> getAllTakenQuizzesForQuiz(Long quizId) {
        return takenQuizDAO.getAllTakenQuizzesForQuiz(quizId);
    }

    public boolean updateTakenQuiz(TakenQuiz takenQuiz) {
        return takenQuizDAO.updateTakenQuiz(takenQuiz);
    }

    public TakenQuiz getLatestTakenQuizForUser(Long userId) {
        return takenQuizDAO.getLatestTakenQuizForUser(userId);
    }

    public List<TakenQuiz> getTakenQuizzesForUserOnDate(Long userId, LocalDateTime date) {
        return takenQuizDAO.getTakenQuizzesForUserOnDate(userId, date);
    }

    public double calculateAverageScoreForUser(Long userId) {
        List<TakenQuiz> quizzes = getAllTakenQuizzesForUser(userId);
        return quizzes.stream().mapToInt(TakenQuiz::getScore).average().orElse(0.0);
    }

    public List<TakenQuiz> getTopScoringUsersForQuiz(Long quizId, int limit) {
        List<TakenQuiz> quizzes = takenQuizDAO.getAllTakenQuizzesForQuiz(quizId);
        return quizzes.stream().sorted((q1, q2) -> Integer.compare(q2.getScore(), q1.getScore())).limit(limit).collect(Collectors.toList());
    }

    public List<TakenQuiz> getAllFailedQuizzesForUser(Long userId) {
        List<TakenQuiz> quizzes = getAllTakenQuizzesForUser(userId);
        return quizzes.stream().filter(q -> q.getScore() < PASSING_SCORE).collect(Collectors.toList());
    }

    public void updateFeedbackForLowScoringQuizzes(Long userId, int threshold, String feedback) {
        List<TakenQuiz> quizzes = getAllTakenQuizzesForUser(userId);
        for (TakenQuiz quiz : quizzes) {
            if (quiz.getScore() < threshold) {
                quiz.setFeedback(feedback);
                updateTakenQuiz(quiz);
            }
        }
    }
    public List<TakenQuiz> getTopScorersByQuiz(Long quizId, int limit) {
        List<TakenQuiz> quizzes = takenQuizDAO.getAllTakenQuizzesForQuiz(quizId);

        // Sort by score and if scores are same, then by time taken
        return quizzes.stream()
                .sorted(Comparator.comparing(TakenQuiz::getScore).reversed()
                        .thenComparing(TakenQuiz::getTimeTaken))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<TakenQuiz> getQuizzesTakenInLastMonth(Long userId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        return takenQuizDAO.getTakenQuizzesForUserOnDate(userId, oneMonthAgo);
    }

    public Map<Long, Long> getMostAttemptedQuizzes(int limit) {
        return takenQuizDAO.getMostAttemptedQuizzes(limit);
    }

    public List<TakenQuiz> getAllTakenQuizzes() {
        return takenQuizDAO.getAllTakenQuizzes();
    }

    public List<TakenQuiz> getRecentQuizTakers(int limit) {
        return takenQuizDAO.getRecentQuizTakers(limit);
    }
    public List<TakenQuiz> getQuizzesWithScoresAboveThreshold(int threshold) {
        return takenQuizDAO.getAllTakenQuizzes().stream()
                .filter(quiz -> quiz.getScore() > threshold)
                .collect(Collectors.toList());
    }


    public double getAverageTimeTakenForQuiz(Long quizId) {
        List<TakenQuiz> quizzesForId = takenQuizDAO.getAllTakenQuizzesForQuiz(quizId);
        return quizzesForId.stream()
                .mapToLong(quiz -> quiz.getTimeTaken().toSeconds())
                .average()
                .orElse(0.0);
    }

    public Long getQuizWithMostRetakes() {
        return takenQuizDAO.getAllTakenQuizzes().stream()
                .collect(Collectors.groupingBy(TakenQuiz::getQuizId, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
