//package DaoTest;

import static org.junit.jupiter.api.Assertions.*;

import Dao.FriendDAO;
import Dao.UserDAO;
import model.Friend;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FriendDAOTest {

    private FriendDAO friendDAO;
    private UserDAO userDAO;
    private User testUser1;
    private User testUser2;


    //TODO in business logic we should make sure when deleting user, delete everything that is connected to it, like objects in friend database and so on
    @BeforeEach
    public void setup() {
        friendDAO = new FriendDAO();
        userDAO = new UserDAO();

        String username1 = "TestUser_" + UUID.randomUUID().toString();
        String username2 = "TestUser_" + UUID.randomUUID().toString();

        testUser1 = new User( username1, "password", username1 + "@gmail.com",  false);
        testUser2 = new User( username2, "password", username2 + "@gmail.com",  false);

        userDAO.createUser(testUser1);
        userDAO.createUser(testUser2);
    }

//    @AfterEach
//    public void tearDown() {
//        // Delete associated friend records first
//        friendDAO.clearFriendsTable();
//
//        // Then delete the users
//        userDAO.deleteUser(testUser1.getUserId());
//        userDAO.deleteUser(testUser2.getUserId());
//    }
    @Test
    public void testCreateAndGetFriend() {
        Friend friend = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.PENDING);

        int createdId = friendDAO.createFriendship(friend);
        assertNotEquals(0, createdId);
        assertEquals(createdId, friend.getId());

        Friend retrievedFriend = friendDAO.getFriendshipById(createdId);
        assertNotNull(retrievedFriend);
        assertEquals(testUser1.getUserId(), retrievedFriend.getUser1Id());
        assertEquals(testUser2.getUserId(), retrievedFriend.getUser2Id());
        assertEquals(Friend.Status.PENDING, retrievedFriend.getStatus());
    }

    @Test
    public void testAcceptFriendRequest() {
        Friend friend = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.PENDING);
        int createdId = friendDAO.createFriendship(friend);

        assertTrue(friendDAO.acceptFriendRequest(testUser1.getUserId(), testUser2.getUserId()));

        Friend updatedFriend = friendDAO.getFriendshipById(createdId);
        assertEquals(Friend.Status.ACCEPTED, updatedFriend.getStatus());
    }

    @Test
    public void testDeclineFriendRequest() {
        Friend friend = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.PENDING);
        int createdId = friendDAO.createFriendship(friend);

        assertTrue(friendDAO.declineFriendRequest(testUser1.getUserId(), testUser2.getUserId()));

        Friend retrievedFriend = friendDAO.getFriendshipById(createdId);
        assertNull(retrievedFriend);
    }

    @Test
    public void testGetAllPendingRequests() {
        Friend friend1 = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.PENDING);

        int pendingRequestsNumBefore = friendDAO.getPendingRequestsForUser(testUser2.getUserId()).size();

        friendDAO.createFriendship(friend1);


        List<Friend> pendingRequests = friendDAO.getPendingRequestsForUser(testUser2.getUserId());
        assertTrue(pendingRequests.size() == pendingRequestsNumBefore + 1);
    }

    @Test
    public void testGetAllFriends() {
        Friend friend1 = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.ACCEPTED);

        int friendsNumBefore = friendDAO.getAllFriendsForUser(testUser2.getUserId()).size();

        friendDAO.createFriendship(friend1);


        List<Friend> friends = friendDAO.getAllFriendsForUser(testUser2.getUserId());
        assertTrue(friendsNumBefore + 1 == friends.size());
    }

    @Test
    public void testDuplicateFriendshipCreation() {
        Friend friend = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.PENDING);
        int createdId = friendDAO.createFriendship(friend);
        assertNotEquals(-1, createdId);  // First creation should succeed

        int duplicateId = friendDAO.createFriendship(friend);
        assertEquals(-1, duplicateId);  // Duplicate creation should fail
    }

    @Test
    public void testUpdateNonExistentFriendship() {
        Friend nonExistentFriend = new Friend(0, 777777, 888888, Friend.Status.PENDING); //  may consider using dynamic users here as well if necessary.
        assertFalse(friendDAO.updateFriendshipStatus(nonExistentFriend));  // Update should fail
    }

    @Test
    public void testAcceptNonExistentFriendRequest() {
        assertFalse(friendDAO.acceptFriendRequest(911111, 101111111));  // Acceptance should fail;
    }

    @Test
    public void testDeclineNonExistentFriendRequest() {
        assertFalse(friendDAO.declineFriendRequest(1111111, 222222222));  // Decline should fail;
    }


    @Test
    public void testGetSuggestedFriendIds() {
        // Create additional users specifically for this test
        User tempUser1 = createUser("TempUser1");
        User tempUser2 = createUser("TempUser2");
        User tempUser3 = createUser("TempUser3");

        // Making testUser1 friends with tempUser1 and tempUser2


        friendDAO.sendFriendRequest(tempUser1.getUserId(), tempUser2.getUserId());
        //for testing purposes in db we can see that create and accept times are different
        int j2 =0;
        for (int i = 0; i < 1900000000; i++) {

            for (int k = 0; k < 1200000000; k++) {
                j2++;

            }
        }
        System.out.println(j2);
        int j11 =0;
        for (int i = 0; i < 1200000000; i++) {

            for (int k = 0; k < 1200000000; k++) {
                j11++;

            }
        }
        System.out.println(j11);

        friendDAO.acceptFriendRequest(tempUser1.getUserId(), tempUser2.getUserId());

        friendDAO.sendFriendRequest(tempUser2.getUserId(), tempUser3.getUserId());
        friendDAO.acceptFriendRequest(tempUser2.getUserId(), tempUser3.getUserId());

        // Get suggested friends for testUser1
        List<Integer> suggestedFriendIds = friendDAO.getSuggestedFriendIds(tempUser1.getUserId());

        assertNotNull(suggestedFriendIds);


        // So, the suggestions should include tempUser3,
        assertTrue(suggestedFriendIds.contains(tempUser3.getUserId()));


    }

    private User createUser(String prefix) {
        String username = prefix + "_" + UUID.randomUUID().toString();
        User user = new User(username, "password", username + "@gmail.com", false);
        userDAO.createUser(user);
        return user;
    }


}


