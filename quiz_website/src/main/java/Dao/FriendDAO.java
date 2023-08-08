package Dao;

import model.Friend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendDAO extends AbstractDAO {

    public int createFriendship(Friend friend) {
        String insertSQL = "INSERT INTO Friends (user1Id, user2Id, status, createdDate,acceptedDate) VALUES (?, ?, ?, ?,?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, friend.getUser1Id());
            ps.setInt(2, friend.getUser2Id());
            ps.setString(3, friend.getStatus().toString());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(friend.getCreatedDate()));
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(friend.getAcceptedDate()));

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating friend failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    friend.setId(generatedKeys.getInt(1));
                    return friend.getId();
                } else {
                    throw new SQLException("Creating friend failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Friend getFriendshipById(int id) {
        String selectSQL = "SELECT * FROM Friends WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapToFriend(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateFriendshipStatus(Friend friend) {
        String updateSQL = "UPDATE Friends SET status=?, acceptedDate=? WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(updateSQL)) {

            ps.setString(1, friend.getStatus().toString());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(friend.getAcceptedDate()));
            ps.setInt(3, friend.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to map a ResultSet to a Friend object
    private Friend mapToFriend(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int user1Id = rs.getInt("user1Id");
        int user2Id = rs.getInt("user2Id");
        Friend.Status status = Friend.Status.valueOf(rs.getString("status"));
        LocalDateTime createdDate = rs.getTimestamp("createdDate").toLocalDateTime();
        LocalDateTime acceptedDate = rs.getTimestamp("acceptedDate").toLocalDateTime();

        Friend friend = new Friend(id, user1Id, user2Id, status);
        friend.setCreatedDate(createdDate);
        friend.setAcceptedDate(acceptedDate);
        return friend;
    }

    public boolean deleteFriendship(int id) {
        String deleteSQL = "DELETE FROM Friends WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteSQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Friend> getPendingRequestsForUser(int userId) {
        String selectSQL = "SELECT * FROM Friends WHERE (user1Id=? OR user2Id=?) AND status='PENDING'";
        List<Friend> friendRequests = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                friendRequests.add(mapToFriend(rs));
            }
            return friendRequests;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Friend> getAllFriendsForUser(int userId) {
        String selectSQL = "SELECT * FROM Friends WHERE (user1Id=? OR user2Id=?) AND status='ACCEPTED'";
        List<Friend> friends = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                friends.add(mapToFriend(rs));
            }
            return friends;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean acceptFriendRequest(int id) {
        String updateSQL = "UPDATE Friends SET status='ACCEPTED' WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(updateSQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean declineFriendRequest(int id) {
        return deleteFriendship(id);  // We already implemented deleteFriendship earlier
    }

}
