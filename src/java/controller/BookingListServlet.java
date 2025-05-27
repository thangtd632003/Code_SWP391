/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dal.DBContext;  // import class kết nối của bạn
import entity.Booking;
import entity.BookingStatus;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/bookings")
public class BookingListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT b.id, b.traveler_id, b.tour_id, b.departure_date, b.num_people, b.status, b.contact_info, " +
                     "b.created_at, b.updated_at " +
                     "FROM bookings b " +
                     "WHERE b.status IN ('pending', 'approved', 'cancelled')";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setTravelerId(rs.getInt("traveler_id"));
                booking.setTourId(rs.getInt("tour_id"));
                booking.setDepartureDate(rs.getDate("departure_date"));
                booking.setNumPeople(rs.getInt("num_people"));
                booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
                booking.setContactInfo(rs.getString("contact_info"));
                booking.setCreatedAt(rs.getTimestamp("created_at"));
                booking.setUpdatedAt(rs.getTimestamp("updated_at"));
                bookings.add(booking);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }

        // Đưa dữ liệu lên request để JSP dùng hiển thị
        request.setAttribute("bookings", bookings);

        // Chuyển tiếp đến trang JSP hiển thị danh sách
        RequestDispatcher dispatcher = request.getRequestDispatcher("booking_list.jsp");
        dispatcher.forward(request, response);
    }
}
