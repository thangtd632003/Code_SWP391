<%@ page import="entity.Booking" %>
<%@ page import="entity.BookingStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bookings List</title>
</head>
<body>
    <h2>List of Bookings</h2>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Traveler ID</th>
                <th>Tour ID</th>
                <th>Departure Date</th>
                <th>Number of People</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="booking" items="${bookings}">
                <tr>
                    <td>${booking.id}</td>
                    <td>${booking.travelerId}</td>
                    <td>${booking.tourId}</td>
                    <td>${booking.departureDate}</td>
                    <td>${booking.numPeople}</td>
                    <td>${booking.status}</td>
                    <td><a href="bookingDetail?id=${booking.id}">View Details</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>