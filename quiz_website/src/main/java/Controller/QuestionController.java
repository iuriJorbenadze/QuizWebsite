package Controller;

import Dao.QuestionDAO;
import com.google.gson.Gson;
import model.Question;
import service.QuestionService;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;
import java.util.stream.Collectors;



@WebServlet("/QuestionController")
public class QuestionController extends HttpServlet {

    private final QuestionService questionService = new QuestionService(new QuestionDAO());;

    public QuestionController() {

    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");


        if ("get".equals(action)) {
            Long id = Long.parseLong(req.getParameter("id"));
            Question question = questionService.getQuestionById(id);
            resp.getWriter().write(toJson(question));
        } else if ("getAllQuestionsForQuiz".equals(action)) {
            try {
                Long quizId = Long.parseLong(req.getParameter("quizId"));
                List<Question> questions = questionService.getAllQuestionsForQuiz(quizId);

                // Convert the list of questions to JSON and send back as a response
                String json = new Gson().toJson(questions); // Assuming you are using Gson library
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(json);
            } catch (NumberFormatException e) {
                // handle exception, maybe return an error response
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid quiz ID");
            }
        } else {
            // Handle unknown action, maybe return a 404 or a default response
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        if (action.startsWith("/complete/")) {
            Long id = Long.parseLong(action.split("/")[2]);
            Question completeQuestion = questionService.getCompleteQuestionById(id);
            resp.getWriter().write(toJson(completeQuestion));
        } else if (action.startsWith("/isCorrect/")) {
            Long id = Long.parseLong(action.split("/")[2]);
            String userAnswer = req.getParameter("answer");
            boolean isCorrect = questionService.isAnswerCorrect(questionService.getQuestionById(id), userAnswer);
            resp.getWriter().write(Boolean.toString(isCorrect));
        } else if (action.startsWith("/options/")) {
            Long id = Long.parseLong(action.split("/")[2]);
            List<String> options = questionService.fetchOptionsForQuestion(id);
            resp.getWriter().write(Json.createArrayBuilder(options).build().toString());
        } else if (action.startsWith("/questionsByIds/")) {
            String[] idStrings = req.getParameter("ids").split(",");
            List<Long> ids = Arrays.stream(idStrings).map(Long::valueOf).collect(Collectors.toList());
            List<Question> questions = questionService.getQuestionsByIds(ids);
            resp.getWriter().write(toJson(questions));
        }
        if (action.startsWith("/randomizedOrder/")) {
            Long id = Long.parseLong(action.split("/")[2]);
            Question randomizedQuestion = questionService.getRandomizedOptionOrder(questionService.getQuestionById(id));
            resp.getWriter().write(toJson(randomizedQuestion));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();

        if (action.equals("/create")) {
            Question question = fromJson(req);
            boolean result = questionService.createQuestion(question);
            resp.getWriter().write(Boolean.toString(result));
        }

        if (action.startsWith("/addOptions/")) {
            Long id = Long.parseLong(action.split("/")[2]);
            Question question = fromJson(req);
            boolean result = questionService.addOptionsToQuestion(id, question.getOptions());
            resp.getWriter().write(Boolean.toString(result));
        }
        if (action.equals("/createBatch")) {
            List<Question> questions = fromJsonList(req);
            boolean result = questionService.createQuestions(questions);
            resp.getWriter().write(Boolean.toString(result));
        }
        if (action.startsWith("/awardPoints/")) {
            Long id = Long.parseLong(action.split("/")[2]);
            String userAnswer = req.getParameter("answer");
            Question question = questionService.getQuestionById(id);
            int points = questionService.awardPoints(question, userAnswer);
            resp.getWriter().write(Integer.toString(points));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();

        if (action.equals("/update")) {
            Question question = fromJson(req);
            boolean result = questionService.updateQuestion(question);
            resp.getWriter().write(Boolean.toString(result));
        }

        if (action.startsWith("/modifyOptions/")) {
            Long id = Long.parseLong(action.split("/")[2]);
            Question question = fromJson(req);
            boolean result = questionService.modifyOptionsForQuestion(id, question.getOptions());
            resp.getWriter().write(Boolean.toString(result));
        } else if (action.equals("/updateComplete")) {
            Question question = fromJson(req);
            boolean result = questionService.updateCompleteQuestion(question);
            resp.getWriter().write(Boolean.toString(result));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo();

        if (action.startsWith("/delete/")) {
            Long id = Long.parseLong(action.split("/")[2]);
            boolean result = questionService.deleteQuestion(id);
            resp.getWriter().write(Boolean.toString(result));
        } else if (action.startsWith("/removeAllOptions/")) {
            Long id = Long.parseLong(action.split("/")[2]);
            boolean result = questionService.removeAllOptionsFromQuestion(id);
            resp.getWriter().write(Boolean.toString(result));
        }
    }



    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(500);
        response.getWriter().write(e.getMessage());
    }


    private String toJson(Question question) {
        JsonObject json = Json.createObjectBuilder()
                .add("questionId", question.getQuestionId())
                .add("quizId", question.getQuizId())
                .add("content", question.getContent())
                .add("correctAnswer", question.getCorrectAnswer())
                .add("options", Json.createArrayBuilder(question.getOptions()))
                .build();
        return json.toString();
    }


    private String toJson(List<Question> questions) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Question question : questions) {
            arrayBuilder.add(toJson(question));
        }
        return arrayBuilder.build().toString();
    }


    private Question fromJson(HttpServletRequest req) throws IOException {
        try (JsonReader reader = Json.createReader(req.getReader())) {
            JsonObject jsonObject = reader.readObject();

            Long questionId = jsonObject.containsKey("questionId") ? jsonObject.getJsonNumber("questionId").longValue() : null;
            Long quizId = jsonObject.getJsonNumber("quizId").longValue();
            String content = jsonObject.getString("content");
            String correctAnswer = jsonObject.getString("correctAnswer");

            List<String> options = jsonObject.getJsonArray("options").stream()
                    .map(JsonValue::toString)
                    .collect(Collectors.toList());

            return new Question(questionId, quizId, content, options, correctAnswer);
        }
    }

    private List<Question> fromJsonList(HttpServletRequest req) throws IOException {
        try (JsonReader reader = Json.createReader(req.getReader())) {
            List<JsonObject> jsonArray = reader.readArray().getValuesAs(JsonObject.class);

            return jsonArray.stream().map(jsonObject -> {
                Long questionId = jsonObject.containsKey("questionId") ? jsonObject.getJsonNumber("questionId").longValue() : null;
                Long quizId = jsonObject.getJsonNumber("quizId").longValue();
                String content = jsonObject.getString("content");
                String correctAnswer = jsonObject.getString("correctAnswer");
                List<String> options = jsonObject.getJsonArray("options").stream()
                        .map(JsonValue::toString)
                        .collect(Collectors.toList());

                return new Question(questionId, quizId, content, options, correctAnswer);
            }).collect(Collectors.toList());
        }
    }  
    //comment


    //all functions added
}