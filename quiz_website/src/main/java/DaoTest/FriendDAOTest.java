package DaoTest;

import static org.junit.jupiter.api.Assertions.*;

import Dao.FriendDAO;
import model.Friend;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class FriendDAOTest {

    private FriendDAO friendDAO;

    @BeforeEach
    public void setup() {
        friendDAO = new FriendDAO();
    }

    @Test
    public void testCreateAndGetFriend() {
        Friend friend = new Friend(0, 1, 2, Friend.Status.PENDING, LocalDateTime.now(),LocalDateTime.now());

        int createdId = friendDAO.createFriendship(friend);
        assertNotEquals(0, createdId);

        Friend retrievedFriend = friendDAO.getFriendshipById(createdId);
        assertNotNull(retrievedFriend);
        assertEquals(1, retrievedFriend.getUser1Id());
        assertEquals(2, retrievedFriend.getUser2Id());
        assertEquals(Friend.Status.PENDING, retrievedFriend.getStatus());
    }

    @Test
    public void testAcceptFriendRequest() {
        Friend friend = new Friend(0, 2, 4, Friend.Status.PENDING, LocalDateTime.now(),LocalDateTime.now());
        int createdId = friendDAO.createFriendship(friend);

        assertTrue(friendDAO.acceptFriendRequest(createdId));
        Friend updatedFriend = friendDAO.getFriendshipById(createdId);
        assertEquals(Friend.Status.ACCEPTED, updatedFriend.getStatus());
    }

    @Test
    public void testDeclineFriendRequest() {
        Friend friend = new Friend(0, 1, 4, Friend.Status.PENDING, LocalDateTime.now(),LocalDateTime.now());
        int createdId = friendDAO.createFriendship(friend);

        assertTrue(friendDAO.declineFriendRequest(createdId));
        Friend retrievedFriend = friendDAO.getFriendshipById(createdId);
        assertNull(retrievedFriend);
    }

    @Test
    public void testGetAllPendingRequests() {
        Friend friend1 = new Friend(0, 1, 4, Friend.Status.PENDING, LocalDateTime.now(),LocalDateTime.now());
        Friend friend2 = new Friend(0, 4, 1, Friend.Status.PENDING, LocalDateTime.now(),LocalDateTime.now());

        friendDAO.createFriendship(friend1);
        friendDAO.createFriendship(friend2);

        List<Friend> pendingRequests = friendDAO.getPendingRequestsForUser(4);
        assertTrue(pendingRequests.size() >= 2);
    }

    @Test
    public void testGetAllFriends() {
        // TODO need to check this test
        Friend friend1 = new Friend(0, 1, 4, Friend.Status.ACCEPTED,LocalDateTime.now(),LocalDateTime.now());
        Friend friend2 = new Friend(0, 4, 1, Friend.Status.ACCEPTED,LocalDateTime.now(),LocalDateTime.now());

        friendDAO.createFriendship(friend1);
        friendDAO.createFriendship(friend2);

        List<Friend> friends = friendDAO.getAllFriendsForUser(4);
        assertTrue(friends.size() >= 2);
    }
    //TODO add more tests
}
