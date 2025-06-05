package dal;

import dal.DBContext;
import entity.User;
import entity.Gender;
import entity.Role;
import entity.Status;

import java.sql.*;
import java.util.*;

public class UserDAO_Long {

    public User login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password_hash = ? AND status = 'active'";

        try (Connection conn = new DBContext().getConnection();
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
        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error updating last login: " + e.getMessage());
        }
    }

    private Connection getConnection() throws Exception {
        return new DBContext().getConnection();
    }

    public static int countGuides() {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'GUIDE'"; // Sửa theo enum Role
        try (Connection conn = new DBContext().getConnection();
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
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'TRAVELER'"; // Sửa theo enum Role
        try (Connection conn = new DBContext().getConnection();
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

    public boolean updateUser(User user, String currentPassword, String newPassword) {
        // Verify current password first if changing password
        if (newPassword != null && !newPassword.isEmpty()) {
            String verifySQL = "SELECT id FROM users WHERE id = ? AND password_hash = ?";
            try (Connection conn = getConnection();
                    PreparedStatement ps = conn.prepareStatement(verifySQL)) {
                ps.setInt(1, user.getId());
                ps.setString(2, currentPassword);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return false; // Current password is incorrect
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        // Update user information
        String sql = "UPDATE users SET full_name=?, email=?, phone=?, gender=?, " +
                "birth_date=?" + (newPassword != null && !newPassword.isEmpty() ? ", password_hash=?" : "") +
                " WHERE id=?";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            ps.setString(paramIndex++, user.getFullName());
            ps.setString(paramIndex++, user.getEmail());
            ps.setString(paramIndex++, user.getPhone());
            ps.setString(paramIndex++, user.getGender().name());
            ps.setDate(paramIndex++, new java.sql.Date(user.getBirthDate().getTime()));
            if (newPassword != null && !newPassword.isEmpty()) {
                ps.setString(paramIndex++, newPassword); // Should hash password in production
            }
            ps.setInt(paramIndex, user.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setFullName(rs.getString("full_name"));
                    user.setPhone(rs.getString("phone"));

                    String genderStr = rs.getString("gender");
                    if (genderStr != null && !genderStr.isEmpty()) {
                        user.setGender(Gender.valueOf(genderStr.toUpperCase()));
                    }

                    user.setBirthDate(rs.getDate("birth_date"));

                    String roleStr = rs.getString("role");
                    if (roleStr != null && !roleStr.isEmpty()) {
                        user.setRole(Role.valueOf(roleStr.toUpperCase()));
                    }

                    String statusStr = rs.getString("status");
                    if (statusStr != null && !statusStr.isEmpty()) {
                        user.setStatus(Status.valueOf(statusStr.toUpperCase()));
                    }

                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));

                    return user;
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setFullName(rs.getString("full_name"));
                    user.setPhone(rs.getString("phone"));

                    String genderStr = rs.getString("gender");
                    if (genderStr != null && !genderStr.isEmpty()) {
                        user.setGender(Gender.valueOf(genderStr.toUpperCase()));
                    }

                    user.setBirthDate(rs.getDate("birth_date"));

                    String roleStr = rs.getString("role");
                    if (roleStr != null && !roleStr.isEmpty()) {
                        user.setRole(Role.valueOf(roleStr.toUpperCase()));
                    }

                    String statusStr = rs.getString("status");
                    if (statusStr != null && !statusStr.isEmpty()) {
                        user.setStatus(Status.valueOf(statusStr.toUpperCase()));
                    }

                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));

                    return user;
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean createUser(User user) {
        String query = "INSERT INTO users (email, password_hash, full_name, role, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole().name());
            ps.setString(5, Status.ACTIVE.name());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            // Get the generated ID
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllGuides() {
        List<User> guides = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'GUIDE'";

        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User guide = new User();
                guide.setId(rs.getInt("id"));
                guide.setEmail(rs.getString("email"));
                guide.setFullName(rs.getString("full_name"));
                guide.setPhone(rs.getString("phone"));
                guide.setBirthDate(rs.getDate("birth_date"));

                String genderStr = rs.getString("gender");
                if (genderStr != null) {
                    guide.setGender(Gender.valueOf(genderStr.toUpperCase()));
                }

                guide.setRole(Role.GUIDE);

                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    guide.setStatus(Status.valueOf(statusStr.toUpperCase()));
                }

                guides.add(guide);
            }
        } catch (Exception e) {
            System.out.println("Error getting guides: " + e.getMessage());
            e.printStackTrace();
        }
        return guides;
    }

    public List<Map<String, Object>> getAllGuidesWithRating() {
        List<Map<String, Object>> guides = new ArrayList<>();
        String query = "SELECT u.*, " +
                "(SELECT COALESCE(AVG(r.rating), 0) " +
                "FROM reviews r " +
                "WHERE r.guide_id = u.id) as avg_rating " +
                "FROM users u " +
                "WHERE u.role = 'GUIDE'";

        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> guide = new HashMap<>();
                guide.put("id", rs.getInt("id"));
                guide.put("email", rs.getString("email"));
                guide.put("fullName", rs.getString("full_name"));
                guide.put("phone", rs.getString("phone"));
                guide.put("gender", rs.getString("gender"));
                guide.put("birthDate", rs.getDate("birth_date"));
                guide.put("status", rs.getString("status"));

                double avgRating = rs.getDouble("avg_rating");
                guide.put("rating", avgRating != 0 ? Math.round(avgRating * 10.0) / 10.0 : 0);

                guides.add(guide);
            }
        } catch (Exception e) {
            System.out.println("Error getting guides with rating: " + e.getMessage());
            e.printStackTrace();
        }
        return guides;
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