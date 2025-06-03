<%@ page import="entity.Booking" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manage Bookings</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.3/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 p-6">
<div class="max-w-7xl mx-auto">
    <h1 class="text-3xl font-bold mb-6 text-center text-blue-600">Booking Management</h1>

    <div class="mb-4 text-right">
        <a href="bookingManager?action=new"
           class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded">Add New Booking</a>
    </div>

    <table class="min-w-full bg-white rounded shadow">
        <thead class="bg-blue-600 text-white">
        <tr>
            <th class="py-2 px-4 text-left">ID</th>
            <th class="py-2 px-4 text-left">Traveler ID</th>
            <th class="py-2 px-4 text-left">Tour ID</th>
            <th class="py-2 px-4 text-left">Departure Date</th>
            <th class="py-2 px-4 text-left">People</th>
            <th class="py-2 px-4 text-left">Status</th>
            <th class="py-2 px-4 text-center">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="booking" items="${bookings}">
            <tr class="border-b hover:bg-gray-100">
                <td class="py-2 px-4">${booking.id}</td>
                <td class="py-2 px-4">${booking.travelerId}</td>
                <td class="py-2 px-4">${booking.tourId}</td>
                <td class="py-2 px-4">${booking.departureDate}</td>
                <td class="py-2 px-4">${booking.numPeople}</td>
                <td class="py-2 px-4">
                    <span class="px-2 py-1 rounded-full
                        ${booking.status == 'pending' ? 'bg-yellow-300 text-black' :
                        booking.status == 'approved' ? 'bg-green-300 text-black' :
                        booking.status == 'cancelled' ? 'bg-red-300 text-black' : ''}">
                        ${booking.status}
                    </span>
                </td>
                <td class="py-2 px-4 text-center space-x-2">
                    <a href="bookingManager?action=detail&id=${booking.id}"
                       class="text-blue-600 hover:underline">View</a>
                    <a href="bookingManager?action=edit&id=${booking.id}"
                       class="text-yellow-600 hover:underline">Edit</a>
                    <a href="bookingManager?action=delete&id=${booking.id}"
                       onclick="return confirm('Are you sure you want to delete this booking?');"
                       class="text-red-600 hover:underline">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>