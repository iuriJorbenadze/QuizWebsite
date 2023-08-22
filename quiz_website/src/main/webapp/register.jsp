<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
</head>
<body>
<form action="UserController" method="post">
    <input type="hidden" name="action" value="registerUser">
    Username: <input type="text" name="username" required><br>
    Password: <input type="password" name="passwordHash" required><br>
    Email: <input type="email" name="email" required><br>
    Admin: <input type="checkbox" name="isAdmin" value="true"><br>
    <input type="submit" value="Register">
</form>
<p>Already have an account? <a href="index.jsp">Login here</a></p>
</body>
</html>
