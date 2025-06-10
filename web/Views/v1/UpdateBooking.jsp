<%-- 
    Document   : UpdateBooking
    Created on : Jun 4, 2025, 2:42:41 PM
    Author     : thang
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="entity.Booking, entity.Tour" %>

<%

    String errorMsg = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Update Booking</title>
  <style>
    body { font-family: Arial, sans-serif; padding: 20px; background: #f4f4f4; }
    .container { max-width: 500px; margin: auto; background: #fff; padding: 20px; border-radius: 6px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);}
    .form-group { margin-bottom: 15px; }
    .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
    .form-group input, .form-group textarea { width: 100%; padding: 6px; box-sizing: border-box; }
    .form-group .readonly { background-color: #e9ecef; }
    .error { color: red; margin-bottom: 10px; }
    button { padding: 8px 16px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
    button:hover { background-color: #0056b3; }
    .top-bar { margin-bottom: 20px; }
    .top-bar a { text-decoration: none; color: #007bff; font-weight: bold; }
    .top-bar a:hover { color: #0056b3; }
  </style>
</head>
<body>
  <div class="top-bar">
    <a href="${pageContext.request.contextPath}/ListBookingTraveler_servlet">← Back to List Booking</a>
  </div>

  <div class="container">
    <h2>Update Booking #<c:out value="${booking.id}"/></h2>
<c:if test="${not empty errorMSG}">
  <div class="error">${errorMSG}</div>
</c:if>

    <form method="post" action="${pageContext.request.contextPath}/UpdateBookingTraveler_servlet">
      <!-- Giấu bookingId -->
      <input type="hidden" name="id" value="${booking.id}"/>

      <div class="form-group">
        <label>Tour Name:</label>
        <input type="text" readonly class="readonly" value="${booking.tourId} - ${tour.name}"/>
      </div>

      <div class="form-group">
        <label>Date of Booking:</label>
        <input type="text" readonly class="readonly"
               value="<fmt:formatDate value='${booking.createdAt}' pattern='yyyy-MM-dd HH:mm:ss'/>"/>
      </div>

      <div class="form-group">
        <label>Departure Date:</label>
        <input type="date" name="departureDate"
               readonly class="readonly"
               value="<fmt:formatDate value='${booking.departureDate}' pattern='yyyy-MM-dd'/>"
               required />
      </div>

      <div class="form-group">
        <label>Number of People (max: ${tour.maxPeoplePerBooking}):</label>
        <input type="number"
               name="numPeople"
               min="1"
               max="${tour.maxPeoplePerBooking}"
               value="${booking.numPeople}"
               required />
      </div>

      <div class="form-group">
        <label>Contact Info:</label>
        <textarea name="contactInfo" rows="3" required>${booking.contactInfo}</textarea>
      </div>

      <div class="form-group">
        <label>Status:</label>
        <input type="text" readonly class="readonly" value="${booking.status}" />
      </div>

      <button type="submit">Update Booking</button>
    </form>
  </div>
</body>
</html>
