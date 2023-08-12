//package DaoTest;

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
    private Long userId = 1L;
    private Long quizId = 1L;

    @BeforeEach
    public void setup() {
        takenQuizDAO = new TakenQuizDAO();
    }

    @Test
    public void testCreateAndGetTakenQuiz() {

        TakenQuiz takenQuiz = new TakenQuiz(null, userId, quizId, 90,
                LocalDateTime.now(), Duration.ofMinutes(15), "Good Quiz", "Completed");



        Long createdId = takenQuizDAO.createTakenQuiz(takenQuiz);
        assertNotNull(createdId);


        TakenQuiz retrievedQuiz = takenQuizDAO.getTakenQuizById(createdId);
        assertNotNull(retrievedQuiz);
        assertNotNull(retrievedQuiz.getTakenQuizId());
        assertEquals(userId, retrievedQuiz.getUserId());
        assertEquals(quizId, retrievedQuiz.getQuizId());
        assertEquals(90, retrievedQuiz.getScore());
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

        int initialCount = takenQuizDAO.getAllTakenQuizzesForQuiz(1L).size();

        TakenQuiz quiz1 = new TakenQuiz(null, userId, quizId, 85,
                today.minusHours(2), Duration.ofMinutes(10), "Morning Quiz", "Completed");
        TakenQuiz quiz2 = new TakenQuiz(null, userId, quizId, 95,
                today, Duration.ofMinutes(12), "Evening Quiz", "Completed");

        takenQuizDAO.createTakenQuiz(quiz1);
        takenQuizDAO.createTakenQuiz(quiz2);

        List<TakenQuiz> takenQuizzes = takenQuizDAO.getTakenQuizzesForUserOnDate(userId, today);
        assertEquals(initialCount+2, takenQuizzes.size());
    }




    @Test
    public void testDeleteTakenQuiz() {
        TakenQuiz quiz = new TakenQuiz(null, userId, quizId, 80,
                LocalDateTime.now(), Duration.ofMinutes(12), "Test Delete Quiz", "Completed");

        Long createdId = takenQuizDAO.createTakenQuiz(quiz);
        TakenQuiz retrievedQuiz = takenQuizDAO.getTakenQuizById(createdId);
        assertNotNull(retrievedQuiz);

        boolean result = takenQuizDAO.deleteTakenQuizById(retrievedQuiz.getTakenQuizId());
        assertTrue(result);

        TakenQuiz afterDelete = takenQuizDAO.getTakenQuizById(createdId);
        assertNull(afterDelete);
    }


    @Test
    public void testGetNonExistentTakenQuiz() {
        TakenQuiz nonExistent = takenQuizDAO.getTakenQuizById(-1L);  // An ID that shouldn't exist
        assertNull(nonExistent);
    }

    @Test
    public void testUpdateNonExistentTakenQuiz() {
        TakenQuiz nonExistentQuiz = new TakenQuiz(-1L, userId, quizId, 80,
                LocalDateTime.now(), Duration.ofMinutes(10), "Doesn't Exist", "Completed");

        boolean result = takenQuizDAO.updateTakenQuiz(nonExistentQuiz);
        assertFalse(result);
    }
}