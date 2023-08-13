package ServiceTest;

import Dao.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


//drop schema before running those tests //IMPORTANT
public class UserServiceTest {

    private UserService userService;
    private UserDAO userDAO;
    private User testUser;

    @BeforeEach
    public void setup() {
        userDAO = new UserDAO();  // Initialize connection to database
        userService = new UserService(userDAO);

        // Creating a test user
        testUser = new User("testUsername", "testPasswordHash", "test11111@email.com", false);
        assertTrue(userService.registerUser(testUser));
    }

    @AfterEach
    public void tearDown() {
        if (testUser != null) {
            userService.deleteUser(testUser.getUserId());
        }
    }

    @Test
    public void testGetUserById() {
        User user = userService.getUserById(testUser.getUserId());
        assertNotNull(user);
        assertEquals(testUser.getUserId(), user.getUserId());
    }

    @Test
    public void testGetUserByUsername() {
        User user = userService.getUserByUsername(testUser.getUsername());
        assertNotNull(user);
        assertEquals(testUser.getUsername(), user.getUsername());
    }

    @Test
    public void testRegisterUserExistingUsername() {
        User newUser = new User("testUsername", "newPasswordHash", "new@email.com", false);
        assertFalse(userService.registerUser(newUser));
    }

    @Test
    public void testRegisterUserNewUsername() {
        User newUser = new User("newUsername", "newPasswordHash", "new@email.com", false);
        assertTrue(userService.registerUser(newUser));
        userService.deleteUser(newUser.getUserId());  // Clean up the user created in this test
    }

    @Test
    public void testUpdateNonExistentUser() {
        User userToUpdate = new User(100, "updatedUsername", "updatedPasswordHash", "updated@email.com", LocalDateTime.now(), false);  // ID that doesn't exist
        assertFalse(userService.updateUser(userToUpdate));
    }

    @Test
    public void testUpdateExistingUser() {
        testUser.setUsername("testtingNewUsername");
        assertTrue(userService.updateUser(testUser));
    }

    @Test
    public void testDeleteUser() {
        assertTrue(userService.deleteUser(testUser.getUserId()));
        testUser = null;  // So that the tearDown doesn't try to delete again
    }

    @Test
    public void testIsUsernameAvailable() {
        assertFalse(userService.isUsernameAvailable(testUser.getUsername()));
        assertTrue(userService.isUsernameAvailable("unregisteredUsername"));
    }

    @Test
    public void testValidateUserCredentials() {
        assertTrue(userService.validateUserCredentials(testUser.getUsername(), testUser.getPasswordHash())); // as of now since password hashing is not implemented yet
        assertFalse(userService.validateUserCredentials(testUser.getUsername(), "wrongPasswordHash"));
    }

    @Test
    public void testIsAdmin() {
        assertFalse(userService.isAdmin(testUser.getUserId()));  // we registered testUser as a non-admin

        // Now promoting the user to admin and testing again
        assertTrue(userService.promoteToAdmin(testUser.getUserId()));
        assertTrue(userService.isAdmin(testUser.getUserId()));
    }

    @Test
    public void testDemoteFromAdminForNonAdminUser() {
        assertFalse(userService.demoteFromAdmin(testUser.getUserId()));  // testUser is not an admin, so demotion should return false
    }

    @Test
    public void testDemoteFromAdminForAdminUser() {
        userService.promoteToAdmin(testUser.getUserId());
        assertTrue(userService.isAdmin(testUser.getUserId()));  // Confirming promotion to admin

        assertTrue(userService.demoteFromAdmin(testUser.getUserId()));  // Now, demotion should return true
        assertFalse(userService.isAdmin(testUser.getUserId()));  // Confirming demotion from admin
    }

    @Test
    public void testGetAllRegularUsers() {
        User adminUser = new User("adminUsername", "adminPasswordHash", "admin@email.com", true);
        assertTrue(userService.registerUser(adminUser));

        List<User> regularUsers = userService.getAllRegularUsers();
        assertTrue(regularUsers.stream().noneMatch(User::isAdmin));  // No user in the list should be an admin

        userService.deleteUser(adminUser.getUserId());  // Clean up the admin user created in this test
    }

    @Test
    public void testGetUserByCredentialsValid() {
        User fetchedUser = userService.getUserByCredentials(testUser.getUsername(), testUser.getPasswordHash());
        assertNotNull(fetchedUser);
        assertEquals(testUser.getUsername(), fetchedUser.getUsername());
    }

    @Test
    public void testGetUserByCredentialsInvalidPassword() {
        User fetchedUser = userService.getUserByCredentials(testUser.getUsername(), "wrongPassword");
        assertNull(fetchedUser);  // The user should not be fetched with a wrong password
    }

    @Test
    public void testGetUserByCredentialsInvalidUsername() {
        User fetchedUser = userService.getUserByCredentials("wrongUsername", testUser.getPasswordHash());
        assertNull(fetchedUser);  // The user should not be fetched with a wrong username
    }



    @Test
    public void testPromoteToAdminForRegularUser() {
        assertFalse(userService.isAdmin(testUser.getUserId())); // Confirming initial state
        assertTrue(userService.promoteToAdmin(testUser.getUserId())); // Promotion should return true
        assertTrue(userService.isAdmin(testUser.getUserId())); // Confirming promotion
    }

    @Test
    public void testPromoteToAdminForAdminUser() {
        userService.promoteToAdmin(testUser.getUserId());
        assertTrue(userService.isAdmin(testUser.getUserId())); // Confirming promotion

        assertFalse(userService.promoteToAdmin(testUser.getUserId())); // Trying to promote again should return false
    }

    @Test
    public void testGetAllAdmins() {
        User adminUser1 = new User("adminUsername1", "adminPasswordHash1", "admin1@email.com", true);
        User adminUser2 = new User("adminUsername2", "adminPasswordHash2", "admin2@email.com", true);
        assertTrue(userService.registerUser(adminUser1));
        assertTrue(userService.registerUser(adminUser2));

        List<User> adminUsers = userService.getAllAdmins();
        assertTrue(adminUsers.size() >= 2);  // At least two admin users should be found
        assertTrue(adminUsers.stream().allMatch(User::isAdmin));  // All users in the list should be admins

        userService.deleteUser(adminUser1.getUserId());  // Cleanup
        userService.deleteUser(adminUser2.getUserId());  // Cleanup
    }

    @Test
    public void testDeleteNonExistentUser() {
        assertFalse(userService.deleteUser(99999));  // Assuming 99999 is a non-existent ID, deletion should return false
    }

    @Test
    public void testUpdateUserPassword() {
        String newPasswordHash = "newPasswordHash";
        testUser.setPasswordHash(newPasswordHash); // Setting a new password hash
        assertTrue(userService.updateUser(testUser));

        User updatedUser = userService.getUserByUsername(testUser.getUsername());
        assertEquals(newPasswordHash, updatedUser.getPasswordHash());
    }
}
