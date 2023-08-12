//package DaoTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


//TODO TO FIX NOT WORKING
@Suite
@SelectClasses({
        UserDAOTest.class,
        QuizDAOTest.class,
        QuestionDAOTest.class,
        TakenQuizDAOTest.class,
        FriendDAOTest.class,
        MessageDAOTest.class,
        AchievementDAOTest.class
})
public class UnitedTest {

    @BeforeAll
    public static void init() {
        System.out.println("Starting all DAO tests.");
    }

    @AfterAll
    public static void finish() {
        System.out.println("Finished all DAO tests.");
    }
}
