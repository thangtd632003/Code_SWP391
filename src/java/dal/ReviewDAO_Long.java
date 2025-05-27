package dal;

import context.DBContext_Long;
import entity.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO_Long {

    private Connection getConnection() throws Exception {
        return new DBContext_Long().getConnection();
    }

    // Lấy tất cả review
    public List<Review> getAllReviews() {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Review r = new Review();
                r.setId(rs.getInt("id"));
                r.setBookingId(rs.getInt("booking_id"));
                r.setGuideId(rs.getInt("guide_id"));
                r.setRating(rs.getInt("rating"));
                r.setComment(rs.getString("comment"));
                r.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Đếm số lượng review
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM reviews";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Thêm review mới
    public boolean addReview(Review review) {
        String sql = "INSERT INTO reviews (booking_id, guide_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, review.getBookingId());
            stmt.setInt(2, review.getGuideId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật review
    public boolean updateReview(Review review) {
        String sql = "UPDATE reviews SET booking_id=?, guide_id=?, rating=?, comment=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, review.getBookingId());
            stmt.setInt(2, review.getGuideId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.setInt(5, review.getId());

            // Add debug logging
            System.out.println("Executing update for review ID: " + review.getId());
            System.out.println("SQL: " + sql);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error updating review: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Xóa review
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy review theo ID
    public Review getReviewById(int reviewId) {
        String sql = "SELECT * FROM reviews WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Review review = new Review();
                review.setId(rs.getInt("id"));
                review.setBookingId(rs.getInt("booking_id"));
                review.setGuideId(rs.getInt("guide_id"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));
                review.setCreatedAt(rs.getTimestamp("created_at"));
                return review;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy các review gần đây
    public List<Review> getRecentReviews(int limit) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getInt("id"));
                    r.setBookingId(rs.getInt("booking_id"));
                    r.setGuideId(rs.getInt("guide_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm kiểm tra kết nối và lấy dữ liệu review đầu tiên
    public void testConnectionAndFetch() {
        String sql = "SELECT * FROM reviews LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                System.out.println("Kết nối thành công! Có dữ liệu review:");
                System.out.println("ID: " + rs.getInt("id") +
                        ", BookingID: " + rs.getInt("booking_id") +
                        ", GuideID: " + rs.getInt("guide_id") +
                        ", Rating: " + rs.getInt("rating") +
                        ", Comment: " + rs.getString("comment"));
            } else {
                System.out.println("Kết nối thành công! Nhưng bảng reviews không có dữ liệu.");
            }
        } catch (Exception e) {
            System.out.println("Kết nối hoặc truy vấn thất bại!");
            e.printStackTrace();
        }
    }

    public double getAverageRating() {
        String sql = "SELECT AVG(rating) as avg_rating FROM Reviews";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        } catch (Exception e) {
            System.out.println("Error getting average rating: " + e.getMessage());
        }
        return 0.0;
    }

    public static void main(String[] args) {
        ReviewDAO_Long dao = new ReviewDAO_Long();
        List<Review> reviews = dao.getAllReviews();
        for (Review r : reviews) {
            System.out.println(r);
        }
    }
}