package Dao;

import model.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO extends AbstractDAO {

    public void clearMessagesTable() {
        String clearTableSQL = "DELETE FROM Messages";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(clearTableSQL)) {

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long createMessage(Message message) {
        String SQL = "INSERT INTO Messages(senderId, receiverId, messageText, dateSent, isRead) VALUES(?, ?, ?, ?, ?)";
        long id = 0;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, message.getSenderId());
            pstmt.setLong(2, message.getReceiverId());
            pstmt.setString(3, message.getContent());
            pstmt.setTimestamp(4, Timestamp.valueOf(message.getSentDate()));
            pstmt.setBoolean(5, message.isRead());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return id;
    }

    public Message getMessageById(long messageId) {
        Message message = null;
        String SQL = "SELECT * FROM Messages WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, messageId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int senderId = rs.getInt("senderId");
                int receiverId = rs.getInt("receiverId");
                String content = rs.getString("messageText");
                LocalDateTime sentDate = rs.getTimestamp("dateSent").toLocalDateTime();
                boolean isRead = rs.getBoolean("isRead");

                message = new Message(messageId, senderId, receiverId, content, sentDate, isRead);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return message;
    }

    public boolean markMessageAsRead(Long messageId) {
        String SQL = "UPDATE Messages SET isRead = ? WHERE id = ?";
        boolean updated = false;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setBoolean(1, true);
            pstmt.setLong(2, messageId);

            updated = pstmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return updated;
    }

    public List<Message> getAllMessagesForUser(int userId) {
        List<Message> messages = new ArrayList<>();
        String SQL = "SELECT * FROM Messages WHERE senderId = ? OR receiverId = ? ORDER BY dateSent DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, userId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Long messageId = rs.getLong("id");
                int senderId = rs.getInt("senderId");
                int receiverId = rs.getInt("receiverId");
                String content = rs.getString("messageText");
                LocalDateTime sentDate = rs.getTimestamp("dateSent").toLocalDateTime();
                boolean isRead = rs.getBoolean("isRead");

                messages.add(new Message(messageId, senderId, receiverId, content, sentDate, isRead));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return messages;
    }

    public List<Message> getUnreadMessagesForUser(int userId) {
        List<Message> messages = new ArrayList<>();
        String SQL = "SELECT * FROM Messages WHERE receiverId = ? AND isRead = false ORDER BY dateSent DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, userId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Long messageId = rs.getLong("id");
                int senderId = rs.getInt("senderId");
                int receiverId = rs.getInt("receiverId");
                String content = rs.getString("messageText");
                LocalDateTime sentDate = rs.getTimestamp("dateSent").toLocalDateTime();
                boolean isRead = rs.getBoolean("isRead");

                messages.add(new Message(messageId, senderId, receiverId, content, sentDate, isRead));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return messages;
    }

    public List<Message> getMessagesBetweenUsers(int user1Id, int user2Id) {
        List<Message> messages = new ArrayList<>();
        String SQL = "SELECT * FROM Messages WHERE (senderId = ? AND receiverId = ?) OR (senderId = ? AND receiverId = ?) ORDER BY dateSent DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, user1Id);
            pstmt.setLong(2, user2Id);
            pstmt.setLong(3, user2Id);
            pstmt.setLong(4, user1Id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Long messageId = rs.getLong("id");
                int senderId = rs.getInt("senderId");
                int receiverId = rs.getInt("receiverId");
                String content = rs.getString("messageText");
                LocalDateTime sentDate = rs.getTimestamp("dateSent").toLocalDateTime();
                boolean isRead = rs.getBoolean("isRead");

                messages.add(new Message(messageId, senderId, receiverId, content, sentDate, isRead));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return messages;
    }

    public boolean deleteMessage(Long messageId) {
        String SQL = "DELETE FROM Messages WHERE id = ?";
        boolean deleted = false;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, messageId);

            deleted = pstmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return deleted;
    }
}

