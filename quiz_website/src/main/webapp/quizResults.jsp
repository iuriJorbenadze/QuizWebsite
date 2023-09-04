<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Results</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        /* General resets */
        body, h1, h2, table, th, td, a {
            margin: 0;
            padding: 0;
            font-family: 'Poppins', sans-serif;
            text-decoration: none;
        }

        body {
            background-color: #f4f4f8;
            color: #333;
            padding: 40px;
            line-height: 1.5;
        }

        h1 {
            font-weight: 600;
            margin-bottom: 20px;
            text-align: center;
        }

        h2 {
            margin-top: 30px;
            font-weight: 500;
            text-align: center;
        }

        /* Table Styles */
        table {
            width: 100%;
            border-collapse: collapse;
            background: #ffffff;
            box-shadow: 0 5px 10px rgba(0,0,0,0.1);
        }

        thead {
            background: #007BFF;
            color: #ffffff;
        }

        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #e0e0e0;
        }

        th {
            font-weight: 500;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        td {
            font-weight: 400;
        }

        /* Result Color-Coding */
        td:last-child {
            font-weight: 600;
        }


        /* Link Styles */
        a.back-to-quiz {
            display: inline-block;
            padding: 10px 15px;
            background-color: #007BFF;
            color: white;
            border-radius: 5px;
            margin: 20px 0;
            transition: background-color 0.3s;
        }

        a.back-to-quiz:hover {
            background-color: #0056b3;
        }

    </style>
</head>
<body>
<h1>Your Quiz Results</h1>

<table border="1">
    <thead>
    <tr>
        <th>Question ID</th>
        <th>Submitted Answer</th>
        <th>Correct Answer</th>
        <th>Result</th>
    </tr>
    </thead>
    <tbody id="resultsTableBody"></tbody>
</table>
<h2 id="finalScore"></h2>

<a href="quiz.jsp" class="back-to-quiz">Back to Quiz Page</a>



<script>
    document.addEventListener("DOMContentLoaded", function(event) {
        // Fetch the quiz results automatically when the page is loaded.
        fetch('/ResultController?action=getQuizResults')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (data.quizResults && data.totalScore !== undefined) {
                    displayResults(data);
                } else {
                    console.error("Failed to load quiz results or data is missing.");
                }
            })

            .then(data => {
                if (data.quizResults && data.totalScore !== undefined) {
                    displayResults(data);
                } else {
                    console.error("Failed to load quiz results or data is missing.");
                }
            })
            .catch(error => {
                console.error("Error fetching quiz results:", error);
            });

        function displayResults(data) {
            const resultsTable = document.getElementById('resultsTableBody');
            resultsTable.innerHTML = ''; // clear existing results

            data.quizResults.forEach(result => {
                const row = document.createElement('tr');

                ['questionId', 'submittedAnswer', 'correctAnswer'].forEach(key => {
                    const cell = document.createElement('td');
                    cell.textContent = result[key] || 'Not Available';
                    row.appendChild(cell);
                });

                const resultCell = document.createElement('td');
                resultCell.textContent = result.isCorrect ? 'Correct' : 'Incorrect';
                resultCell.style.color = result.isCorrect ? '#28a745' : '#dc3545'; // color-coding based on result
                row.appendChild(resultCell);

                resultsTable.appendChild(row);
            });

            document.getElementById('finalScore').textContent = "Final Score: " + data.totalScore;
        }


    });
</script>
</body>
</html>
