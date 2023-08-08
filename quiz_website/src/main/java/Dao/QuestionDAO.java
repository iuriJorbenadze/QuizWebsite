package Dao;

import model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionDAO extends AbstractDAO {

    private void validateQuestionId(Question question) throws IllegalArgumentException {
        if (question.getQuestionId() == null) {
            throw new IllegalArgumentException("Question ID cannot be null.");
        }
    }


    public Question getQuestionById(Long id) {
        Question question = null;
        String query = "SELECT * FROM Questions WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                question = mapResultSetToQuestion(resultSet);
                // Fetch options for the question
                List<String> options = getOptionsForQuestion(id);
                question.setOptions(options);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return question;
    }

    public List<Question> getQuestionsByQuizId(Long quizId) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM Questions WHERE quizId = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, quizId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                questions.add(mapResultSetToQuestion(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }


    public boolean createQuestion(Question question) {
        if (question.getOptions() == null || question.getOptions().isEmpty()) {
            return false;  // Reject questions with no options
        }

        String insertQuestionQuery = "INSERT INTO Questions(quizId, content) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuestionQuery, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, question.getQuizId());
            statement.setString(2, question.getContent());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating question failed, no rows affected.");
            }

            long generatedQuestionId;
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedQuestionId = generatedKeys.getLong(1);

                    //IMPORTANT, setting id after saving into DB
                    question.setQuestionId(generatedQuestionId);
                } else {
                    throw new SQLException("Creating question failed, no ID obtained.");
                }
            }

            // Insert options into the Options table and get the ID of the correct answer
            long correctOptionId = insertOptionsForQuestion(generatedQuestionId, question.getOptions(), question.getCorrectAnswer());

            // Update the Questions table with the ID of the correct answer
            updateQuestionWithCorrectOptionId(generatedQuestionId, correctOptionId);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public boolean createQuestion(Question question) {
//        String insertQuestionQuery = "INSERT INTO Questions(quizId, content) VALUES (?, ?)";
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(insertQuestionQuery, Statement.RETURN_GENERATED_KEYS)) {
//
//            statement.setLong(1, question.getQuizId());
//            statement.setString(2, question.getContent());
//
//            int affectedRows = statement.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Creating question failed, no rows affected.");
//            }
//
//            long generatedQuestionId;
//            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    generatedQuestionId = generatedKeys.getLong(1);
//                } else {
//                    throw new SQLException("Creating question failed, no ID obtained.");
//                }
//            }
//
//            // Insert options into the Options table
//            insertOptionsForQuestion(generatedQuestionId, question.getOptions());
//
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    // This method just inserts options
    private void insertOptionsForQuestion(Long questionId, List<String> options) {
        String insertOptionQuery = "INSERT INTO Options(questionId, optionText) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertOptionQuery)) {

            for (String option : options) {
                statement.setLong(1, questionId);
                statement.setString(2, option);
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean updateQuestion(Question question) {
        validateQuestionId(question);

        String query = "UPDATE Questions SET quizId = ?, content = ? WHERE id = ?";
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Update the question
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, question.getQuizId());
                statement.setString(2, question.getContent());
                statement.setLong(3, question.getQuestionId());

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    //throw new SQLException("Updating question failed, no rows affected.");
                    return false; // No rows affected means question with the provided ID doesn't exist.
                }
            }

            // Remove existing options for the question
            if (!deleteOptionsForQuestion(question.getQuestionId(), connection)) {
                connection.rollback();
                return false;
            }

            // Insert new options for the question
            if (!createOptionsForQuestion(question.getQuestionId(), question.getOptions(), connection)) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            // If an exception happens, try to roll back
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
            }

            // Ideally, you'd log the exception here using a logging framework
            e.printStackTrace();

            return false;
        } finally {
            // Close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    // Ideally, you'd log the exception here
                    closeEx.printStackTrace();
                }
            }
        }
    }


    public boolean deleteQuestion(Long questionId) {
        if (questionId == null) {
            throw new IllegalArgumentException("Question ID cannot be null.");
        }

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);  // Start a transaction

            // First, delete the associated options
            if (!deleteOptionsForQuestion(questionId, connection)) {
                connection.rollback();  // If deleting options failed, rollback transaction and return false
                return false;
            }

            String query = "DELETE FROM Questions WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, questionId);
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    connection.rollback();  // If no rows affected, rollback transaction and return false
                    return false;
                }
            }

            connection.commit();  // Commit the transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean deleteOptionsForQuestion(Long questionId, Connection connection) throws SQLException {
        String deleteOptionsSQL = "DELETE FROM Options WHERE questionId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteOptionsSQL)) {
            preparedStatement.setLong(1, questionId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows >= 0;  // returns true if at least one option was deleted or no options were associated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private Question mapResultSetToQuestion(ResultSet resultSet) throws SQLException {
        Long questionId = resultSet.getLong("id");
        Long quizId = resultSet.getLong("quizId");
        String content = resultSet.getString("content");

        // Fetch the options for the question
        List<String> options = getOptionsForQuestion(questionId);

        // Assuming the "correctOptionId" maps to one of the options and you want to fetch it:
        Long correctOptionId = resultSet.getLong("correctOptionId");
        String correctAnswer = fetchOptionById(correctOptionId);

        return new Question(questionId, quizId, content, options, correctAnswer);
    }

    private String fetchOptionById(Long optionId) throws SQLException {
        String optionContent = null;
        String query = "SELECT optionText FROM Options WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, optionId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                optionContent = rs.getString("optionText");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optionContent;
    }






    // This method inserts options and returns the ID of the correct option
    private long insertOptionsForQuestion(Long questionId, List<String> options, String correctAnswer) throws SQLException {
        long correctOptionId = -1;

        String insertOptionQuery = "INSERT INTO Options(questionId, optionText) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertOptionQuery, Statement.RETURN_GENERATED_KEYS)) {

            for (String option : options) {
                statement.setLong(1, questionId);
                statement.setString(2, option);
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long optionId = generatedKeys.getLong(1);
                        if (option.equals(correctAnswer)) {
                            correctOptionId = optionId;
                        }
                    }
                }
            }
        }

        if (correctOptionId == -1) {
            throw new SQLException("Unable to find the ID of the correct answer.");
        }

        return correctOptionId;
    }

    private void updateQuestionWithCorrectOptionId(Long questionId, Long correctOptionId) throws SQLException {
        String updateQuery = "UPDATE Questions SET correctOptionId = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setLong(1, correctOptionId);
            statement.setLong(2, questionId);
            statement.executeUpdate();
        }
    }



    //////////////////////////OPTIONS METHODS

    public boolean createOptionsForQuestion(Long questionId, List<String> options) {
        return createOptionsForQuestion(questionId, options, null);
    }


    public boolean createOptionsForQuestion(Long questionId, List<String> options, Connection providedConnection) {
        String insertOptionSQL = "INSERT INTO Options (questionId, optionText) VALUES (?, ?)";

        Connection connection = null;
        boolean isExternalConnection = providedConnection != null;

        try {
            // If an external connection is provided, use it. Otherwise, create a new connection.
            connection = isExternalConnection ? providedConnection : getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertOptionSQL)) {
                for (String option : options) {
                    preparedStatement.setLong(1, questionId);
                    preparedStatement.setString(2, option);
                    preparedStatement.addBatch();
                }
                int[] affectedRows = preparedStatement.executeBatch();
                return Arrays.stream(affectedRows).sum() == options.size();  // all insertions should succeed
            }
        } catch (SQLException e) {
            // Ideally, you'd log the exception here using a logging framework
            e.printStackTrace();
            return false;
        } finally {
            // Only close the connection if it was internally created (i.e., not provided externally)
            if (!isExternalConnection && connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    // Ideally, you'd log the exception here
                    closeEx.printStackTrace();
                }
            }
        }
    }


    public List<String> getOptionsForQuestion(Long questionId) {
        List<String> options = new ArrayList<>();
        String selectOptionsSQL = "SELECT optionText FROM options WHERE questionId = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectOptionsSQL)) {
            preparedStatement.setLong(1, questionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                options.add(resultSet.getString("optionText"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return options;
    }

    public boolean updateOptionsForQuestion(Long questionId, List<String> newOptions) {
        // First, delete the old options
        String deleteOptionsSQL = "DELETE FROM options WHERE questionId = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteOptionsSQL)) {
            preparedStatement.setLong(1, questionId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Then, insert the new options
        return createOptionsForQuestion(questionId, newOptions);
    }
    public boolean deleteOptionsForQuestion(Long questionId) {
        String deleteOptionsSQL = "DELETE FROM Options WHERE questionId = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteOptionsSQL)) {
            preparedStatement.setLong(1, questionId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;  // returns true if at least one option was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
