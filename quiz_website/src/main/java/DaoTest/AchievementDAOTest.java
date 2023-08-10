package DaoTest;

import Dao.AchievementDAO;
import Dao.UserDAO;
import model.Achievement;
import model.Achievement.AchievementType;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        userDAO.deleteUser(testUser.getUserId());
    }

    @Test
    public void testCreateAchievement() {
        Achievement achievement = new Achievement(0, testUser.getUserId(), AchievementType.AMATEUR_AUTHOR.getName(), new Date());
        int id = achievementDAO.createAchievement(achievement);
        assertTrue(id > 0);
        assertEquals(id, achievement.getId());
    }

    @Test
    public void testGetAchievementsByUserId() {
        Achievement achievement1 = new Achievement(0, testUser.getUserId(), AchievementType.AMATEUR_AUTHOR.getName(), new Date());
        Achievement achievement2 = new Achievement(0, testUser.getUserId(), AchievementType.PROLIFIC_AUTHOR.getName(), new Date());

        achievementDAO.createAchievement(achievement1);
        achievementDAO.createAchievement(achievement2);

        List<Achievement> achievements = achievementDAO.getAchievementsByUserId(testUser.getUserId());
        assertTrue(achievements.size() >= 2);

        // Asserting that our testUser has earned the AMATEUR_AUTHOR achievement
        boolean hasAmateurAuthor = achievements.stream().anyMatch(a -> a.getAchievementName() == AchievementType.AMATEUR_AUTHOR.getName());
        assertTrue(hasAmateurAuthor);
    }

}
