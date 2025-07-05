package controller;
import java.sql.Connection;

import dal.DBContext;
import dal.UserDAO_Long;
import dal.ReviewDAO_Long;
import entity.Review;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import dal.StatisticsDao;
import java.io.IOException;
import java.util.List;
import dal.tourDao;
import entity.Tour;
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ReviewDAO_Long reviewDAO = new ReviewDAO_Long();
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Refresh user data
        User refreshedUser = new UserDAO_Long().getUserById(user.getId());
        if (refreshedUser != null) {
            session.setAttribute("user", refreshedUser);
            user = refreshedUser;
        }

        // Nếu là GUIDE
        if ("guide".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/guide-dashboard");
            return;
        }

        // Nếu là TRAVELER
        if ("traveler".equalsIgnoreCase(user.getRole().name())) {
            // khởi tạo StatisticsDao
            try (Connection conn = new DBContext().getConnection()) {
                StatisticsDao stats = new StatisticsDao(conn);
                tourDao tourdao= new tourDao(conn);
              List<Tour> t = tourdao.getToursBookedByUser(user.getId());
                // tổng booking của traveler
                int totalBookings      = stats.countBookingsByTraveler(user.getId());
                // tổng booking đã approved của traveler
                int approvedBookings   = stats.countApprovedBookingsByTraveler(user.getId());
                request.setAttribute("totalBookings", totalBookings);
                request.setAttribute("approvedBookings", approvedBookings);
                request.setAttribute("tours", t);
            } catch (Exception e) {
                throw new ServletException("Cannot load booking statistics", e);
            }
            // forward về trang riêng của traveler
            request.getRequestDispatcher("dashboardTraveler.jsp")
                   .forward(request, response);
            return;
        }

        // Còn lại: ADMIN hoặc khác
        double averageRating = reviewDAO.getAverageRating();
        int totalGuides     = UserDAO_Long.countGuides();
        int totalTravelers  = UserDAO_Long.countTravelers();
   try (Connection conn = new DBContext().getConnection()) {
        StatisticsDao stats = new StatisticsDao(conn);
        int toursNumber = stats.countAllTours();
        int bookingNumber = stats.countApprovedBookings();
        request.setAttribute("toursNumber", toursNumber);
        request.setAttribute("bookingNumber", bookingNumber);
           } catch (Exception e) {
                throw new ServletException("Cannot load booking statistics", e);
            }
        request.setAttribute("averageRating", String.format("%.1f", averageRating));
        request.setAttribute("totalGuides", totalGuides);
        request.setAttribute("totalTravelers", totalTravelers);
        request.setAttribute("reviewCount", reviewDAO.countAll());
        
        request.setAttribute("recentReviews", reviewDAO.getRecentReviews(5));

        request.getRequestDispatcher("dashboard.jsp")
               .forward(request, response);
    }
    
}