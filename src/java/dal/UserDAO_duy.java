/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author thang
 */
import entity.User;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO_duy extends DBContext {

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (email, password_hash, full_name, phone, role) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPasswordHash()); // Should be hashed
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole().name());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error in registerUser: " + e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(UserDAO_duy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean emailExists(String email) throws Exception {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Có kết quả -> email đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}