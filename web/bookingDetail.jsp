<%@ page import="entity.Booking" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Booking Details</title>
</head>
<body>
    <h2>Booking Details</h2>
    <table border="1">
        <tr>
            <th>ID</th>
            <td>${booking.id}</td>
        </tr>
        <tr>
            <th>Traveler ID</th>
            <td>${booking.travelerId}</td>
        </tr>
        <tr>
            <th>Tour ID</th>
            <td>${booking.tourId}</td>
        </tr>
        <tr>
            <th>Departure Date</th>
            <td>${booking.departureDate}</td>
        </tr>
        <tr>
            <th>Number of People</th>
            <td>${booking.numPeople}</td>
        </tr>
        <tr>
            <th>Contact Info</th>
            <td>${booking.contactInfo}</td>
        </tr>
        <tr>
            <th>Status</th>
            <td>${booking.status}</td>
        </tr>
    </table>

    <c:if test="${booking.status == 'pending'}">
        <form action="bookingDetail" method="post">
            <input type="hidden" name="id" value="${booking.id}">
            <input type="submit" name="action" value="cancel" />
        </form>
    </c:if>
</body>
</html>