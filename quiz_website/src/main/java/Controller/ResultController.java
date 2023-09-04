package Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.cj.conf.ConnectionUrlParser;
import model.ResultInfo;
import model.ResultsResponse;
import model.TakenQuiz;
import model.User;
import service.TakenQuizService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/ResultController")
public class ResultController extends HttpServlet {

    private TakenQuizService takenQuizService = new TakenQuizService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        System.out.println("GET Request action: " + action);

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

            case "getQuizResults":

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                Gson gson = new Gson();

                List<ResultInfo> results = (List<ResultInfo>) req.getSession().getAttribute("quizResults");
                Integer totalScore = (Integer) req.getSession().getAttribute("totalScore");

                if(results == null || totalScore == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"message\":\"Results or totalScore not found in the session.\"}");
                    System.out.println("getQuizResults, results or total score is null");
                    return;  // Important: This will exit the method early to avoid further processing.
                }

                //Long quizIdCur = Long.parseLong(req.getParameter("quizId"));


                String jsonResponse = gson.toJson(new ResultsResponse( totalScore,results));
                resp.getWriter().write(jsonResponse);


                System.out.println("getQuizResults json: "+ jsonResponse);
                break;

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        System.out.println("POST Request action: " + action);

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

                // Retrieve the combined payload
                Type payloadType = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> payload = gson.fromJson(jsonPayload, payloadType);

                // Extract userAnswers and quizId from the payload
                Type answersType = new TypeToken<Map<Long, String>>(){}.getType();
//                System.out.println("payload answers: " + payload.get("answers").toString());

                String answersJson = gson.toJson(payload.get("answers"));
                Map<Long, String> userAnswers = gson.fromJson(answersJson, answersType);


                Long quizIdCur = Long.parseLong(payload.get("quizId").toString());

                int score = takenQuizService.calculateScore(userAnswers);
                System.out.println("Calculated Score: " + score);

                // Creating the TakenQuiz object
                LocalDateTime endTime = LocalDateTime.now();
                LocalDateTime startTime = (LocalDateTime) req.getSession().getAttribute("startTime");
                if (startTime == null) {
                    // Handle the case if startTime is not set, e.g., set to current time or log an error
                    startTime = LocalDateTime.now();
                }
                Duration durationTaken = Duration.between(startTime, endTime);

                TakenQuiz takenQuiz = new TakenQuiz();
                User currentUser = (User) req.getSession().getAttribute("user");
                if (currentUser != null) {
                    takenQuiz.setUserId((long)currentUser.getUserId());
                } else {
                    // Handle the scenario when the user object is not found in the session
                    System.err.println("No user found in session!");
                }

                takenQuiz.setQuizId(quizIdCur);
                takenQuiz.setScore(score);
                takenQuiz.setAttemptDate(endTime);
                takenQuiz.setTimeTaken(durationTaken);

                //feedback should be added from a user after finishing

                takenQuiz.setStatus("Completed");

                // Save takenQuiz to database
                takenQuizService.createTakenQuiz(takenQuiz);
                System.out.println("Taken quiz created");

                if (isAjaxRequest(req)) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    resp.getWriter().write("{\"message\": \"Quiz submitted successfully!\", \"score\":" + score + "}");
                } else {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    ConnectionUrlParser.Pair<Integer, List<ResultInfo>> result = takenQuizService.calculateScoreDetailed(userAnswers);

                    // This is a potential place to set the attributes
                    req.getSession().setAttribute("quizResults", result.right);
                    req.getSession().setAttribute("totalScore", result.left);

                    // Convert the result to JSON and send it back
                    String jsonResponse = gson.toJson(new ResultsResponse(result.left, result.right));
                    resp.getWriter().write(jsonResponse);
                    System.out.println("Response JSON: " + jsonResponse);
                }

            } catch (Exception e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\":\"Error processing quiz submission\"}");
            }
        }




    }

    private boolean isAjaxRequest(HttpServletRequest req) {
        String requestedWithHeader = req.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWithHeader);
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