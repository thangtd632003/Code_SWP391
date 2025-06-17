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
public class BookingDao {
     private Connection conn;
     public BookingDao(Connection conn){
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
       booking.setTourName(rs.getString("tour_name"));
    booking.setTourPrice(rs.getBigDecimal("tour_price"));
    booking.setTourDays(rs.getInt("tour_days"));
    booking.setTourLanguage(rs.getString("tour_language"));
    booking.setTourItinerary(rs.getString("tour_itinerary"));
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

    // ===== 5. Sort bookings của traveler với column truyền vào =====
    public List<Booking> sortBookingsByTravelerId(
            int travelerId,
            String sortBy,
            boolean sortAsc
    ) throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        // 1. Map sortBy sang tên cột trong DB
        String column;
        if (sortBy == null) {
            column = "updated_at";
        } else {
            switch (sortBy.toLowerCase()) {
                case "id":
                    column = "id";
                    break;
                case "tourid":
                case "tour_id":
                    column = "tour_id";
                    break;
                case "travelerid":
                case "traveler_id":
                    column = "traveler_id";
                    break;
                case "numpeople":
                case "num_people":
                    column = "num_people";
                    break;
                case "contact":
                case "contact_info":
                    column = "contact_info";
                    break;
                case "status":
                    column = "status";
                    break;
                case "departure":
                case "departure_date":
                    column = "departure_date";
                    break;
                case "created_at":
                    column = "created_at";
                    break;
                case "updated_at":
                    column = "updated_at";
                    break;
                default:
                    column = "updated_at";
            }
        }

        // 2. Xây dựng SQL với ORDER BY động
        String sql = "SELECT * FROM bookings " +
                     "WHERE traveler_id = ? " +
                     "ORDER BY " + column + (sortAsc ? " ASC" : " DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, travelerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }

        return bookings;
    }

    // ===== 6. Sort bookings của guide với column truyền vào =====
    public List<Booking> sortBookingsByGuideId(
            int guideId,
            String sortBy,
            boolean sortAsc
    ) throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        // 1. Map sortBy sang tên cột trong DB (giống hệt bên trên)
        String column;
        if (sortBy == null) {
            column = "updated_at";
        } else {
            switch (sortBy.toLowerCase()) {
                case "id":
                    column = "id";
                    break;
                case "tourid":
                case "tour_id":
                    column = "tour_id";
                    break;
                case "travelerid":
                case "traveler_id":
                    column = "traveler_id";
                    break;
                case "numpeople":
                case "num_people":
                    column = "num_people";
                    break;
                case "contact":
                case "contact_info":
                    column = "contact_info";
                    break;
                case "status":
                    column = "status";
                    break;
                case "departure":
                case "departure_date":
                    column = "departure_date";
                    break;
                case "created_at":
                    column = "created_at";
                    break;
                case "updated_at":
                    column = "updated_at";
                    break;
                default:
                    column = "updated_at";
            }
        }

        // 2. Xây dựng SQL với JOIN sang tours để lọc theo guide_id, kèm ORDER BY
        String sql = """
            SELECT b.* 
            FROM bookings b
            JOIN tours t ON b.tour_id = t.id
            WHERE t.guide_id = ?
            ORDER BY b.""" + column + (sortAsc ? " ASC" : " DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, guideId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }

        return bookings;
    }

    // ===== 7. Vừa search vừa sort cho traveler với column truyền vào =====
    public List<Booking> searchAndSortBookingsByTravelerId(
            int travelerId,
            String keyword,
            String sortBy,
            boolean sortAsc
    ) throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        // 1. Map sortBy sang tên cột trong DB
        String column;
        if (sortBy == null) {
            column = "updated_at";
        } else {
            switch (sortBy.toLowerCase()) {
                case "id":
                    column = "id";
                    break;
                case "tourid":
                case "tour_id":
                    column = "tour_id";
                    break;
                case "travelerid":
                case "traveler_id":
                    column = "traveler_id";
                    break;
                case "numpeople":
                case "num_people":
                    column = "num_people";
                    break;
                case "contact":
                case "contact_info":
                    column = "contact_info";
                    break;
                case "status":
                    column = "status";
                    break;
                case "departure":
                case "departure_date":
                    column = "departure_date";
                    break;
                case "created_at":
                    column = "created_at";
                    break;
                case "updated_at":
                    column = "updated_at";
                    break;
                default:
                    column = "updated_at";
            }
        }

        // 2. Xây dựng SQL với search và ORDER BY
        String sql = "SELECT * FROM bookings " +
                     "WHERE traveler_id = ? AND contact_info LIKE ? " +
                     "ORDER BY " + column + (sortAsc ? " ASC" : " DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, travelerId);
            ps.setString(2, "%" + keyword.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }

        return bookings;
    }

    // ===== 8. Vừa search vừa sort cho guide với column truyền vào =====
    public List<Booking> searchAndSortBookingsByGuideId(
            int guideId,
            String keyword,
            String sortBy,
            boolean sortAsc
    ) throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        // 1. Map sortBy sang tên cột trong DB
        String column;
        if (sortBy == null) {
            column = "updated_at";
        } else {
            switch (sortBy.toLowerCase()) {
                case "id":
                    column = "id";
                    break;
                case "tourid":
                case "tour_id":
                    column = "tour_id";
                    break;
                case "travelerid":
                case "traveler_id":
                    column = "traveler_id";
                    break;
                case "numpeople":
                case "num_people":
                    column = "num_people";
                    break;
                case "contact":
                case "contact_info":
                    column = "contact_info";
                    break;
                case "status":
                    column = "status";
                    break;
                case "departure":
                case "departure_date":
                    column = "departure_date";
                    break;
                case "created_at":
                    column = "created_at";
                    break;
                case "updated_at":
                    column = "updated_at";
                    break;
                default:
                    column = "updated_at";
            }
        }

        // 2. Xây dựng SQL với JOIN sang tours, search và ORDER BY
        String sql = """
            SELECT b.* 
            FROM bookings b
            JOIN tours t ON b.tour_id = t.id
            WHERE t.guide_id = ? AND b.contact_info LIKE ?
            ORDER BY b.""" + column + (sortAsc ? " ASC" : " DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, guideId);
            ps.setString(2, "%" + keyword.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }

        return bookings;
    }

    public List<Booking> searchBookingsByTravelerId(int travelerId, String keyword) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE traveler_id = ? AND contact_info LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, travelerId);
            ps.setString(2, "%" + keyword.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }
        return bookings;
    }

    public List<Booking> searchBookingsByGuideId(int guideId, String keyword) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.*
              FROM bookings b
              JOIN tours t ON b.tour_id = t.id
             WHERE t.guide_id = ? AND b.contact_info LIKE ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, guideId);
            ps.setString(2, "%" + keyword.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
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

 /**
     * 1. Kiểm tra xem user này đã có 1 booking trùng hoàn toàn cùng ngày departure_date hay chưa.
     *    Trả về true nếu có xung đột (tức đã tồn tại booking khác của user với cùng ngày).
     */
    public boolean isSameDateConflictForUser(int travelerId, java.util.Date departureDate) throws SQLException {
       String sql = "SELECT COUNT(*) FROM bookings " +
             "WHERE traveler_id = ? " +
             "AND departure_date = ? " +
             "AND status NOT IN ('cancelled', 'rejected')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, travelerId);
            // Chuyển java.util.Date sang java.sql.Date
            ps.setDate(2, new java.sql.Date(departureDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * 2. Kiểm tra xung đột khoảng thời gian giữa booking mới và booking cũ của cùng user.
     *    Khoảng thời gian của booking được tính là:
     *      [departure_date, departure_date + days_of_tour]
     *    Hai khoảng thời gian (newStart, newEnd) và (existStart, existEnd) xung đột nếu:
     *      newStart < existEnd AND existStart < newEnd
     *
     *    newEnd = DATE_ADD(newStart, INTERVAL newDays DAY)
     *    existEnd = DATE_ADD(b.departure_date, INTERVAL t.days DAY)
     *
     *    Trả về true nếu có booking cũ nào của travelerId overlap khoảng newStart..newEnd.
     *
     * @param travelerId   ID của traveler
     * @param newDeparture Ngày mới (java.util.Date) muốn book
     * @param newDays      Số ngày của tour mới
     * @return true nếu có xung đột, false nếu không
     */
    public boolean isPeriodConflictForUser(int travelerId, java.util.Date newDeparture, int newDays) throws SQLException {
        String sql = ""
            + "SELECT COUNT(*) "
            + "FROM bookings b "
            + "  JOIN tours t ON b.tour_id = t.id "
            + "WHERE b.traveler_id = ? "
                  + "  AND b.status NOT IN ('cancelled', 'rejected') "
            + "  AND NOT ( "
            + "         DATE_ADD(b.departure_date, INTERVAL t.days DAY) <= ? "
            + "      OR  b.departure_date >= DATE_ADD(?, INTERVAL ? DAY) "
            + "      )";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // 1: travelerId
            ps.setInt(1, travelerId);
            // 2: newDeparture (áp dụng cho existEnd <= newStart)
            ps.setDate(2, new java.sql.Date(newDeparture.getTime()));
            // 3: newDeparture (áp dụng cho existStart >= newEnd)
            ps.setDate(3, new java.sql.Date(newDeparture.getTime()));
            // 4: newDays
            ps.setInt(4, newDays);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * 3. Kiểm tra xung đột khoảng thời gian booking mới với các booking đã "APPROVED" của guide,
     *    dù booking đó là của bất kỳ user nào.
     *
     *    Giả sử "APPROVED" mapping sang giá trị BookingStatus.APPROVED.name().
     *    Logic điều kiện xung đột tương tự trên, nhưng chỉ lấy những booking
     *    mà tour thuộc về guideId và status = 'APPROVED'.
     *
     * @param guideId      ID của guide
     * @param newDeparture Ngày mới (java.util.Date) muốn book
     * @param newDays      Số ngày của tour mới
     * @return true nếu có xung đột với booking APPROVED của guide, false nếu không
     */
    public boolean isPeriodConflictForGuide(int guideId, java.util.Date newDeparture, int newDays) throws SQLException {
        String sql = ""
            + "SELECT COUNT(*) "
            + "FROM bookings b "
            + "  JOIN tours t ON b.tour_id = t.id "
            + "WHERE t.guide_id = ? "
            + "  AND b.status = ? "
            + "  AND NOT ( "
            + "        DATE_ADD(b.departure_date, INTERVAL t.days DAY) <= ? "
            + "     OR b.departure_date >= DATE_ADD(?, INTERVAL ? DAY) "
            + "      )";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // 1: guideId
            ps.setInt(1, guideId);
            // 2: status "APPROVED"
            ps.setString(2, BookingStatus.APPROVED.name());
            // 3: newDeparture (áp dụng cho existEnd <= newStart)
            ps.setDate(3, new java.sql.Date(newDeparture.getTime()));
            // 4: newDeparture (áp dụng cho existStart >= newEnd)
            ps.setDate(4, new java.sql.Date(newDeparture.getTime()));
            // 5: newDays
            ps.setInt(5, newDays);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
 public boolean addBooking(Booking booking) throws SQLException {
String sql = "INSERT INTO bookings "
           + "(traveler_id, tour_id, num_people, contact_info, status, departure_date, "
           + "tour_name, tour_price, tour_days, tour_language, tour_itinerary, "
           + "created_at, updated_at) "
           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
    
    try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, booking.getTravelerId());
        ps.setInt(2, booking.getTourId());
        ps.setInt(3, booking.getNumPeople());
        ps.setString(4, booking.getContactInfo());
        ps.setString(5, booking.getStatus().name());
        ps.setDate(6, new java.sql.Date(booking.getDepartureDate().getTime()));

        // Các trường snapshot
        ps.setString(7, booking.getTourName());
        ps.setBigDecimal(8, booking.getTourPrice());
        ps.setInt(9, booking.getTourDays());
        ps.setString(10, booking.getTourLanguage());
ps.setString(11, booking.getTourItinerary());
        int affected = ps.executeUpdate();
        if (affected > 0) {
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    booking.setId(rs.getInt(1));
                }
            }
            return true;
        }
        return false;
    }
    }
   public boolean isDepartureInPast(int bookingId) throws SQLException {
        String sql = "SELECT departure_date FROM bookings WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.sql.Date dep = rs.getDate("departure_date");
                    // So sánh với ngày hôm nay (loại bỏ phần giờ)
                    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                    return dep.before(today);
                }
            }
        }
        return true; // Nếu không tìm thấy booking, coi như "không thể approve"
    }

    /**
     * 2. Kiểm tra xung đột với các booking đã được "APPROVED" khác của cùng guide.
     *    Trả về true nếu có ít nhất một booking APPROVED khác overlap khoảng thời gian.
     *
     *    bookingId: ID của booking hiện tại (đang muốn approve) → để loại trừ chính nó
     *    newDeparture, newDays: khoảng thời gian của booking này
     */
    public boolean hasApprovedConflict(int bookingId, java.util.Date newDeparture, int newDays) throws SQLException {
        String sql = ""
            + "SELECT COUNT(*) "
            + "FROM bookings b "
            + "  JOIN tours t ON b.tour_id = t.id "
            + "WHERE b.id <> ? "
            + "  AND t.guide_id = ( "
            + "       SELECT t2.guide_id "
            + "       FROM bookings b2 "
            + "         JOIN tours t2 ON b2.tour_id = t2.id "
            + "       WHERE b2.id = ? "
            + "    ) "
            + "  AND b.status = ? "
            + "  AND NOT ( "
            + "         DATE_ADD(b.departure_date, INTERVAL t.days DAY) <= ? "
            + "      OR b.departure_date >= DATE_ADD(?, INTERVAL ? DAY) "
            + "      )";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setInt(2, bookingId);
            ps.setString(3, BookingStatus.APPROVED.name());
            ps.setDate(4, new java.sql.Date(newDeparture.getTime()));
            ps.setDate(5, new java.sql.Date(newDeparture.getTime()));
            ps.setInt(6, newDays);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * 3. Lấy danh sách các booking có trạng thái CHƯA APPROVED (PENDING) của cùng guide,
     *    mà khoảng thời gian (departure_date + days) overlap với booking mới.
     *    Dùng để thông báo guide: "Những booking chưa duyệt khác cũng trùng thời gian".
     */
    public List<Booking> getOverlappingPendingForGuide(int bookingId, java.util.Date  newDeparture, int newDays) throws SQLException {
        List<Booking> result = new ArrayList<>();

        String sql = ""
            + "SELECT b.* "
            + "FROM bookings b "
            + "  JOIN tours t ON b.tour_id = t.id "
            + "WHERE b.id <> ? "
            + "  AND t.guide_id = ( "
            + "       SELECT t2.guide_id "
            + "       FROM bookings b2 "
            + "         JOIN tours t2 ON b2.tour_id = t2.id "
            + "       WHERE b2.id = ? "
            + "    ) "
            + "  AND b.status = ? "
            + "  AND NOT ( "
            + "         DATE_ADD(b.departure_date, INTERVAL t.days DAY) <= ? "
            + "      OR b.departure_date >= DATE_ADD(?, INTERVAL ? DAY) "
            + "      )";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setInt(2, bookingId);
            ps.setString(3, BookingStatus.PENDING.name());
            ps.setDate(4, new java.sql.Date(newDeparture.getTime()));
            ps.setDate(5, new java.sql.Date(newDeparture.getTime()));
            ps.setInt(6, newDays);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToBooking(rs));
                }
            }
        }
        return result;
    }

}
