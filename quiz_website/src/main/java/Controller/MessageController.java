package Controller;

import model.Message;
import service.MessageService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/messages/*")
public class MessageController extends HttpServlet {

    private final MessageService messageService = new MessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            int userId = Integer.parseInt(req.getParameter("userId"));
            List<Message> messages = messageService.getAllMessagesForUser(userId);
        } else {
            String[] pathParts = pathInfo.split("/");
            if (pathParts[1].equals("unread")) {
                int userId = Integer.parseInt(req.getParameter("userId"));
                List<Message> unreadMessages = messageService.getUnreadMessagesForUser(userId);
            } else if (pathParts[1].equals("between")) {
                int user1Id = Integer.parseInt(req.getParameter("user1Id"));
                int user2Id = Integer.parseInt(req.getParameter("user2Id"));
                List<Message> messagesBetween = messageService.getMessagesBetweenUsers(user1Id, user2Id);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String messageType = req.getParameter("type");
        if ("FRIEND_REQUEST".equals(messageType)) {
            int senderId = Integer.parseInt(req.getParameter("senderId"));
            int receiverId = Integer.parseInt(req.getParameter("receiverId"));
            messageService.sendFriendRequest(senderId, receiverId);
        } else if ("CHALLENGE".equals(messageType)) {
            int senderId = Integer.parseInt(req.getParameter("senderId"));
            int receiverId = Integer.parseInt(req.getParameter("receiverId"));
            int quizId = Integer.parseInt(req.getParameter("quizId"));
            int score = Integer.parseInt(req.getParameter("score"));
            messageService.sendChallenge(senderId, receiverId, quizId, score);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long messageId = Long.parseLong(req.getParameter("messageId"));
        boolean deleted = messageService.deleteMessage(messageId);

    }

}
