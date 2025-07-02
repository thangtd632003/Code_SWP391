/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import entity.Booking;
import entity.BookingStatus;

import java.sql.*;
import java.util.*;
/**
 *
 * @author thang
 */
public class BookingAdminDao {
    private final Connection conn;

    // Các cột cho phép sắp xếp
    private static final Set<String> ALLOWED_SORT_FIELDS =
        Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "id",
            "tour_id",
            "traveler_id",
            "num_people",
            "contact_info",
            "status",
            "departure_date",
            "created_at",
            "updated_at"
        )));

    public BookingAdminDao(Connection conn) {
        this.conn = conn;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getInt("id"));
        b.setTourId(rs.getInt("tour_id"));
        b.setTravelerId(rs.getInt("traveler_id"));
        b.setNumPeople(rs.getInt("num_people"));
        b.setContactInfo(rs.getString("contact_info"));
        try {
            b.setStatus(BookingStatus.valueOf(rs.getString("status").trim().toUpperCase()));
        } catch (Exception ex) {
            b.setStatus(BookingStatus.PENDING);
        }
        b.setDepartureDate(rs.getDate("departure_date"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        b.setUpdatedAt(rs.getTimestamp("updated_at"));
        return b;
    }

    /** 1. Lấy toàn bộ booking */
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToBooking(rs));
            }
        }
        return list;
    }

    /** 2. Sắp xếp theo bất kỳ cột nào trong ALLOWED_SORT_FIELDS */
    public List<Booking> sortBookingsByField(String sortField, boolean asc) throws SQLException {
        if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
            sortField = "id";
        }
        String sql = "SELECT * FROM bookings ORDER BY " + sortField + (asc ? " ASC" : " DESC");
        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToBooking(rs));
            }
        }
        return list;
    }

    /** 3. Tìm kiếm theo contact_info (có thể mở rộng thêm các trường khác) */
    public List<Booking> searchBookings(String keyword) throws SQLException {
        String sql = ""
          + "SELECT * FROM bookings "
          + "WHERE LOWER(contact_info) LIKE ?";
        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToBooking(rs));
                }
            }
        }
        return list;
    }

    /** 4. Kết hợp tìm kiếm + sắp xếp */
    public List<Booking> searchAndSortBookings(String keyword, String sortField, boolean asc) throws SQLException {
        if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
            sortField = "id";
        }
        String sql = ""
          + "SELECT * FROM bookings "
          + "WHERE LOWER(contact_info) LIKE ? "
          + "ORDER BY " + sortField + (asc ? " ASC" : " DESC");
        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToBooking(rs));
                }
            }
        }
        return list;
    }
}
