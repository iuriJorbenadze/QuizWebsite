package model;


import java.util.Date;

public class Achievement {
    public enum AchievementType {
        AMATEUR_AUTHOR("Amateur Author", "The user created a quiz.", "icon_amateur.png"),
        PROLIFIC_AUTHOR("Prolific Author", "The user created five quizzes.", "icon_prolific.png"),
        PRODIGIOUS_AUTHOR("Prodigious Author", "The user created ten quizzes.", "icon_prodigious.png"),
        QUIZ_MACHINE("Quiz Machine", "The user took ten quizzes.", "icon_machine.png"),
        I_AM_THE_GREATEST("I am the Greatest", "The user had the highest score on a quiz.", "icon_greatest.png"),
        PRACTICE_MAKES_PERFECT("Practice Makes Perfect", "The user took a quiz in practice mode.", "icon_practice.png");

        private final String displayName;
        private final String description;
        private final String iconFilename;

        AchievementType(String displayName, String description, String iconFilename) {
            this.displayName = displayName;
            this.description = description;
            this.iconFilename = iconFilename;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }

        public String getIconFilename() {
            return iconFilename;
        }
    }

    private int id;
    private int userId;
    private AchievementType achievementType;
    private Date dateEarned;


    public Achievement() {
    }

    public Achievement( int userId, AchievementType achievementType, Date dateEarned) {

        this.userId = userId;
        this.achievementType = achievementType;
        this.dateEarned = dateEarned;
    }
    public Achievement(int id, int userId, AchievementType achievementType, Date dateEarned) {
        this.id = id;
        this.userId = userId;
        this.achievementType = achievementType;
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

    public AchievementType getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(AchievementType achievementType) {
        this.achievementType = achievementType;
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
                ", achievementType=" + achievementType +
                ", dateEarned=" + dateEarned +
                '}';
    }
}
