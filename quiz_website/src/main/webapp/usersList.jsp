<!DOCTYPE html>
<html>
<head>
    <title>Users List</title>
</head>
<body>
<h2>Users List</h2>

<%-- Iterate through the users list and display --%>
<% List<User> users = (List<User>) request.getAttribute("users");
    for(User user : users) {
%>
<p><%= user.toString() %></p>
<% } %>

<a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>
