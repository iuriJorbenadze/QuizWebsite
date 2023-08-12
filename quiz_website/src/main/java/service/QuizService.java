package service;

import Dao.QuizDAO;
import model.Quiz;

import java.util.List;

public class QuizService {

    private QuizDAO quizDAO;

    // Constructor
    public QuizService(QuizDAO quizDAO) {
        this.quizDAO = quizDAO;
    }

    // Fetch a quiz by its ID
    public Quiz getQuizById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Quiz ID cannot be null.");
        }
        return quizDAO.getQuizById(id);
    }

    // Fetch all quizzes
    public List<Quiz> getAllQuizzes() {
        return quizDAO.getAllQuizzes();
    }

    // Create a new quiz
    public boolean createQuiz(Quiz quiz) {
        validateQuiz(quiz);

        // Ensuring the quiz name is unique (as an example)
        for (Quiz existingQuiz : getAllQuizzes()) {
            if (existingQuiz.getTitle().equalsIgnoreCase(quiz.getTitle())) {
                throw new IllegalArgumentException("A quiz with this name already exists.");
            }
        }

        return quizDAO.createQuiz(quiz);
    }

    // Update an existing quiz
    public boolean updateQuiz(Quiz quiz) {
        validateQuiz(quiz);

        if (quiz.getQuizId() == null) {
            throw new IllegalArgumentException("Quiz ID is required for updating.");
        }

        Quiz existingQuiz = getQuizById(quiz.getQuizId());
        if (existingQuiz == null) {
            throw new IllegalArgumentException("Quiz with the provided ID does not exist.");
        }

        return quizDAO.updateQuiz(quiz);
    }

    // Delete a quiz
    public boolean deleteQuiz(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Quiz ID cannot be null.");
        }

        Quiz existingQuiz = getQuizById(id);
        if (existingQuiz == null) {
            throw new IllegalArgumentException("Quiz with the provided ID does not exist.");
        }

        return quizDAO.deleteQuiz(id);
    }

    // Private helper method to validate a quiz
    private void validateQuiz(Quiz quiz) {
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz cannot be null.");
        }

        if (quiz.getTitle() == null || quiz.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Quiz name cannot be empty.");
        }


    }
}