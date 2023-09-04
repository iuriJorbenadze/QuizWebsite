package Controller;

import Dao.QuestionDAO;
import convinience.GsonUtil;
import Dao.QuizDAO;
import com.google.gson.Gson;
import model.Option;
import model.Question;
import model.Quiz;
import service.QuestionService;
import service.QuizService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/QuizController")
public class QuizController extends HttpServlet {

    private QuizService quizService = new QuizService(new QuizDAO());
    private QuestionService questionService = new QuestionService(new QuestionDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter out = resp.getWriter();

        switch (action) {
            case "getQuizById":
                Long id = Long.parseLong(req.getParameter("id"));
                Quiz quizById = quizService.getQuizById(id);
                if (quizById != null) {
                    if ("json".equals(req.getParameter("format"))) {
                        String json = GsonUtil.CUSTOM_GSON.toJson(quizById);
                        out.print(json);
                    } else {
                        req.setAttribute("quiz", quizById);
                        req.getRequestDispatcher("/quiz.jsp").forward(req, resp);
                    }
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

            case "displayQuiz":
                req.getRequestDispatcher("/quiz.jsp").forward(req, resp);
                break;

            case "takeQuiz":
                Long quizId = Long.parseLong(req.getParameter("quizId"));
                Quiz quiz = quizService.getQuizById(quizId);

                List<Question> questions = questionService.getAllQuestionsForQuiz(quizId);

                req.setAttribute("quiz", quiz);
                req.setAttribute("questions", questions);
                req.getRequestDispatcher("/takeQuiz.jsp").forward(req, resp);
                break;
            case "submitQuiz":
                Long quizIdForSubmission = Long.parseLong(req.getParameter("quizId"));  // Make sure to include the quizId as a hidden field in your form
                List<Question> questionsForSubmission = questionService.getAllQuestionsForQuiz(quizIdForSubmission);

                Map<Long, String> answers = new HashMap<>();
                for(Question q: questionsForSubmission) {
                    String answer = req.getParameter("answer" + q.getQuestionId());
                    answers.put(q.getQuestionId(), answer);
                }

                int score = quizService.calculateScore(answers);
                // Store the score and maybe redirect the user to a results page
                break;


            case "createQuiz":
                Long userId = Long.parseLong(req.getParameter("userId"));
                // You might also store the userId in the session or request for use in the JSP form
                req.setAttribute("userId", userId);
                req.getRequestDispatcher("/createQuiz.jsp").forward(req, resp);
                break;


//            case "displayQuiz":
//                String quizIdStr = req.getParameter("quizId");
//                if (quizIdStr == null || quizIdStr.trim().isEmpty()) {
//                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                    out.println("Missing or incorrect quizId parameter.");
//                    return; // Exit the method
//                }
//
//                Long quizId;
//                try {
//                    quizId = Long.parseLong(quizIdStr);
//                } catch (NumberFormatException e) {
//                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                    out.println("Invalid quizId parameter.");
//                    return; // Exit the method
//                }
//
//                Quiz quiz = quizService.getQuizById(quizId);
//                if (quiz != null) {
//                    req.setAttribute("quiz", quiz);
//                    req.getRequestDispatcher("/quiz.jsp").forward(req, resp);
//                } else {
//                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                    out.println("Quiz not found.");
//                }
//                break;

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String operation = req.getParameter("operation");

        switch (operation) {
            case "createQuiz":
                // Extracting details from the form
                String title = req.getParameter("title");
                String description = req.getParameter("description");
                Long createdByUserId = Long.parseLong(req.getParameter("createdByUserId"));
                LocalDateTime createdDate = LocalDateTime.now();

                // Creating the Quiz object
                Quiz newQuiz = new Quiz(null, title, description, createdByUserId, createdDate);
                handleQuizCreationOrUpdate(newQuiz, resp);

                // Fetching the ID of the newly created Quiz
                Long quizId = newQuiz.getQuizId();

                // Extract questions, options, and correct answers from the request
                String[] questionContents = req.getParameterValues("questionText[]");

                if (questionContents != null) {
                    for (int i = 0; i < questionContents.length; i++) {
                        List<String> options = new ArrayList<>();
                        for (int j = 1; j <= 4; j++) {
                            String[] optionArray = req.getParameterValues("option" + j + "[]");
                            if (optionArray != null && optionArray.length > i) {
                                options.add(optionArray[i]);
                            }
                        }

                        String[] correctOptionsArray = req.getParameterValues("correctOption[]");
                        String correctOption = (correctOptionsArray != null && correctOptionsArray.length > i)
                                ? options.get(Integer.parseInt(correctOptionsArray[i]) - 1)
                                : null;

                        Question newQuestion = new Question(quizId, questionContents[i], options, correctOption);
                        questionService.createQuestion(newQuestion);
                    }
                }

                // Redirect to the quiz page after creation
                resp.sendRedirect("/QuizController?action=displayQuiz&quizId=" + quizId);
                break;
        }
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