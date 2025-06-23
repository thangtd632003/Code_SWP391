package controller;

import com.sun.jdi.connect.spi.Connection;
import dal.BookingDao;
import dal.DBContext;
import dal.tourDao;
import dal.ReviewDAO_Long;
import dal.guideDao;
import dal.userDao;
import entity.Booking;
import entity.Review;
import entity.Tour;
import entity.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DashboardGuide", urlPatterns = {"/guide-dashboard"})

public class GuideDashboardServlet extends HttpServlet {
    private guideDao guideDAO;
    private ReviewDAO_Long reviewDAO;
    private tourDao tourDAO;
    private BookingDao bookingDAO;

    public void init() throws ServletException {
        try {
            DBContext dbContext = new DBContext(); 
            java.sql.Connection conn = dbContext.getConnection();
            
           
            tourDAO = new tourDao(conn);
            bookingDAO = new BookingDao(conn);
            reviewDAO = new ReviewDAO_Long();
        } catch (Exception e) {
            throw new ServletException("Không thể khởi tạo database connection", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
         // Debug: In thông tin session và user
    System.out.println("Session ID: " + session.getId());
    System.out.println("User trong session: " + (currentUser != null ? "Có" : "Không"));
    
    if (currentUser != null) {
        System.out.println("User ID: " + currentUser.getId());
        System.out.println("User Email: " + currentUser.getEmail());
        System.out.println("User Role: " + currentUser.getRole());
        System.out.println("User Name: " + currentUser.getFullName());
        // In thêm các thông tin khác của user nếu cần
    } else {
        System.out.println("Không có user nào trong session");
    }
        if (currentUser == null || !currentUser.getRole().name().equalsIgnoreCase("guide")) {
            response.sendRedirect("login");
            return;
        }

        int guideId = currentUser.getId();

        // Lấy thống kê tổng quan
        int totalTours = tourDAO.countToursByGuideId(guideId);
        int totalBookings = bookingDAO.countBookingsByGuideId(guideId);
        int pendingBookings = bookingDAO.countPendingBookingsByGuideId(guideId);
        double averageRating = reviewDAO.getAverageRatingByGuideId(guideId);
        System.out.println(totalTours);
        // Lấy danh sách booking và review gần đây
        List<Booking> recentBookings = bookingDAO.getRecentBookingsByGuideId(guideId, 5);
        List<Review> recentReviews = reviewDAO.getRecentReviews(5);
        List<Tour> upcomingTours = tourDAO.getUpcomingToursByGuideId(guideId);

        // Set attributes
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("totalTours", totalTours);
        System.out.println(totalTours);
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("pendingBookings", pendingBookings);
        request.setAttribute("averageRating", averageRating);
        request.setAttribute("recentBookings", recentBookings);
        request.setAttribute("recentReviews", recentReviews);
        request.setAttribute("upcomingTours", upcomingTours);

        // Forward to dashboard.jsp
        request.getRequestDispatcher("dashboardGuide.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("updateBookingStatus".equals(action)) {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String status = request.getParameter("status");
            
            boolean success = bookingDAO.updateBookingStatus(bookingId, status);
            
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":" + success + "}");
        }
    }
}
