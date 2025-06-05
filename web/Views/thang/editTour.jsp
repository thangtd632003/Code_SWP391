<%-- 
    Document   : editTour
    Created on : May 27, 2025, 9:58:28 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entity.Tour" %>
<%
    Tour tour = (Tour) request.getAttribute("tour");
    String updateStatus = (String) request.getAttribute("updateStatus");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Edit Tour</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #f4f4f4;
      padding: 20px;
    }
    .top-bar {
      position: fixed;
      top: 0; left: 0; width: 100%;
      background: #fff;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      padding: 10px 20px;
      z-index: 100;
    }
    .top-bar a {
      text-decoration: none;
      color: #007bff;
      font-weight: bold;
    }
    .container {
      max-width: 600px;
      margin: 80px auto 0;
      background: #fff;
      padding: 20px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
      border-radius: 8px;
    }
    h2 {
      margin-top: 0;
    }
    .message {
      padding: 10px;
      margin-bottom: 15px;
      border-radius: 4px;
    }
    .success { background: #d4edda; color: #155724; }
    .fail    { background: #f8d7da; color: #721c24; }
    .form-group {
      margin-bottom: 15px;
    }
    label {
      display: block;
      font-weight: bold;
      margin-bottom: 5px;
      color: #333;
    }
    input[type="text"],
    input[type="number"],
    textarea,
    select {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 4px;
      font-size: 1rem;
      box-sizing: border-box;
    }
    input:focus,
    textarea:focus,
    select:focus {
      outline: none;
      border-color: #007bff;
      box-shadow: 0 0 5px rgba(0,123,255,0.5);
    }
    button {
      padding: 10px 20px;
      background: #007bff;
      color: #fff;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 1rem;
    }
    button:hover {
      background: #0056b3;
    }
  </style>
</head>
<body>
  <div class="top-bar">
    <a href="${pageContext.request.contextPath}/tourList_thang">← Back to Tour List</a>
  </div>

  <div class="container">
    <h2>Edit Tour #<c:out value="${tour.id}"/></h2>

    <c:if test="${updateStatus == 'success'}">
      <div class="message success">Update succes!</div>
    </c:if>
    <c:if test="${updateStatus == 'fail'}">
      <div class="message fail">Update fail! Try again</div>
    </c:if>

    <form action="editTour_thang" method="post">
      <input type="hidden" name="id" value="${tour.id}" />

      <div class="form-group">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="${tour.name}" required />
      </div>

      <div class="form-group">
        <label for="description">Description:</label>
        <textarea id="description" name="description" rows="3">${tour.description}</textarea>
      </div>

      <div class="form-group">
        <label for="itinerary">Itinerary:</label>
        <textarea id="itinerary" name="itinerary" rows="3">${tour.itinerary}</textarea>
      </div>

      <div class="form-group">
        <label for="price">Price(USD):</label>
        <input type="number" id="price" name="price" step="0.01" min="0" value="${tour.price}" required />
      </div>

      <div class="form-group">
        <label for="maxPeople">Max People Per Booking:</label>
        <input type="number" id="maxPeople" name="maxPeople" min="1" value="${tour.maxPeoplePerBooking}" required />
      </div>

      <div class="form-group">
        <label for="days">Days:</label>
        <input type="number" id="days" name="days" min="1" value="${tour.days}" required />
      </div>

      <div class="form-group">
        <label for="language">Language:</label>
        <select id="language" name="language" required>
          <c:forEach var="lang" items="${['English','Japanese','Chinese','Germany','VietNamese']}">
            <option value="${lang}" <c:if test="${lang == tour.language}">selected</c:if>>
              ${lang}
            </option>
          </c:forEach>
        </select>
      </div>

      <div class="form-group">
        <label for="status">Status:</label>
        <select id="status" name="status" required>
          <c:forEach var="s" items="${['ACTIVE','LOCKED']}">
            <option value="${s}" <c:if test="${s == tour.status.name()}">selected</c:if>>
              ${s}
            </option>
          </c:forEach>
        </select>
      </div>

      <button type="submit">Update Tour</button>
    </form>
  </div>
</body>
</html>

