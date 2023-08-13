package Dao;

import model.Friend;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendDAO extends AbstractDAO {


    //returns first id of created friendship connections (2 are created firsts id is returned)
    public int createFriendship(Friend friend) {
        if (doesFriendshipExists(friend.getUser1Id(), friend.getUser2Id())) {
            System.out.println("Friendship already exists");
            return -1;  // Indicating that the friendship already exists
        }

        String insertSQL = "INSERT INTO Friends (user1Id, user2Id, status, createdDate, acceptedDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            // Insert (user1, user2)
            ps.setInt(1, friend.getUser1Id());
            ps.setInt(2, friend.getUser2Id());
            ps.setString(3, friend.getStatus().toString());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(friend.getCreatedDate()));

            //IMPORTANT
            if (friend.getStatus() == Friend.Status.PENDING){
                //null should be saved to db (unless overriden)
                ps.setTimestamp(5, null);
            }else {
                ps.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            }




            int affectedRows = ps.executeUpdate();


            //IMPORTANT
            //IGNORING SECOND ID
            int friendshipId;
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    friend.setId(generatedKeys.getInt(1));
                    friendshipId = friend.getId();
                } else {
                    throw new SQLException("Creating friend failed, no ID obtained.");
                }
            }


            // Insert (user2, user1)
            ps.setInt(1, friend.getUser2Id());
            ps.setInt(2, friend.getUser1Id());
            ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating friend failed, no rows affected.");
            }

            //IMPORTANT
            return friendshipId;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public int  sendFriendRequest(int user1Id, int user2Id) {
        Friend friendRequest = new Friend(0, user1Id, user2Id, Friend.Status.PENDING);
        return createFriendship(friendRequest);
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

    public Friend getFriendshipByUserIds(int user1Id, int user2Id) {
        String selectSQL = "SELECT * FROM Friends WHERE (user1Id=? AND user2Id=?) OR (user1Id=? AND user2Id=?) LIMIT 1";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL)) {

            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, user2Id);
            ps.setInt(4, user1Id);

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
        if (getFriendshipByUserIds(friend.getUser1Id(), friend.getUser2Id()) == null) {
            System.out.println("Friendship does not exist, it is null");
            return false;  // Indicate that the friendship doesn't exist
        }

        String updateSQL = "UPDATE Friends SET status=?, acceptedDate=? WHERE (user1Id=? AND user2Id=?) OR (user1Id=? AND user2Id=?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(updateSQL)) {

            ps.setString(1, friend.getStatus().toString());

            //ps.setTimestamp(2, java.sql.Timestamp.valueOf(friend.getAcceptedDate()));
            if (friend.getStatus() == Friend.Status.ACCEPTED) {
                ps.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            } else {
                ps.setTimestamp(2, null);
            }

            ps.setInt(3, friend.getUser1Id());
            ps.setInt(4, friend.getUser2Id());
            ps.setInt(5, friend.getUser2Id());
            ps.setInt(6, friend.getUser1Id());

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

        //IMPORTANT
        Timestamp timestamp = rs.getTimestamp("acceptedDate");
        LocalDateTime acceptedDate = (timestamp != null) ? timestamp.toLocalDateTime() : null;


        Friend friend = new Friend(id, user1Id, user2Id, status);
        friend.setCreatedDate(createdDate);
        friend.setAcceptedDate(acceptedDate);
        return friend;
    }


    public boolean deleteFriendship(int user1Id, int user2Id) {
        Friend existingFriendship = getFriendshipByUserIds(user1Id, user2Id);
        if (existingFriendship == null){
            System.out.println("There is no friendship, so can't delete");
            return false;
        }


        String deleteSQL = "DELETE FROM Friends WHERE (user1Id=? AND user2Id=?) OR (user1Id=? AND user2Id=?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteSQL)) {

            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, user2Id);
            ps.setInt(4, user1Id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean clearFriendshipsForUser(int userId) {
        String deleteSQL = "DELETE FROM Friends WHERE user1Id=? OR user2Id=?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteSQL)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // will need to check only 1 column
    //TODO once user logs in this will be executed and we will notify user that someone want to add hom as friend
    public List<Friend> getPendingRequestsForUser(int userId) {
        String selectSQL = "SELECT * FROM Friends WHERE user1Id=? AND status='PENDING'";
        List<Friend> friendRequests = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL)) {

            ps.setInt(1, userId);
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


    // will need to check only 1 column
    public List<Friend> getAllFriendsForUser(int userId) {
        String selectSQL = "SELECT * FROM Friends WHERE user1Id=? AND status='ACCEPTED'";
        List<Friend> friends = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL)) {

            ps.setInt(1, userId);
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


    public boolean acceptFriendRequest(int user1Id, int user2Id) {

        Friend existingFriendship = getFriendshipByUserIds(user1Id, user2Id);
        if (existingFriendship == null){
            System.out.println("There is no friendship, so can't decline null");
            return false;
        }
        if (existingFriendship.getStatus() != Friend.Status.PENDING) {
            System.out.println("There is no pending status, so can't accept");
            return false;  // Indicate that there's no pending request to accept
        }

        String updateSQL = "UPDATE Friends SET status='ACCEPTED', acceptedDate=? WHERE (user1Id=? AND user2Id=?) OR (user1Id=? AND user2Id=?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(updateSQL)) {

            ps.setTimestamp(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, user1Id);
            ps.setInt(3, user2Id);
            ps.setInt(4, user2Id);
            ps.setInt(5, user1Id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean declineFriendRequest(int user1Id, int user2Id) {
        Friend existingFriendship = getFriendshipByUserIds(user1Id, user2Id);
        if (existingFriendship == null){
            System.out.println("There is no friendship, so can't decline null");
            return false;
        }
        if (existingFriendship.getStatus() != Friend.Status.PENDING) {
            System.out.println("There is no pending status, so can't decline");
            return false;  // Indicate that there's no pending request to decline
        }

        return deleteFriendship(user1Id, user2Id);  // We already implemented deleteFriendship earlier
    }

    private boolean doesFriendshipExists(int user1Id, int user2Id) {
        String selectSQL = "SELECT * FROM Friends WHERE (user1Id=? AND user2Id=?) OR (user1Id=? AND user2Id=?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL)) {

            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, user2Id);
            ps.setInt(4, user1Id);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clearFriendsTable() {
        String deleteAllSQL = "DELETE FROM Friends";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteAllSQL)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getSuggestedFriendIds(int userId) {
        // This is a basic suggestion based on mutual friends.
        // You can enhance it by using more sophisticated algorithms.
        String selectSQL = "SELECT DISTINCT f2.user2Id FROM Friends f1 JOIN Friends f2 ON f1.user2Id = f2.user1Id WHERE f1.user1Id = ? AND f2.user2Id <> ?";
        List<Integer> suggestedIds = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                suggestedIds.add(rs.getInt("user2Id"));
            }

            return suggestedIds;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
