package Dao;

import model.TakenQuiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TakenQuizDAO extends AbstractDAO{
    public Long createTakenQuiz(TakenQuiz takenQuiz) {
        String insertSQL = "INSERT INTO TakenQuiz(userId, quizId, score, dateTaken, timeTaken, feedback, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = getConnection().prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, takenQuiz.getUserId());
            pstmt.setLong(2, takenQuiz.getQuizId());
            pstmt.setInt(3, takenQuiz.getScore());
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(takenQuiz.getAttemptDate()));
            pstmt.setLong(5, takenQuiz.getTimeTaken().getSeconds());
            pstmt.setString(6, takenQuiz.getFeedback());
            pstmt.setString(7, takenQuiz.getStatus());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating takenQuiz failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    takenQuiz.setTakenQuizId(generatedKeys.getLong(1));
                    return takenQuiz.getTakenQuizId();
                } else {
                    throw new SQLException("Creating takenQuiz failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            // Handle exception
        }
        return null;
    }

    public TakenQuiz getTakenQuizById(Long id) {
        TakenQuiz takenQuiz = null;
        String selectSQL = "SELECT * FROM TakenQuiz WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(selectSQL)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                takenQuiz = new TakenQuiz();
                takenQuiz.setTakenQuizId(rs.getLong("id"));
                takenQuiz.setUserId(rs.getLong("userId"));
                takenQuiz.setQuizId(rs.getLong("quizId"));
                takenQuiz.setScore(rs.getInt("score"));
                takenQuiz.setAttemptDate(rs.getTimestamp("dateTaken").toLocalDateTime());
                takenQuiz.setTimeTaken(Duration.ofSeconds(rs.getLong("timeTaken")));
                takenQuiz.setFeedback(rs.getString("feedback"));
                takenQuiz.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return takenQuiz;
    }

    public List<TakenQuiz> getAllTakenQuizzesForUser(Long userId) {
        List<TakenQuiz> takenQuizzes = new ArrayList<>();
        String selectSQL = "SELECT * FROM TakenQuiz WHERE userId = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(selectSQL)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TakenQuiz takenQuiz = new TakenQuiz();
                takenQuiz.setTakenQuizId(rs.getLong("id"));
                takenQuiz.setUserId(rs.getLong("userId"));
                takenQuiz.setQuizId(rs.getLong("quizId"));
                takenQuiz.setScore(rs.getInt("score"));
                takenQuiz.setAttemptDate(rs.getTimestamp("dateTaken").toLocalDateTime());
                takenQuiz.setTimeTaken(Duration.ofSeconds(rs.getLong("timeTaken")));
                takenQuiz.setFeedback(rs.getString("feedback"));
                takenQuiz.setStatus(rs.getString("status"));

                takenQuizzes.add(takenQuiz);
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return takenQuizzes;
    }

    public boolean deleteTakenQuizById(Long takenQuizId) {
        String deleteSQL = "DELETE FROM TakenQuiz WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(deleteSQL)) {
            pstmt.setLong(1, takenQuizId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            // Handle exception
        }
        return false;
    }

    public List<TakenQuiz> getAllTakenQuizzesForQuiz(Long quizId) {
        List<TakenQuiz> takenQuizzes = new ArrayList<>();
        String selectSQL = "SELECT * FROM TakenQuiz WHERE quizId = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(selectSQL)) {
            pstmt.setLong(1, quizId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TakenQuiz takenQuiz = new TakenQuiz();
                takenQuiz.setTakenQuizId(rs.getLong("id"));
                takenQuiz.setUserId(rs.getLong("userId"));
                takenQuiz.setQuizId(rs.getLong("quizId"));
                takenQuiz.setScore(rs.getInt("score"));
                takenQuiz.setAttemptDate(rs.getTimestamp("dateTaken").toLocalDateTime());
                takenQuiz.setTimeTaken(Duration.ofSeconds(rs.getLong("timeTaken")));
                takenQuiz.setFeedback(rs.getString("feedback"));
                takenQuiz.setStatus(rs.getString("status"));

                takenQuizzes.add(takenQuiz);
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return takenQuizzes;
    }

    public boolean updateTakenQuiz(TakenQuiz takenQuiz) {
        String updateSQL = "UPDATE TakenQuiz SET userId=?, quizId=?, score=?, dateTaken=?, timeTaken=?, feedback=?, status=? WHERE id=?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(updateSQL)) {
            pstmt.setLong(1, takenQuiz.getUserId());
            pstmt.setLong(2, takenQuiz.getQuizId());
            pstmt.setInt(3, takenQuiz.getScore());
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(takenQuiz.getAttemptDate()));
            pstmt.setLong(5, takenQuiz.getTimeTaken().getSeconds());
            pstmt.setString(6, takenQuiz.getFeedback());
            pstmt.setString(7, takenQuiz.getStatus());
            pstmt.setLong(8, takenQuiz.getTakenQuizId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            // Handle exception
        }
        return false;
    }

    public TakenQuiz getLatestTakenQuizForUser(Long userId) {
        TakenQuiz takenQuiz = null;
        String selectSQL = "SELECT * FROM TakenQuiz WHERE userId = ? ORDER BY dateTaken DESC LIMIT 1";

        try (PreparedStatement pstmt = getConnection().prepareStatement(selectSQL)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                takenQuiz = new TakenQuiz();
                takenQuiz.setTakenQuizId(rs.getLong("id"));
                takenQuiz.setUserId(rs.getLong("userId"));
                takenQuiz.setQuizId(rs.getLong("quizId"));
                takenQuiz.setScore(rs.getInt("score"));
                takenQuiz.setAttemptDate(rs.getTimestamp("dateTaken").toLocalDateTime());
                takenQuiz.setTimeTaken(Duration.ofSeconds(rs.getLong("timeTaken")));
                takenQuiz.setFeedback(rs.getString("feedback"));
                takenQuiz.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return takenQuiz;
    }

    public List<TakenQuiz> getTakenQuizzesForUserOnDate(Long userId, LocalDateTime date) {
        List<TakenQuiz> takenQuizzes = new ArrayList<>();
        String selectSQL = "SELECT * FROM TakenQuiz WHERE userId = ? AND DATE(dateTaken) = DATE(?)";

        try (PreparedStatement pstmt = getConnection().prepareStatement(selectSQL)) {
            pstmt.setLong(1, userId);
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TakenQuiz takenQuiz = new TakenQuiz();
                takenQuiz.setTakenQuizId(rs.getLong("id"));
                takenQuiz.setUserId(rs.getLong("userId"));
                takenQuiz.setQuizId(rs.getLong("quizId"));
                takenQuiz.setScore(rs.getInt("score"));
                takenQuiz.setAttemptDate(rs.getTimestamp("dateTaken").toLocalDateTime());
                takenQuiz.setTimeTaken(Duration.ofSeconds(rs.getLong("timeTaken")));
                takenQuiz.setFeedback(rs.getString("feedback"));
                takenQuiz.setStatus(rs.getString("status"));

                takenQuizzes.add(takenQuiz);
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return takenQuizzes;
    }




}
