<%-- 
    Document   : userDetail.jsp
    Created on : Jun 5, 2025, 3:21:10 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entity.User" %>

<%
    User u = (User) request.getAttribute("userDetail");
    if (u == null) {
        // Redirect nếu lỡ trang này được gọi mà userDetail không tồn tại
        response.sendRedirect(request.getContextPath() + "/userList_thang");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Detail User</title>
  <style>
    body { font-family: Arial, sans-serif; padding: 20px; background: #f4f4f4; }
    .container { background: #fff; padding: 20px; border-radius: 6px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); max-width: 500px; margin: 40px auto; }
    .container h2 { margin-bottom: 20px; }
    .field { margin-bottom: 12px; }
    .field label { font-weight: bold; }
    .back { margin-top: 20px; display: inline-block; }
  </style>
</head>
<body>
  <div class="container">
    <h2>Detail User</h2>
    <div class="field">
      <label>ID:</label> <span>${userDetail.id}</span>
    </div>
    <div class="field">
      <label>Full Name:</label> <span>${userDetail.fullName}</span>
    </div>
    <div class="field">
      <label>Email:</label> <span>${userDetail.email}</span>
    </div>
    <div class="field">
      <label>Phone:</label> <span>${userDetail.phone}</span>
    </div>
    <div class="field">
      <label>Status:</label> <span>${userDetail.status}</span>
    </div>
    <div class="field">
      <label>Created At:</label> <span>${userDetail.createdAt}</span>
    </div>
    <div class="field">
      <label>Updated At:</label> <span>${userDetail.updatedAt}</span>
    </div>

    <a href="${pageContext.request.contextPath}/userList_thang" class="back">← Back to User List</a>
  </div>
</body>
</html>
