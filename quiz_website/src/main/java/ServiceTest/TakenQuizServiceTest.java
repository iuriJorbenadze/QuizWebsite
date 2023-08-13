package ServiceTest;

import static org.junit.jupiter.api.Assertions.*;

import Dao.QuizDAO;
import Dao.UserDAO;
import model.TakenQuiz;
import model.User;
import model.Quiz;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TakenQuizService;
import service.UserService;
import service.QuizService;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TakenQuizServiceTest {

    private TakenQuizService takenQuizService;
    private UserService userService;
    private QuizService quizService;

    private UserDAO userDAO;
    private QuizDAO quizDAO;

    private List<User> usersToDelete = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        takenQuizService = new TakenQuizService();
        userDAO = new UserDAO();
        quizDAO = new QuizDAO();
        userService = new UserService(userDAO);
        quizService = new QuizService(quizDAO);
    }

    @AfterEach
    public void tearDown() {
        for (User user : usersToDelete) {
            List<Quiz> quizzesByUser = quizDAO.getQuizzesByUser((long)user.getUserId());
            for (Quiz quiz : quizzesByUser) {
                quizDAO.deleteQuiz(quiz.getQuizId());
            }
            userDAO.deleteUser(user.getUserId());
        }
        usersToDelete.clear();
    }

    @Test
    public void testCreateAndGetTakenQuiz() {
        User newUser = new User("testUsername", "testHashedPassword", "testEmail@example.com", false);
        boolean userCreated = userService.registerUser(newUser);
        assertTrue(userCreated, "Failed to create user");

        Quiz newQuiz = new Quiz("Sample Quiz", "Sample Description", (long)newUser.getUserId(), LocalDateTime.now());
        boolean quizCreated = quizService.createQuiz(newQuiz);
        assertTrue(quizCreated, "Failed to create quiz");

        TakenQuiz quiz = new TakenQuiz(null, (long)newUser.getUserId(), newQuiz.getQuizId(), 8, LocalDateTime.now(), Duration.ofMinutes(10), "Good111", "Completed");
        Long id = takenQuizService.createTakenQuiz(quiz);

        TakenQuiz retrievedQuiz = takenQuizService.getTakenQuizById(id);
        assertNotNull(retrievedQuiz);
        assertEquals(quiz.getUserId(), retrievedQuiz.getUserId());
        assertEquals(quiz.getQuizId(), retrievedQuiz.getQuizId());
    }

    @Test
    public void testGetAllTakenQuizzesForUser() {
        User user = createUniqueUser();
        createQuizAndTakenQuizForUser(user);
        List<TakenQuiz> quizzes = takenQuizService.getAllTakenQuizzesForUser((long)user.getUserId());
        assertNotNull(quizzes);
        assertFalse(quizzes.isEmpty());
    }

    @Test
    public void testDeleteTakenQuizById() {
        User user = createUser();
        Long quizId = createQuizAndTakenQuizForUser(user).getQuizId();
        boolean deleted = takenQuizService.deleteTakenQuizById(quizId);
        TakenQuiz retrievedQuiz = takenQuizService.getTakenQuizById(quizId);
        assertTrue(deleted);
        assertNull(retrievedQuiz);
    }

    @Test
    public void testUpdateTakenQuiz() {
        User user = createUser();
        TakenQuiz quiz = createQuizAndTakenQuizForUser(user);
        quiz.setScore(9);
        boolean updated = takenQuizService.updateTakenQuiz(quiz);
        TakenQuiz updatedQuiz = takenQuizService.getTakenQuizById(quiz.getTakenQuizId());
        assertTrue(updated);
        assertEquals(9, updatedQuiz.getScore());
    }

    @Test
    public void testCalculateAverageScoreForUser() {
        User user = createUser();
        createQuizAndTakenQuizForUser(user);
        createQuizAndTakenQuizForUser(user);
        double average = takenQuizService.calculateAverageScoreForUser((long)user.getUserId());
        assertEquals(8.0, average);
    }


    @Test
    public void shouldCreateAndRetrieveTakenQuiz() {
        // Using unique data
        User newUser = createUniqueUser();
        createAndAssertQuizForUser(newUser);
    }

    @Test
    public void shouldRetrieveAllTakenQuizzesForUser() {
        User user = createUniqueUser();
        createAndAssertQuizForUser(user);
        List<TakenQuiz> quizzes = takenQuizService.getAllTakenQuizzesForUser((long)user.getUserId());
        assertNotNull(quizzes);
        assertFalse(quizzes.isEmpty());
    }
    private User createUser() {
        User newUser = new User("testUsername", "testHashedPassword", "testEmail@example.com", false);
        userService.registerUser(newUser);
        usersToDelete.add(newUser);
        return newUser;
    }

    private TakenQuiz createQuizAndTakenQuizForUser(User user) {
        if(user.getUserId() == 0) {
            throw new IllegalStateException("User creation failed");
        }

        String uniqueTitle = "Sample Quiz " + System.currentTimeMillis();
        Quiz newQuiz = new Quiz(uniqueTitle, "Sample Description", (long)user.getUserId(), LocalDateTime.now());

        if(!quizService.createQuiz(newQuiz) || newQuiz.getQuizId() == null) {
            throw new IllegalStateException("Quiz creation failed");
        }

        TakenQuiz takenQuiz = new TakenQuiz(null, (long)user.getUserId(), newQuiz.getQuizId(), 8, LocalDateTime.now(), Duration.ofMinutes(10), "Good", "Completed");
        Long takenQuizId = takenQuizService.createTakenQuiz(takenQuiz);

        if(takenQuizId == null) {
            throw new IllegalStateException("TakenQuiz creation failed");
        }
        return takenQuiz;
    }



    private User createUniqueUser() {
        String uniqueValue = Long.toString(System.currentTimeMillis());
        User newUser = new User("testUsername" + uniqueValue, "testHashedPassword" + uniqueValue, "testEmail" + uniqueValue + "@example.com", false);
        boolean isRegistered = userService.registerUser(newUser);
        if (!isRegistered || newUser.getUserId() == 0) {
            throw new IllegalStateException("User creation failed");
        }
        usersToDelete.add(newUser);
        return newUser;
    }

    private TakenQuiz createAndAssertQuizForUser(User user) {
        String uniqueTitle = "Sample Quiz " + System.currentTimeMillis();
        Quiz newQuiz = new Quiz(uniqueTitle, "Sample Description", (long)user.getUserId(), LocalDateTime.now());
        boolean isCreated = quizService.createQuiz(newQuiz);
        if (!isCreated || newQuiz.getQuizId() == null) {
            throw new IllegalStateException("Quiz creation failed");
        }
        TakenQuiz takenQuiz = new TakenQuiz(null, (long)user.getUserId(), newQuiz.getQuizId(), 8, LocalDateTime.now(), Duration.ofMinutes(10), "Good", "Completed");
        Long takenQuizId = takenQuizService.createTakenQuiz(takenQuiz);
        if (takenQuizId == null) {
            throw new IllegalStateException("TakenQuiz creation failed");
        }
        return takenQuiz;
    }
}
