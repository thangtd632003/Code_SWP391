<%-- 
    Document   : guideProfile
    Created on : May 21, 2025, 8:17:38 AM
    Author     : thang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entity.GuideProfile" %>
<%
    GuideProfile guide = (GuideProfile) request.getAttribute("guideProfile");
    boolean hasGuide = (guide != null);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Guide Profile</title>
    <style>
      /* Reset cơ bản */
      * { box-sizing: border-box; margin: 0; padding: 0; }

      body {
        font-family: Arial, sans-serif;
        background: #f4f7fa;
        color: #333;
        padding: 20px;
      }

      h2 {
        text-align: center;
        margin-bottom: 20px;
        color: #2c3e50;
      }

      .form-container {
        background: #fff;
        max-width: 500px;
        margin: 0 auto;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
      }

      .form-container label {
        display: block;
        margin-bottom: 6px;
        font-weight: bold;
      }

      .form-container input[type="text"],
      .form-container textarea,
      .form-container input[type="file"] {
        width: 100%;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ccd0d5;
        border-radius: 4px;
        font-size: 14px;
      }

      .form-container textarea {
        resize: vertical;
        min-height: 80px;
      }

      .avatar-preview {
        display: block;
        margin: 10px auto 20px;
        border-radius: 50%;
        border: 2px solid #ddd;
      }

      .form-container button {
        width: 100%;
        background: #3498db;
        color: #fff;
        padding: 12px;
        font-size: 16px;
        font-weight: bold;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        transition: background .3s;
      }

      .form-container button:hover {
        background: #2980b9;
      }

      .message {
        max-width: 500px;
        margin: 0 auto 20px;
        padding: 12px;
        text-align: center;
        border-radius: 4px;
      }
      .message.success { background: #e8f5e9; color: #2e7d32; }
      .message.fail    { background: #ffebee; color: #c62828; }
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
<div class="top-bar">
  <a href="/Code_SWP391/login">← Dashboard</a>
</div>
  <c:if test="${not empty param.update}">
    <div class="message ${param.update == 'success' ? 'success' : 'fail'}">
      <c:choose>
        <c:when test="${param.update == 'success'}">
          Profile updated successfully!
        </c:when>
        <c:otherwise>
          Failed to update profile. Please try again.
        </c:otherwise>
      </c:choose>
    </div>
  </c:if>

  <div class="form-container">
    <h2>Guide Profile Information</h2>
    <form action="guideProfile_thang" method="post" enctype="multipart/form-data">
      <label for="guideId">Guide ID:</label>
      <input type="text" name="guideId"
             value="<%= hasGuide ? guide.getGuideId() : "" %>"
             readonly />

      <label for="bio">Biography:</label>
      <textarea name="bio" rows="5"><%= hasGuide && guide.getBio() != null
                                     ? guide.getBio() : "" %></textarea>

      <label>Current Avatar:</label>
      <img class="avatar-preview" width="120" height="120"
           src="<%= hasGuide && guide.getAvatarUrl() != null
                  ? guide.getAvatarUrl() : "default.jpg" %>"
           alt="Avatar" />

      <label for="avatar">Update Avatar:</label>
      <input type="file" name="avatar" accept="image/*" />

      <button type="submit">Update Profile</button>
    </form>
  </div>

</body>
</html>
