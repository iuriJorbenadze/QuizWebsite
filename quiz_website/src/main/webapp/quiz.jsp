<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Details</title>
</head>
<body>

<h1 id="quizTitle">Loading...</h1>
<p id="quizDescription">Loading description...</p>
<p id="quizCreatedBy">Created By: Loading...</p>
<p id="quizCreatedDate">Created Date: Loading...</p>

<button onclick="startQuiz()">Start Quiz</button>

<script>
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const quizId = urlParams.get('quizId');


        fetch(`http://localhost:8082/QuizController?action=getQuizById&id=${quizId}&format=json`)
            .then(response => response.json())
            .then(data => {
                // Populate the page with the quiz data
                document.getElementById("quizTitle").textContent = data.title;
                document.getElementById("quizDescription").textContent = data.description;
                document.getElementById("quizCreatedBy").textContent = `Created By: User ${data.createdByUserId}`;
                document.getElementById("quizCreatedDate").textContent = `Created Date: ${data.createdDate}`;
            })
            .catch(error => console.error('Error fetching quiz data:', error));
    }

    function startQuiz() {
        // Logic to initiate the quiz, probably navigating to another page
    }
</script>

</body>
</html>
