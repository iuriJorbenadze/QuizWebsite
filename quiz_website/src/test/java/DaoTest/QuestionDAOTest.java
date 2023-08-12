//package DaoTest;

import Dao.QuestionDAO;
import model.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionDAOTest {

    private QuestionDAO questionDAO;

    @BeforeEach
    public void setup() {
        questionDAO = new QuestionDAO();
        // TODO: You might want to set up a test database connection here or ensure your database is in a known state
    }
    @AfterEach
    public void tearDown() {
        questionDAO.clearQuestionsTable();
    }



    @Test
    public void testCreateAndGetQuestion() {
        Question question = new Question(null, 1L, "What's the capital of France?", Arrays.asList("Berlin", "Paris", "London"), "Paris");
        assertTrue(questionDAO.createQuestion(question));

        assertNotNull(question.getQuestionId(), "Question ID should be set after creation.");

        Question retrievedQuestion = questionDAO.getQuestionById(question.getQuestionId());
        assertEquals(question.getContent(), retrievedQuestion.getContent());
        assertEquals(question.getCorrectAnswer(), retrievedQuestion.getCorrectAnswer());
        assertIterableEquals(question.getOptions(), retrievedQuestion.getOptions());
    }

    @Test
    public void testUpdateQuestion() {
        Question question = new Question(null, 1L, "What's the capital of France?", Arrays.asList("Berlin", "Paris", "London"), "Paris");
        assertTrue(questionDAO.createQuestion(question));

        question.setContent("What's the capital of Germany?");
        question.setCorrectAnswer("Berlin");
        question.setOptions(Arrays.asList("Berlin", "Paris", "Rome"));

        assertTrue(questionDAO.updateQuestion(question));

        // Refresh the object's state from the database
        question = questionDAO.getQuestionById(question.getQuestionId());



        Question updatedQuestion = questionDAO.getQuestionById(question.getQuestionId());
        assertEquals(question.getContent(), updatedQuestion.getContent());
        assertEquals(question.getCorrectAnswer(), updatedQuestion.getCorrectAnswer());
        assertIterableEquals(question.getOptions(), updatedQuestion.getOptions());
    }

    @Test
    public void testDeleteQuestion() {
        Question question = new Question(null, 1L, "What's the capital of France?", Arrays.asList("Berlin", "Paris", "London"), "Paris");
        assertTrue(questionDAO.createQuestion(question));

        assertTrue(questionDAO.deleteQuestion(question.getQuestionId()));
        assertNull(questionDAO.getQuestionById(question.getQuestionId()));
    }

    @Test
    public void testGetQuestionsByQuizId() {
        // Step 1: Get the initial count of questions for the quizId
        int initialCount = questionDAO.getQuestionsByQuizId(1L).size();

        Question question1 = new Question(null, 1L, "What's the capital of France?", Arrays.asList("Berlin", "Paris", "London"), "Paris");
        Question question2 = new Question(null, 1L, "Which is the largest planet?", Arrays.asList("Earth", "Mars", "Jupiter"), "Jupiter");

        assertTrue(questionDAO.createQuestion(question1));
        assertTrue(questionDAO.createQuestion(question2));

        List<Question> questions = questionDAO.getQuestionsByQuizId(1L);

        // Step 2: Compare the count after insertion with the expected count
        assertEquals(initialCount + 2, questions.size());
    }

    @Test
    public void testGetNonExistentQuestion() {
        assertNull(questionDAO.getQuestionById(-1L));  // Assuming -1L is an ID that doesn't exist in your database
    }
    @Test
    public void testQuestionOrdering() {
        Question question1 = new Question(null, 1L, "Q1?", Arrays.asList("A", "B", "C"), "A");
        Question question2 = new Question(null, 1L, "Q2?", Arrays.asList("D", "E", "F"), "D");

        assertTrue(questionDAO.createQuestion(question1));
        assertTrue(questionDAO.createQuestion(question2));

        List<Question> questions = questionDAO.getQuestionsByQuizId(1L);

        // Fetch questions based on their IDs
        Question retrievedQuestion1 = questions.stream()
                .filter(q -> q.getQuestionId().equals(question1.getQuestionId()))
                .findFirst()
                .orElse(null);

        Question retrievedQuestion2 = questions.stream()
                .filter(q -> q.getQuestionId().equals(question2.getQuestionId()))
                .findFirst()
                .orElse(null);

        assertNotNull(retrievedQuestion1, "Question 1 was not found.");
        assertNotNull(retrievedQuestion2, "Question 2 was not found.");

        assertEquals(question1.getContent(), retrievedQuestion1.getContent());
        assertEquals(question2.getContent(), retrievedQuestion2.getContent());
    }

    @Test
    public void testDeleteNonExistentQuestion() {
        assertFalse(questionDAO.deleteQuestion(-1L));  // Assuming -1L doesn't exist, the method should return false
    }
    @Test
    public void testUpdateNonExistentQuestion() {
        Question nonExistentQuestion = new Question(-1L, 1L, "Doesn't exist?", Arrays.asList("No", "Yes"), "No");
        assertFalse(questionDAO.updateQuestion(nonExistentQuestion));
    }


    @Test
    public void testCreateQuestionWithNoOptions() {
        Question question = new Question(null, 1L, "Is this question valid?", null, "Yes");
        assertFalse(questionDAO.createQuestion(question));
    }

    @Test
    public void testCreateQuestionWithEmptyOptions() {
        Question question = new Question(null, 1L, "Is this question valid?", new ArrayList<>(), "Yes");
        assertFalse(questionDAO.createQuestion(question));
    }
    @Test
    public void testCreateQuestionWithMismatchedAnswer() {
        Question question = new Question(null, 1L, "Is this correct?", Arrays.asList("Option1", "Option2"), "MismatchedAnswer");
        assertFalse(questionDAO.createQuestion(question));
    }
    @Test
    public void testUpdateQuestionQuizId() {
        Question question = new Question(null, 1L, "What's the capital of France?", Arrays.asList("Berlin", "Paris", "London"), "Paris");
        assertTrue(questionDAO.createQuestion(question));

        question.setQuizId(2L);
        assertTrue(questionDAO.updateQuestion(question));

        Question updatedQuestion = questionDAO.getQuestionById(question.getQuestionId());
        assertEquals(2L, updatedQuestion.getQuizId());
    }
    @Test
    public void testCreateQuestionWithEmptyContent() {
        Question question = new Question(null, 1L, "", Arrays.asList("Option1", "Option2"), "Option1");
        assertFalse(questionDAO.createQuestion(question), "Questions with empty content shouldn't be created.");
    }

    @Test
    public void testQuestionOrderingMultiple() {
        Question question1 = new Question(null, 1L, "Q1?", Arrays.asList("A", "B", "C"), "A");
        Question question2 = new Question(null, 1L, "Q2?", Arrays.asList("D", "E", "F"), "D");
        Question question3 = new Question(null, 1L, "Q3?", Arrays.asList("G", "H", "I"), "G");

        assertTrue(questionDAO.createQuestion(question1));
        assertTrue(questionDAO.createQuestion(question2));
        assertTrue(questionDAO.createQuestion(question3));

        List<Question> questions = questionDAO.getQuestionsByQuizId(1L);
        assertEquals(3, questions.size());

        // Assuming questions are ordered by their creation order
        assertEquals(question1.getContent(), questions.get(0).getContent());
        assertEquals(question2.getContent(), questions.get(1).getContent());
        assertEquals(question3.getContent(), questions.get(2).getContent());
    }


}
