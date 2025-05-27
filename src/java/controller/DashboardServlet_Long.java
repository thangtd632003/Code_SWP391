package controller;

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
        HttpSession session = request.getSession(false);
    User usersession = (User) session.getAttribute("user");
        if(usersession!=null && usersession.getRole().toString().equalsIgnoreCase("traveler")){
                request.getRequestDispatcher("dashboardTraveler.jsp").forward(request, response);
                return;
        }
        if (usersession!=null && usersession.getRole().toString().equalsIgnoreCase("guide")){
             request.getRequestDispatcher("dashboardGuide.jsp").forward(request, response);
                return;
        }
        
        request.setAttribute("reviewCount", reviewDAO.countAll());
        request.setAttribute("recentReviews", reviewDAO.getRecentReviews(5));
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}