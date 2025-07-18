package controller;

import dal.ReviewDAO_2;
import entity.Review;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RatingServlet", urlPatterns = {"/rating"})
public class RatingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String bookingIdParam = request.getParameter("bookingId");
        String guideIdParam = request.getParameter("guideId");

        if (bookingIdParam != null && guideIdParam != null) {
            try {
                int bookingId = Integer.parseInt(bookingIdParam);
                int guideId = Integer.parseInt(guideIdParam);

                ReviewDAO_2 reviewDAO = new ReviewDAO_2();
                boolean hasReview = reviewDAO.hasReviewForBooking(bookingId); // Giả sử có phương thức này

                if (hasReview) {
                    request.setAttribute("successMessage", "Bạn đã đánh giá booking này trước đó!");
                    request.getRequestDispatcher("thank-you.jsp").forward(request, response);
                    return;
                }

                request.setAttribute("bookingId", bookingId);
                request.setAttribute("guideId", guideId);
                request.getRequestDispatcher("rating.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID không hợp lệ");
            }
        } else {
            request.setAttribute("errorMessage", "Thiếu thông tin booking hoặc guide");
        }

        
        response.sendRedirect("dashboard.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        String bookingIdParam = request.getParameter("bookingId");
        String guideIdParam = request.getParameter("guideId");
        String ratingParam = request.getParameter("rating");
        String comment = request.getParameter("comment");
        System.out.println("POST Parameters: bookingId=" + bookingIdParam + 
                       ", guideId=" + guideIdParam + 
                       ", rating=" + ratingParam + 
                       ", comment=" + comment);
        try {
           
            int bookingId = Integer.parseInt(bookingIdParam);
            int guideId = Integer.parseInt(guideIdParam);
            int rating = Integer.parseInt(ratingParam);
              System.out.println("Converted values: bookingId=" + bookingId + 
                           ", guideId=" + guideId + 
                           ", rating=" + rating);
        
            
            if (rating < 1 || rating > 5) {
                request.setAttribute("errorMessage", "Đánh giá phải từ 1 đến 5 sao");
                request.getRequestDispatcher("rating.jsp").forward(request, response);
                return;
            }
            System.out.println(rating);
          
            Review review = new Review();
            review.setBookingId(bookingId);
            review.setGuideId(guideId);
            review.setRating(rating);
            review.setComment(comment);

            ReviewDAO_2 reviewDAO = new ReviewDAO_2();
            boolean success = reviewDAO.addReview(review);

            if (success) {
                request.setAttribute("successMessage", "Cảm ơn bạn đã đánh giá!");
                request.getRequestDispatcher("thank-you.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Có lỗi xảy ra khi lưu đánh giá");
                request.getRequestDispatcher("rating.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ");
            request.getRequestDispatcher("rating.jsp").forward(request, response);
        }
    }
}
