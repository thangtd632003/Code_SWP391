<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
  <title>Report a Problem</title>
  <style>
    /* Reset một số mặc định */
    * {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: #f0f2f5;
      display: flex;
      align-items: center;
      justify-content: center;
      min-height: 100vh;
      color: #333;
    }

    .container {
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
      max-width: 500px;
      width: 100%;
      padding: 30px;
      animation: fadeIn 0.4s ease-out;
    }

    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(-10px); }
      to   { opacity: 1; transform: translateY(0); }
    }

    h2 {
      text-align: center;
      margin-bottom: 20px;
      color: #444;
    }

    .message {
      margin-bottom: 20px;
      padding: 12px 16px;
      border-radius: 4px;
      font-size: 0.95rem;
    }
    .success {
      background: #e6ffed;
      border: 1px solid #a6f4c5;
      color: #217a4d;
    }
    .fail {
      background: #ffe6e6;
      border: 1px solid #f4a6a6;
      color: #7a2121;
    }

    label {
      display: block;
      margin-bottom: 8px;
      font-weight: 600;
    }

    textarea {
      width: 100%;
      min-height: 140px;
      padding: 12px;
      border: 1px solid #ccc;
      border-radius: 4px;
      resize: vertical;
      font-size: 1rem;
      line-height: 1.4;
      transition: border-color 0.2s;
    }
    textarea:focus {
      border-color: #6c63ff;
      outline: none;
      box-shadow: 0 0 4px rgba(108,99,255,0.3);
    }

    .btn-group {
      display: flex;
      justify-content: space-between;
      gap: 10px;
      margin-top: 20px;
    }

    button {
      flex: 1;
      padding: 10px 0;
      font-size: 1rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      transition: background 0.2s, transform 0.1s;
    }
    button[type="submit"] {
      background: #6c63ff;
      color: #fff;
    }
    button[type="submit"]:hover {
      background: #5950d4;
    }
    button[type="button"] {
      background: #e0e0e0;
      color: #333;
    }
    button[type="button"]:hover {
      background: #c6c6c6;
    }
    button:active {
      transform: scale(0.98);
    }
  </style>
</head>
<body>
  <div class="container">
    <h2>Send us your report</h2>

    <c:choose>
      <c:when test="${param.msg == 'reportSent'}">
        <div class="message success">Thank you—your report has been sent!</div>
      </c:when>
      <c:when test="${fn:startsWith(param.msg,'reportFail')}">
        <div class="message fail">Sorry—could not send report.</div>
      </c:when>
    </c:choose>

    <form method="post" action="${pageContext.request.contextPath}/report_page">
      <label for="feedback">Your Feedback:</label>
      <textarea id="feedback" name="feedback" required></textarea>

      <div class="btn-group">
        <button type="submit">Submit Report</button>
        <button type="button" onclick="location.href='${pageContext.request.contextPath}/dashboard'">
          ← Back to Dashboard
        </button>
      </div>
    </form>
  </div>
</body>
</html>
