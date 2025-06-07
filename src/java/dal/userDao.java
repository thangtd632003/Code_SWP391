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

public class userDao{
    private Connection conn;

    public userDao(Connection conn) {
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
 public List<User> searchUsers(String keyword) throws SQLException {
        List<User> result = new ArrayList<>();
        String sql = "SELECT * FROM users " +
                     "WHERE LOWER(full_name) LIKE ? OR LOWER(email) LIKE ? " +
                     "ORDER BY updated_at DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToUser(rs));
                }
            }
        }
        return result;
    }

    /**
     * 2. Lấy tất cả User, sắp xếp theo updated_at (asc hoặc desc).
     *    Nếu asc = true => ASC, ngược lại => DESC.
     */
    public List<User> sortUsersByUpdatedAt(boolean asc) throws SQLException {
        List<User> result = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY updated_at " + (asc ? "ASC" : "DESC");
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(mapResultSetToUser(rs));
            }
        }
        return result;
    }

    /**
     * 3. Kết hợp search + sort:
     *    Tìm (full_name hoặc email) LIKE %keyword% và sắp xếp theo updated_at.
     */
    public List<User> searchAndSortUsers(String keyword, boolean asc) throws SQLException {
        List<User> result = new ArrayList<>();
        String sql = "SELECT * FROM users " +
                     "WHERE LOWER(full_name) LIKE ? OR LOWER(email) LIKE ? " +
                     "ORDER BY updated_at " + (asc ? "ASC" : "DESC");
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToUser(rs));
                }
            }
        }
        return result;
    }

    /**
     * 4. Đổi mật khẩu (password_hash) theo id.
     */
    public boolean changePasswordById(int userId, String newPasswordHash) throws SQLException {
        String sql = "UPDATE users SET password_hash = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPasswordHash);
            stmt.setInt(2, userId);
            int affected = stmt.executeUpdate();
            return affected > 0;
        }
    }

    /**
     * 5. Đổi trạng thái (status) theo id.
     */
    public boolean changeStatusById(int userId, Status newStatus) throws SQLException {
        String sql = "UPDATE users SET status = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus.name());
            stmt.setInt(2, userId);
            int affected = stmt.executeUpdate();
            return affected > 0;
        }
    }

    /**
     * Hàm hỗ trợ mapping ResultSet => User entity.
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setPhone(rs.getString("phone"));

        String genderStr = rs.getString("gender");
        if (genderStr != null) {
            user.setGender(Gender.valueOf(genderStr.toUpperCase()));
        }

        java.sql.Date bd = rs.getDate("birth_date");

        if (bd != null) {
            user.setBirthDate(bd);
        }

        user.setRole(Role.valueOf(rs.getString("role").toUpperCase()));
        user.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}

