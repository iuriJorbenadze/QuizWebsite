package model;


import java.util.Date;

public class Achievement {
    private int id;
    private int userId;
    private String achievementName;
    private Date dateEarned;

    public Achievement() {

    }

    public Achievement(int id, int userId, String achievementName, Date dateEarned) {
        this.id = id;
        this.userId = userId;
        this.achievementName = achievementName;
        this.dateEarned = dateEarned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }

    public Date getDateEarned() {
        return dateEarned;
    }

    public void setDateEarned(Date dateEarned) {
        this.dateEarned = dateEarned;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "id=" + id +
                ", userId=" + userId +
                ", achievementName='" + achievementName + '\'' +
                ", dateEarned=" + dateEarned +
                '}';
    }
}
