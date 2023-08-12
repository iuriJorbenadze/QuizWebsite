package ServiceTest;

import Dao.QuestionDAO;
import model.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.QuestionService;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionServiceTest {

    private QuestionDAO questionDAO;
    private QuestionService questionService;

    @BeforeEach
    public void setup() {
        questionDAO = new QuestionDAO();  // Assuming you set up a test database connection in the constructor
        questionService = new QuestionService(questionDAO);
    }

    @AfterEach
    public void tearDown() {
        questionDAO.clearQuestionsTable();  // This should remove all entries from the questions table
    }

    @Test
    public void testCreateAndGetQuestion() {
        Question sampleQuestion = new Question(null, 2L, "Sample Question", Collections.singletonList("Option1"), "Option1");

        // Creating the question
        assertTrue(questionService.createQuestion(sampleQuestion));

        // Retrieving the question
        Question retrievedQuestion = questionService.getQuestionById(sampleQuestion.getQuestionId());

        assertEquals(sampleQuestion.getContent(), retrievedQuestion.getContent());
        assertEquals(sampleQuestion.getOptions(), retrievedQuestion.getOptions());
        assertEquals(sampleQuestion.getCorrectAnswer(), retrievedQuestion.getCorrectAnswer());
    }

    @Test
    public void testGetQuestionById() {
        Question sampleQuestion = new Question(1L, 2L, "Sample Question", Collections.singletonList("Option1"), "Option1");
        questionDAO.createQuestion(sampleQuestion);  // Assuming you have this method in your DAO

        Question retrievedQuestion = questionService.getQuestionById(1L);

        assertEquals(sampleQuestion.getContent(), retrievedQuestion.getContent());
        assertEquals(sampleQuestion.getCorrectAnswer(), retrievedQuestion.getCorrectAnswer());
    }
    @Test
    public void testCreateQuestion() {
        Question sampleQuestion = new Question(null, 2L, "Sample Question", Collections.singletonList("Option1"), "Option1");
        assertTrue(questionService.createQuestion(sampleQuestion));
        assertNotNull(questionService.getQuestionById(sampleQuestion.getQuestionId()));
    }
    @Test
    public void testUpdateQuestion() {
        Question sampleQuestion = new Question(1L, 2L, "Sample Question", Collections.singletonList("Option1"), "Option1");
        questionDAO.createQuestion(sampleQuestion);

        sampleQuestion.setContent("Updated Content");
        assertTrue(questionService.updateQuestion(sampleQuestion));

        Question updatedQuestion = questionService.getQuestionById(1L);
        assertEquals("Updated Content", updatedQuestion.getContent());
    }
    @Test
    public void testAwardPoints() {
        Question sampleQuestion = new Question(1L, 2L, "Sample Question", Collections.singletonList("Option1"), "Option1");
        assertEquals(1, questionService.awardPoints(sampleQuestion, "Option1"));
        assertEquals(0, questionService.awardPoints(sampleQuestion, "WrongOption"));
    }
    @Test
    public void testValidateQuestionEmptyContent() {
        Question invalidQuestion = new Question(null, 2L, "   ", Collections.singletonList("Option1"), "Option1");
        assertThrows(IllegalArgumentException.class, () -> questionService.createQuestion(invalidQuestion));
    }

    @Test
    public void testValidateQuestionInvalidOption() {
        Question invalidQuestion = new Question(null, 2L, "Sample Question", Arrays.asList("Option1", ""), "Option1");
        assertThrows(IllegalArgumentException.class, () -> questionService.createQuestion(invalidQuestion));
    }

}
