<%@ page import="model.User" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard</title>

    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

    <!-- FontAwesome for icons -->
    <script src="https://kit.fontawesome.com/a076d05399.js"></script>

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@300;400;600&display=swap" rel="stylesheet">

    <style>
        body {
            /*background-image: url('assets/Cool Sky.jpg');*/
            background-size: cover;
            background-repeat: no-repeat;
            font-family: 'Open Sans', sans-serif;
            color: #eee;
        }
        .background-style {
            background-color: rgb(255, 255, 255);
            /*background-image: url('assets/Cool Sky.jpg');*/
            background-size: cover;
            background-repeat: no-repeat;
            min-height: 100vh;
        }

        .container-fluid {
            border-radius: 5px;
            min-height: 100vh;
        }

        .d-flex.justify-content-between.align-items-center {
            align-items: center;
            justify-content: space-between;
        }

        .container-fluid.bg-dark.text-white {
            overflow: hidden;
            padding-top: 10px;
            padding-bottom: 10px;
        }

        .welcome-message {
            background-color: transparent;
            border: 1px solid #eee;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .welcome-message h1 {
            font-weight: 600;
            color: #304af1;
        }


        aside {
            position: fixed;
            top: 0;
            left: 0;
            height: 100vh;
            overflow-y: auto; /* Allow vertical scroll if content overflows */
            background-color: transparent;
            border-right: 1px solid #eee;
        }

        /*!* Custom scrollbar *!*/
        /*aside::-webkit-scrollbar {*/
        /*    width: 8px;*/
        /*}*/

        /*aside::-webkit-scrollbar-thumb {*/
        /*    background: rgba(255, 255, 255, 0.3);*/
        /*    border-radius: 4px;*/
        /*}*/

        /*aside::-webkit-scrollbar-thumb:hover {*/
        /*    background: rgba(255, 255, 255, 0.5);*/
        /*}*/

        /*aside::-webkit-scrollbar-track {*/
        /*    background: rgba(255, 255, 255, 0.1);*/
        /*    border-radius: 4px;*/
        /*}*/


        main {
            padding-left: 30%; /* Adjust this value based on the width of your sidebar, the default col-md-3 in Bootstrap 4 is around 25%, but adding a bit more ensures some spacing */
            background-color: transparent;
            border-radius: 5px;
            margin: 20px 0;
        }


        table {
            width: 100%;
            border-collapse: collapse;
        }

        table, th, td {
            border: 1px solid #eee;
            padding: 10px;
        }

        th {
            background-color: rgba(147, 112, 219, 0.8);
        }

        .alert {
            margin-top: 20px;
            font-weight: 400;
        }

        #notification-center .btn-info {
            background-color: rgba(8, 41, 252, 0.7);
            margin-bottom: 10px;
        }

        #notification-center .dropdown-menu {
            background-color: rgba(8, 41, 252, 0.7);
        }

        #notification-center .dropdown-item:hover {
            background-color: rgba(105, 35, 253, 0.7);
        }

        .container-fluid.main-content-container > .row {
            display: flex;
            align-items: center;
            height: 100vh;
        }


    </style>
</head>

<body>
<% User loggedInUser = (User) session.getAttribute("user"); %>