////package DaoTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import Dao.FriendDAO;
//import Dao.UserDAO;
//import model.Friend;
//import model.User;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//public class FriendDAOTest {
//
//    private FriendDAO friendDAO;
//    private UserDAO userDAO;
//    private User testUser1;
//    private User testUser2;
//
//
//    //TODO in business logic we should make sure when deleting user, delete everything that is connected to it, like objects in friend database and so on
//    @BeforeEach
//    public void setup() {
//        friendDAO = new FriendDAO();
//        userDAO = new UserDAO();
//
//        String username1 = "TestUser_" + UUID.randomUUID().toString();
//        String username2 = "TestUser_" + UUID.randomUUID().toString();
//
//        testUser1 = new User( username1, "password", username1 + "@gmail.com",  false);
//        testUser2 = new User( username2, "password", username2 + "@gmail.com",  false);
//
//        userDAO.createUser(testUser1);
//        userDAO.createUser(testUser2);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        // Delete associated friend records first
//        friendDAO.clearFriendsTable();
//
//        // Then delete the users
//        userDAO.deleteUser(testUser1.getUserId());
//        userDAO.deleteUser(testUser2.getUserId());
//    }
//    @Test
//    public void testCreateAndGetFriend() {
//        Friend friend = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.PENDING);
//
//        int createdId = friendDAO.createFriendship(friend);
//        assertNotEquals(0, createdId);
//        assertEquals(createdId, friend.getId());
//
//        Friend retrievedFriend = friendDAO.getFriendshipById(createdId);
//        assertNotNull(retrievedFriend);
//        assertEquals(testUser1.getUserId(), retrievedFriend.getUser1Id());
//        assertEquals(testUser2.getUserId(), retrievedFriend.getUser2Id());
//        assertEquals(Friend.Status.PENDING, retrievedFriend.getStatus());
//    }
//
//    @Test
//    public void testAcceptFriendRequest() {
//        Friend friend = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.PENDING);
//        int createdId = friendDAO.createFriendship(friend);
//
//        assertTrue(friendDAO.acceptFriendRequest(testUser1.getUserId(), testUser2.getUserId()));
//
//        Friend updatedFriend = friendDAO.getFriendshipById(createdId);
//        assertEquals(Friend.Status.ACCEPTED, updatedFriend.getStatus());
//    }
//
//    @Test
//    public void testDeclineFriendRequest() {
//        Friend friend = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.PENDING);
//        int createdId = friendDAO.createFriendship(friend);
//
//        assertTrue(friendDAO.declineFriendRequest(testUser1.getUserId(), testUser2.getUserId()));
//
//        Friend retrievedFriend = friendDAO.getFriendshipById(createdId);
//        assertNull(retrievedFriend);
//    }
//
//    @Test
//    public void testGetAllPendingRequests() {
//        Friend friend1 = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.PENDING);
//        Friend friend2 = new Friend(0, testUser2.getUserId(), testUser1.getUserId(), Friend.Status.PENDING);
//
//        friendDAO.createFriendship(friend1);
//        friendDAO.createFriendship(friend2);
//
//        List<Friend> pendingRequests = friendDAO.getPendingRequestsForUser(testUser2.getUserId());
//        assertTrue(pendingRequests.size() >= 1);
//    }
//
//    @Test
//    public void testGetAllFriends() {
//        Friend friend1 = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.ACCEPTED);
//
//        int friendsNumBefore = friendDAO.getAllFriendsForUser(testUser2.getUserId()).size();
//
//        friendDAO.createFriendship(friend1);
//
//
//        List<Friend> friends = friendDAO.getAllFriendsForUser(testUser2.getUserId());
//        assertTrue(friendsNumBefore + 1 == friends.size());
//    }
//
//    @Test
//    public void testDuplicateFriendshipCreation() {
//        Friend friend = new Friend(0, testUser1.getUserId(), testUser2.getUserId(), Friend.Status.PENDING);
//        int createdId = friendDAO.createFriendship(friend);
//        assertNotEquals(-1, createdId);  // First creation should succeed
//
//        int duplicateId = friendDAO.createFriendship(friend);
//        assertEquals(-1, duplicateId);  // Duplicate creation should fail
//    }
//
//    @Test
//    public void testUpdateNonExistentFriendship() {
//        Friend nonExistentFriend = new Friend(0, 7, 8, Friend.Status.PENDING); //  may consider using dynamic users here as well if necessary.
//        assertFalse(friendDAO.updateFriendshipStatus(nonExistentFriend));  // Update should fail
//    }
//
//    @Test
//    public void testAcceptNonExistentFriendRequest() {
//        assertFalse(friendDAO.acceptFriendRequest(9, 10));  // Acceptance should fail; again, consider using dynamic users.
//    }
//
//    @Test
//    public void testDeclineNonExistentFriendRequest() {
//        assertFalse(friendDAO.declineFriendRequest(11, 12));  // Decline should fail; again, consider using dynamic users.
//    }
//}



