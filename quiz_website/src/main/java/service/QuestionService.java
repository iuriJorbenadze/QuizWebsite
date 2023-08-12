package service;

import Dao.QuestionDAO;
import model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionService {

    private QuestionDAO questionDAO;

    public QuestionService(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    public Question getQuestionById(Long id) {
        validateId(id);
        return questionDAO.getQuestionById(id);
    }

    public List<Question> getAllQuestionsForQuiz(Long quizId) {
        validateId(quizId);
        return questionDAO.getQuestionsByQuizId(quizId);
    }

    public boolean createQuestion(Question question) {
        validateQuestion(question);
        return questionDAO.createQuestion(question);
    }

    public boolean updateQuestion(Question question) {
        validateQuestion(question);
        if (question.getQuestionId() == null) {
            throw new IllegalArgumentException("Question ID must be provided for update.");
        }
        return questionDAO.updateQuestion(question);
    }

    public boolean deleteQuestion(Long id) {
        validateId(id);
        return questionDAO.deleteQuestion(id);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID provided.");
        }
    }

    public int awardPoints(Question question, String userAnswer) {
        if (question.getCorrectAnswer().equals(userAnswer)) {
            return 1; // For example, award 1 point for a correct answer
        } else {
            return 0; // No points for a wrong answer
        }
    }

    //IMPORTANT NEW QUESTION IS CREATED AND RETURNED
    public Question getRandomizedOptionOrder(Question question) {
        List<String> randomizedOptions = new ArrayList<>(question.getOptions());
        Collections.shuffle(randomizedOptions);
        return new Question(
                question.getQuestionId(),
                question.getQuizId(),
                question.getContent(),
                randomizedOptions,
                question.getCorrectAnswer()
        );
    }

    private void validateQuestion(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null.");
        }
        if (question.getQuizId() == null || question.getQuizId() <= 0) {
            throw new IllegalArgumentException("Invalid Quiz ID provided.");
        }
        if (question.getContent() == null || question.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Question content cannot be empty.");
        }
        if (question.getOptions() == null || question.getOptions().isEmpty()) {
            throw new IllegalArgumentException("Options list cannot be empty.");
        }
        if (question.getCorrectAnswer() == null || question.getCorrectAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("Correct answer cannot be empty.");
        }
        if (!question.getOptions().contains(question.getCorrectAnswer())) {
            throw new IllegalArgumentException("Correct answer must be one of the provided options.");
        }
    }
}
