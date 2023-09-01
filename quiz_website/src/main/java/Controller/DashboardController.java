package Controller;

import service.TakenQuizService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/dashboardController")
public class DashboardController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        TakenQuizService takenQuizService = new TakenQuizService();
//        Map<Long, Long> popularQuizzes = takenQuizService.getMostAttemptedQuizzes(5);  // for the top 5 quizzes, for instance
//        req.setAttribute("popularQuizzes", popularQuizzes);
        req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
    }

}
