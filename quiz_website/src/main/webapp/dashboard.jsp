<%@ page import="model.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Dashboard</title>
    <style>
        /* Inline CSS */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            padding: 20px;
        }
        .welcome-message {
            text-align: center;
            margin-top: 50px;
        }
    </style>
</head>
<body>
<div class="welcome-message">
    <h1>Welcome to the User Dashboard!</h1>
    <p>From here, you can access your quizzes, interact with friends, check messages and see your achievements.</p>
</div>
<!-- Other content here -->



<table id="popularQuizzesTable">
    <thead>
    <tr>
        <th>Quiz Name</th>
        <th>Attempts</th>
    </tr>
    </thead>
    <tbody>
    <!-- Rows will be appended here dynamically -->
    </tbody>
</table>


<a href="QuizController?action=createQuiz&userId=<%=((User) session.getAttribute("user")).getUserId()%>">Create Quiz</a>


<script>
    fetch('http://localhost:8082/ResultController?action=getMostAttemptedQuizzes&limit=5')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector('#popularQuizzesTable tbody');
            const fetchPromises = [];

            for (const [quizId, attempts] of Object.entries(data)) {
                const row = document.createElement('tr');

                const quizTitleCell = document.createElement('td');
                // Creating a hyperlink for the quiz title
                const quizLink = document.createElement('a');
                quizLink.href = `/QuizController?action=displayQuiz&quizId=${quizId}`;

                quizTitleCell.appendChild(quizLink);
                row.appendChild(quizTitleCell);

                const attemptsCell = document.createElement('td');
                attemptsCell.textContent = attempts;
                row.appendChild(attemptsCell);

                tableBody.appendChild(row);

                fetchPromises.push(
                    fetch(`http://localhost:8082/QuizController?action=getQuizById&id=${quizId}&format=json`)
                        .then(response => response.json())
                        .then(quizData => {
                            console.log(quizData); // For debugging
                            quizLink.textContent = quizData.title;  // Assigning text to the hyperlink
                        })

                );
            }

            return Promise.all(fetchPromises);
        })
        .catch(error => console.error('Error fetching data:', error));
</script>

</body>
</html>
