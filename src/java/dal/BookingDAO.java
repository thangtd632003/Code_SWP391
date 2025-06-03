/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.Booking;
import entity.BookingStatus;

public class BookingDAO {
    private Connection connection;

    public BookingDAO() throws Exception {
        // Dùng DBContext để lấy kết nối
        connection = new DBContext().getConnection();
    }

    // Ví dụ: Lấy danh sách booking
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE status IN ('pending','approved','cancelled')";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setTravelerId(rs.getInt("traveler_id"));
                b.setTourId(rs.getInt("tour_id"));
                b.setDepartureDate(rs.getDate("departure_date"));
                b.setNumPeople(rs.getInt("num_people"));
                b.setContactInfo(rs.getString("contact_info"));
                b.setStatus(BookingStatus.valueOf(rs.getString("status").toUpperCase()));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Booking getBookingById(int id) {
        Booking booking = null;
        String query = "SELECT * FROM bookings WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return booking;
    }

    // Thêm mới booking
    public boolean insertBooking(Booking booking) {
        String query = "INSERT INTO bookings (traveler_id, tour_id, departure_date, num_people, contact_info, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getTravelerId());
            statement.setInt(2, booking.getTourId());
            statement.setDate(3, new java.sql.Date(booking.getDepartureDate().getTime()));
            statement.setInt(4, booking.getNumPeople());
            statement.setString(5, booking.getContactInfo());
            statement.setString(6, booking.getStatus().name().toLowerCase());
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật booking
    public boolean updateBooking(Booking booking) {
        String query = "UPDATE bookings SET traveler_id=?, tour_id=?, departure_date=?, num_people=?, contact_info=?, status=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getTravelerId());
            statement.setInt(2, booking.getTourId());
            statement.setDate(3, new java.sql.Date(booking.getDepartureDate().getTime()));
            statement.setInt(4, booking.getNumPeople());
            statement.setString(5, booking.getContactInfo());
            statement.setString(6, booking.getStatus().name().toLowerCase());
            statement.setInt(7, booking.getId());
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa booking theo ID
    public boolean deleteBooking(int id) {
        String query = "DELETE FROM bookings WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}