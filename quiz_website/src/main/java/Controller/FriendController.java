package Controller;

import model.Friend;
import service.FriendService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class FriendController extends HttpServlet {

    private final FriendService friendService;

    public FriendController() {
        this.friendService = new FriendService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        switch (action) {
            case "viewPendingRequests":
                int userIdForPending = Integer.parseInt(request.getParameter("userId"));
                List<Friend> pendingRequests = friendService.getPendingFriendRequests(userIdForPending);
                request.setAttribute("pendingRequests", pendingRequests);
                request.getRequestDispatcher("/viewPendingRequests.jsp").forward(request, response);
                break;

            case "viewAllFriends":
                int userIdForAll = Integer.parseInt(request.getParameter("userId"));
                List<Friend> allFriends = friendService.getAllFriends(userIdForAll);
                request.setAttribute("allFriends", allFriends);
                request.getRequestDispatcher("/viewAllFriends.jsp").forward(request, response);
                break;

            case "getFriendshipDetails":
                int user1IdForDetails = Integer.parseInt(request.getParameter("user1Id"));
                int user2IdForDetails = Integer.parseInt(request.getParameter("user2Id"));
                Friend friendshipDetails = friendService.getFriendshipDetails(user1IdForDetails, user2IdForDetails);
                request.setAttribute("friendshipDetails", friendshipDetails);
                request.getRequestDispatcher("/friendshipDetails.jsp").forward(request, response);
                break;

            case "areTheyFriends":
                int user1IdForCheck = Integer.parseInt(request.getParameter("user1Id"));
                int user2IdForCheck = Integer.parseInt(request.getParameter("user2Id"));
                boolean areFriends = friendService.areTheyFriends(user1IdForCheck, user2IdForCheck);
                response.getWriter().write(areFriends ? "Yes" : "No");
                break;

            case "countFriends":
                int userIdForCount = Integer.parseInt(request.getParameter("userId"));
                int friendCount = friendService.countFriends(userIdForCount);
                response.getWriter().write(String.valueOf(friendCount));
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        switch (action) {
            case "sendFriendRequest":
                int senderId = Integer.parseInt(request.getParameter("senderId"));
                int receiverId = Integer.parseInt(request.getParameter("receiverId"));
                friendService.sendFriendRequest(senderId, receiverId);
                response.sendRedirect("somePage.jsp"); // Redirect to some page after sending the request
                break;

            case "acceptFriendRequest":
                int senderIdForAccept = Integer.parseInt(request.getParameter("senderId"));
                int receiverIdForAccept = Integer.parseInt(request.getParameter("receiverId"));
                friendService.acceptFriendRequest(senderIdForAccept, receiverIdForAccept);
                response.sendRedirect("somePage.jsp"); // Redirect to some page after accepting the request
                break;

            case "declineFriendRequest":
                int senderIdForDecline = Integer.parseInt(request.getParameter("senderId"));
                int receiverIdForDecline = Integer.parseInt(request.getParameter("receiverId"));
                friendService.declineFriendRequest(senderIdForDecline, receiverIdForDecline);
                response.sendRedirect("somePage.jsp"); // Redirect to some page after declining the request
                break;

            case "removeFriend":
                int user1Id = Integer.parseInt(request.getParameter("user1Id"));
                int user2Id = Integer.parseInt(request.getParameter("user2Id"));
                friendService.removeFriend(user1Id, user2Id);
                response.sendRedirect("somePage.jsp"); // Redirect to some page after removing a friend
                break;

            case "clearAllFriendships":
                friendService.clearAllFriendships();
                response.getWriter().write("All friendships cleared!");
                break;

        }
    }
    //all functions added
}
