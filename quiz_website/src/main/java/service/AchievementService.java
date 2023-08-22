package service;

import Dao.AchievementDAO;
import model.Achievement;

import java.util.Date;
import java.util.List;

public class AchievementService {

    private final AchievementDAO achievementDAO;

    public AchievementService(AchievementDAO achievementDAO) {
        this.achievementDAO = achievementDAO;
    }

    public AchievementService() {
        this.achievementDAO = new AchievementDAO();
    }

    public int createAchievement(Achievement achievement) {
        return achievementDAO.createAchievement(achievement);
    }

    public List<Achievement> getAchievementsByUserId(int userId) {
        return achievementDAO.getAchievementsByUserId(userId);
    }

    public Achievement getAchievementById(int achievementId) {
        return achievementDAO.getAchievementById(achievementId);
    }

    public boolean updateAchievement(Achievement achievement) {
        return achievementDAO.updateAchievement(achievement);
    }

    public boolean deleteAchievementById(int achievementId) {
        return achievementDAO.deleteAchievementById(achievementId);
    }

    public boolean userHasAchievementType(int userId, Achievement.AchievementType type) {
        List<Achievement> achievements = getAchievementsByUserId(userId);
        for (Achievement achievement : achievements) {
            if (achievement.getAchievementType() == type) {
                return true;
            }
        }
        return false;
    }

    public void awardAchievement(int userId, Achievement.AchievementType type) {
        if (!userHasAchievementType(userId, type)) {
            Achievement achievement = new Achievement();
            achievement.setUserId(userId);
            achievement.setAchievementType(type);
            achievement.setDateEarned(new Date());
            createAchievement(achievement);
        }
    }
}
