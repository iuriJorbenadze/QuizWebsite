package DaoTest;

import model.User;
import Dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;


    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        // Reset the test database or use a test-specific configuration if necessary
    }

    @Test
    void testAddUser() {
        User newUser = new User("testUsername123", "testPasswordHash", "test123@email.com", Timestamp.valueOf(LocalDateTime.now()),false);
        assertTrue(userDAO.createUser(newUser));
    }

    @Test
    void testGetUserById() {
        User newUser = new User("testUsername324", "testPasswordHash", "test324@email.com", Timestamp.valueOf(LocalDateTime.now()),false);
        userDAO.createUser(newUser);

        User retrievedUser = userDAO.getUserByUsername(newUser.getUsername());
        assertEquals(newUser.getUsername(), retrievedUser.getUsername());
    }

    @Test
    void testUpdateUser() {
        User newUser = new User("testUsername45", "testPasswordHash", "test45@email.com", Timestamp.valueOf(LocalDateTime.now()),false);
        userDAO.createUser(newUser);

        newUser.setUsername("updatedUsername");
        assertTrue(userDAO.updateUser(newUser));

        User updatedUser = userDAO.getUserById(newUser.getUserId());
        assertEquals("updatedUsername", updatedUser.getUsername());
    }

    @Test
    void testDeleteUser() {
        User newUser = new User("testUsername65", "testPasswordHash", "test65@email.com", Timestamp.valueOf(LocalDateTime.now()),false);
        userDAO.createUser(newUser);
        assertTrue(userDAO.deleteUser(newUser.getUserId()));

        assertNull(userDAO.getUserById(newUser.getUserId()));
    }
}
