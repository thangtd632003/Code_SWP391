/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import entity.Booking;
import entity.BookingStatus;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.*;
/**
 *
 * @author thang
 */
public class BookingDao_thang {
     private Connection conn;
     public BookingDao_thang(Connection conn){
         this.conn = conn;
     }
 public List<Booking> getBookingsByTravelerId(int travelerId) throws Exception {
    List<Booking> bookings = new ArrayList<>();
    String sql = "SELECT * FROM bookings WHERE traveler_id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, travelerId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Booking booking = mapResultSetToBooking(rs);
            bookings.add(booking);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return bookings;
}

   public List<Booking> getBookingsByGuideId(int guideId) {
    List<Booking> bookings = new ArrayList<>();
    String sql = """
        SELECT b.* FROM bookings b
        JOIN tours t ON b.tour_id = t.id
        WHERE t.guide_id = ?
    """;
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, guideId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Booking booking = mapResultSetToBooking(rs);
            bookings.add(booking);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return bookings;
}

public Booking getBookingById(int bookingId) {
    String sql = "SELECT * FROM bookings WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, bookingId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return mapResultSetToBooking(rs);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
public boolean updateBooking(Booking booking) {
    String sql = """
        UPDATE bookings 
        SET traveler_id = ?, 
            tour_id = ?, 
            num_people = ?, 
            contact_info = ?, 
            status = ?, 
            departure_date = ?
            
        WHERE id = ?
    """;
    
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, booking.getTravelerId());
        ps.setInt(2, booking.getTourId());
        ps.setInt(3, booking.getNumPeople());
        ps.setString(4, booking.getContactInfo());
        ps.setString(5, booking.getStatus().name());
        ps.setDate(6, new java.sql.Date(booking.getDepartureDate().getTime()));
        ps.setInt(7, booking.getId());

        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public boolean updateBookingStatus(int bookingId, BookingStatus newStatus) {
    String sql = "UPDATE bookings SET status = ? WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, newStatus.name());
        ps.setInt(2, bookingId);
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
    Booking booking = new Booking();
    booking.setId(rs.getInt("id"));
    booking.setTravelerId(rs.getInt("traveler_id"));
    booking.setTourId(rs.getInt("tour_id"));
    booking.setNumPeople(rs.getInt("num_people"));
    booking.setContactInfo(rs.getString("contact_info"));
   try {
    booking.setStatus(BookingStatus.valueOf(rs.getString("status").trim().toUpperCase()));
} catch (IllegalArgumentException | NullPointerException e) {
    // fallback hoặc log lỗi
    booking.setStatus(BookingStatus.PENDING); // hoặc null hoặc throw
}
    booking.setCreatedAt(rs.getTimestamp("created_at"));
    booking.setUpdatedAt(rs.getTimestamp("updated_at"));
    booking.setDepartureDate(rs.getDate("departure_date"));
    return booking;
}
public List<Booking> searchBookings(String keyword) {
    List<Booking> bookings = new ArrayList<>();
    String sql = "SELECT * FROM Booking WHERE contact_info LIKE ?";
    
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + keyword + "%");
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setTravelerId(rs.getInt("traveler_id"));
                // Lưu ý: có thể tên cột là tour_id hoặc schedule_id tùy DB
                booking.setTourId(rs.getInt("schedule_id")); 
                booking.setNumPeople(rs.getInt("num_people"));
                booking.setContactInfo(rs.getString("contact_info"));
                booking.setStatus(BookingStatus.valueOf(rs.getString("status").toUpperCase()));
                booking.setCreatedAt(rs.getTimestamp("created_at"));
                booking.setUpdatedAt(rs.getTimestamp("updated_at"));
                booking.setDepartureDate(rs.getDate("departure_date"));
                // ... thêm các trường khác nếu có
                bookings.add(booking);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return bookings;
}
public List<Booking> sortBookings(String sortBy, boolean sortAsc) {
    List<Booking> bookings = new ArrayList<>();

    // Mapping sortBy từ input Java sang cột trong CSDL
    String column;
    switch (sortBy.toLowerCase()) {
        case "tourid":
            // Lưu ý: nếu DB thực sự đặt tên cột là tour_id
            column = "schedule_id"; 
            break;
        case "numpeople":
            column = "num_people";
            break;
        case "status":
            column = "status";
            break;
        default:
            // Mặc định sắp xếp theo updated_at
            column = "updated_at";
    }

    String sql = "SELECT * FROM Booking ORDER BY " + column + (sortAsc ? " ASC" : " DESC");

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setTravelerId(rs.getInt("traveler_id"));
                booking.setTourId(rs.getInt("schedule_id"));
                booking.setNumPeople(rs.getInt("num_people"));
                booking.setContactInfo(rs.getString("contact_info"));
                booking.setStatus(BookingStatus.valueOf(rs.getString("status").toUpperCase()));
                booking.setCreatedAt(rs.getTimestamp("created_at"));
                booking.setUpdatedAt(rs.getTimestamp("updated_at"));
                booking.setDepartureDate(rs.getDate("departure_date"));
                // ... thêm các trường khác nếu có
                bookings.add(booking);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return bookings;
}

}
