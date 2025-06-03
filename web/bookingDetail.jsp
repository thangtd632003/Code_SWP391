<%@ page import="entity.Booking" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Booking Detail</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.3/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 font-sans antialiased">

    <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <h2 class="text-3xl font-bold text-center text-blue-600 mb-6">Booking Details</h2>

        <div class="bg-white shadow-lg rounded-lg p-6">
            <table class="min-w-full bg-white">
                <tbody>
                    <tr>
                        <th class="text-left px-4 py-2">Booking ID</th>
                        <td class="px-4 py-2">${booking.id}</td>
                    </tr>
                    <tr>
                        <th class="text-left px-4 py-2">Traveler ID</th>
                        <td class="px-4 py-2">${booking.travelerId}</td>
                    </tr>
                    <tr>
                        <th class="text-left px-4 py-2">Tour ID</th>
                        <td class="px-4 py-2">${booking.tourId}</td>
                    </tr>
                    <tr>
                        <th class="text-left px-4 py-2">Departure Date</th>
                        <td class="px-4 py-2">${booking.departureDate}</td>
                    </tr>
                    <tr>
                        <th class="text-left px-4 py-2">Number of People</th>
                        <td class="px-4 py-2">${booking.numPeople}</td>
                    </tr>
                    <tr>
                        <th class="text-left px-4 py-2">Contact Info</th>
                        <td class="px-4 py-2">${booking.contactInfo}</td>
                    </tr>
                    <tr>
                        <th class="text-left px-4 py-2">Status</th>
                        <td class="px-4 py-2">
                            <span class="px-3 py-1 rounded-full 
                                ${booking.status == 'pending' ? 'bg-yellow-300 text-black' : 
                                 booking.status == 'approved' ? 'bg-green-300 text-black' : 
                                 booking.status == 'cancelled' ? 'bg-red-300 text-black' : ''}">
                                ${booking.status}
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>

            <c:if test="${booking.status == 'pending'}">
                <form action="bookingDetail" method="post" class="mt-4">
                    <input type="hidden" name="id" value="${booking.id}">
                    <button type="submit" name="action" value="cancel" class="bg-red-500 text-white px-4 py-2 rounded-full hover:bg-red-600">
                        Cancel Booking
                    </button>
                </form>
            </c:if>
        </div>
    </div>

</body>
</html>