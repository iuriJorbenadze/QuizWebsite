package ServiceTest;


import Dao.QuizDAO;
import model.Quiz;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.QuizService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizServiceTest {

    private QuizService quizService;
    private QuizDAO quizDAO;

    @BeforeEach
    public void setup() {
        quizDAO = new QuizDAO();  // Initialize connection to database
        quizService = new QuizService(quizDAO);

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testGetQuizByIdWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> quizService.getQuizById(null));
    }

    @Test
    public void testGetAllQuizzes() {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        assertNotNull(quizzes);  // Check that the list is not null
        assertFalse(quizzes.isEmpty());  // Check that there's at least one quiz (depends on your seed data)
    }

    @Test
    public void testCreateQuizWithExistingName() {
        Quiz existingQuiz = quizService.getAllQuizzes().get(0);  // get first quiz from the seeded data
        Quiz newQuiz = new Quiz();
        newQuiz.setTitle(existingQuiz.getTitle());
        assertThrows(IllegalArgumentException.class, () -> quizService.createQuiz(newQuiz));
    }



    @Test
    public void testUpdateQuizWithoutId() {
        Quiz quizToUpdate = new Quiz();
        quizToUpdate.setTitle("Some Title");
        assertThrows(IllegalArgumentException.class, () -> quizService.updateQuiz(quizToUpdate));
    }

    @Test
    public void testUpdateQuizWithValidData() {
        Quiz quizToUpdate = quizService.getAllQuizzes().get(0);  // get first quiz from the seeded data
        quizToUpdate.setTitle("Updated Title");
        assertTrue(quizService.updateQuiz(quizToUpdate));
    }

    @Test
    public void testDeleteQuizWithValidId() {
        Quiz quizToDelete = quizService.getAllQuizzes().get(0);  // get first quiz from the seeded data
        assertTrue(quizService.deleteQuiz(quizToDelete.getQuizId()));
    }

    @Test
    public void testValidateQuizWithNull() {
        assertThrows(IllegalArgumentException.class, () -> quizService.createQuiz(null));
    }

    @Test
    public void testValidateQuizWithEmptyTitle() {
        Quiz emptyTitleQuiz = new Quiz();
        emptyTitleQuiz.setTitle("");
        assertThrows(IllegalArgumentException.class, () -> quizService.createQuiz(emptyTitleQuiz));
    }
}
