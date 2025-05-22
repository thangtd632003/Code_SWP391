/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author thang
 */


import entity.GuideProfile;
import entity.User;
import java.sql.*;

public class guideDao_thang {
    private Connection conn;

    public guideDao_thang(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lấy GuideProfile theo guideId
     */
    public GuideProfile getGuideProfileById(int guideId) throws SQLException {
        String sql = "SELECT * FROM guide_profiles WHERE guide_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guideId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    GuideProfile profile = new GuideProfile();
                    profile.setGuideId(rs.getInt("guide_id"));
                    profile.setBio(rs.getString("bio"));
                    profile.setAvatarUrl(rs.getString("avatar_url"));
                    // document_proof bỏ qua
                    profile.setUpdatedAt(rs.getTimestamp("updated_at"));
                    // Set user reference tối thiểu với id
                
                    return profile;
                }
            }
        }
        return null;
    }

    /**
     * Thêm mới GuideProfile (không xử lý documentProof)
     */
    public boolean addGuideProfile(GuideProfile profile) throws SQLException {
        String sql = "INSERT INTO guide_profiles (guide_id, bio, avatar_url) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, profile.getGuideId());
            stmt.setString(2, profile.getBio());
            stmt.setString(3, profile.getAvatarUrl());
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    /**
     * Cập nhật GuideProfile (không xử lý documentProof)
     */
    public boolean updateGuideProfile(GuideProfile profile) throws SQLException {
        String sql = "UPDATE guide_profiles SET bio = ?, avatar_url = ?, updated_at = NOW() WHERE guide_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profile.getBio());
            stmt.setString(2, profile.getAvatarUrl());
            stmt.setInt(3, profile.getGuideId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
}
