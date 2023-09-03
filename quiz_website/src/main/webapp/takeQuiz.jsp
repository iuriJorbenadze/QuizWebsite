<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Take Quiz</title>
</head>
<body>

<h1 id="quizTitle">Loading Quiz Title...</h1>
<p id="quizDescription">Loading Quiz Description...</p>

<div id="questionsContainer">
    <!-- Questions will be dynamically populated here -->
</div>

<button onclick="submitAnswers()">Submit Quiz</button>



<script>
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const quizId = urlParams.get('quizId');

        // Fetch quiz details
        fetch(`http://localhost:8082/QuizController?action=getQuizById&id=${quizId}&format=json`)
            .then(response => response.json())
            .then(data => {
                document.getElementById("quizTitle").textContent = data.title;
                document.getElementById("quizDescription").textContent = data.description;
            });

        // Fetch questions for the quiz
        fetch(`http://localhost:8082/QuestionController?action=getAllQuestionsForQuiz&quizId=${quizId}`)
            .then(response => response.json())
            .then(questions => {
                const questionsContainer = document.getElementById("questionsContainer");

                questions.forEach(question => {
                    // Create a div for each question
                    const questionDiv = document.createElement("div");
                    var questionHTML = '<h3>' + question.content + '</h3><ul>';

                    for (var i = 0; i < question.options.length; i++) {
                        questionHTML += '<li><input type="radio" name="question' + question.questionId + '" value="' + question.options[i] + '"> ' + question.options[i] + '</li>';
                    }

                    questionHTML += '</ul>';
                    questionDiv.innerHTML = questionHTML;

                    questionsContainer.appendChild(questionDiv);
                });
            });
    }

    function submitAnswers() {
        // Logic to gather answers and submit to server, then redirect to results page or show results.
    }

</script>

</body>
</html>
