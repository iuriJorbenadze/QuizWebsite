<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Details</title>
    <style>
        /* Reset margins and paddings */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* Background and font setting */
        body {
            font-family: 'Arial', sans-serif;
            line-height: 1.6;
            background: #f4f7f9;
            color: #333;
            padding: 20px;
        }

        /* Centered content */
        main {
            max-width: 800px;
            margin: 40px auto;
            background: #fff;
            padding: 30px;
            box-shadow: 0 3px 20px rgba(0, 0, 0, 0.1);
        }

        /* Styling Headings */
        h1 {
            font-size: 2rem;
            margin-bottom: 15px;
        }

        p {
            margin-bottom: 20px;
            font-size: 1.1rem;
        }

        /* Button Styling */
        button {
            background-color: #007BFF;
            color: #fff;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #0056b3;
        }

        /* Dashboard link styling */
        a {
            display: inline-block;
            margin-top: 20px;
            color: #007BFF;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        a:hover {
            color: #0056b3;
        }

    </style>
</head>

<body>

<main>
    <h1 id="quizTitle">Loading...</h1>
    <p id="quizDescription">Loading description...</p>
    <p id="quizCreatedBy">Created By: Loading...</p>
    <p id="quizCreatedDate">Created Date: Loading...</p>

    <button onclick="startQuiz()">Start Quiz</button>

    <a href="dashboard.jsp">Back to Dashboard</a>
</main>




<script>
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const quizId = urlParams.get('quizId');





        // Inside the fetch .then block in quiz.jsp
        fetch(`http://localhost:8082/QuizController?action=getQuizById&id=${quizId}&format=json`)
            .then(response => response.json())
            .then(data => {
                // Now fetch the user details using the 'createdByUserId'
                fetch(`http://localhost:8082/UserController?action=getUserById&id=${data.createdByUserId}`)
                    .then(userResponse => userResponse.json())
                    .then(userData => {
                        const userLink = `<a href="/UserController?action=displayUser&id=${userData.userId}">${userData.username}</a>`;

                        document.getElementById("quizCreatedBy").innerHTML = `Created By: ${userLink}`;
                    })
                    .catch(userError => console.error('Error fetching user data:', userError));

                // Populate the page with the quiz data
                document.getElementById("quizTitle").textContent = data.title;
                document.getElementById("quizDescription").textContent = data.description;
                document.getElementById("quizCreatedDate").textContent = `Created Date: ${data.createdDate}`;
            })
            .catch(error => console.error('Error fetching quiz data:', error));

    }

    function startQuiz() {
        const urlParams = new URLSearchParams(window.location.search);
        const quizId = urlParams.get('quizId');
        window.location.href = `/QuizController?action=takeQuiz&quizId=${quizId}`;
    }

</script>

</body>
</html>
