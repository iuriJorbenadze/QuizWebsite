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
            margin: 0;
        }

        .login-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px;
            text-align: center;
        }

        .login-container h2 {
            margin-bottom: 20px;
            color: #007BFF;
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

        .login-container .error {
            color: red;
            display: none;
        }

        .login-container p {
            margin-top: 10px;
        }

        .login-container a {
            color: #007BFF;
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>Register</h2>
    <form action="UserController" method="post">
        <input type="hidden" name="action" value="registerUser">
        <label for="username">Username:</label>
        <input type="text" name="username" id="username" required><br>
        <label for="password">Password:</label>
        <input type="password" name="passwordHash" id="password" required><br>
        <label for="email">Email:</label>
        <input type="email" name="email" id="email" required><br>
        <label for="isAdmin">Admin:</label>
        <input type="checkbox" name="isAdmin" id="isAdmin" value="true"><br>
        <button type="submit">Register</button>
    </form>
    <p>Already have an account? <a href="index.jsp">Login here</a></p>
</div>
</body>
</html>
