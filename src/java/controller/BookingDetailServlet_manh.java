/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author MSI_No1
 */
import dal.DBContext;
import entity.Booking;
import entity.BookingStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/bookingDetail")
public class BookingDetailServlet_manh extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int bookingId = Integer.parseInt(request.getParameter("id"));
        Booking booking = null;
        try {
            booking = getBookingById(bookingId);
        } catch (Exception ex) {
            Logger.getLogger(BookingDetailServlet_manh.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (booking == null) {
            response.sendRedirect("bookingList");
            return;
        }

        request.setAttribute("booking", booking);
        request.getRequestDispatcher("bookingDetail.jsp").forward(request, response);
    }

    private Booking getBookingById(int id) throws Exception {
        Booking booking = null;
        String query = "SELECT * FROM bookings WHERE id = ?";

        try (Connection connection = new DBContext().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setTravelerId(rs.getInt("traveler_id"));
                booking.setTourId(rs.getInt("tour_id"));
                booking.setDepartureDate(rs.getDate("departure_date"));
                booking.setNumPeople(rs.getInt("num_people"));
                booking.setContactInfo(rs.getString("contact_info"));
                booking.setStatus(BookingStatus.valueOf(rs.getString("status").toUpperCase()));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return booking;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int bookingId = Integer.parseInt(request.getParameter("id"));
        String action = request.getParameter("action");

        if ("cancel".equals(action)) {
            try {
                cancelBooking(bookingId);
            } catch (Exception ex) {
                Logger.getLogger(BookingDetailServlet_manh.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("bookingList");
        }
    }

    private void cancelBooking(int bookingId) throws Exception {
        String query = "UPDATE bookings SET status = 'cancelled' WHERE id = ?";

        try (Connection connection = new DBContext().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookingId);
            statement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}