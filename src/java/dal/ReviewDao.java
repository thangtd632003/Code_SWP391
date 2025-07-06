/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import entity.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author thang
 */
public class ReviewDao {
        private final Connection conn;
public ReviewDao (Connection conn) {
        this.conn = conn;
    }
     public boolean addReview(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (booking_id, guide_id, rating, comment, created_at) "
                   + "VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, review.getBookingId());
            ps.setInt(2, review.getGuideId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            return ps.executeUpdate() > 0;
        }
    }

    /** 2. Lấy review theo bookingId */
    public List<Review> getReviewsByBookingId(int bookingId) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReview(rs));
                }
            }
        }
        return list;
    }

    /** 3. Lấy review theo guideId */
    public List<Review> getReviewsByGuideId(int guideId) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE guide_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, guideId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReview(rs));
                }
            }
        }
        return list;
    }

    /** 4. Sort review của guide theo created_at ASC/DESC */
    public List<Review> sortReviewsByGuideId(int guideId, boolean asc) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE guide_id = ? ORDER BY created_at " + (asc ? "ASC" : "DESC");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, guideId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReview(rs));
                }
            }
        }
        return list;
    }

    /** 5. Search review của guide theo comment LIKE %keyword% */
    public List<Review> searchReviewsByGuideId(int guideId, String keyword) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE guide_id = ? AND LOWER(comment) LIKE ? ORDER BY created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, guideId);
            ps.setString(2, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReview(rs));
                }
            }
        }
        return list;
    }

    /** 6. Kết hợp search + sort cho review của guide */
    public List<Review> searchAndSortReviewsByGuideId(int guideId, String keyword, boolean asc) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews "
                   + "WHERE guide_id = ? AND LOWER(comment) LIKE ? "
                   + "ORDER BY created_at " + (asc ? "ASC" : "DESC");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, guideId);
            ps.setString(2, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReview(rs));
                }
            }
        }
        return list;
    }

    /** Utility: ánh xạ ResultSet → Review */
    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setId(rs.getInt("id"));
        r.setBookingId(rs.getInt("booking_id"));
        r.setGuideId(rs.getInt("guide_id"));
        r.setRating(rs.getInt("rating"));
        r.setComment(rs.getString("comment"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}
