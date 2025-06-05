package dal;

import entity.Status;
import entity.Tour;
import java.sql.*;
import java.util.*;

public class tourDao_thang {
    private Connection conn;

    public tourDao_thang(Connection conn) {
        this.conn = conn;
    }

    

  public boolean createTour(Tour tour) throws SQLException {
    String sql = "INSERT INTO tours (guide_id, name, description, itinerary, price, max_people_per_booking, days, language, status) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, tour.getGuideId());
        stmt.setString(2, tour.getName());
        stmt.setString(3, tour.getDescription());
        stmt.setString(4, tour.getItinerary());
        stmt.setBigDecimal(5, tour.getPrice());
        stmt.setInt(6, tour.getMaxPeoplePerBooking());
        stmt.setInt(7, tour.getDays());
        stmt.setString(8, tour.getLanguage());
        stmt.setString(9, tour.getStatus().name());

        int affected = stmt.executeUpdate();
        if (affected > 0) {
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    tour.setId(rs.getInt(1));
                }
            }
            return true;
        }
    }
    return false;
}
    /**
     * Cập nhật tour
     */
   public boolean updateTour(Tour tour) throws SQLException {
    String sql = "UPDATE tours SET name = ?, description = ?, itinerary = ?, price = ?, max_people_per_booking = ?, " +
                 "days = ?, language = ?, status = ?, updated_at = NOW() WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, tour.getName());
        stmt.setString(2, tour.getDescription());
        stmt.setString(3, tour.getItinerary());
        stmt.setBigDecimal(4, tour.getPrice());
        stmt.setInt(5, tour.getMaxPeoplePerBooking());
        stmt.setInt(6, tour.getDays());
        stmt.setString(7, tour.getLanguage());
        stmt.setString(8, tour.getStatus().name());
        stmt.setInt(9, tour.getId());

        int affected = stmt.executeUpdate();
        return affected > 0;
    }
}

public boolean toggleTourStatus(int tourId) throws SQLException {
    String selectSql = "SELECT status FROM tours WHERE id = ?";
    String updateSql = "UPDATE tours SET status = ?, updated_at = NOW() WHERE id = ?";

    try (
        PreparedStatement selectStmt = conn.prepareStatement(selectSql);
        PreparedStatement updateStmt = conn.prepareStatement(updateSql)
    ) {
     
        selectStmt.setInt(1, tourId);
        try (ResultSet rs = selectStmt.executeQuery()) {
            if (rs.next()) {
                String currentStatus = rs.getString("status");
                Status newStatus;

                if (Status.ACTIVE.name().equalsIgnoreCase(currentStatus)) {
                    newStatus = Status.LOCKED;
                } else {
                    newStatus = Status.ACTIVE;
                }

                // Bước 3: Cập nhật
                updateStmt.setString(1, newStatus.name());
                updateStmt.setInt(2, tourId);
                int affected = updateStmt.executeUpdate();
                return affected > 0;
            }
        }
    }
    return false;
}

    /**
     * Xóa tour theo id
     */
    public boolean deleteTour(int tourId) throws SQLException {
        String sql = "DELETE FROM tours WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tourId);
            int affected = stmt.executeUpdate();
            return affected > 0;
        }
    }

    /**
     * Lấy danh sách tour theo guide_id
     */
    public List<Tour> getToursByGuideId(int guideId) throws SQLException {
        String sql = "SELECT * FROM tours WHERE guide_id = ? ORDER BY created_at DESC";
        List<Tour> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guideId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tour t = new Tour();
                    t.setId(rs.getInt("id"));
                    t.setGuideId(rs.getInt("guide_id"));
                    t.setName(rs.getString("name"));
                    t.setDescription(rs.getString("description"));
                    t.setItinerary(rs.getString("itinerary"));
                    t.setPrice(rs.getBigDecimal("price"));
                    t.setMaxPeoplePerBooking(rs.getInt("max_people_per_booking"));
                    t.setDays(rs.getInt("days"));
                    t.setLanguage(rs.getString("language"));
                    t.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                    t.setCreatedAt(rs.getTimestamp("created_at"));
                    t.setUpdatedAt(rs.getTimestamp("updated_at"));
                    list.add(t);
                }
            }
        }
        return list;
    }

    /**
     * Lấy tour theo id
     */
    public Tour getTourById(int tourId) throws SQLException {
        String sql = "SELECT * FROM tours WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tourId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Tour t = new Tour();
                    t.setId(rs.getInt("id"));
                    t.setGuideId(rs.getInt("guide_id"));
                    t.setName(rs.getString("name"));
                    t.setDescription(rs.getString("description"));
                    t.setItinerary(rs.getString("itinerary"));
                    t.setPrice(rs.getBigDecimal("price"));
                    t.setMaxPeoplePerBooking(rs.getInt("max_people_per_booking"));
                    t.setDays(rs.getInt("days"));
                       t.setLanguage(rs.getString("language"));
                    t.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                    t.setCreatedAt(rs.getTimestamp("created_at"));
                    t.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return t;
                }
            }
        }
        return null;
    }
    public List<Tour> getAllTours() throws SQLException {
    List<Tour> tours = new ArrayList<>();
    String sql = "SELECT * FROM tours";

    try (PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Tour t = new Tour();
            t.setId(rs.getInt("id"));
            t.setGuideId(rs.getInt("guide_id"));
            t.setName(rs.getString("name"));
            t.setDescription(rs.getString("description"));
            t.setItinerary(rs.getString("itinerary"));
            t.setPrice(rs.getBigDecimal("price"));
            t.setMaxPeoplePerBooking(rs.getInt("max_people_per_booking"));
            t.setLanguage(rs.getString("language"));
            t.setStatus(entity.Status.valueOf(rs.getString("status").toUpperCase()));
            t.setCreatedAt(rs.getTimestamp("created_at"));
            t.setUpdatedAt(rs.getTimestamp("updated_at"));

            tours.add(t);
        }
    }

    return tours;
}
    
    /**
     * Tìm (theo guideId và keyword) và sắp xếp theo sortBy/ sortAsc.
     *
     * @param guideId   ID của guide (để lấy tours chỉ của guide đó)
     * @param keyword   từ khóa tìm kiếm (tìm trong cột name); nếu null hoặc empty thì coi như không filter
     * @param sortBy    tên trường dùng để sort (ví dụ: "id", "name", "price", "max_people_per_booking", "days", "language", "status", "updated_at")
     * @param sortAsc   true = ASC, false = DESC
     * @return List<Tour> đã tìm và sắp xếp xong
     * @throws SQLException
     */
