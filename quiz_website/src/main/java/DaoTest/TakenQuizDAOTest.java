package DaoTest;

import static org.junit.jupiter.api.Assertions.*;

import Dao.TakenQuizDAO;
import model.TakenQuiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TakenQuizDAOTest {

    private TakenQuizDAO takenQuizDAO;
    private Long userId;   // Assuming a user already exists in the DB with this ID.
    private Long quizId;   // Assuming a quiz already exists in the DB with this ID.

    @BeforeEach
    public void setup() {
        // Initialize your DAO. It's good to clear the database here or set up a mock DB.
        takenQuizDAO = new TakenQuizDAO();
    }

    @Test
    public void testCreateAndGetTakenQuiz() {
        TakenQuiz takenQuiz = new
                TakenQuiz(1L, userId, quizId, 90,
                LocalDateTime.now(), Duration.ofMinutes(15), "Good Quiz", "Completed");

        // Test creation
        Long createdId = takenQuizDAO.createTakenQuiz(takenQuiz);
        assertNotNull(createdId);

        // Test retrieval
        TakenQuiz retrievedQuiz = takenQuizDAO.getTakenQuizById(createdId);
        assertEquals(takenQuiz.getUserId(), retrievedQuiz.getUserId());
        assertEquals(takenQuiz.getQuizId(), retrievedQuiz.getQuizId());
        assertEquals(takenQuiz.getScore(), retrievedQuiz.getScore());
    }

    @Test
    public void testUpdateTakenQuiz() {
        TakenQuiz takenQuiz = new TakenQuiz(2L, userId, quizId, 85,
                LocalDateTime.now(), Duration.ofMinutes(10), "Challenging!", "Completed");
        Long createdId = takenQuizDAO.createTakenQuiz(takenQuiz);
        takenQuiz.setTakenQuizId(createdId);
        takenQuiz.setScore(95);

        assertTrue(takenQuizDAO.updateTakenQuiz(takenQuiz));

        TakenQuiz updatedQuiz = takenQuizDAO.getTakenQuizById(createdId);
        assertEquals(95, updatedQuiz.getScore());
    }

    @Test
    public void testLatestTakenQuizForUser() {
        TakenQuiz quiz1 = new TakenQuiz(1L,userId, quizId, 85,
                LocalDateTime.now().minusHours(2), Duration.ofMinutes(10), "Earlier Quiz", "Completed");
        TakenQuiz quiz2 = new TakenQuiz(1L, userId, quizId, 95,
                LocalDateTime.now(), Duration.ofMinutes(12), "Latest Quiz", "Completed");

        takenQuizDAO.createTakenQuiz(quiz1);
        takenQuizDAO.createTakenQuiz(quiz2);

        TakenQuiz latestQuiz = takenQuizDAO.getLatestTakenQuizForUser(userId);
        assertEquals(95, latestQuiz.getScore());
        assertEquals("Latest Quiz", latestQuiz.getFeedback());
    }

    @Test
    public void testGetTakenQuizzesForUserOnDate() {
        LocalDateTime today = LocalDateTime.now();

        TakenQuiz quiz1 = new TakenQuiz(null, userId, quizId, 85,
                today.minusHours(2), Duration.ofMinutes(10), "Morning Quiz", "Completed");
        TakenQuiz quiz2 = new TakenQuiz(null, userId, quizId, 95,
                today, Duration.ofMinutes(12), "Evening Quiz", "Completed");

        takenQuizDAO.createTakenQuiz(quiz1);
        takenQuizDAO.createTakenQuiz(quiz2);

        List<TakenQuiz> takenQuizzes = takenQuizDAO.getTakenQuizzesForUserOnDate(userId, today);
        assertEquals(2, takenQuizzes.size());
    }

    // ... Add more tests as needed ...
}
