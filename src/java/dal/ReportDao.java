/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author thang
 */
public class ReportDao {
    private final Connection conn;
    public ReportDao(Connection conn) {
        this.conn = conn;
    }
      public String getFirstAdminEmail() throws SQLException {
        String sql = "SELECT email FROM users WHERE role = 'ADMIN' LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("email");
            }
        }
        return null;
    }
}
