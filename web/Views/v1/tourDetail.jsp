<%-- 
    Document   : tourDetail_servlet
    Created on : Jun 2, 2025, 11:15:23 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Tour Detail</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f6f8;
      margin: 0;
      padding: 20px;
    }

    h2 {
      color: #333;
      margin-bottom: 20px;
    }

    .tour-detail {
      background-color: #fff;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
      max-width: 600px;
      margin: auto;
    }

    .tour-detail p {
      margin: 10px 0;
      line-height: 1.6;
    }

    .tour-detail strong {
      display: inline-block;
      width: 150px;
      color: #555;
    }

    a {
      display: inline-block;
      margin-top: 20px;
      text-decoration: none;
      color: #007BFF;
      font-weight: bold;
    }

    a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
  <div class="tour-detail">
    <h2>Tour Detail</h2>

    <c:if test="${tour != null}">
      <p><strong>ID:</strong> ${tour.id}</p>
      <p><strong>Name:</strong> ${tour.name}</p>
       <p><strong>Id Guide:</strong> ${tour.guideId}</p>
      <p><strong>Name Guide:</strong> ${guidename}</p>
      <p><strong>Email Guide:</strong> ${guideEmail}</p>
      <p><strong>Description:</strong> ${tour.description}</p>
      <p><strong>Itinerary:</strong> ${tour.itinerary}</p>
      <p><strong>Price(USD):</strong> ${tour.price}</p>
      <p><strong>Max People:</strong> ${tour.maxPeoplePerBooking}</p>
      <p><strong>Days:</strong> ${tour.days}</p>
      <p><strong>Language:</strong> ${tour.language}</p>
      <p><strong>Status:</strong> ${tour.status}</p>
      <p><strong>Created At:</strong> ${tour.createdAt}</p>
      <p><strong>Updated At:</strong> ${tour.updatedAt}</p>
    </c:if>

    <a href="listTourAdmin_servlet">← Back to List</a>
  </div>
</body>
</html>
