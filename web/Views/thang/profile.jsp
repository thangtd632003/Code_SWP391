<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    entity.User user = (entity.User) request.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            color: #333;
        }
        .profile-container {
            width: 450px;
            margin: 40px auto;
            background: #fff;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            border-radius: 8px;
        }
        .profile-container h2 {
            text-align: center;
            margin-bottom: 20px;
            font-size: 24px;
            color: #444;
        }
        form label {
            display: block;
            margin: 12px 0 6px;
            font-weight: bold;
        }
        form input[type="text"],
        form input[type="email"],
        form input[type="date"] {
            width: 90%;
            padding: 8px 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }
        .inline {
            display: inline-block;
            margin-right: 15px;
            font-weight: normal;
        }
        form input[type="radio"] {
            margin-right: 4px;
        }
        .message {
            text-align: center;
            margin-bottom: 10px;
            font-size: 14px;
        }
        .text-success { color: #2a8f2a; }
        .text-error { color: #d9534f; }
        button.submit-btn {
            width: 100%;
            background-color: #5a67d8;
            color: #fff;
            border: none;
            padding: 10px;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 20px;
        }
        button.submit-btn:hover {
            background-color: #434190;
        }
        .top-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #f0f0f0;
  padding: 10px 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  z-index: 1000;
}

.top-bar a {
  text-decoration: none;
  color: #007bff;
  font-weight: bold;
  font-size: 16px;
}

.top-bar a:hover {
  color: #0056b3;
}
    </style>
</head>
<body>
<div class="profile-container">
    <h2>Personal information</h2>

    <c:if test="${param.update == 'success'}">
        <p class="message text-success">Update success!</p>
    </c:if>
    <c:if test="${param.update == 'fail'}">
        <p class="message text-error">Update fail.</p>
    </c:if>
<div class="top-bar">
  <a href="/Code_SWP391/login">← Dashboard</a>
</div>
    <form action="ProfileTravler_thang" method="post">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="${user.email}" readonly />

        <label for="fullName">FullName:</label>
        <input type="text" id="fullName" name="fullName" value="${user.fullName}"  required/>

        <label for="phone">PhoneNumber:</label>
        <input type="text" id="phone" name="phone" value="${user.phone}"  />

        <label>Gender:</label>
        <label class="inline">
            <input type="radio" name="gender" value="MALE"
                   <c:if test="${user.gender.name() == 'MALE'}">checked</c:if> /> Male
        </label>
        <label class="inline">
            <input type="radio" name="gender" value="FEMALE"
                   <c:if test="${user.gender.name() == 'FEMALE'}">checked</c:if> /> Female
        </label>
        <label class="inline">
            <input type="radio" name="gender" value="OTHER"
                   <c:if test="${user.gender.name() == 'OTHER'}">checked</c:if> /> Other
        </label>

        <label for="birthDate">Date of Birth:</label>
        <input type="date" id="birthDate" name="birthDate"
               value="${user.birthDate}" />

        <label for="roleDisplay">Role:</label>
        <span id="roleDisplay">${user.role.name()}</span>
        <input type="hidden" name="role" value="${user.role.name()}" />

        <label for="statusDisplay">Status:</label>
        <span id="statusDisplay">${user.status.name()}</span>
        <input type="hidden" name="status" value="${user.status.name()}" />

        <button type="submit" class="submit-btn">Update</button>
    </form>
</div>
</body>
</html>