<div class="container-fluid background-style">
    <div class="row">
        <!-- Sidebar -->
        <aside class="col-md-3 px-0 background-style">
            <ul class="list-group">
                <li class="list-group-item bg-transparent"><a href="#home">Home</a></li>
                <li class="list-group-item bg-transparent"><a href="#announcements">Announcements</a></li>
                <li class="list-group-item bg-transparent"><a href="#popular-quizzes">Popular Quizzes</a></li>
                <li class="list-group-item bg-transparent"><a href="#recent-quizzes">Recently Created Quizzes</a></li>
                <li class="list-group-item bg-transparent"><a href="#my-quizzes">My Quizzes</a></li>
                <li class="list-group-item bg-transparent"><a href="#achievements">Achievements</a></li>
                <li class="list-group-item bg-transparent"><a href="#messages">Messages</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <main class="col-md-9 background-style">
            <div class="d-flex justify-content-end py-2 px-4">

                <div class="dropdown">
                    <button class="btn btn-secondary dropdown-toggle" type="button" data-toggle="dropdown">
                        <i class="fa fa-user-circle" aria-hidden="true"></i> <%= loggedInUser.getUsername() %>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-right">
                        <li><a class="dropdown-item" href="/UserController?action=displayUser&id=<%= loggedInUser.getUserId() %>">View Profile</a></li>
                        <li><a class="dropdown-item" href="#settings">Settings</a></li>
                        <li><a class="dropdown-item" href="#logout">Logout</a></li>
                    </ul>
                </div>


            </div>

            <section class="welcome-message">
                <h1>Welcome, <%= loggedInUser.getUsername() %>!</h1>
                <p>We hope you enjoy your experience here.</p>
            </section>

            <!-- Announcements -->
            <section id="announcements">
                <h2>Announcements</h2>
                <p>Stay updated with the latest news and updates.</p>
                <div class="alert alert-warning">
                    <strong>Attention!</strong> We will have a maintenance downtime tomorrow from 3 PM to 5 PM.
                </div>
            </section>

            <!-- Popular Quizzes Table -->
            <section id="popular-quizzes">
                <h2>Popular Quizzes</h2>
                <table id="popularQuizzesTable">
                    <thead>
                    <tr>
                        <th>Quiz Title</th>
                        <th>Attempts</th>
                    </tr>
                    </thead>
                    <tbody></tbody>
                </table>

            </section>
        </main>
    </div>
</div>

<!-- Notification Center -->
<div id="notification-center" class="position-fixed" style="right: 20px; bottom: 20px;">
    <button type="button" class="btn btn-info" data-toggle="dropdown">
        Notifications <span class="badge badge-light">4</span>
    </button>
    <div class="dropdown-menu dropdown-menu-right">
        <a class="dropdown-item" href="#notif1">You have a new message from Admin</a>
        <a class="dropdown-item" href="#notif2">Your quiz "History 101" has 5 new attempts</a>
        <a class="dropdown-item" href="#notif3">You received a badge: Quiz Master!</a>
        <a class="dropdown-item" href="#notif4">Your quiz "Math Basics" has been approved</a>
    </div>
</div>

<!-- Bootstrap JS, Popper.js, and Quizzes Table Script -->

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>


