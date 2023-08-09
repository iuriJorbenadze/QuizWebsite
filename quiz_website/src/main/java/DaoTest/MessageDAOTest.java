package DaoTest;

import static org.junit.jupiter.api.Assertions.*;

import Dao.MessageDAO;
import Dao.UserDAO;
import model.Message;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MessageDAOTest {

    private MessageDAO messageDAO;
    private UserDAO userDAO;
    private User testUser1;
    private User testUser2;

    @BeforeEach
    public void setup() {
        messageDAO = new MessageDAO();
        userDAO = new UserDAO();

        String username1 = "TestUser_" + UUID.randomUUID().toString();
        String username2 = "TestUser_" + UUID.randomUUID().toString();

        testUser1 = new User(username1, "password", username1 + "@gmail.com", false);
        testUser2 = new User(username2, "password", username2 + "@gmail.com", false);

        userDAO.createUser(testUser1);
        userDAO.createUser(testUser2);
    }

    @AfterEach
    public void tearDown() {
        messageDAO.clearMessagesTable();

        userDAO.deleteUser(testUser1.getUserId());
        userDAO.deleteUser(testUser2.getUserId());
    }

    @Test
    public void testSendMessage() {
        Message message = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Hello", LocalDateTime.now(), false);
        Long messageId = messageDAO.createMessage(message);
        assertNotNull(messageId);
        assertEquals(messageId, message.getMessageId());
    }

    @Test
    public void testGetAllMessagesForUser() {
        Message message1 = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Hello1", LocalDateTime.now(), false);
        Message message2 = new Message(0L, testUser2.getUserId(), testUser1.getUserId(), "Hello2", LocalDateTime.now(), true);

        messageDAO.createMessage(message1);
        messageDAO.createMessage(message2);

        List<Message> messages = messageDAO.getAllMessagesForUser(testUser1.getUserId());
        assertTrue(messages.size() >= 1);
    }

    @Test
    public void testGetUnreadMessagesForUser() {
        Message message = new Message(0L, testUser2.getUserId(), testUser1.getUserId(), "Hello1", LocalDateTime.now(), false);

        messageDAO.createMessage(message);
        List<Message> unreadMessages = messageDAO.getUnreadMessagesForUser(testUser1.getUserId());
        assertTrue(unreadMessages.size() >= 1);
    }

    @Test
    public void testGetMessagesBetweenUsers() {
        Message message1 = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Hello1", LocalDateTime.now(), false);
        Message message2 = new Message(0L, testUser2.getUserId(), testUser1.getUserId(), "Hello2", LocalDateTime.now(), true);

        messageDAO.createMessage(message1);
        messageDAO.createMessage(message2);

        List<Message> messages = messageDAO.getMessagesBetweenUsers(testUser1.getUserId(), testUser2.getUserId());
        assertTrue(messages.size() >= 2);
    }

    @Test
    public void testDeleteMessage() {
        Message message = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Hello", LocalDateTime.now(), false);
        Long messageId = messageDAO.createMessage(message);

        boolean deleted = messageDAO.deleteMessage(messageId);
        assertTrue(deleted);

        Message retrievedMessage = messageDAO.getMessageById(messageId);
        assertNull(retrievedMessage);
    }
}
