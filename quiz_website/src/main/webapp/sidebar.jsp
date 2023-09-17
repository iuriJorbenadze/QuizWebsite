<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Fixed Sidebar</title>
  <style>
    body, html {
      margin: 0;
      padding: 0;
      font-family: Arial, sans-serif;
      height: 100%;
    }

    .sidebar {
      position: fixed;
      top: 0;
      left: 0;
      width: 200px;
      height: 100%;
      background-color: #333;
      color: white;
      padding: 15px;
    }

    .main-content {
      margin-left: 220px;  /* Width of the sidebar + some margin */
      padding: 15px;
      width: calc(100% - 220px);
      box-sizing: border-box;
      height: 100%;
      overflow-y: scroll;  /* To ensure the content scrolls */
    }

    p {
      margin-bottom: 20px;
    }


  </style>

</head>
<body>
<div class="sidebar">
  Sidebar Content
</div>
<div class="main-content">
  <!-- Generate a lot of content for scrolling -->
  <p>
    Lorem ipsum ... [repeat this or other content many times]
  </p>
</div>
</body>
</html>
