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

    public List<String> fetchOptionsForQuestion(Long questionId) {
        validateId(questionId);
        return questionDAO.getOptionsForQuestion(questionId);
    }

    public boolean modifyOptionsForQuestion(Long questionId, List<String> newOptions) {
        validateId(questionId);
        if (newOptions == null || newOptions.isEmpty()) {
            throw new IllegalArgumentException("Options list cannot be empty.");
        }
        return questionDAO.updateOptionsForQuestion(questionId, newOptions);
    }

    // Ensures the options for the question are created in the DB
    public boolean addOptionsToQuestion(Long questionId, List<String> options) {
        validateId(questionId);
        return questionDAO.createOptionsForQuestion(questionId, options);
    }

    // Deletes all options for a specific question
    public boolean removeAllOptionsFromQuestion(Long questionId) {
        validateId(questionId);
        return questionDAO.deleteOptionsForQuestion(questionId);
    }

    // Helpful method to get a question with its associated options
    public Question getCompleteQuestionById(Long id) {
        Question question = getQuestionById(id);
        List<String> options = fetchOptionsForQuestion(id);
        question.setOptions(options); // Assuming a setter for options in Question class
        return question;
    }

    // Method to validate and update a question with its options
    public boolean updateCompleteQuestion(Question question) {
        validateQuestion(question);
        if (updateQuestion(question)) {
            return modifyOptionsForQuestion(question.getQuestionId(), question.getOptions());
        }
        return false;
    }




    // Batch creation of questions for a specific quiz
    public boolean createQuestions(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("Questions list cannot be empty.");
        }
        for (Question question : questions) {
            validateQuestion(question);
            if (!createQuestion(question)) {
                return false;
            }
        }
        return true;
    }

    // Batch retrieval based on a list of IDs
    public List<Question> getQuestionsByIds(List<Long> ids) {
        List<Question> questions = new ArrayList<>();
        for (Long id : ids) {
            questions.add(getQuestionById(id));
        }
        return questions;
    }

    // Check if an answer is correct without awarding points
    public boolean isAnswerCorrect(Question question, String userAnswer) {
        return question.getCorrectAnswer().equals(userAnswer);
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

        for (String option : question.getOptions()) {
            if (option.trim().isEmpty()) {
                throw new IllegalArgumentException("Option content cannot be empty.");
            }
        }

    }

    // Handle database or other exceptions
    private <T> T handleException(TryReturnSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception ex) {
            // Log the exception
            // Here you can use any logging library like SLF4J, Log4J etc.
            System.err.println("An error occurred: " + ex.getMessage());  // For the sake of simplicity, using System.err
            throw new RuntimeException("Operation failed. Please try again.");
        }
    }

    @FunctionalInterface
    private interface TryReturnSupplier<T> {
        T get() throws Exception;
    }
}
