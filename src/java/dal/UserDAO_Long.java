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
                    user.setGender(Gender.valueOf(rs.getString("gender").toUpperCase()));
                    user.setBirthDate(rs.getDate("birth_date"));
                    user.setRole(Role.valueOf(rs.getString("role").toUpperCase()));
                    user.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
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
}