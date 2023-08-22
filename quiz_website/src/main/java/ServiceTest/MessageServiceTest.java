package ServiceTest;

import Dao.MessageDAO;
import model.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.MessageService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageServiceTest {

    private MessageService messageService;
    private MessageDAO messageDAO;
    private Message testMessage;

    @BeforeEach
    public void setup() {
        messageDAO = new MessageDAO();
        messageService = new MessageService(messageDAO);


        testMessage = new Message(null, 1, 2, "Test message", LocalDateTime.now(), false, Message.MessageType.NOTE, null, null);
        assertNotNull(messageService.createMessage(testMessage));
    }

    @AfterEach
    public void tearDown() {
        if (testMessage != null) {
            messageService.deleteMessage(testMessage.getMessageId());
        }
    }

    @Test
    public void testCreateAndRetrieveMessage() {
        Message message = messageService.getMessageById(testMessage.getMessageId());
        assertNotNull(message);
        assertEquals(testMessage.getContent(), message.getContent());
    }

    @Test
    public void testMarkMessageAsRead() {
        assertFalse(testMessage.isRead());
        assertTrue(messageService.markMessageAsRead(testMessage.getMessageId()));

        Message updatedMessage = messageService.getMessageById(testMessage.getMessageId());
        assertTrue(updatedMessage.isRead());
    }

    @Test
    public void testDeleteMessage() {
        assertTrue(messageService.deleteMessage(testMessage.getMessageId()));
        assertNull(messageService.getMessageById(testMessage.getMessageId()));

        testMessage = null;
    }

    @Test
    public void testSendFriendRequest() {
        Long messageId = messageService.sendFriendRequest(1, 4);
        assertNotNull(messageId);

        Message friendRequestMessage = messageService.getMessageById(messageId);
        assertEquals("Would you like to be friends?", friendRequestMessage.getContent());

        messageService.deleteMessage(friendRequestMessage.getMessageId());
    }

    @Test
    public void testSendChallenge() {
        Long messageId = messageService.sendChallenge(1, 4, 1001, 95);
        assertNotNull(messageId);

        Message challengeMessage = messageService.getMessageById(messageId);
        assertEquals("I scored 95 on this quiz! Can you beat me?", challengeMessage.getContent());


    }

    @Test
    public void testGetAllMessagesForUser() {
        List<Message> messages = messageService.getAllMessagesForUser(1);
        assertNotNull(messages);
        assertTrue(messages.size() > 0);
    }

    @Test
    public void testGetUnreadMessagesForUser() {
        List<Message> unreadMessages = messageService.getUnreadMessagesForUser(2);
        assertTrue(unreadMessages.stream().anyMatch(msg -> !msg.isRead()));
    }

    @Test
    public void testGetMessagesBetweenUsers() {
        List<Message> messagesBetweenUsers = messageService.getMessagesBetweenUsers(1, 2);
        assertTrue(messagesBetweenUsers.stream().anyMatch(msg -> (msg.getSenderId() == 1 && msg.getReceiverId() == 2) || (msg.getSenderId() == 2 && msg.getReceiverId() == 1)));
    }
}