package Controller;

import Dao.QuizDAO;
import model.Quiz;
import service.QuizService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/QuizController")
public class QuizController extends HttpServlet {

    private QuizService quizService = new QuizService(new QuizDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter out = resp.getWriter();

        switch (action) {
            case "getQuizById":
                Long id = Long.parseLong(req.getParameter("id"));
                Quiz quizById = quizService.getQuizById(id);
                if (quizById != null) {
                    out.print(quizById.toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("Quiz not found.");
                }
                break;

            case "getAllQuizzes":
                List<Quiz> quizzes = quizService.getAllQuizzes();
                for (Quiz quiz : quizzes) {
                    out.println(quiz.toString());
                }
                break;

            default:
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Invalid action");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        Long createdByUserId = Long.parseLong(req.getParameter("createdByUserId"));
        LocalDateTime createdDate = LocalDateTime.now();

        Quiz newQuiz = new Quiz(title, description, createdByUserId, createdDate);
        handleQuizCreationOrUpdate(newQuiz, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        Long createdByUserId = Long.parseLong(req.getParameter("createdByUserId"));
        LocalDateTime createdDate = LocalDateTime.parse(req.getParameter("createdDate"));

        Quiz updateQuiz = new Quiz(id, title, description, createdByUserId, createdDate);
        handleQuizCreationOrUpdate(updateQuiz, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        Long deleteId = Long.parseLong(req.getParameter("id"));

        try {
            if (quizService.deleteQuiz(deleteId)) {
                out.println("Quiz deleted successfully!");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Quiz deletion failed!");
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(e.getMessage());
        }
    }

    private void handleQuizCreationOrUpdate(Quiz quiz, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try {
            if (quiz.getQuizId() == null && quizService.createQuiz(quiz)) {
                out.println("Quiz created successfully!");
            } else if (quizService.updateQuiz(quiz)) {
                out.println("Quiz updated successfully!");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(quiz.getQuizId() == null ? "Quiz creation failed!" : "Quiz update failed!");
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(e.getMessage());
        }
    }
    //all functions added
}