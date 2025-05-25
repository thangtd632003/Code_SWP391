package controller;

import dal.ReviewDAO_Long;
import entity.Review;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/reviews/*")
public class ReviewServlet extends HttpServlet {
    private ReviewDAO_Long reviewDAO;

    @Override
    public void init() {
        reviewDAO = new ReviewDAO_Long();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        // Return specific review if ID is provided
        if (pathInfo != null && pathInfo.length() > 1) {
            int reviewId = Integer.parseInt(pathInfo.substring(1));
            Review review = reviewDAO.getReviewById(reviewId);
            
            if (review != null) {
                response.setContentType("application/json");
                response.getWriter().write(convertReviewToJSON(review));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Review not found\"}");
            }
        }
    }
    
    // Helper method to convert Review to JSON
    private String convertReviewToJSON(Review review) {
        return String.format(
            "{\"id\":%d,\"bookingId\":%d,\"guideId\":%d,\"rating\":%d,\"comment\":\"%s\"}",
            review.getId(),
            review.getBookingId(),
            review.getGuideId(),
            review.getRating(),
            review.getComment()
        );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Review review = new Review();
        review.setBookingId(Integer.parseInt(request.getParameter("bookingId")));
        review.setGuideId(Integer.parseInt(request.getParameter("guideId")));
        review.setRating(Integer.parseInt(request.getParameter("rating")));
        review.setComment(request.getParameter("comment"));

        boolean success = reviewDAO.addReview(review);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":" + success + "}");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Read the request body
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            // Parse the URL parameters from request body
            String[] params = sb.toString().split("&");
            Map<String, String> paramMap = new HashMap<>();
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    paramMap.put(keyValue[0], URLDecoder.decode(keyValue[1], "UTF-8"));
                }
            }
            
            // Get reviewId from path
            String pathInfo = request.getPathInfo();
            int reviewId = Integer.parseInt(pathInfo.substring(1));
            
            // Create and populate Review object
            Review review = new Review();
            review.setId(reviewId);
            review.setBookingId(Integer.parseInt(paramMap.get("bookingId")));
            review.setGuideId(Integer.parseInt(paramMap.get("guideId")));
            review.setRating(Integer.parseInt(paramMap.get("rating")));
            review.setComment(paramMap.get("comment"));
            
            // Update review
            boolean success = reviewDAO.updateReview(review);
            
            // Send response
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":" + success + "}");
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        int reviewId = Integer.parseInt(pathInfo.substring(1));

        boolean success = reviewDAO.deleteReview(reviewId);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":" + success + "}");
    }
}