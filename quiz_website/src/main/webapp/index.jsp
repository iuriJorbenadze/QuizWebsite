<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
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


<div class="login-container">
    <h2 style="color: #007BFF;">Login</h2>
    <form name="loginForm" action="/UserController" method="POST" onsubmit="return validateForm();">
        <!-- Hidden input field for the action parameter -->
        <input type="hidden" name="action" value="validateUserCredentials">
        <input type="text" name="username" placeholder="Username">
        <input type="password" name="password" placeholder="Password">
        <button type="submit" >Login</button>
    </form>

    <div id="error-message" class="error">Both fields are required!</div>

    <p>Don't have an account? <a href="register.jsp">Register here</a></p>
</div>
<script>
    function validateForm() {
        const username = document.forms["loginForm"]["username"].value;
        const password = document.forms["loginForm"]["password"].value;

        if (username === "" || password === "") {
            document.getElementById("error-message").style.display = "block";
            return false; // Prevent form submission
        }
        return true; // Allow form submission
    }
</script>

</body>
</html>
