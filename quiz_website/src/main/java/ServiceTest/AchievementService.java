package ServiceTest;

import Dao.AchievementDAO;
import Dao.UserDAO;
import model.Achievement;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AchievementService;
import service.UserService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementServiceTest {

    private AchievementService achievementService;
    private AchievementDAO achievementDAO;
    private UserService userService;
    private User testUser;
    private Achievement testAchievement;

    @BeforeEach
    public void setup() {
        achievementDAO = new AchievementDAO();
        achievementService = new AchievementService(achievementDAO);

        testUser = new User("testAchievementUser", "testPassword", "testAchievementUser@email.com", false);
        userService = new UserService(new UserDAO());
        assertTrue(userService.registerUser(testUser));


        testAchievement = new Achievement(testUser.getUserId(), Achievement.AchievementType.AMATEUR_AUTHOR, new Date());
        assertTrue(achievementService.createAchievement(testAchievement)>0);
    }

    @AfterEach
    public void tearDown() {
        if (testAchievement != null) {
            achievementService.deleteAchievementById(testAchievement.getId());
        }
        if (testUser != null) {
            userService.deleteUser(testUser.getUserId());
        }
    }

    @Test
    public void testGetAchievementById() {
        Achievement achievement = achievementService.getAchievementById(testAchievement.getId());
        assertNotNull(achievement);
        assertEquals(testAchievement.getId(), achievement.getId());
    }

    @Test
    public void testGetAchievementsByUserId() {
        List<Achievement> achievements = achievementService.getAchievementsByUserId(testUser.getUserId());
        assertTrue(achievements.stream().anyMatch(a -> a.getId() == testAchievement.getId()));
    }

    @Test
    public void testCreateExistingAchievement() {
        Achievement duplicateAchievement = new Achievement(testUser.getUserId(), Achievement.AchievementType.I_AM_THE_GREATEST, new Date());
        Achievement duplicateAchievement2 = new Achievement(testUser.getUserId(), Achievement.AchievementType.I_AM_THE_GREATEST, new Date());
        int result = achievementService.createAchievement(duplicateAchievement);
        int result2 = achievementService.createAchievement(duplicateAchievement2);
        assertEquals(-2, result2);
    }

    @Test
    public void testCreateNewAchievement() {
        Achievement newAchievement = new Achievement(testUser.getUserId(), Achievement.AchievementType.I_AM_THE_GREATEST, new Date());
        int id = achievementService.createAchievement(newAchievement);
        assertTrue(id > 0);

        achievementService.deleteAchievementById(id);
    }

    @Test
    public void testUpdateAchievement() {
        testAchievement.setDateEarned(new Date());
        assertTrue(achievementService.updateAchievement(testAchievement));
    }

    @Test
    public void testDeleteAchievement() {
        assertTrue(achievementService.deleteAchievementById(testAchievement.getId()));
        testAchievement = null;
    }
}
