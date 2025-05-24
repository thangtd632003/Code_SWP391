<%-- 
    Document   : createTour
    Created on : May 24, 2025, 8:29:41 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Create New Tour</title>
  <style>
    body { font-family: Arial; padding:20px; background:#f4f4f4; }
    .form-box { background:#fff; padding:20px; max-width:500px; margin:0 auto; box-shadow:0 2px 8px rgba(0,0,0,0.1); }
    label { display:block; margin:12px 0 4px; font-weight:bold; }
    input, textarea { width:100%; padding:8px; border:1px solid #ccc; border-radius:4px; }
    button { margin-top:16px; padding:10px 16px; background:#28a745; color:#fff; border:none; border-radius:4px; cursor:pointer; }
    .message { text-align:center; margin-bottom:12px; padding:8px; }
    .success { color:#155724; background:#d4edda; }
    .fail    { color:#721c24; background:#f8d7da; }
  </style>
</head>
<body>
  <div class="form-box">
    <h2>Create New Tour</h2>

    <c:if test="${param.create == 'success'}">
      <div class="message success">Tour created successfully!</div>
    </c:if>
    <c:if test="${param.create == 'fail'}">
      <div class="message fail">Failed to create tour. Please check your input.</div>
    </c:if>

    <form action="createTour_thang" method="post">
      <label for="name">Name:</label>
      <input type="text" id="name" name="name" required />

      <label for="description">Description:</label>
      <textarea id="description" name="description" rows="4"></textarea>

      <label for="itinerary">Itinerary:</label>
      <textarea id="itinerary" name="itinerary" rows="4"></textarea>

      <label for="price">Price:</label>
      <input type="number" id="price" name="price" step="0.01" required />

      <label for="maxPeople">Max People Per Booking:</label>
      <input type="number" id="maxPeople" name="maxPeople" required />

      <button type="submit">Create Tour</button>
    </form>
  </div>
</body>
</html>

