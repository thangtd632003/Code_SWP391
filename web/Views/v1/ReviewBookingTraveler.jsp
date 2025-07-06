<%-- 
    Document   : ReviewBookingTraveler
    Created on : Jul 6, 2025, 3:09:21 PM
    Author     : thang
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Review Booking</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #f4f4f4;
      padding: 20px;
      margin: 0;
    }
    .top-bar {
      position: fixed;
      top: 0; left: 0; width: 100%;
      background: #007bff;
      color: #fff;
      padding: 10px 20px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      z-index: 100;
    }
    .top-bar a {
      color: #fff;
      text-decoration: none;
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
      color: #333;
    }
    .message {
      padding: 10px;
      margin-bottom: 15px;
      border-radius: 4px;
      font-weight: bold;
    }
    .success { background: #d4edda; color: #155724; }
    .fail    { background: #f8d7da; color: #721c24; }

    .booking-info p {
      margin: 8px 0;
      line-height: 1.5;
    }
    .booking-info strong {
      display: inline-block;
      width: 140px;
      color: #555;
    }

    .reviews {
      margin-top: 20px;
    }
    .reviews h3 {
      margin-bottom: 10px;
      color: #007bff;
    }
    .review-item {
      border-bottom: 1px solid #eee;
      padding: 10px 0;
    }
    .review-item:last-child { border-bottom: none; }
    .review-item p {
      margin: 4px 0;
    }
    .review-item .rating {
      font-weight: bold;
      color: #ff8c00;
    }

    .form-group {
      margin-bottom: 15px;
    }
    label {
      display: block;
      font-weight: bold;
      margin-bottom: 5px;
      color: #333;
    }
    select, textarea {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 4px;
      box-sizing: border-box;
    }
    select:focus, textarea:focus {
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
    <a href="${pageContext.request.contextPath}/ListBookingTraveler_servlet">← Back to Bookings</a>
  </div>

  <div class="container">
    <h2>Booking #<c:out value="${booking.id}"/> Review</h2>

    <c:if test="${param.msg == 'reviewOk'}">
      <div class="message success">Review submitted successfully!</div>
    </c:if>
    <c:if test="${param.msg == 'reviewFail'}">
      <div class="message fail">Failed to submit review. Please try again.</div>
    </c:if>

    <div class="booking-info">
      <p><strong>Departure Date:</strong> <c:out value="${booking.departureDate}"/></p>
      <p><strong>People:</strong> <c:out value="${booking.numPeople}"/></p>
      <p><strong>Status:</strong> <c:out value="${booking.status}"/></p>
    </div>

    <div class="reviews">
      <h3>Existing Reviews</h3>
      <c:choose>
        <c:when test="${not empty reviews}">
          <c:forEach var="r" items="${reviews}">
            <div class="review-item">
              <p class="rating">Rating: <c:out value="${r.rating}"/> / 5</p>
              <p><c:out value="${r.comment}"/></p>
              <p><small>On <c:out value="${r.createdAt}"/></small></p>
            </div>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <p>No reviews yet.</p>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- Only show add form if no existing reviews -->
    <c:if test="${empty reviews}">
      <form method="post" action="${pageContext.request.contextPath}/ReviewBookingTraveler_servlet">
        <input type="hidden" name="bookingId" value="${booking.id}"/>
        <div class="form-group">
          <label for="rating">Rating:</label>
          <select id="rating" name="rating" required>
            <option value="">-- Select --</option>
            <c:forEach var="i" begin="1" end="5">
              <option value="${i}">${i}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group">
          <label for="comment">Comment:</label>
          <textarea id="comment" name="comment" rows="4" required></textarea>
        </div>
        <button type="submit">Submit Review</button>
      </form>
    </c:if>

  </div>
</body>
</html>

