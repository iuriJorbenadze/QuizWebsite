package DaoTest;

import model.Quiz;
import Dao.QuizDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QuizDAOTest {

    private QuizDAO quizDAO;

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
}
