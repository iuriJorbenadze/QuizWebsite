<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }

        .login-container {
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px;
        }

        .login-container h2 {
            text-align: center;
        }

        .login-container form {
            display: flex;
            flex-direction: column;
        }

        .login-container form input {
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .login-container form button {
            padding: 10px;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 10px;
        }

        .login-container form button:hover {
            background-color: #0056b3;
        }

        .error {
            color: red;
            display: none;
        }
    </style>
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
