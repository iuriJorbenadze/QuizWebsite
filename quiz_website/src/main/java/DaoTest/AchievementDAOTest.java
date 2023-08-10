package DaoTest;

import Dao.AchievementDAO;
import Dao.UserDAO;
import model.Achievement;
import model.Achievement.AchievementType;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementDAOTest {

    private AchievementDAO achievementDAO;
    private UserDAO userDAO;
    private User testUser;

    @BeforeEach
    public void setup() {
        achievementDAO = new AchievementDAO();
        userDAO = new UserDAO();

        String username = "TestUser_" + UUID.randomUUID().toString();
        testUser = new User(username, "password", username + "@gmail.com", false);
        userDAO.createUser(testUser);
    }

    @AfterEach
    public void tearDown() {
        userDAO.deleteUser(testUser.getUserId()); //this will cause cascade and delete acheievement as well
    }

    @Test
    public void testCreateAchievement() {
        Achievement achievement = new Achievement(0, testUser.getUserId(), AchievementType.AMATEUR_AUTHOR, new Date());
        int id = achievementDAO.createAchievement(achievement);
        assertTrue(id > 0);
        assertEquals(id, achievement.getId());
    }

    @Test
    public void testGetAchievementsByUserId() {
        Achievement achievement1 = new Achievement(0, testUser.getUserId(), AchievementType.AMATEUR_AUTHOR, new Date());
        Achievement achievement2 = new Achievement(0, testUser.getUserId(), AchievementType.PROLIFIC_AUTHOR, new Date());

        achievementDAO.createAchievement(achievement1);
        achievementDAO.createAchievement(achievement2);

        List<Achievement> achievements = achievementDAO.getAchievementsByUserId(testUser.getUserId());
        assertTrue(achievements.size() >= 2);

        // Asserting that our testUser has earned the AMATEUR_AUTHOR achievement
        boolean hasAmateurAuthor = achievements.stream().anyMatch(a -> a.getAchievementType() == AchievementType.AMATEUR_AUTHOR);
        assertTrue(hasAmateurAuthor);
    }



    @Test
    public void testDuplicateAchievementInsertion() {
        Achievement achievement1 = new Achievement(0, testUser.getUserId(), AchievementType.AMATEUR_AUTHOR, new Date());
        int id1 = achievementDAO.createAchievement(achievement1);
        assertTrue(id1 > 0); // This asserts that the first insertion was successful

        Achievement achievement2 = new Achievement(0, testUser.getUserId(), AchievementType.AMATEUR_AUTHOR, new Date());
        int result = achievementDAO.createAchievement(achievement2);
        assertEquals(-2, result); // This asserts that the second insertion was rejected due to duplication
    }


    @Test
    public void testGetAchievementsForNonExistentUser() {
        List<Achievement> achievements = achievementDAO.getAchievementsByUserId(-1);
        assertTrue(achievements.isEmpty());
    }

    @Test
    public void testDeletionOfUserAlsoDeletesTheirAchievements() {
        Achievement achievement = new Achievement(0, testUser.getUserId(), AchievementType.AMATEUR_AUTHOR, new Date());
        achievementDAO.createAchievement(achievement);

        userDAO.deleteUser(testUser.getUserId());

        List<Achievement> achievementsAfterDeletion = achievementDAO.getAchievementsByUserId(testUser.getUserId());
        assertTrue(achievementsAfterDeletion.isEmpty());
    }
    @Test
    public void testGetAchievementById() {
        Achievement newAchievement = new Achievement(0, testUser.getUserId(), Achievement.AchievementType.AMATEUR_AUTHOR, new Date());
        int id = achievementDAO.createAchievement(newAchievement);

        Achievement retrievedAchievement = achievementDAO.getAchievementById(id);
        assertNotNull(retrievedAchievement);
        assertEquals(id, retrievedAchievement.getId());
        assertEquals(Achievement.AchievementType.AMATEUR_AUTHOR, retrievedAchievement.getAchievementType());
    }

    @Test
    public void testUpdateAchievement() {
        Achievement newAchievement = new Achievement(0, testUser.getUserId(), Achievement.AchievementType.AMATEUR_AUTHOR, new Date());
        int id = achievementDAO.createAchievement(newAchievement);

        Achievement toUpdate = new Achievement(id, testUser.getUserId(), Achievement.AchievementType.QUIZ_MACHINE, new Date());
        assertTrue(achievementDAO.updateAchievement(toUpdate));

        Achievement updatedAchievement = achievementDAO.getAchievementById(id);
        assertEquals(Achievement.AchievementType.QUIZ_MACHINE, updatedAchievement.getAchievementType());
    }

    @Test
    public void testDeleteAchievementById() {
        Achievement newAchievement = new Achievement(0, testUser.getUserId(), Achievement.AchievementType.AMATEUR_AUTHOR, new Date());
        int id = achievementDAO.createAchievement(newAchievement);

        assertTrue(achievementDAO.deleteAchievementById(id));

        assertNull(achievementDAO.getAchievementById(id));
    }

    @Test
    public void testGetAllAchievementsForUser() {
        int userId = testUser.getUserId();

        // Create a list of achievements
        List<Achievement> testAchievements = new ArrayList<>();
        testAchievements.add(new Achievement(0, userId, Achievement.AchievementType.AMATEUR_AUTHOR, new Date()));
        testAchievements.add(new Achievement(0, userId, Achievement.AchievementType.QUIZ_MACHINE, new Date()));

        // Insert the test achievements into the database
        for (Achievement achievement : testAchievements) {
            achievementDAO.createAchievement(achievement);
        }

        // Fetch the achievements for the user
        List<Achievement> achievements = achievementDAO.getAchievementsByUserId(userId);

        // Verify the fetched achievements
        assertFalse(achievements.isEmpty());
        assertTrue(achievements.size() >= testAchievements.size());  // There might be other achievements for the user

        // Clean up: Delete the test achievements from the database
        for (Achievement achievement : testAchievements) {
            achievementDAO.deleteAchievementById(achievement.getId());  // Assuming you have a deleteAchievementById() method
        }
    }


    @Test
    public void testGetNonExistingAchievementById() {
        int nonExistingId = 99999;  // Some random ID that doesn't exist in your database
        Achievement retrievedAchievement = achievementDAO.getAchievementById(nonExistingId);
        assertNull(retrievedAchievement);
    }

    @Test
    public void testUpdateNonExistingAchievement() {
        Achievement toUpdate = new Achievement(99999, testUser.getUserId(), Achievement.AchievementType.QUIZ_MACHINE, new Date());
        assertFalse(achievementDAO.updateAchievement(toUpdate));
    }

    @Test
    public void testDeleteNonExistingAchievementById() {
        assertFalse(achievementDAO.deleteAchievementById(99999));
    }

    @Test
    public void testAchievementCountForUser() {
        int userId = testUser.getUserId();
        List<Achievement> achievements = achievementDAO.getAchievementsByUserId(userId);
        int originalSize = achievements.size();

        Achievement newAchievement = new Achievement(0, userId, Achievement.AchievementType.AMATEUR_AUTHOR, new Date());
        achievementDAO.createAchievement(newAchievement);

        achievements = achievementDAO.getAchievementsByUserId(userId);
        assertEquals(originalSize + 1, achievements.size());
    }

}
