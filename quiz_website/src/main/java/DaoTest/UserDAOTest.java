package DaoTest;

import model.User;
import Dao.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;
    private Random random;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        random = new Random();
        // Reset the test database or use a test-specific configuration if necessary
    }

    @AfterEach
    void tearDown() {
        // we can delete test users here or reset the test database to its initial state.
    }

    @Test
    void testAddUser() {
        User newUser = createRandomTestUser();
        assertTrue(userDAO.createUser(newUser));
    }

    @Test
    void testGetUserById() {
        User newUser = createRandomTestUser();
        userDAO.createUser(newUser);

        User retrievedUser = userDAO.getUserByUsername(newUser.getUsername());
        assertEquals(newUser.getUsername(), retrievedUser.getUsername());
    }

    @Test
    void testUpdateUser() {
        User newUser = createRandomTestUser();
        userDAO.createUser(newUser);

        newUser.setUsername("updatedUsername" + random.nextInt(1000));
        assertTrue(userDAO.updateUser(newUser));

        User updatedUser = userDAO.getUserById(newUser.getUserId());
        assertEquals(newUser.getUsername(), updatedUser.getUsername());
    }

    @Test
    void testDeleteUser() {
        User newUser = createRandomTestUser();
        userDAO.createUser(newUser);
        assertTrue(userDAO.deleteUser(newUser.getUserId()));

        assertNull(userDAO.getUserById(newUser.getUserId()));
    }

    private User createRandomTestUser() {
        String randomSuffix = String.valueOf(random.nextInt(10000));
        return new User(null, "testUsername" + randomSuffix, "testPasswordHash", "test" + randomSuffix + "@email.com", LocalDateTime.now(), false);
    }
}
