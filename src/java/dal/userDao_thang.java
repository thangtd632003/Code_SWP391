/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author thang
 */
import entity.Gender;
import entity.Role;
import entity.Status;
import entity.User;
import java.sql.*;
import java.util.*;

public class userDao_thang{
    private Connection conn;

    public userDao_thang(Connection conn) {
        this.conn = conn;
    }

    // Lấy thông tin user theo email
    public User getUserByEmail(String email) throws SQLException {
       String sql = "SELECT * FROM users WHERE email = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setFullName(rs.getString("full_name"));
            user.setPhone(rs.getString("phone"));

            // Gender có thể null nên kiểm tra
            String genderStr = rs.getString("gender");
            if (genderStr != null) {
                user.setGender(Gender.valueOf(genderStr.toUpperCase()));
            }

            user.setBirthDate(rs.getDate("birth_date"));

            user.setRole(Role.valueOf(rs.getString("role").toUpperCase()));
            user.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
            user.setCreatedAt(rs.getTimestamp("created_at"));
            user.setUpdatedAt(rs.getTimestamp("updated_at"));
            return user;
        }
    }
    return null;
    }

    // Cập nhật thông tin user theo email
    public boolean updateUserByEmail(String email, User updatedUser) throws SQLException {
    String sql = "UPDATE users SET full_name = ?, phone = ?, gender = ?, birth_date = ?, role = ?, status = ?, updated_at = NOW() WHERE email = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, updatedUser.getFullName());
        stmt.setString(2, updatedUser.getPhone());

        // gender có thể null
        if (updatedUser.getGender() != null) {
            stmt.setString(3, updatedUser.getGender().name().toLowerCase()); // lowercase để phù hợp ENUM trong DB
        } else {
            stmt.setNull(3, Types.VARCHAR);
        }

        // birthDate có thể null
        if (updatedUser.getBirthDate() != null) {
            stmt.setDate(4, updatedUser.getBirthDate());
        } else {
            stmt.setNull(4, Types.DATE);
        }

        stmt.setString(5, updatedUser.getRole().name());
        stmt.setString(6, updatedUser.getStatus().name());
        stmt.setString(7, email);

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }
}
    
      public boolean updatePasswordByEmail(String email, String newPasswordHash) throws SQLException {
        String sql = "UPDATE users SET password_hash = ?, updated_at = NOW() WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPasswordHash);
            stmt.setString(2, email);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
      
       public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
       public User getUserById(int userId) throws SQLException {
    String sql = "SELECT * FROM users WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));

                // Gender có thể null nên kiểm tra
                String genderStr = rs.getString("gender");
                if (genderStr != null) {
                    user.setGender(Gender.valueOf(genderStr.toUpperCase()));
                }

                user.setBirthDate(rs.getDate("birth_date"));
                user.setRole(Role.valueOf(rs.getString("role").toUpperCase()));
                user.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));
                return user;
            }
        }
    }
    return null;
}

}

