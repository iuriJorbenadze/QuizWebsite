<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="model.User" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>User Profile</title>
</head>
<body>
<main>
  <h1>User Profile</h1>

  <%
    User user = (User) request.getAttribute("user");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
  %>

  <p><strong>Username:</strong> <%= user.getUsername() %></p>
  <p><strong>Email:</strong> <%= user.getEmail() %></p>
  <p><strong>Date Registered:</strong> <%= user.getDateRegistered().format(formatter) %></p>
  <p><strong>Is Admin:</strong> <%= user.isAdmin() ? "Yes" : "No" %></p>

  <a href="dashboardController">Back to Dashboard</a>
</main>
</body>
</html>
