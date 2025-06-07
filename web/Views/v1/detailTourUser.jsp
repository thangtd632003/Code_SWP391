<%-- 
    Document   : detailTourUser_servlet
    Created on : Jun 6, 2025, 4:01:57 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Chi tiết Tour</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #e3f2fd; /* xanh nhạt */
      margin: 0; padding: 0;
    }
    .top-bar {
      background-color: #1565c0; /* xanh đậm */
      color: #fff;
      padding: 12px 20px;
    }
    .top-bar a {
      color: #fff;
      text-decoration: none;
      font-weight: bold;
    }
    .container {
      margin: 20px auto;
      max-width: 800px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
      padding: 20px;
    }
    .tour-header {
      display: flex;
      gap: 20px;
      align-items: center;
    }
    .tour-header img {
      width: 200px;
      height: 140px;
      object-fit: cover;
      border-radius: 6px;
    }
    .tour-info h2 {
      margin: 0;
      color: #1565c0;
    }
    .tour-info p {
      margin: 6px 0;
      color: #555;
    }
    .guide-box {
      margin-top: 24px;
      border-top: 1px solid #ddd;
      padding-top: 16px;
    }
    .guide-box img {
      width: 100px;
      height: 100px;
      object-fit: cover;
      border-radius: 50%;
      margin-right: 12px;
    }
    .guide-info {
      display: flex;
      align-items: center;
    }
    .guide-text h3 {
      margin: 0;
      color: #333;
    }
    .guide-text p {
      margin: 4px 0;
      color: #555;
    }
  </style>
</head>
<body>
  <div class="top-bar">
    <a href="${pageContext.request.contextPath}/listTourUser_servlet">&larr; Return List</a>
  </div>

  <div class="container">
    <!-- Thông tin Tour -->
    <div class="tour-header">
      <div class="tour-info">
        <h2><c:out value="${tour.name}" /></h2>
        <p><strong>Price(USD):</strong> <c:out value="${tour.price}" /> VND</p>
        <p><strong>Days:</strong> <c:out value="${tour.days}" /></p>
        <p><strong>Guide language used for instructions :</strong> <c:out value="${tour.language}" /></p>
        <p><strong>Description:</strong> <c:out value="${tour.description}" /></p>
                <p><strong>Itinerary:</strong> <c:out value="${tour.itinerary}" /></p>

      </div>
    </div>

    <!-- Thông tin Guide -->
    <div class="guide-box">
      <h3>Guide:</h3>
      <div class="guide-info">
        <img src="${guideProfile != null && guideProfile.avatarUrl != null 
                     ? guideProfile.avatarUrl 
                     : 'default-guide.jpg'}" 
             alt="Guide image" />
        <div class="guide-text">
          <h3><c:out value="${guideUser.fullName}" /></h3>
          <p><strong>Email:</strong> <c:out value="${guideUser.email}" /></p>
          <c:if test="${guideProfile != null && guideProfile.bio != null}">
            <p><strong>Bio:</strong> <c:out value="${guideProfile.bio}" /></p>
          </c:if>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
