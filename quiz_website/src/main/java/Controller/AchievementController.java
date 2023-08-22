package Controller;

import model.Achievement;
import service.AchievementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@WebServlet("/AchievementController")
public class AchievementController extends HttpServlet {

    private AchievementService achievementService = new AchievementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter out = resp.getWriter();

        switch (action) {
            case "getAchievementById":
                int achievementId = Integer.parseInt(req.getParameter("achievementId"));
                Achievement achievement = achievementService.getAchievementById(achievementId);
                if (achievement != null) {
                    out.print(achievement.toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("Achievement not found.");
                }
                break;

            case "getAchievementsByUserId":
                int userId = Integer.parseInt(req.getParameter("userId"));
                List<Achievement> achievements = achievementService.getAchievementsByUserId(userId);
                for (Achievement achievementByUser : achievements) {
                    out.println(achievementByUser.toString());
                }
                break;

            default:
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Invalid action");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            Achievement.AchievementType type = Achievement.AchievementType.valueOf(req.getParameter("achievementType"));

            if (!achievementService.userHasAchievementType(userId, type)) {
                achievementService.awardAchievement(userId, type);
                out.println("Achievement awarded successfully!");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("User already has this achievement.");
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        try {
            int achievementId = Integer.parseInt(req.getParameter("achievementId"));
            int userId = Integer.parseInt(req.getParameter("userId"));
            Achievement.AchievementType type = Achievement.AchievementType.valueOf(req.getParameter("achievementType"));
            Achievement updatedAchievement = new Achievement(achievementId, userId, type, new Date());
            if (achievementService.updateAchievement(updatedAchievement)) {
                out.println("Achievement updated successfully!");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Achievement update failed!");
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        try {
            int achievementId = Integer.parseInt(req.getParameter("achievementId"));
            if (achievementService.deleteAchievementById(achievementId)) {
                out.println("Achievement deleted successfully!");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Achievement deletion failed!");
            }
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(e.getMessage());
        }
    }
}
