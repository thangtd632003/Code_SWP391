<%@ page import="entity.Booking" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${booking != null ? "Edit Booking" : "New Booking"}</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.3/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 p-6">
<div class="max-w-xl mx-auto bg-white p-6 rounded shadow">
    <h2 class="text-2xl font-bold mb-4 text-center">${booking != null ? "Edit Booking" : "New Booking"}</h2>

    <form action="bookingManager" method="post" class="space-y-4">
        <input type="hidden" name="id" value="${booking != null ? booking.id : ''}"/>

        <div>
            <label for="travelerId" class="block mb-1 font-semibold">Traveler ID</label>
            <input type="number" id="travelerId" name="travelerId" required
                   value="${booking != null ? booking.travelerId : ''}"
                   class="w-full border border-gray-300 rounded px-3 py-2"/>
        </div>

        <div>
            <label for="tourId" class="block mb-1 font-semibold">Tour ID</label>
            <input type="number" id="tourId" name="tourId" required
                   value="${booking != null ? booking.tourId : ''}"
                   class="w-full border border-gray-300 rounded px-3 py-2"/>
        </div>

        <div>
            <label for="departureDate" class="block mb-1 font-semibold">Departure Date</label>
            <input type="date" id="departureDate" name="departureDate" required
                  value="${booking.departureDate != null ? booking.departureDate : ''}"

                   class="w-full border border-gray-300 rounded px-3 py-2"/>
        </div>

        <div>
            <label for="numPeople" class="block mb-1 font-semibold">Number of People</label>
            <input type="number" id="numPeople" name="numPeople" required
                   value="${booking != null ? booking.numPeople : ''}"
                   class="w-full border border-gray-300 rounded px-3 py-2"/>
        </div>

        <div>
            <label for="contactInfo" class="block mb-1 font-semibold">Contact Info</label>
            <input type="text" id="contactInfo" name="contactInfo" required
                   value="${booking != null ? booking.contactInfo : ''}"
                   class="w-full border border-gray-300 rounded px-3 py-2"/>
        </div>

        <div>
            <label for="status" class="block mb-1 font-semibold">Status</label>
            <select id="status" name="status" required class="w-full border border-gray-300 rounded px-3 py-2">
                <option value="pending" ${booking != null && booking.status.name().equalsIgnoreCase("pending") ? "selected" : ""}>Pending</option>
                <option value="approved" ${booking != null && booking.status.name().equalsIgnoreCase("approved") ? "selected" : ""}>Approved</option>
                <option value="cancelled" ${booking != null && booking.status.name().equalsIgnoreCase("cancelled") ? "selected" : ""}>Cancelled</option>
            </select>
        </div>

        <div class="text-center">
            <button type="submit" class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700">
                ${booking != null ? "Update" : "Create"}
            </button>
        </div>
    </form>
</div>
</body>
</html>