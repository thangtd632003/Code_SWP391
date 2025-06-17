<%-- 
    Document   : bookingDetailGuide
    Created on : Jun 3, 2025, 8:27:56 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Booking Detail (Guide)</title>
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

    .booking-detail {
      background-color: #fff;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
      max-width: 600px;
      margin: auto;
    }

    .booking-detail p {
      margin: 10px 0;
      line-height: 1.6;
    }

    .booking-detail strong {
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
  <div class="booking-detail">
    <h2>Booking Detail (Guide)</h2>

    <c:if test="${booking != null}">
      <p><strong>ID:</strong> ${booking.id}</p>
       <p><strong>Traveler Name:</strong> ${travelerName}</p>
      <p><strong>Tour:</strong> ${tour.name}</p>
        <p><strong>Days of tour:</strong> ${booking.tourDays}</p>
      <p><strong>Price(USD):</strong>${booking.tourPrice}</p>
       <p><strong>Language used for instruction :</strong>${booking.tourLanguage}</p>
        <p><strong>Itinerary :</strong>${booking.tourItinerary}</p>
      <p><strong>Date:</strong> ${booking.departureDate}</p>
      <p><strong>People:</strong> ${booking.numPeople}</p>
      <p><strong>Status:</strong> ${booking.status}</p>
      <p><strong>Created At:</strong> ${booking.createdAt}</p>
      <p><strong>Updated At:</strong> ${booking.updatedAt}</p>
    </c:if>

    <a href="/Code_SWP391/ListBookingGuide_servlet">← Back to List</a>
  </div>
</body>
</html>

