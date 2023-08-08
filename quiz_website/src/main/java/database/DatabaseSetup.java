package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseSetup {

    private String jdbcBaseURL;
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private String dbName;

    public DatabaseSetup() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/database.properties"));
            jdbcBaseURL = properties.getProperty("db.baseurl");
            jdbcURL = properties.getProperty("db.url");
            jdbcUsername = properties.getProperty("db.user");
            jdbcPassword = properties.getProperty("db.password");
            dbName = properties.getProperty("db.name");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Connection getBaseConnection() throws SQLException {
        return DriverManager.getConnection(jdbcBaseURL, jdbcUsername, jdbcPassword);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public void setupDatabase() {
        try (Connection baseConnection = getBaseConnection(); Statement stmt = baseConnection.createStatement()) {
            // Check and create the database if it doesn't exist
            String checkDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + dbName;
            stmt.executeUpdate(checkDatabaseQuery);

            // Now that we ensured the database exists, let's create tables in it
            try (Connection connection = getConnection(); Statement stmt2 = connection.createStatement()) {
                createUsersTable(stmt2);
                createQuizTable(stmt2);
                createQuestionsTable(stmt2);
                createOptionsTable(stmt2);
                createTakenQuizTable(stmt2);
                createMessagesTable(stmt2);
                createAchievementsTable(stmt2);
                createFriendsTable(stmt2);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private void createUsersTable(Statement stmt) throws SQLException {
        String createUsersSQL = "CREATE TABLE IF NOT EXISTS Users(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(255) NOT NULL UNIQUE," +
                "passwordHash VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "dateRegistered TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "isAdmin BOOLEAN DEFAULT FALSE" +
                ")";
        stmt.executeUpdate(createUsersSQL);
    }

    private void createQuizTable(Statement stmt) throws SQLException {
        String createQuizSQL = "CREATE TABLE IF NOT EXISTS Quizzes(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "description TEXT," +
                "createdByUserId INT," +
                "createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(createdByUserId) REFERENCES Users(id)" +
                ")";
        stmt.executeUpdate(createQuizSQL);
    }

    private void createQuestionsTable(Statement stmt) throws SQLException {
        String createQuestionsSQL = "CREATE TABLE IF NOT EXISTS Questions(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "quizId INT NOT NULL," +
                "content TEXT NOT NULL," +
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
                "timeTaken BIGINT," +  // storing duration as seconds
                "feedback TEXT," +
                "status VARCHAR(255)," +
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

