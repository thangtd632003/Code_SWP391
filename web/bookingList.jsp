<%@ page import="entity.Booking" %>
<%@ page import="entity.BookingStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bookings List</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.3/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 font-sans antialiased">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <h2 class="text-3xl font-bold text-center text-blue-600 mb-6">List of Bookings</h2>

        <table class="min-w-full bg-white shadow-lg rounded-lg overflow-hidden">
            <thead class="bg-blue-500 text-white">
                <tr>
                    <th class="px-4 py-2 text-left">ID</th>
                    <th class="px-4 py-2 text-left">Traveler ID</th>
                    <th class="px-4 py-2 text-left">Tour ID</th>
                    <th class="px-4 py-2 text-left">Departure Date</th>
                    <th class="px-4 py-2 text-left">Number of People</th>
                    <th class="px-4 py-2 text-left">Status</th>
                    <th class="px-4 py-2 text-left">Action</th>
                </tr>
            </thead>
            <tbody class="text-gray-700">
                <c:forEach var="booking" items="${bookings}">
                    <tr class="hover:bg-gray-100 border-b">
                        <td class="px-4 py-2">${booking.id}</td>
                        <td class="px-4 py-2">${booking.travelerId}</td>
                        <td class="px-4 py-2">${booking.tourId}</td>
                        <td class="px-4 py-2">${booking.departureDate}</td>
                        <td class="px-4 py-2">${booking.numPeople}</td>
                        <td class="px-4 py-2">
                            <span class="px-3 py-1 rounded-full 
                                ${booking.status == 'pending' ? 'bg-yellow-300 text-black' : 
                                 booking.status == 'approved' ? 'bg-green-300 text-black' : 
                                 booking.status == 'cancelled' ? 'bg-red-300 text-black' : ''}">
                                ${booking.status}
                            </span>
                        </td>
                        <td class="px-4 py-2">
                            <a href="bookingDetail?id=${booking.id}" class="text-blue-500 hover:underline">View Details</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>