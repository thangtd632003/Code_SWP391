package controller;

import com.google.gson.Gson;
import dal.UserDAO_Long;
import dal.ReviewDAO_Long;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@WebServlet("/api/guides/*")
public class GuideDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String pathInfo = request.getPathInfo();
            UserDAO_Long userDAO = new UserDAO_Long();
            
            // Nếu không có pathInfo hoặc là "/", trả về danh sách tất cả guides
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Map<String, Object>> guides = userDAO.getAllGuidesWithRating();
                String jsonGuides = new Gson().toJson(guides);
                response.getWriter().write(jsonGuides);
                return;
            }
            
            // Nếu có ID, trả về thông tin chi tiết của guide
            try {
                int guideId = Integer.parseInt(pathInfo.substring(1));
                User guide = userDAO.getUserById(guideId);
                
                if (guide == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Guide not found\"}");
                    return;
                }
                
                ReviewDAO_Long reviewDAO = new ReviewDAO_Long();
                double avgRating = reviewDAO.getAverageRatingByGuideId(guideId);
                
                Map<String, Object> guideDetails = new HashMap<>();
                guideDetails.put("id", guide.getId());
                guideDetails.put("fullName", guide.getFullName());
                guideDetails.put("email", guide.getEmail());
                guideDetails.put("phone", guide.getPhone());
                guideDetails.put("gender", guide.getGender() != null ? guide.getGender().toString() : null);
                guideDetails.put("birthDate", guide.getBirthDate());
                guideDetails.put("status", guide.getStatus() != null ? guide.getStatus().toString() : null);
                guideDetails.put("rating", avgRating);
                
                String jsonResponse = new Gson().toJson(guideDetails);
                response.getWriter().write(jsonResponse);
                
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid guide ID format\"}");
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}