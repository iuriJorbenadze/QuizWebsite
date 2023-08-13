package ServiceTest;

import static org.junit.jupiter.api.Assertions.*;

import Dao.FriendDAO;
import Dao.UserDAO;
import model.User;
import model.Friend;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FriendService;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

public class FriendServiceTest {

    private FriendService friendService;
    private UserService userService;

    private UserDAO userDAO;
    private FriendDAO friendDAO; // Assuming there's a DAO for friendships.

    private List<User> usersToDelete = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        friendService = new FriendService();
        userDAO = new UserDAO();
        friendDAO = new FriendDAO(); // Assuming you've defined this.
        userService = new UserService(userDAO);
    }

    @AfterEach
    public void tearDown() {
        for (User user : usersToDelete) {
            friendDAO.clearFriendshipsForUser(user.getUserId()); // Assuming this method is defined in FriendDAO.
            userDAO.deleteUser(user.getUserId());
        }
        usersToDelete.clear();
    }

    @Test
    public void testSendAndAcceptFriendRequest() {
        User user1 = createUniqueUser();
        User user2 = createUniqueUser();
        int friendshipId = friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());
        assertTrue(friendshipId > 0, "Failed to send friend request");
        boolean requestAccepted = friendService.acceptFriendRequest(user2.getUserId(), user1.getUserId());
        assertTrue(requestAccepted, "Failed to accept friend request");

        Friend friendship = friendService.getFriendshipDetails(user1.getUserId(), user2.getUserId());
        assertEquals(Friend.Status.ACCEPTED, friendship.getStatus(), "Friendship status mismatch");
    }

    @Test
    public void testGetPendingFriendRequests() {
        User user1 = createUniqueUser();
        User user2 = createUniqueUser();
        friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());

        List<Friend> pendingRequests = friendService.getPendingFriendRequests(user2.getUserId());
        assertEquals(1, pendingRequests.size(), "Pending requests count mismatch");
    }

    @Test
    public void testGetAllFriends() {
        User user1 = createUniqueUser();
        User user2 = createUniqueUser();
        User user3 = createUniqueUser();
        friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());
        friendService.acceptFriendRequest(user2.getUserId(), user1.getUserId());
        friendService.sendFriendRequest(user1.getUserId(), user3.getUserId());
        friendService.acceptFriendRequest(user3.getUserId(), user1.getUserId());

        List<Friend> friends = friendService.getAllFriends(user1.getUserId());
        assertEquals(2, friends.size(), "Friends count mismatch");
    }

    // Additional tests as required...

    private User createUniqueUser() {
        String uniqueValue = Long.toString(System.currentTimeMillis());
        User newUser = new User("testUsername" + uniqueValue, "testHashedPassword" + uniqueValue, "testEmail" + uniqueValue + "@example.com", false);
        boolean isRegistered = userService.registerUser(newUser);
        if (!isRegistered || newUser.getUserId() == 0) {
            throw new IllegalStateException("User creation failed");
        }
        usersToDelete.add(newUser);
        return newUser;
    }

    @Test
    public void testSendFriendRequestToSelf() {
        User user1 = createUniqueUser();
        int friendshipId = friendService.sendFriendRequest(user1.getUserId(), user1.getUserId());
        assertEquals(-1, friendshipId, "User should not be able to send friend request to themselves");
    }

    @Test
    public void testDeclineFriendRequest() {
        User user1 = createUniqueUser();
        User user2 = createUniqueUser();
        friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());
        boolean requestDeclined = friendService.declineFriendRequest(user2.getUserId(), user1.getUserId());
        assertTrue(requestDeclined, "Failed to decline friend request");

        Friend friendship1 = friendService.getFriendshipDetails(user1.getUserId(), user2.getUserId());
        Friend friendship2 = friendService.getFriendshipDetails(user2.getUserId(), user1.getUserId());
        assertNull(friendship1, "Friendship should be null after decline for user1 -> user2");
        assertNull(friendship2, "Friendship should be null after decline for user2 -> user1");
    }

    @Test
    public void testRemoveFriend() {
        User user1 = createUniqueUser();
        User user2 = createUniqueUser();
        friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());
        friendService.acceptFriendRequest(user2.getUserId(), user1.getUserId());
        boolean isRemoved = friendService.removeFriend(user1.getUserId(), user2.getUserId());
        assertTrue(isRemoved, "Failed to remove friend");

        Friend friendship = friendService.getFriendshipDetails(user1.getUserId(), user2.getUserId());
        assertNull(friendship, "Friendship should be null after removal");
    }

    @Test
    public void testMultipleFriendRequests() {
        User user1 = createUniqueUser();
        User user2 = createUniqueUser();
        int friendshipId1 = friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());
        int friendshipId2 = friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());

        assertTrue(friendshipId1 > 0, "Failed to send first friend request");
        assertEquals(-1, friendshipId2, "Should not be able to send another friend request while one is still pending");
    }

    @Test
    public void testClearAllFriendships() {
        User user1 = createUniqueUser();
        User user2 = createUniqueUser();
        User user3 = createUniqueUser();
        friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());
        friendService.acceptFriendRequest(user2.getUserId(), user1.getUserId());
        friendService.sendFriendRequest(user1.getUserId(), user3.getUserId());
        friendService.acceptFriendRequest(user3.getUserId(), user1.getUserId());

        friendService.clearAllFriendships();
        List<Friend> friendsUser1 = friendService.getAllFriends(user1.getUserId());
        assertEquals(0, friendsUser1.size(), "All friendships should be cleared");
    }

    @Test
    public void testAreTheyFriends() {
        User user1 = createUniqueUser();
        User user2 = createUniqueUser();

        // At the beginning, they shouldn't be friends.
        assertFalse(friendService.areTheyFriends(user1.getUserId(), user2.getUserId()));

        // Making them friends.
        friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());
        friendService.acceptFriendRequest(user2.getUserId(), user1.getUserId());

        // Now, they should be friends.
        assertTrue(friendService.areTheyFriends(user1.getUserId(), user2.getUserId()));
    }

    @Test
    public void testCountFriends() {
        User user1 = createUniqueUser();
        User user2 = createUniqueUser();
        User user3 = createUniqueUser();

        // Initially, user1 should have 0 friends.
        assertEquals(0, friendService.countFriends(user1.getUserId()));

        // Making user1 and user2 friends.
        friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());
        friendService.acceptFriendRequest(user2.getUserId(), user1.getUserId());

        // Now, user1 should have 1 friend.
        assertEquals(1, friendService.countFriends(user1.getUserId()));

        // Making user1 and user3 friends.
        friendService.sendFriendRequest(user1.getUserId(), user3.getUserId());
        friendService.acceptFriendRequest(user3.getUserId(), user1.getUserId());

        // Now, user1 should have 2 friends.
        assertEquals(2, friendService.countFriends(user1.getUserId()));
    }

    @Test
    public void testGetSuggestedFriends() {
        // Create a set of users
        User user1 = createUniqueUser();
        User user2 = createUniqueUser();
        User user3 = createUniqueUser();
        User user4 = createUniqueUser();

        // Creating friendships
        friendService.sendFriendRequest(user1.getUserId(), user2.getUserId());
        friendService.acceptFriendRequest(user2.getUserId(), user1.getUserId());
        friendService.sendFriendRequest(user2.getUserId(), user3.getUserId());
        friendService.acceptFriendRequest(user3.getUserId(), user2.getUserId());

        // When querying for suggestions for user1, assuming the suggestion method suggests friends of friends, user3 should be among the suggestions since user2 is a common friend.
        List<User> suggestedUsers = friendService.getSuggestedFriends(user1.getUserId());

        assertNotNull(suggestedUsers);

        // Check if user3 is among the suggestions.
        boolean containsUser3 = false;
        for (User u : suggestedUsers) {
            if (u.getUserId() == user3.getUserId()) {
                containsUser3 = true;
                break;
            }
        }

        assertTrue(containsUser3);
    }
}
