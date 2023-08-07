package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseSetup {

    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;

    public DatabaseSetup() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/database.properties"));
            jdbcURL = properties.getProperty("db.url");
            jdbcUsername = properties.getProperty("db.user");
            jdbcPassword = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public void setupDatabase() {
        try (Connection connection = getConnection(); Statement stmt = connection.createStatement()) {
            createUsersTable(stmt);
            createQuizTable(stmt);
            createQuestionsTable(stmt);
            createOptionsTable(stmt);
            createTakenQuizTable(stmt);
            createMessagesTable(stmt);
            createAchievementsTable(stmt);
            createFriendsTable(stmt);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createUsersTable(Statement stmt) throws SQLException {
        String createUsersSQL = "CREATE TABLE IF NOT EXISTS Users(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(255) NOT NULL UNIQUE," +
                "passwordHash VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "dateCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "isAdmin BOOLEAN DEFAULT FALSE" +
                ")";
        stmt.executeUpdate(createUsersSQL);
    }

    private void createQuizTable(Statement stmt) throws SQLException {
        String createQuizSQL = "CREATE TABLE IF NOT EXISTS Quizzes(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "description TEXT," +
                "creatorId INT," +
                "FOREIGN KEY(creatorId) REFERENCES Users(id)" +
                ")";
        stmt.executeUpdate(createQuizSQL);
    }

    private void createQuestionsTable(Statement stmt) throws SQLException {
        String createQuestionsSQL = "CREATE TABLE IF NOT EXISTS Questions(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "quizId INT NOT NULL," +
                "questionText TEXT NOT NULL," +
                "correctOptionId INT," +
                "FOREIGN KEY(quizId) REFERENCES Quizzes(id)" +
                ")";
        stmt.executeUpdate(createQuestionsSQL);
    }

    private void createOptionsTable(Statement stmt) throws SQLException {
        String createOptionsSQL = "CREATE TABLE IF NOT EXISTS Options(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "questionId INT NOT NULL," +
                "optionText TEXT NOT NULL," +
                "FOREIGN KEY(questionId) REFERENCES Questions(id)" +
                ")";
        stmt.executeUpdate(createOptionsSQL);
    }

    private void createTakenQuizTable(Statement stmt) throws SQLException {
        String createTakenQuizSQL = "CREATE TABLE IF NOT EXISTS TakenQuiz(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "userId INT NOT NULL," +
                "quizId INT NOT NULL," +
                "score INT NOT NULL," +
                "dateTaken TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(userId) REFERENCES Users(id)," +
                "FOREIGN KEY(quizId) REFERENCES Quizzes(id)" +
                ")";
        stmt.executeUpdate(createTakenQuizSQL);
    }

    private void createMessagesTable(Statement stmt) throws SQLException {
        String createMessagesSQL = "CREATE TABLE IF NOT EXISTS Messages(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "senderId INT NOT NULL," +
                "receiverId INT NOT NULL," +
                "messageText TEXT NOT NULL," +
                "dateSent TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(senderId) REFERENCES Users(id)," +
                "FOREIGN KEY(receiverId) REFERENCES Users(id)" +
                ")";
        stmt.executeUpdate(createMessagesSQL);
    }

    private void createAchievementsTable(Statement stmt) throws SQLException {
        String createAchievementsSQL = "CREATE TABLE IF NOT EXISTS Achievements(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "userId INT NOT NULL," +
                "achievementName VARCHAR(255) NOT NULL," +
                "dateEarned TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(userId) REFERENCES Users(id)" +
                ")";
        stmt.executeUpdate(createAchievementsSQL);
    }

    private void createFriendsTable(Statement stmt) throws SQLException {
        String createFriendsSQL = "CREATE TABLE IF NOT EXISTS Friends(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user1Id INT NOT NULL," +
                "user2Id INT NOT NULL," +
                "status ENUM('PENDING', 'ACCEPTED') NOT NULL DEFAULT 'PENDING'," +
                "FOREIGN KEY(user1Id) REFERENCES Users(id)," +
                "FOREIGN KEY(user2Id) REFERENCES Users(id)" +
                ")";
        stmt.executeUpdate(createFriendsSQL);
    }

    public static void main(String[] args) {
        DatabaseSetup setup = new DatabaseSetup();
        setup.setupDatabase();
        System.out.println("Database setup completed!");
    }
}

