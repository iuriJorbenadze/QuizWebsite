package Dao;

import model.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO extends AbstractDAO {

    public Quiz getQuizById(Long id) {
        Quiz quiz = null;
        String query = "SELECT * FROM Quizzes WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                quiz = mapResultSetToQuiz(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quiz;
    }

    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM Quizzes";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                quizzes.add(mapResultSetToQuiz(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    public boolean createQuiz(Quiz quiz) {
        String query = "INSERT INTO Quizzes(title, description, createdByUserId, createdDate) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, quiz.getTitle());
            statement.setString(2, quiz.getDescription());
            statement.setLong(3, quiz.getCreatedByUserId());
            statement.setTimestamp(4, Timestamp.valueOf(quiz.getCreatedDate()));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating quiz failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    quiz.setQuizId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating quiz failed, no ID obtained.");
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateQuiz(Quiz quiz) {
        String query = "UPDATE Quizzes SET title = ?, description = ?, createdByUserId = ?, createdDate = ? WHERE Id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, quiz.getTitle());
            statement.setString(2, quiz.getDescription());
            statement.setLong(3, quiz.getCreatedByUserId());
            statement.setTimestamp(4, Timestamp.valueOf(quiz.getCreatedDate()));
            statement.setLong(5, quiz.getQuizId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteQuiz(Long id) {
        String query = "DELETE FROM Quizzes WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Quiz mapResultSetToQuiz(ResultSet resultSet) throws SQLException {
        Quiz quiz = new Quiz(
        resultSet.getLong("id"),
        resultSet.getString("title"),
        resultSet.getString("description"),
        resultSet.getLong("createdByUserId"),
        resultSet.getTimestamp("createdDate").toLocalDateTime());
        return quiz;
    }
}
