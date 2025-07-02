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
public class StatisticsDao {
      private final Connection conn;

    public StatisticsDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Tổng số tour trong hệ thống
     */
    public int countAllTours() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tours";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Tổng số user trong hệ thống
     */
    public int countAllUsers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Tổng số user có role = 'TRAVELER'
     */
    public int countTravelers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'TRAVELER'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Tổng số user có role = 'GUIDE'
     */
    public int countGuides() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'GUIDE'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Tổng số booking có status = 'APPROVED' trong hệ thống
     */
    public int countApprovedBookings() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE status = 'APPROVED'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Tổng số booking của một user (traveler) bất kể trạng thái
     */
    public int countBookingsByTraveler(int travelerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE traveler_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, travelerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /**
     * Tổng số booking APPROVED của một user (traveler)
     */
    public int countApprovedBookingsByTraveler(int travelerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE traveler_id = ? AND status = 'APPROVED'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, travelerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