public List<Tour> searchTours(String keyword) {
    List<Tour> tours = new ArrayList<>();
    String sql = "SELECT * FROM tours WHERE name LIKE ?";
    
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + keyword + "%");
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tour t = new Tour();
                t.setId(rs.getInt("id"));
                t.setGuideId(rs.getInt("guide_id"));
                t.setName(rs.getString("name"));
                t.setDescription(rs.getString("description"));
                t.setItinerary(rs.getString("itinerary"));
                t.setPrice(rs.getBigDecimal("price"));
                t.setMaxPeoplePerBooking(rs.getInt("max_people_per_booking"));
                t.setDays(rs.getInt("days"));
                t.setLanguage(rs.getString("language"));
                t.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setUpdatedAt(rs.getTimestamp("updated_at"));
                // ... thêm các trường khác nếu có
                tours.add(t);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return tours;
}
public List<Tour> sortTours(String sortBy, boolean sortAsc) {
    List<Tour> tours = new ArrayList<>();

    // Validation / mapping sortBy từ input Java sang cột trong CSDL
    String column;
    switch (sortBy.toLowerCase()) {
        case "days":
            column = "days";
            break;
        case "maxpeopleperbooking":
            column = "max_people_per_booking";
            break;
        case "price":
            column = "price";
            break;
        case "language":
            column = "language";
            break;
        case "status":
            column = "status";
            break;
        default:
            // Nếu không khớp bất kỳ case nào, mặc định sắp xếp theo updated_at
            column = "updated_at";
    }

    String sql = "SELECT * FROM tours ORDER BY " + column + (sortAsc ? " ASC" : " DESC");

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tour t = new Tour();
                t.setId(rs.getInt("id"));
                t.setGuideId(rs.getInt("guide_id"));
                t.setName(rs.getString("name"));
                t.setDescription(rs.getString("description"));
                t.setItinerary(rs.getString("itinerary"));
                t.setPrice(rs.getBigDecimal("price"));
                t.setMaxPeoplePerBooking(rs.getInt("max_people_per_booking"));
                t.setDays(rs.getInt("days"));
                t.setLanguage(rs.getString("language"));
                t.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setUpdatedAt(rs.getTimestamp("updated_at"));
                // ... thêm các trường khác nếu có
                tours.add(t);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return tours;
}
public List<Tour> searchToursByGuideId(int guideId, String keyword) throws SQLException {
    List<Tour> tours = new ArrayList<>();
    String sql = "SELECT * FROM tours "
               + "WHERE guide_id = ? "
               + "  AND name LIKE ? "
               + "ORDER BY created_at DESC";  // Hoặc bạn có thể đổi sang ORDER BY updated_at tuỳ ý

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, guideId);
        if (keyword == null || keyword.trim().isEmpty()) {
            ps.setString(2, "%");
        } else {
            ps.setString(2, "%" + keyword.trim() + "%");
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tour t = new Tour();
                t.setId(rs.getInt("id"));
                t.setGuideId(rs.getInt("guide_id"));
                t.setName(rs.getString("name"));
                t.setDescription(rs.getString("description"));
                t.setItinerary(rs.getString("itinerary"));
                t.setPrice(rs.getBigDecimal("price"));
                t.setMaxPeoplePerBooking(rs.getInt("max_people_per_booking"));
                t.setDays(rs.getInt("days"));
                t.setLanguage(rs.getString("language"));
                t.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setUpdatedAt(rs.getTimestamp("updated_at"));
                tours.add(t);
            }
        }
    }
    return tours;
}
public List<Tour> sortToursByGuideId(int guideId, String sortBy, boolean sortAsc) throws SQLException {
    List<Tour> tours = new ArrayList<>();

    // 1. Map sortBy sang tên cột trong DB
    String column;
    if (sortBy == null) {
        column = "created_at";
    } else {
        switch (sortBy.toLowerCase()) {
            case "id":
                column = "id";
                break;
            case "name":
                column = "name";
                break;
            case "price":
                column = "price";
                break;
            case "maxpeopleperbooking":
            case "max_people_per_booking":
                column = "max_people_per_booking";
                break;
            case "days":
                column = "days";
                break;
            case "language":
                column = "language";
                break;
            case "status":
                column = "status";
                break;
            case "updated_at":
                column = "updated_at";
                break;
            default:
                column = "created_at";
        }
    }

    String sql = "SELECT * FROM tours "
               + "WHERE guide_id = ? "
               + "ORDER BY " + column + (sortAsc ? " ASC" : " DESC");

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, guideId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tour t = new Tour();
                t.setId(rs.getInt("id"));
                t.setGuideId(rs.getInt("guide_id"));
                t.setName(rs.getString("name"));
                t.setDescription(rs.getString("description"));
                t.setItinerary(rs.getString("itinerary"));
                t.setPrice(rs.getBigDecimal("price"));
                t.setMaxPeoplePerBooking(rs.getInt("max_people_per_booking"));
                t.setDays(rs.getInt("days"));
                t.setLanguage(rs.getString("language"));
                t.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setUpdatedAt(rs.getTimestamp("updated_at"));
                tours.add(t);
            }
        }
    }
    return tours;
}
public List<Tour> searchAndSortToursByGuideId(int guideId, String keyword, String sortBy, boolean sortAsc) throws SQLException {
    List<Tour> tours = new ArrayList<>();

    // 1. Map sortBy sang tên cột
    String column;
    if (sortBy == null) {
        column = "created_at";
    } else {
        switch (sortBy.toLowerCase()) {
            case "id":
                column = "id";
                break;
            case "name":
                column = "name";
                break;
            case "price":
                column = "price";
                break;
            case "maxpeopleperbooking":
            case "max_people_per_booking":
                column = "max_people_per_booking";
                break;
            case "days":
                column = "days";
                break;
            case "language":
                column = "language";
                break;
            case "status":
                column = "status";
                break;
            case "updated_at":
                column = "updated_at";
                break;
            default:
                column = "created_at";
        }
    }

    String sql = "SELECT * FROM tours "
               + "WHERE guide_id = ? "
               + "  AND name LIKE ? "
               + "ORDER BY " + column + (sortAsc ? " ASC" : " DESC");

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, guideId);
        if (keyword == null || keyword.trim().isEmpty()) {
            ps.setString(2, "%");
        } else {
            ps.setString(2, "%" + keyword.trim() + "%");
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tour t = new Tour();
                t.setId(rs.getInt("id"));
                t.setGuideId(rs.getInt("guide_id"));
                t.setName(rs.getString("name"));
                t.setDescription(rs.getString("description"));
                t.setItinerary(rs.getString("itinerary"));
                t.setPrice(rs.getBigDecimal("price"));
                t.setMaxPeoplePerBooking(rs.getInt("max_people_per_booking"));
                t.setDays(rs.getInt("days"));
                t.setLanguage(rs.getString("language"));
                t.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setUpdatedAt(rs.getTimestamp("updated_at"));
                tours.add(t);
            }
        }
    }
    return tours;
}
  public List<Tour> searchAndSortTours(String keyword, String sortBy, boolean sortAsc) throws SQLException {
        List<Tour> tours = new ArrayList<>();

        // 1. Chuẩn hóa keyword
        String kw = (keyword == null) ? "" : keyword.trim();

        // 2. Mapping sortBy sang tên cột trong DB (đảm bảo lowercase)
        String column;
        if (sortBy == null || sortBy.trim().isEmpty()) {
            column = "updated_at";  // default
        } else {
            switch (sortBy.trim().toLowerCase()) {
                case "id":
                    column = "id";
                    break;
                case "name":
                    column = "name";
                    break;
                case "price":
                    column = "price";
                    break;
                case "max_people_per_booking":
                    column = "max_people_per_booking";
                    break;
                case "days":
                    column = "days";
                    break;
                case "language":
                    column = "language";
                    break;
                case "status":
                    column = "status";
                    break;
                default:
                    column = "updated_at";
            }
        }

        // 3. Xây dựng SQL
        String sql = "SELECT * FROM tours WHERE name LIKE ? ORDER BY " + column + (sortAsc ? " ASC" : " DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + kw + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tour t = new Tour();
                t.setId(rs.getInt("id"));
                t.setGuideId(rs.getInt("guide_id"));
                t.setName(rs.getString("name"));
                t.setDescription(rs.getString("description"));
                t.setItinerary(rs.getString("itinerary"));
                t.setPrice(rs.getBigDecimal("price"));
                t.setMaxPeoplePerBooking(rs.getInt("max_people_per_booking"));
                t.setDays(rs.getInt("days"));
                t.setLanguage(rs.getString("language"));
                t.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setUpdatedAt(rs.getTimestamp("updated_at"));
                tours.add(t);
                }
            }
        }

        return tours;
    }
     
    public List<Tour> getTop10ByBookings() throws SQLException {
        String sql = 
            "SELECT t.*, COUNT(b.id) AS booking_count " +
            "FROM tours t " +
            "JOIN bookings b ON t.id = b.tour_id " +
            "GROUP BY t.id " +
            "ORDER BY booking_count DESC " +
            "LIMIT 10";
        List<Tour> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Tour t = mapResultSetToTour(rs);
                // (booking_count có thể dùng nếu cần hiển thị số booking)
                list.add(t);
            }
        }
        return list;
    }

    /**
     * Lấy danh sách Tour mà user (traveler) đã booking.
     */
    public List<Tour> getToursBookedByUser(int travelerId) throws SQLException {
        String sql = 
             "SELECT t.* " +
            "FROM tours t " +
            "JOIN bookings b ON t.id = b.tour_id " +
            "WHERE b.traveler_id = ? " +
            "GROUP BY t.id " +
            "ORDER BY MAX(b.created_at) DESC";
        List<Tour> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, travelerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTour(rs));
                }
            }
        }
        return list;
    }

    /**
     * Search Tour theo tên (keyword). 
     * Tìm tất cả tour có tên chứa keyword (không phân biệt hoa thường).
     */
   

    // Helper: map một row trong ResultSet sang entity.Tour
    private Tour mapResultSetToTour(ResultSet rs) throws SQLException {
        Tour t = new Tour();
        t.setId(rs.getInt("id"));
        t.setGuideId(rs.getInt("guide_id"));
        t.setName(rs.getString("name"));
        t.setDescription(rs.getString("description"));
        t.setItinerary(rs.getString("itinerary"));
        t.setPrice(rs.getBigDecimal("price"));
        t.setMaxPeoplePerBooking(rs.getInt("max_people_per_booking"));
        t.setDays(rs.getInt("days"));
        t.setLanguage(rs.getString("language"));
        t.setStatus(entity.Status.valueOf(rs.getString("status").toUpperCase()));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        t.setUpdatedAt(rs.getTimestamp("updated_at"));
        // Giả sử bảng tours có cột `image_url` để hiển thị ảnh; nếu không có thì có thể set 1 đường dẫn mặc định
      
        return t;
    }
}