<script>



    (async function populateQuizzes() {
        try {
            const response = await fetch('http://localhost:8082/ResultController?action=getMostAttemptedQuizzes&limit=5');
            const data = await response.json();

            const tableBody = document.querySelector('#popularQuizzesTable tbody');
            for (const [quizId, attempts] of Object.entries(data)) {
                const quizDetailResponse = await fetch(`http://localhost:8082/QuizController?action=getQuizById&id=${quizId}&format=json`);
                const quizData = await quizDetailResponse.json();

                const row = document.createElement('tr');

                const quizTitleCell = document.createElement('td');
                const quizLink = document.createElement('a');
                quizLink.href = `/QuizController?action=displayQuiz&quizId=${quizId}`;
                quizLink.textContent = quizData.title;

                quizTitleCell.appendChild(quizLink);  // Ensure quizLink isn't null
                row.appendChild(quizTitleCell);        // Ensure quizTitleCell isn't null

                const attemptsCell = document.createElement('td');
                attemptsCell.textContent = attempts;
                row.appendChild(attemptsCell);         // Ensure attemptsCell isn't null

                tableBody.appendChild(row);            // Ensure row isn't null

            }
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    })();
</script>

</body>
</html>




<%--<%@ page import="model.User" %>--%>
<%--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>--%>
<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--<head>--%>
<%--    <meta charset="UTF-8">--%>
<%--    <title>User Dashboard</title>--%>
<%--    <style>--%>
<%--        /* Inline CSS */--%>
<%--        body {--%>
<%--            font-family: Arial, sans-serif;--%>
<%--            background-color: #f4f4f4;--%>
<%--            padding: 20px;--%>
<%--        }--%>
<%--        .welcome-message {--%>
<%--            text-align: center;--%>
<%--            margin-top: 50px;--%>
<%--        }--%>
<%--    </style>--%>
<%--</head>--%>
<%--<body>--%>
<%--<div class="welcome-message">--%>
<%--    <h1>Welcome to the User Dashboard!</h1>--%>
<%--    <p>From here, you can access your quizzes, interact with friends, check messages and see your achievements.</p>--%>
<%--</div>--%>
<%--<!-- Other content here -->--%>



<%--<table id="popularQuizzesTable">--%>
<%--    <thead>--%>
<%--    <tr>--%>
<%--        <th>Quiz Name</th>--%>
<%--        <th>Attempts</th>--%>
<%--    </tr>--%>
<%--    </thead>--%>
<%--    <tbody>--%>
<%--    <!-- Rows will be appended here dynamically -->--%>
<%--    </tbody>--%>
<%--</table>--%>


<%--<a href="QuizController?action=createQuiz&userId=<%=((User) session.getAttribute("user")).getUserId()%>">Create Quiz</a>--%>


<%--<% User loggedInUser = (User) session.getAttribute("user"); %>--%>

<%--<div class="dropdown">--%>
<%--    <button class="dropbtn">--%>
<%--        <i class="fa fa-user-circle" aria-hidden="true"></i> <%= loggedInUser.getUsername() %>--%>
<%--        <i class="fa fa-caret-down"></i>--%>
<%--    </button>--%>
<%--    <div class="dropdown-content">--%>
<%--        <a href="/UserController?action=displayUser&id=<%= loggedInUser.getUserId() %>">View Profile</a>--%>
<%--        <a href="#settings">Settings</a>--%>
<%--        <a href="#logout">Logout</a>--%>
<%--    </div>--%>
<%--</div>--%>



<%--<script>--%>
<%--    fetch('http://localhost:8082/ResultController?action=getMostAttemptedQuizzes&limit=5')--%>
<%--        .then(response => response.json())--%>
<%--        .then(data => {--%>
<%--            const tableBody = document.querySelector('#popularQuizzesTable tbody');--%>
<%--            const fetchPromises = [];--%>

<%--            for (const [quizId, attempts] of Object.entries(data)) {--%>
<%--                const row = document.createElement('tr');--%>

<%--                const quizTitleCell = document.createElement('td');--%>
<%--                // Creating a hyperlink for the quiz title--%>
<%--                const quizLink = document.createElement('a');--%>
<%--                quizLink.href = `/QuizController?action=displayQuiz&quizId=${quizId}`;--%>

<%--                quizTitleCell.appendChild(quizLink);--%>
<%--                row.appendChild(quizTitleCell);--%>

<%--                const attemptsCell = document.createElement('td');--%>
<%--                attemptsCell.textContent = attempts;--%>
<%--                row.appendChild(attemptsCell);--%>

<%--                tableBody.appendChild(row);--%>

<%--                fetchPromises.push(--%>
<%--                    fetch(`http://localhost:8082/QuizController?action=getQuizById&id=${quizId}&format=json`)--%>
<%--                        .then(response => response.json())--%>
<%--                        .then(quizData => {--%>
<%--                            console.log(quizData); // For debugging--%>
<%--                            quizLink.textContent = quizData.title;  // Assigning text to the hyperlink--%>
<%--                        })--%>

<%--                );--%>
<%--            }--%>

<%--            return Promise.all(fetchPromises);--%>
<%--        })--%>
<%--        .catch(error => console.error('Error fetching data:', error));--%>
<%--</script>--%>

<%--</body>--%>
<%--</html>--%>
