<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Danh sách Booking</title>
</head>
<body>
<h2>Danh sách Booking</h2>

<table border="1" cellpadding="8">
    <tr>
        <th>ID</th>
        <th>Traveler ID</th>
        <th>Tour ID</th>
        <th>Ngày khởi hành</th>
        <th>Số người</th>
        <th>Trạng thái</th>
        <th>Chi tiết</th>
    </tr>
    <c:forEach var="b" items="${bookings}">
        <tr>
            <td>${b.id}</td>
            <td>${b.travelerId}</td>
            <td>${b.tourId}</td>
            <td>${b.departureDate}</td>
            <td>${b.numPeople}</td>
            <td>${b.status}</td>
            <td><a href="bookingDetail.jsp?id=${b.id}">Xem chi tiết</a></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>