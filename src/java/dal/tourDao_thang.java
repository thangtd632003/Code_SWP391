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

    /**
     * Tạo mới tour
     */
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

}
