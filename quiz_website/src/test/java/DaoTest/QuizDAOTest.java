//package DaoTest;

import model.Quiz;
import Dao.QuizDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizDAOTest {

    private QuizDAO quizDAO;

    //TODO add user creation into db before testing quiz
    //should have some users created in database otherwise those tests will fail, because quiz is created by user
    @BeforeEach
    void setUp() {
        quizDAO = new QuizDAO();

    }

    @Test
    void testAddQuiz() {
        Quiz newQuiz = new Quiz("Sample Quiz Title", "Sample Description", 1L, LocalDateTime.now());
        assertTrue(quizDAO.createQuiz(newQuiz));
        assertNotNull(newQuiz.getQuizId());  // Ensure the ID got set after insertion
    }

    @Test
    void testGetQuizById() {
        Quiz newQuiz = new Quiz("Quiz Title 2", "Description 2", 1L, LocalDateTime.now());
        quizDAO.createQuiz(newQuiz);
        Long savedQuizId = newQuiz.getQuizId();

        Quiz retrievedQuiz = quizDAO.getQuizById(savedQuizId);
        assertEquals(newQuiz.getTitle(), retrievedQuiz.getTitle());
    }

    @Test
    void testUpdateQuiz() {
        Quiz newQuiz = new Quiz("Quiz Title 3", "Description 3", 1L, LocalDateTime.now());
        quizDAO.createQuiz(newQuiz);
        newQuiz.setTitle("Updated Quiz Title");

        assertTrue(quizDAO.updateQuiz(newQuiz));

        Quiz updatedQuiz = quizDAO.getQuizById(newQuiz.getQuizId());
        assertEquals("Updated Quiz Title", updatedQuiz.getTitle());
    }

    @Test
    void testDeleteQuiz() {
        Quiz newQuiz = new Quiz("Quiz Title 4", "Description 4", 1L, LocalDateTime.now());
        quizDAO.createQuiz(newQuiz);

        assertTrue(quizDAO.deleteQuiz(newQuiz.getQuizId()));
        assertNull(quizDAO.getQuizById(newQuiz.getQuizId())); // After deletion, the quiz should not be found
    }

    @Test
    void testGetQuizzesByUser() {

        List<Quiz> quizzesByUserTemp = quizDAO.getQuizzesByUser(1L);
        int sizeBefore = quizzesByUserTemp.size();
        // Given
        Quiz quiz1 = new Quiz("Quiz User 1", "Description User 1", 1L, LocalDateTime.now());
        Quiz quiz2 = new Quiz("Quiz User 2", "Description User 2", 1L, LocalDateTime.now());
        Quiz quiz3 = new Quiz("Quiz User 3", "Description User 3", 1L, LocalDateTime.now());

        // Creating the quizzes
        quizDAO.createQuiz(quiz1);
        quizDAO.createQuiz(quiz2);
        quizDAO.createQuiz(quiz3);

        // When
        List<Quiz> quizzesByUser = quizDAO.getQuizzesByUser(1L);  // Assuming user with ID 1L is the one used in tests

        // Then
        assertNotNull(quizzesByUser);
        assertEquals(sizeBefore + 3, quizzesByUser.size());

        assertTrue(quizzesByUser.stream().anyMatch(quiz -> quiz.getTitle().equals("Quiz User 1")));
        assertTrue(quizzesByUser.stream().anyMatch(quiz -> quiz.getTitle().equals("Quiz User 2")));
        assertTrue(quizzesByUser.stream().anyMatch(quiz -> quiz.getTitle().equals("Quiz User 3")));
    }

}
