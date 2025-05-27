package dal;

import context.DBContext_Long;
import entity.User;
import entity.Gender;
import entity.Role;
import entity.Status;
import java.sql.*;

public class UserDAO_Long {
    
    public User login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password_hash = ? AND status = 'active'";
        
        try (Connection conn = new DBContext_Long().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, email);
            ps.setString(2, password); // Trong thực tế nên hash password
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setFullName(rs.getString("full_name"));
                    user.setPhone(rs.getString("phone"));

                    String genderStr = rs.getString("gender");
                    user.setGender(genderStr != null ? Gender.valueOf(genderStr.toUpperCase()) : null);

                    user.setBirthDate(rs.getDate("birth_date"));

                    String roleStr = rs.getString("role");
                    user.setRole(roleStr != null ? Role.valueOf(roleStr.toUpperCase()) : null);

                    String statusStr = rs.getString("status");
                    user.setStatus(statusStr != null ? Status.valueOf(statusStr.toUpperCase()) : null);

                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return user;
                }
            }
        } catch (Exception e) {
            System.out.println("Error at UserDAO: " + e.getMessage());
        }
        return null;
    }

    public void updateLastLogin(int userId) {
        String query = "UPDATE users SET updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = new DBContext_Long().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error updating last login: " + e.getMessage());
        }
    }

    private Connection getConnection() throws Exception {
        return new DBContext_Long().getConnection();
    }

    public static int countGuides() {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'GUIDE'";  // Sửa theo enum Role
        try (Connection conn = new DBContext_Long().getConnection();
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

    public static int countTravelers() {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'TRAVELER'";  // Sửa theo enum Role
        try (Connection conn = new DBContext_Long().getConnection();
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
    public static void main(String[] args) {
        UserDAO_Long dao = new UserDAO_Long();
        // Thử đăng nhập với email và password mẫu
        String email = "admin@gmail.com";
        String password = "123456";
        entity.User user = dao.login(email, password);
        if (user != null) {
            System.out.println("Đăng nhập thành công: " + user);
        } else {
            System.out.println("Đăng nhập thất bại hoặc không tìm thấy user.");
        }
    }
}