package DaoTest;

import static org.junit.jupiter.api.Assertions.*;

import Dao.MessageDAO;
import Dao.UserDAO;
import model.Message;
import model.Message.MessageType;
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
        Message message = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Hello", LocalDateTime.now(), false, MessageType.NOTE, null, null);
        Long messageId = messageDAO.createMessage(message);
        assertNotNull(messageId);
        assertEquals(messageId, message.getMessageId());
    }

    @Test
    public void testGetAllMessagesForUser() {
        Message message1 = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Hello1", LocalDateTime.now(), false, MessageType.NOTE, null, null);
        Message message2 = new Message(0L, testUser2.getUserId(), testUser1.getUserId(), "Hello2", LocalDateTime.now(), true, MessageType.NOTE, null, null);

        messageDAO.createMessage(message1);
        messageDAO.createMessage(message2);

        List<Message> messages = messageDAO.getAllMessagesForUser(testUser1.getUserId());
        assertTrue(messages.size() >= 2); // It should return both messages sent and received
    }




    @Test
    public void testGetUnreadMessagesForUser() {
        Message message = new Message(0L, testUser2.getUserId(), testUser1.getUserId(), "Hello1", LocalDateTime.now(), false, MessageType.NOTE, null, null);

        messageDAO.createMessage(message);
        List<Message> unreadMessages = messageDAO.getUnreadMessagesForUser(testUser1.getUserId());
        assertTrue(unreadMessages.size() >= 1);
    }

    @Test
    public void testGetMessagesBetweenUsers() {
        Message message1 = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Hello1", LocalDateTime.now(), false, MessageType.NOTE, null, null);
        Message message2 = new Message(0L, testUser2.getUserId(), testUser1.getUserId(), "Hello2", LocalDateTime.now(), true, MessageType.NOTE, null, null);

        messageDAO.createMessage(message1);
        messageDAO.createMessage(message2);

        List<Message> messages = messageDAO.getMessagesBetweenUsers(testUser1.getUserId(), testUser2.getUserId());
        assertTrue(messages.size() >= 2);
    }

    @Test
    public void testDeleteMessage() {
        Message message = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Hello", LocalDateTime.now(), false, MessageType.NOTE, null, null);
        Long messageId = messageDAO.createMessage(message);

        boolean deleted = messageDAO.deleteMessage(messageId);
        assertTrue(deleted);

        Message retrievedMessage = messageDAO.getMessageById(messageId);
        assertNull(retrievedMessage);
    }

    @Test
    public void testMarkMessageAsRead() {
        Message message = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Hello", LocalDateTime.now(), false, MessageType.NOTE, null, null);
        Long messageId = messageDAO.createMessage(message);
        boolean marked = messageDAO.markMessageAsRead(messageId);
        assertTrue(marked);

        Message retrievedMessage = messageDAO.getMessageById(messageId);
        assertTrue(retrievedMessage.isRead());
    }

    @Test
    public void testRetrieveNonExistentMessage() {
        Message retrievedMessage = messageDAO.getMessageById(99999L);  // Assuming 99999 is a non-existent ID
        assertNull(retrievedMessage);
    }

    @Test
    public void testMessageTypes() {
        Message friendRequest = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Friend Request content", LocalDateTime.now(), false, MessageType.FRIEND_REQUEST, null, null);
        Message challenge = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Challenge content", LocalDateTime.now(), false, MessageType.CHALLENGE, 12345, 85);  // Assuming 12345 is a quiz ID
        Message note = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Note content", LocalDateTime.now(), false, MessageType.NOTE, null, null);

        Long friendRequestId = messageDAO.createMessage(friendRequest);
        Long challengeId = messageDAO.createMessage(challenge);
        Long noteId = messageDAO.createMessage(note);

        assertNotNull(friendRequestId);
        assertNotNull(challengeId);
        assertNotNull(noteId);
    }

    @Test
    public void testOrderOfMessagesForUser() {
        Message olderMessage = new Message(0L, testUser1.getUserId(), testUser2.getUserId(), "Hello1", LocalDateTime.now().minusDays(1), false, Message.MessageType.NOTE, null, null);
        Message newerMessage = new Message(0L, testUser2.getUserId(), testUser1.getUserId(), "Hello2", LocalDateTime.now(), true, Message.MessageType.NOTE, null, null);

        messageDAO.createMessage(olderMessage);
        messageDAO.createMessage(newerMessage);

        List<Message> messages = messageDAO.getAllMessagesForUser(testUser1.getUserId());
        assertTrue(messages.get(0).getSentDate().isAfter(messages.get(1).getSentDate()));
    }
}
