package service;

import Dao.UserDAO;
import model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Fetch a user by their ID
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    // Fetch a user by their username
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    // Fetch all users
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Create a new user
    public boolean registerUser(User user) {
        // Check if the username already exists
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            // Username already exists, return false or throw an exception
            return false;
        }
        return userDAO.createUser(user);
    }

    // Update user details
    public boolean updateUser(User user) {
        // Ensure the user exists before updating
        User existingUser = userDAO.getUserById(user.getUserId());
        if (existingUser == null) {
            // User doesn't exist, return false or throw an exception
            return false;
        }
        return userDAO.updateUser(user);
    }

    // Delete a user by their ID
    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }

    // Fetch all admin users
    public List<User> getAllAdmins() {
        return userDAO.getAllAdmins();
    }

    // Check if a username is available
    public boolean isUsernameAvailable(String username) {
        User existingUser = userDAO.getUserByUsername(username);
        return existingUser == null;
    }

    // Validate user credentials for login
    public boolean validateUserCredentials(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPasswordHash().equals(password)) { //TODO PASSWORD HASHS ARE NOT IMPLEMENTED YET
            return true;
        }
        return false;
    }



    // Check if a user is an admin
    public boolean isAdmin(int userId) {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            return user.isAdmin();  // Assuming the User class has an isAdmin() method
        }
        return false;
    }

    // Promote a user to admin status
    public boolean promoteToAdmin(int userId) {
        User user = userDAO.getUserById(userId);
        if (user != null && !user.isAdmin()) {
            user.setAdmin(true);  // Assuming the User class has a setAdmin() method
            return userDAO.updateUser(user);
        }
        return false;
    }

    // Demote an admin to regular user status
    public boolean demoteFromAdmin(int userId) {
        User user = userDAO.getUserById(userId);
        if (user != null && user.isAdmin()) {
            user.setAdmin(false);  // Assuming the User class has a setAdmin() method
            return userDAO.updateUser(user);
        }
        return false;
    }

    // Get a list of all non-admin users
    public List<User> getAllRegularUsers() {
        List<User> allUsers = userDAO.getAllUsers();
        return allUsers.stream()
                .filter(user -> !user.isAdmin())
                .collect(Collectors.toList());
    }

    // Validate and return a user by credentials
    public User getUserByCredentials(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPasswordHash().equals(password)) { //TODO PASSWORD HASH
            return user;
        }
        return null;
    }
}