/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import dal.BookingDAO;
import entity.Booking;
import entity.BookingStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/bookingManager")
public class BookingManagerServlet_manh extends HttpServlet {

    private BookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        try {
            bookingDAO = new BookingDAO();
        } catch (Exception e) {
            throw new ServletException("Cannot initialize BookingDAO", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                request.getRequestDispatcher("bookingForm.jsp").forward(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteBooking(request, response);
                break;
            case "detail":
                showDetail(request, response);
                break;
            case "list":
            default:
                listBooking(request, response);
                break;
        }
    }

    private void listBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Booking> bookings = bookingDAO.getAllBookings();
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("bookingList.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Booking booking = bookingDAO.getBookingById(id);
        if (booking == null) {
            response.sendRedirect("bookingManager");
            return;
        }
        request.setAttribute("booking", booking);
        request.getRequestDispatcher("bookingForm.jsp").forward(request, response);
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Booking booking = bookingDAO.getBookingById(id);
        if (booking == null) {
            response.sendRedirect("bookingManager");
            return;
        }
        request.setAttribute("booking", booking);
        request.getRequestDispatcher("bookingDetail.jsp").forward(request, response);
    }

    private void deleteBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        bookingDAO.deleteBooking(id);
        response.sendRedirect("bookingManager");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String travelerIdStr = request.getParameter("travelerId");
        String tourIdStr = request.getParameter("tourId");
        String departureDateStr = request.getParameter("departureDate");
        String numPeopleStr = request.getParameter("numPeople");
        String contactInfo = request.getParameter("contactInfo");
        String statusStr = request.getParameter("status");

        try {
            Booking booking = new Booking();
            if (idStr != null && !idStr.isEmpty()) {
                booking.setId(Integer.parseInt(idStr));
            }
            booking.setTravelerId(Integer.parseInt(travelerIdStr));
            booking.setTourId(Integer.parseInt(tourIdStr));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date departureDate = sdf.parse(departureDateStr);
            booking.setDepartureDate(departureDate);

            booking.setNumPeople(Integer.parseInt(numPeopleStr));
            booking.setContactInfo(contactInfo);

            BookingStatus status = BookingStatus.valueOf(statusStr.toUpperCase());
            booking.setStatus(status);

            if (idStr == null || idStr.isEmpty()) {
                bookingDAO.insertBooking(booking);
            } else {
                bookingDAO.updateBooking(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("bookingManager");
    }
}