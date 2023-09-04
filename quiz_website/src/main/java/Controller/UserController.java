package Controller;

import model.User;
import service.TakenQuizService;
import service.UserService;
import Dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/UserController")
public class UserController extends HttpServlet {

    private UserService userService = new UserService(new UserDAO()); // Initialize with your DAO

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter out = resp.getWriter();

        switch (action) {
            case "getUserById":
                int id = Integer.parseInt(req.getParameter("id"));
                User userById = userService.getUserById(id);
                if (userById != null) {
                    out.print(userById.toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                break;

            case "getAllUsers":
                List<User> users = userService.getAllUsers();
                for (User user : users) {
                    out.println(user.toString());
                }
                break;

            case "getAllAdmins":
                List<User> admins = userService.getAllAdmins();
                for (User admin : admins) {
                    out.println(admin.toString());
                }
                break;

            case "getAllRegularUsers":
                List<User> regularUsers = userService.getAllRegularUsers();
                for (User user : regularUsers) {
                    out.println(user.toString());
                }
                break;

            case "getUserByUsername":
                String username = req.getParameter("username");
                User userByUsername = userService.getUserByUsername(username);
                if (userByUsername != null) {
                    out.println(userByUsername.toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                break;

            case "isUsernameAvailable":
                String checkUsername = req.getParameter("username");
                if (userService.isUsernameAvailable(checkUsername)) {
                    out.println("Username is available.");
                } else {
                    out.println("Username is taken.");
                }
                break;

//            case "validateUserCredentials":
//                String loginUsername = req.getParameter("username");
//                String loginPassword = req.getParameter("password");  // remember, we're just matching password hashes in the example
//                if (userService.validateUserCredentials(loginUsername, loginPassword)) {
//                    out.println("Credentials are valid.");
//                } else {
////                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////                    out.println("Invalid credentials.");
//                      resp.sendRedirect("index.jsp?error=true");
//                }
//                break;

            case "isAdmin":
                int checkAdminId = Integer.parseInt(req.getParameter("id"));
                if (userService.isAdmin(checkAdminId)) {
                    out.println("User is an admin.");
                } else {
                    out.println("User is not an admin.");
                }
                break;

            case "getUserByCredentials":
                String credUsername = req.getParameter("username");
                String credPassword = req.getParameter("password");
                User userByCredentials = userService.getUserByCredentials(credUsername, credPassword);
                if (userByCredentials != null) {
                    out.println(userByCredentials.toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println("Invalid credentials or user not found.");
                }
                break;

            default:
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Invalid action get");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter out = resp.getWriter();

        switch (action) {
            case "registerUser":
                String regUsername = req.getParameter("username");
                String passwordHash = req.getParameter("passwordHash");
                String email = req.getParameter("email");
                boolean isAdmin = Boolean.parseBoolean(req.getParameter("isAdmin"));

                User newUser = new User(regUsername, passwordHash, email, isAdmin);
                if (userService.registerUser(newUser)) {
                    out.println("User registered successfully!");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.println("Username already exists!");
                }
                break;

            case "updateUser":
                int updateId = Integer.parseInt(req.getParameter("id"));
                String updateUsername = req.getParameter("username");
                String updatePasswordHash = req.getParameter("passwordHash");
                String updateEmail = req.getParameter("email");
                boolean updateIsAdmin = Boolean.parseBoolean(req.getParameter("isAdmin"));

                User updateUser = new User(updateId, updateUsername, updatePasswordHash, updateEmail, null, updateIsAdmin);
                if (userService.updateUser(updateUser)) {
                    out.println("User updated successfully!");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.println("User not found or update failed!");
                }
                break;
//a
            //LOGIN
            case "validateUserCredentials":
                String loginUsername = req.getParameter("username");
                String loginPassword = req.getParameter("password"); // This will later be hashed before comparison

                User authenticatedUser = userService.getUserByCredentials(loginUsername, loginPassword);

                if (authenticatedUser != null) {
                    // The user is authenticated

                    // Store the user object in the session
                    HttpSession session = req.getSession();
                    session.setAttribute("user", authenticatedUser);

                    // Redirect to a dashboard controller or servlet
                    resp.sendRedirect("dashboardController");
                } else {
                    resp.sendRedirect("index.jsp?error=true");
                }
                break;




            case "deleteUser":
                int deleteId = Integer.parseInt(req.getParameter("id"));
                if (userService.deleteUser(deleteId)) {
                    out.println("User deleted successfully!");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.println("User not found or deletion failed!");
                }
                break;

            case "promoteToAdmin":
                int promoteId = Integer.parseInt(req.getParameter("id"));
                if (userService.promoteToAdmin(promoteId)) {
                    out.println("User promoted to admin!");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.println("User not found or user is already an admin!");
                }
                break;

            case "demoteFromAdmin":
                int demoteId = Integer.parseInt(req.getParameter("id"));
                if (userService.demoteFromAdmin(demoteId)) {
                    out.println("Admin demoted to regular user!");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.println("User not found or user is not an admin!");
                }
                break;



            default:
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Invalid action post");
                break;
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        PrintWriter out = resp.getWriter();

        switch (action) {
            case "updateUser":
                int updateId = Integer.parseInt(req.getParameter("id"));
                String updateUsername = req.getParameter("username");
                String updatePasswordHash = req.getParameter("passwordHash");
                String updateEmail = req.getParameter("email");
                boolean updateIsAdmin = Boolean.parseBoolean(req.getParameter("isAdmin"));

                User updateUser = new User(updateId, updateUsername, updatePasswordHash, updateEmail, null, updateIsAdmin);
                if (userService.updateUser(updateUser)) {
                    out.println("User updated successfully!");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.println("User not found or update failed!");
                }
                break;



            default:
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Invalid action");
                break;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        int deleteId = Integer.parseInt(req.getParameter("id"));

        if (userService.deleteUser(deleteId)) {
            out.println("User deleted successfully!");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("User not found or deletion failed!");
        }
    }
    //all functions added
}