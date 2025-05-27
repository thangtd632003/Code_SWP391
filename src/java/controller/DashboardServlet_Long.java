package controller;

import dal.UserDAO_Long;
import dal.ReviewDAO_Long;
import entity.Review;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet_Long extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ReviewDAO_Long reviewDAO = new ReviewDAO_Long();

        double averageRating = reviewDAO.getAverageRating();
        int totalGuides = UserDAO_Long.countGuides();
        int totalTravelers = UserDAO_Long.countTravelers();


        request.setAttribute("averageRating", String.format("%.1f", averageRating));
        request.setAttribute("totalGuides", totalGuides);
        request.setAttribute("totalTravelers", totalTravelers);
        request.setAttribute("reviewCount", reviewDAO.countAll());
        request.setAttribute("recentReviews", reviewDAO.getRecentReviews(5));
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}