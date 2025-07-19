<%-- 
    Document   : changePassword
    Created on : Jun 3, 2025, 5:22:31 PM
    Author     :_servlet
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Change Password</title>
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
    input[type="text"], input[type="password"] {
      width: 100%;
      padding: 8px;
      margin-bottom: 14px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    button {
      width: 100%;
      padding: 10px;
      background: #28a745;
      color: #fff;
      border: none;
      border-radius: 4px;
      font-size: 16px;
      cursor: pointer;
    }
    button:hover {
      background: #218838;
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
    <h2>Change Password</h2>

    <c:if test="${not empty error}">
      <div class="error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/ChangePassword_servlet" method="post">
      <label for="otp">Enter OTP:</label>
      <input type="text" id="otp" name="otp" placeholder="6-digit code" required />

      <label for="newPassword">New Password:</label>
      <input type="password" id="newPassword" name="newPassword" required />

      <label for="confirmPassword">Confirm Password:</label>
      <input type="password" id="confirmPassword" name="confirmPassword" required />

      <button type="submit">Update Password</button>
    </form>
  </div>
</body>
</html>

