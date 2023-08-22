package service;

import model.Message;
import Dao.MessageDAO;

import java.time.LocalDateTime;
import java.util.List;

public class MessageService {

    private final MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public void clearMessagesTable() {
        messageDAO.clearMessagesTable();
    }


    public Long createMessage(Message message) {
        return messageDAO.createMessage(message);
    }

    public Message getMessageById(Long messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public boolean markMessageAsRead(Long messageId) {
        return messageDAO.markMessageAsRead(messageId);
    }

    public List<Message> getAllMessagesForUser(int userId) {
        return messageDAO.getAllMessagesForUser(userId);
    }

    public List<Message> getUnreadMessagesForUser(int userId) {
        return messageDAO.getUnreadMessagesForUser(userId);
    }

    public List<Message> getMessagesBetweenUsers(int user1Id, int user2Id) {
        return messageDAO.getMessagesBetweenUsers(user1Id, user2Id);
    }

    public boolean deleteMessage(Long messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public Long sendFriendRequest(int senderId, int receiverId) {
        String content = "Would you like to be friends?";
        Message message = new Message(null, senderId, receiverId, content, LocalDateTime.now(), false, Message.MessageType.FRIEND_REQUEST, null, null);
        return messageDAO.createMessage(message);
    }

    public Long sendChallenge(int senderId, int receiverId, int quizId, int score) {
        String challengeContent = "I scored " + score + " on this quiz! Can you beat me?";
        Message message = new Message(null, senderId, receiverId, challengeContent, LocalDateTime.now(), false, Message.MessageType.CHALLENGE, quizId, score);
        return messageDAO.createMessage(message);
    }
}
