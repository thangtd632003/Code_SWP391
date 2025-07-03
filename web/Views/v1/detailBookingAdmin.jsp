<%-- 
    Document   : detailBookingAdmin
    Created on : Jul 2, 2025, 8:46:12 PM
    Author     : thang
--%>

<%-- 
    Document   : bookingDetailTraveler
    Created on : Jun 3, 2025, 8:26:02 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Booking Detail (Traveler)</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f6f8;
      margin: 0;
      padding: 20px;
    }
    .booking-detail {
      background-color: #fff;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
      max-width: 700px;
      margin: auto;
    }
    h2 {
      color: #333;
      margin-bottom: 20px;
    }
    .booking-detail p {
      margin: 10px 0;
      line-height: 1.6;
    }
    .booking-detail strong {
      display: inline-block;
      width: 180px;
      color: #555;
    }
    a.back-link {
      display: inline-block;
      margin-top: 20px;
      text-decoration: none;
      color: #007BFF;
      font-weight: bold;
    }
    a.back-link:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
  <div class="booking-detail">
    <h2>Booking Detail</h2>

    <c:if test="${booking != null}">
      <p><strong>Booking ID:</strong> ${booking.id}</p>
      <p><strong>Departure Date:</strong> ${booking.departureDate}</p>
      <p><strong>Number of People:</strong> ${booking.numPeople}</p>
      <p><strong>Status:</strong> ${booking.status}</p>
      <p><strong>Created At:</strong> ${booking.createdAt}</p>
      <p><strong>Updated At:</strong> ${booking.updatedAt}</p>

      <hr/>

      <h3>Tour Information</h3>
      <p><strong>Tour ID:</strong> ${tour.id}</p>
         <p><strong>Name:</strong> ${booking.tourName}</p>
      <p><strong>Price (USD):</strong> ${booking.tourPrice}</p>
      <p><strong>Days:</strong> ${booking.tourDays}</p>
      <p><strong>Language:</strong> ${booking.tourLanguage}</p>
      <p><strong>Itinerary:</strong> ${booking.tourItinerary}</p>

      <hr/>

      <h3>Guide Information</h3>
      <p><strong>Guide ID:</strong> ${guide.id}</p>
      <p><strong>Name:</strong> ${guide.fullName}</p>
      <p><strong>Email:</strong> ${guide.email}</p>
     

      <hr/>

      <h3>Traveler Information</h3>
      <p><strong>Traveler ID:</strong> ${traveler.id}</p>
      <p><strong>Name:</strong> ${traveler.fullName}</p>
      <p><strong>Email:</strong> ${traveler.email}</p>
    </c:if>

    <a href="${pageContext.request.contextPath}/ListBookingAdmin_servlet" class="back-link">
      ← Back to Booking List
    </a>
  </div>
</body>
</html>
