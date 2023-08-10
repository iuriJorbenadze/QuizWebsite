package model;


import java.util.Date;

public class Achievement {
    public enum AchievementType {
        AMATEUR_AUTHOR("AMATEUR_AUTHOR", "The user created a quiz."),
        PROLIFIC_AUTHOR("PROLIFIC_AUTHOR", "The user created five quizzes."),
        PRODIGIOUS_AUTHOR("PRODIGIOUS_AUTHOR", "The user created ten quizzes."),
        QUIZ_MACHINE("QUIZ_MACHINE", "The user took ten quizzes."),
        I_AM_THE_GREATEST("I_AM_THE_GREATEST", "The user had the highest score on a quiz."),
        PRACTICE_MAKES_PERFECT("PRACTICE_MAKES_PERFECT", "The user took a quiz in practice mode.");

        // You could also add an icon filename or URL field here.
        private final String name;
        private final String description;

        AchievementType(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

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
