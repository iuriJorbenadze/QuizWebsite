package Dao;

import model.Achievement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AchievementDAO extends AbstractDAO {

    public int createAchievement(Achievement achievement) {
        // Check if the user already has the achievement
        List<Achievement> existingAchievements = getAchievementsByUserId(achievement.getUserId());
        for (Achievement existing : existingAchievements) {
            if (existing.getAchievementType().name().equals(achievement.getAchievementType().name())) {
                return -2; // Return specific value to indicate achievement already exists
            }
        }

        String query = "INSERT INTO Achievements(userId, achievementName, dateEarned) VALUES (?, ?, ?)";


        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, achievement.getUserId());
            // Using the name() method to get the enum constant's name
            statement.setString(2, achievement.getAchievementType().name());
            statement.setDate(3, new java.sql.Date(achievement.getDateEarned().getTime()));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating achievement failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    achievement.setId(id);
                    return id;
                } else {
                    throw new SQLException("Creating achievement failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Indicate failure
        }
    }

    public List<Achievement> getAchievementsByUserId(int userId) {
        List<Achievement> achievements = new ArrayList<>();
        String query = "SELECT * FROM Achievements WHERE userId = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                // Directly getting the AchievementType from the resultSet
                Achievement.AchievementType type = Achievement.AchievementType.valueOf(resultSet.getString("achievementName"));
                Date dateEarned = resultSet.getDate("dateEarned");

                achievements.add(new Achievement(id, userId, type, dateEarned));  // Changed from type.getName() to type
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return achievements;
    }

    // 1. Retrieve a specific achievement by its ID.
    public Achievement getAchievementById(int achievementId) {
        String query = "SELECT * FROM Achievements WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, achievementId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("userId");
                Achievement.AchievementType type = Achievement.AchievementType.valueOf(resultSet.getString("achievementName"));
                Date dateEarned = resultSet.getDate("dateEarned");

                return new Achievement(id, userId, type, dateEarned);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no record is found or there's an exception.
    }

    // 2. Update a specific achievement.
    public boolean updateAchievement(Achievement achievement) {
        String query = "UPDATE Achievements SET userId = ?, achievementName = ?, dateEarned = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, achievement.getUserId());
            statement.setString(2, achievement.getAchievementType().name());
            statement.setDate(3, new java.sql.Date(achievement.getDateEarned().getTime()));
            statement.setInt(4, achievement.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false on failure.
        }
    }

    // 3. Delete an achievement by its ID.
    public boolean deleteAchievementById(int achievementId) {
        String query = "DELETE FROM Achievements WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, achievementId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false on failure.
        }
    }
}
