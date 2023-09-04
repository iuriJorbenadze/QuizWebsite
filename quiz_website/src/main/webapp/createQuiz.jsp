<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
  <title>Create Quiz</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f4f4f4;
    }

    form {
      max-width: 800px;
      margin: 30px auto;
      padding: 20px;
      background-color: #fff;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
      border-radius: 8px;
    }

    label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
      color: #555;
    }

    input[type="text"], textarea, select {
      width: 100%;
      padding: 10px;
      margin-bottom: 15px;
      border: 1px solid #ccc;
      border-radius: 4px;
      font-size: 16px;
    }

    #questionsContainer .question {
      margin-bottom: 20px;
      border: 1px solid #ccc;
      padding: 15px;
      border-radius: 5px;
      background-color: #f9f9f9;
    }

    button, input[type="submit"] {
      padding: 10px 15px;
      font-size: 16px;
      background-color: #007BFF;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      transition: background-color 0.3s ease;
    }

    button:hover, input[type="submit"]:hover {
      background-color: #0056b3;
    }
  </style>
</head>
<body>

<form action="QuizController" method="post" id="createQuizForm">
  <input type="hidden" name="operation" value="createQuiz">
  <input type="hidden" name="createdByUserId" value="<%= request.getAttribute("userId") %>">
  <label for="title">Title:</label>
  <input type="text" name="title" required>
  <label for="description">Description:</label>
  <textarea name="description" required></textarea>

  <div id="questionsContainer"></div>

  <button type="button" onclick="addQuestionFields()">Add Question</button>
  <input type="submit" value="Create Quiz">
</form>



<script>
  function addQuestionFields() {
    const questionsContainer = document.getElementById('questionsContainer');

    const questionDiv = document.createElement('div');
    questionDiv.classList.add('question');

    const questionLabel = document.createElement('label');
    questionLabel.innerHTML = "Question:";
    const questionTextarea = document.createElement('textarea');
    questionTextarea.name = "questionText[]";  // Using array notation to handle multiple questions

    questionDiv.appendChild(questionLabel);
    questionDiv.appendChild(questionTextarea);

    for (let i = 1; i <= 4; i++) {
      const optionLabel = document.createElement('label');
      optionLabel.innerHTML = `Option ${i}:`;
      const optionInput = document.createElement('input');
      optionInput.type = "text";
      optionInput.name = `option${i}[]`;  // Using array notation to handle multiple options

      questionDiv.appendChild(optionLabel);
      questionDiv.appendChild(optionInput);
    }

    const correctOptionLabel = document.createElement('label');
    correctOptionLabel.innerHTML = "Correct Option:";
    const correctOptionSelect = document.createElement('select');
    correctOptionSelect.name = "correctOption[]";
    for (let i = 1; i <= 4; i++) {
      const optionElem = document.createElement('option');
      optionElem.value = i;
      optionElem.innerHTML = `Option ${i}`;
      correctOptionSelect.appendChild(optionElem);
    }

    questionDiv.appendChild(correctOptionLabel);
    questionDiv.appendChild(correctOptionSelect);
    questionsContainer.appendChild(questionDiv);
  }
</script>

</body>
</html>
