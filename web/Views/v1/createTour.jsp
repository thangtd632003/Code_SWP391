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
            label {
    display: block;
    font-weight: bold;
    margin-bottom: 0.5rem;
    color: #333;
  }

  select {
    width: 100%;
    padding: 0.6rem;
    border: 1px solid #ccc;
    border-radius: 8px;
    font-size: 1rem;
    background-color: #fdfdfd;
    transition: border-color 0.3s ease;
  }

  select:focus {
    border-color: #007bff;
    outline: none;
    box-shadow: 0 0 5px rgba(0,123,255,0.5);
  }

  option {
    padding: 0.5rem;
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
    <div class="top-bar">
  <a href="/Code_SWP391/tourList_servlet">← Tour List</a>
</div>
  <div class="form-box">
    <h2>Create New Tour</h2>

    <c:if test="${param.create == 'success'}">
      <div class="message success">Tour created successfully!</div>
    </c:if>
    <c:if test="${param.create == 'fail'}">
      <div class="message fail">Failed to create tour. Please check your input.</div>
    </c:if>

 <form action="createTour_servlet" method="post">
  <label for="name">Name:</label>
  <input type="text" id="name" name="name" required />

  <label for="description">Description:</label>
  <textarea id="description" name="description" rows="4"></textarea>

  <label for="itinerary">Itinerary:</label>
  <textarea id="itinerary" name="itinerary" rows="4"></textarea>

  <label for="price">Price(USD):</label>
  <input type="number" id="price" name="price" step="0.01" min="0" required />

  <label for="maxPeople">Max People Per Booking:</label>
  <input type="number" id="maxPeople" name="maxPeople" min="1" required />

  <label for="days">Days:</label>
  <input type="number" id="days" name="days" min="1" required />

  <label for="language">Language:</label>
  <select id="language" name="language" required>
    <option value="English">English</option>
    <option value="Japanese">Japanese</option>
    <option value="Chinese">Chinese</option>
    <option value="Germany">Germany</option>
    <option value="VietNamese">VietNamese</option>
  </select>

  <button type="submit">Create Tour</button>
</form>
  </div>
</body>
</html>