//package DaoTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import Dao.FriendDAO;
//import Dao.UserDAO;
//import model.Friend;
//import model.User;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.util.List;
//
//public class FriendDAOTest {
//
//    private FriendDAO friendDAO = new FriendDAO();
//    private UserDAO userDAO = new UserDAO();
//
//    @BeforeEach
//    public void setup() {
////        friendDAO = new FriendDAO();
////        userDAO = new UserDAO();
//        friendDAO.clearFriendsTable();
//
//        // Create users for testing
//        userDAO.createUser(new User(11L, "TestUser11", "password11", "TestUser11@gmail.com", LocalDateTime.now(), false));
//        userDAO.createUser(new User(55L, "TestUser55", "password55", "TestUser11@gmail.com", LocalDateTime.now(), false));
//    }
//
//    @AfterEach
//    public void tearDown() {
//        // Delete test users after tests
//
//        userDAO.deleteUser(11L);
//        userDAO.deleteUser(55L);
//    }
//
//
//    @Test
//    public void testCreateAndGetFriend() {
//        Friend friend = new Friend(0, 1, 2, Friend.Status.PENDING, LocalDateTime.now(),LocalDateTime.now());
//
//        int createdId = friendDAO.createFriendship(friend);
//        assertNotEquals(0, createdId);
//        assertEquals(createdId, friend.getId());
//
//        Friend retrievedFriend = friendDAO.getFriendshipById(createdId);
//        assertNotNull(retrievedFriend);
//        assertEquals(1, retrievedFriend.getUser1Id());
//        assertEquals(2, retrievedFriend.getUser2Id());
//        assertEquals(Friend.Status.PENDING, retrievedFriend.getStatus());
//    }
//
//    @Test
//    public void testAcceptFriendRequest() {
//        Friend friend = new Friend(0, 2, 4, Friend.Status.PENDING, LocalDateTime.now(),LocalDateTime.now());
//        int createdId = friendDAO.createFriendship(friend);
//
//        assertTrue(friendDAO.acceptFriendRequest(friend.getUser1Id(), friend.getUser2Id()));
//        Friend updatedFriend = friendDAO.getFriendshipById(createdId);
//        assertEquals(Friend.Status.ACCEPTED, updatedFriend.getStatus());
//    }
//
//    @Test
//    public void testDeclineFriendRequest() {
//        Friend friend = new Friend(0, 1, 4, Friend.Status.PENDING, LocalDateTime.now(),LocalDateTime.now());
//        int createdId = friendDAO.createFriendship(friend);
//
//        assertTrue(friendDAO.declineFriendRequest(friend.getUser1Id(), friend.getUser2Id()));
//        Friend retrievedFriend = friendDAO.getFriendshipById(createdId);
//        assertNull(retrievedFriend);
//    }
//
//    @Test
//    public void testGetAllPendingRequests() {
//        Friend friend1 = new Friend(0, 1, 4, Friend.Status.PENDING, LocalDateTime.now(),LocalDateTime.now());
//        Friend friend2 = new Friend(0, 4, 1, Friend.Status.PENDING, LocalDateTime.now(),LocalDateTime.now());
//
//        friendDAO.createFriendship(friend1);
//        friendDAO.createFriendship(friend2);
//
//        List<Friend> pendingRequests = friendDAO.getPendingRequestsForUser(4);
//        assertTrue(pendingRequests.size() >= 1);
//    }
//
//    @Test
//    public void testGetAllFriends() {
//        // TODO need to check this test
//        Friend friend1 = new Friend(0, 1, 4, Friend.Status.ACCEPTED,LocalDateTime.now(),LocalDateTime.now());
//        Friend friend2 = new Friend(0, 4, 1, Friend.Status.ACCEPTED,LocalDateTime.now(),LocalDateTime.now());
//
//        friendDAO.createFriendship(friend1);
//        friendDAO.createFriendship(friend2);
//
//        List<Friend> friends = friendDAO.getAllFriendsForUser(4);
//        assertTrue(friends.size() >= 1);
//    }
//    //TODO add more tests
//
//    @Test
//    public void testDuplicateFriendshipCreation() {
//        Friend friend = new Friend(0, 11, 55, Friend.Status.PENDING, LocalDateTime.now(), LocalDateTime.now());
//        int createdId = friendDAO.createFriendship(friend);
//        assertNotEquals(-1, createdId);  // First creation should succeed
//
//        int duplicateId = friendDAO.createFriendship(friend);
//        assertEquals(-1, duplicateId);  // Duplicate creation should fail
//    }
//    @Test
//    public void testUpdateNonExistentFriendship() {
//        Friend nonExistentFriend = new Friend(0, 7, 8, Friend.Status.PENDING);
//        assertFalse(friendDAO.updateFriendshipStatus(nonExistentFriend));  // Update should fail
//    }
//    @Test
//    public void testAcceptNonExistentFriendRequest() {
//        assertFalse(friendDAO.acceptFriendRequest(9, 10));  // Acceptance should fail
//    }
//    @Test
//    public void testDeclineNonExistentFriendRequest() {
//        assertFalse(friendDAO.declineFriendRequest(11, 12));  // Decline should fail
//    }
//
//}
