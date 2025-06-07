<%-- 
    Document   : changePassword
    Created on : Jun 3, 2025, 5:13:25 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Forgot Password</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #f4f6f8;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100vh;
      margin: 0;
    }
    .form-container {
      background: #fff;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
      width: 350px;
    }
    h2 {
      margin-bottom: 20px;
      color: #333;
      text-align: center;
    }
    label {
      display: block;
      margin-bottom: 6px;
      color: #555;
    }
    input[type="email"] {
      width: 100%;
      padding: 8px;
      margin-bottom: 14px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    button {
      width: 100%;
      padding: 10px;
      background: #007BFF;
      color: #fff;
      border: none;
      border-radius: 4px;
      font-size: 16px;
      cursor: pointer;
    }
    button:hover {
      background: #0056b3;
    }
    .error {
      color: #b71c1c;
      background: #ffcdd2;
      padding: 8px;
      margin-bottom: 12px;
      border-radius: 4px;
      text-align: center;
    }
  </style>
</head>
<body>
  <div class="form-container">
    <h2>Forgot Password</h2>

    <!-- Hiển thị lỗi nếu có -->
    <c:if test="${not empty error}">
      <div class="error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/ForgotPassword_servlet" method="post">
      <label for="email">Enter your email:</label>
      <input type="email" id="email" name="email" placeholder="you@example.com" required />

      <button type="submit">Send OTP</button>
    </form>
  </div>
</body>
</html>
