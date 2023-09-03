package Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.TakenQuiz;
import service.TakenQuizService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/ResultController")
public class ResultController extends HttpServlet {

    private TakenQuizService takenQuizService = new TakenQuizService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "getById":
                Long id = Long.parseLong(req.getParameter("id"));
                TakenQuiz takenQuiz = takenQuizService.getTakenQuizById(id);
                resp.getWriter().write(takenQuiz.toString());
                break;

            case "getAllForUser":
                Long userId = Long.parseLong(req.getParameter("userId"));
                List<TakenQuiz> quizzesForUser = takenQuizService.getAllTakenQuizzesForUser(userId);
                resp.getWriter().write(quizzesForUser.toString());
                break;

            case "getLatestForUser":
                userId = Long.parseLong(req.getParameter("userId"));
                TakenQuiz latestQuiz = takenQuizService.getLatestTakenQuizForUser(userId);
                resp.getWriter().write(latestQuiz.toString());
                break;

            case "getTopScoringUsersForQuiz":
                Long quizId = Long.parseLong(req.getParameter("quizId"));
                int limit = Integer.parseInt(req.getParameter("limit"));
                List<TakenQuiz> topScoringUsers = takenQuizService.getTopScoringUsersForQuiz(quizId, limit);
                resp.getWriter().write(topScoringUsers.toString());
                break;

            case "getFailedQuizzesForUser":
                userId = Long.parseLong(req.getParameter("userId"));
                List<TakenQuiz> failedQuizzes = takenQuizService.getAllFailedQuizzesForUser(userId);
                resp.getWriter().write(failedQuizzes.toString());
                break;
            case "calculateAverageScoreForUser":
                userId = Long.parseLong(req.getParameter("userId"));
                double averageScore = takenQuizService.calculateAverageScoreForUser(userId);
                resp.getWriter().write(Double.toString(averageScore));
                break;

            case "getQuizzesTakenInLastMonth":
                userId = Long.parseLong(req.getParameter("userId"));
                List<TakenQuiz> quizzes = takenQuizService.getQuizzesTakenInLastMonth(userId);
                resp.getWriter().write(quizzes.toString());
                break;

            case "getMostAttemptedQuizzes":
                limit = Integer.parseInt(req.getParameter("limit"));
                Map<Long, Long> mostAttempted = takenQuizService.getMostAttemptedQuizzes(limit);

                String json = new Gson().toJson(mostAttempted);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(json);
                break;


            case "getRecentQuizTakers":
                limit = Integer.parseInt(req.getParameter("limit"));
                List<TakenQuiz> recentTakers = takenQuizService.getRecentQuizTakers(limit);
                resp.getWriter().write(recentTakers.toString());
                break;

            case "getQuizzesWithScoresAboveThreshold":
                int threshold = Integer.parseInt(req.getParameter("threshold"));
                List<TakenQuiz> aboveThresholdQuizzes = takenQuizService.getQuizzesWithScoresAboveThreshold(threshold);
                resp.getWriter().write(aboveThresholdQuizzes.toString());
                break;

            case "getAverageTimeTakenForQuiz":
                quizId = Long.parseLong(req.getParameter("quizId"));
                double averageTime = takenQuizService.getAverageTimeTakenForQuiz(quizId);
                resp.getWriter().write(Double.toString(averageTime));
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("create".equals(action)) {
            TakenQuiz takenQuiz = new TakenQuiz();
            // Populate the takenQuiz object with data...
            takenQuizService.createTakenQuiz(takenQuiz);
            resp.getWriter().write("Quiz result saved successfully!");
        } else if ("updateFeedbackForLowScoringQuizzes".equals(action)) {
            Long userId = Long.parseLong(req.getParameter("userId"));
            int threshold = Integer.parseInt(req.getParameter("threshold"));
            String feedback = req.getParameter("feedback");
            takenQuizService.updateFeedbackForLowScoringQuizzes(userId, threshold, feedback);
            resp.getWriter().write("Feedback updated for low scoring quizzes!");
        }



        if ("submitQuiz".equals(action)) {
            System.out.println("Inside submitQuiz action");
            try {
                String jsonPayload = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                System.out.println("jsonpayload: " + jsonPayload);

                Gson gson = new Gson();
                Type type = new TypeToken<Map<Long, String>>(){}.getType();

                Map<Long, String> userAnswers = gson.fromJson(jsonPayload, type);
                int score = takenQuizService.calculateScore(userAnswers);

                System.out.println("Calculated Score: " + score);

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write("{\"message\": \"Quiz submitted successfully!\", \"score\":" + score + "}");

            } catch (Exception e) {
                e.printStackTrace(); // to get a clear understanding of what went wrong
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\":\"Error processing quiz submission\"}");
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TakenQuiz takenQuiz = new TakenQuiz();
        takenQuizService.updateTakenQuiz(takenQuiz);
        resp.getWriter().write("Quiz result updated successfully!");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        takenQuizService.deleteTakenQuizById(id);
        resp.getWriter().write("Quiz result deleted successfully!");
    }
    //all functions added
}