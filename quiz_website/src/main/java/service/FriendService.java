package service;


import Dao.UserDAO;
import model.Friend;
import Dao.FriendDAO;
import model.User;

import java.time.LocalDateTime;
import java.util.List;

public class FriendService {

    private final FriendDAO friendDAO;
    private final UserDAO userDAO;

    public FriendService() {
        this.friendDAO = new FriendDAO();
        this.userDAO = new UserDAO();
    }

    public int sendFriendRequest(int senderId, int receiverId) {
        if (senderId == receiverId) {
            System.out.println("User cannot send friend request to themselves");
            return -1;
        }
        Friend friend = new Friend(0, senderId, receiverId, Friend.Status.PENDING);
        return friendDAO.createFriendship(friend);
    }

    public boolean acceptFriendRequest(int senderId, int receiverId) {
        return friendDAO.acceptFriendRequest(senderId, receiverId);
    }

    public boolean declineFriendRequest(int senderId, int receiverId) {
        return friendDAO.declineFriendRequest(senderId, receiverId);
    }

    public List<Friend> getPendingFriendRequests(int userId) {
        return friendDAO.getPendingRequestsForUser(userId);
    }

    public List<Friend> getAllFriends(int userId) {
        return friendDAO.getAllFriendsForUser(userId);
    }

    public boolean removeFriend(int user1Id, int user2Id) {
        return friendDAO.deleteFriendship(user1Id, user2Id);
    }

    public Friend getFriendshipDetails(int user1Id, int user2Id) {
        return friendDAO.getFriendshipByUserIds(user1Id, user2Id);
    }

    public void clearAllFriendships() {
        friendDAO.clearFriendsTable();
    }

    public boolean areTheyFriends(int user1Id, int user2Id) {
        Friend friendship = friendDAO.getFriendshipByUserIds(user1Id, user2Id);
        return friendship != null && friendship.getStatus() == Friend.Status.ACCEPTED;
    }

    public int countFriends(int userId) {
        return friendDAO.getAllFriendsForUser(userId).size();
    }

    public List<User> getSuggestedFriends(int userId) {
        // This method assumes you have a method in UserDAO to get users by a list of ids.
        List<Integer> suggestedFriendIds = friendDAO.getSuggestedFriendIds(userId);
        return userDAO.getUsersByIds(suggestedFriendIds);
    }
}
