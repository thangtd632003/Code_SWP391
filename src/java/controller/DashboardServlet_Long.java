package controller;

import dal.UserDAO_Long;
import dal.ReviewDAO_Long;
import entity.Review;
import entity.User;
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
         if(user.getRole().name().equalsIgnoreCase("guide")){
               request.getRequestDispatcher("dashboardGuide.jsp").forward(request, response);
         }
          if(user.getRole().name().equalsIgnoreCase("traveler")){
               request.getRequestDispatcher("dashboardTraveler.jsp").forward(request, response);
         }
        // Refresh user data from database
        UserDAO_Long userDAO = new UserDAO_Long();
        User refreshedUser = userDAO.getUserById(user.getId()); 
        if (refreshedUser != null) {
            session.setAttribute("user", refreshedUser);
        }

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