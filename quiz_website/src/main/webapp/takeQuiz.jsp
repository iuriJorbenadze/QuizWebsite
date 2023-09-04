<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Take Quiz</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Arial', sans-serif;
            background: #f4f7f9;
            color: #333;
            padding: 20px;
        }

        main {
            max-width: 800px;
            margin: 40px auto;
            background: #fff;
            padding: 30px;
            box-shadow: 0 3px 20px rgba(0, 0, 0, 0.1);
        }

        h1 {
            font-size: 2rem;
            margin-bottom: 20px;
        }

        p {
            margin-bottom: 20px;
            font-size: 1.1rem;
        }

        #questionsContainer div {
            background-color: #f9f9f9;
            border: 1px solid #e1e1e1;
            padding: 15px;
            margin-bottom: 15px;
            border-radius: 5px;
        }

        #questionsContainer h3 {
            margin-bottom: 15px;
        }

        ul {
            list-style-type: none;
        }

        li {
            margin: 10px 0;
        }

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

    </style>
</head>

<body>

<main>
    <h1 id="quizTitle">Loading Quiz Title...</h1>
    <p id="quizDescription">Loading Quiz Description...</p>

    <div id="questionsContainer">
        <!-- Questions will be dynamically populated here -->
    </div>

    <button id="submitQuiz">Submit Quiz</button>
</main>



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



    document.getElementById('submitQuiz').addEventListener('click', async function() {
        let answers = {};

        document.querySelectorAll('input[name^="question"]').forEach(input => {
            if (input.checked) {
                let questionId = input.name.replace('question', '');
                answers[questionId] = input.value;
            }
        });

        let urlParams = new URLSearchParams(window.location.search);
        let quizId = urlParams.get('quizId');

        let payload = {
            answers: answers,
            quizId: quizId
        };

        let response = await fetch('/ResultController?action=submitQuiz', {
            method: 'POST',
            body: JSON.stringify(payload),
            headers: {
                'Content-Type': 'application/json'
            }
        });

        let result = await response.json();

        if (result.quizResults && typeof result.totalScore !== 'undefined') {
            window.location.href = "/quizResults.jsp?quizId=" + quizId;
        } else {
            console.error("Quiz submission failed");
            alert(result.message);
        }

        if (!response.ok) {
            console.error("Quiz submission failed due to network or server error");
            alert("Network or server error occurred");
            return;
        }
    });




    // fetch('/ResultController?action=submitQuiz', {
    //     method: 'POST',
    //     body: JSON.stringify(data), // your quiz data
    //     headers: {
    //         'Content-Type': 'application/json'
    //     }
    // })
    //     .then(response => response.json())
    //     .then(data => {
    //         if (data.status === 'success') {
    //             window.location.href = "/quizResults.jsp";
    //         } else {
    //             console.error("Quiz submission failed");
    //         }
    //     })
    //     .catch((error) => {
    //         console.error('Error:', error);
    //     });




</script>

</body>
</html>
