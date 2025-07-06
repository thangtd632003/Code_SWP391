/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dal.BookingDao;
import dal.ReviewDao;
import entity.Booking;
import entity.Review;
import entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author thang
 */
public class ReviewBookingTraveler_servlet extends HttpServlet {
    private BookingDao bookingDao;
    private ReviewDao reviewDao;
  public void init() throws ServletException {
        super.init();
        try {
            Connection conn = new dal.DBContext().getConnection();
            bookingDao = new BookingDao(conn);
            reviewDao  = new ReviewDao(conn);
        } catch (Exception e) {
            throw new ServletException("Unable to initialize DAOs", e);
        }
    }
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ReviewBookingTraveler_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReviewBookingTraveler_servlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       HttpSession session = request.getSession(false);
        User user = session != null ? (User)session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!"traveler".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileGuide_thang");
            return;
        }
        String idParam = request.getParameter("bookingId");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/ListBookingTraveler_thang");
            return;
        }
        int bookingId = Integer.parseInt(idParam);
        try {
            Booking booking = bookingDao.getBookingById(bookingId);
            if (booking == null || booking.getTravelerId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/ListBookingTraveler_thang");
                return;
            }
            List<Review> reviews = reviewDao.getReviewsByBookingId(bookingId);

            request.setAttribute("booking", booking);
            request.setAttribute("reviews", reviews);
            request.setAttribute("msg", request.getParameter("msg"));
            request.getRequestDispatcher("/Views/v1/ReviewBookingTraveler.jsp")
                   .forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(ReviewBookingTraveler_servlet.class.getName())
                  .log(Level.SEVERE, "Error in doGet", ex);
            throw new ServletException(ex);
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
          HttpSession session = request.getSession(false);
        User user = session != null ? (User)session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!"traveler".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileGuide_thang");
            return;
        }
        String idParam = request.getParameter("bookingId");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/ListBookingTraveler_thang");
            return;
        }
        int bookingId = Integer.parseInt(idParam);
        try {
            Booking booking = bookingDao.getBookingById(bookingId);
            if (booking == null || booking.getTravelerId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/ListBookingTraveler_thang");
                return;
            }
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            Review r = new Review();
            r.setBookingId(bookingId);
            r.setGuideId(booking.getTourId()); // lưu guideId đúng entity nếu Tour được join
            r.setRating(rating);
            r.setComment(comment);

            boolean ok = reviewDao.addReview(r);

            String msg = ok ? "reviewOk" : "reviewFail";
            response.sendRedirect(request.getContextPath()
                + "/ReviewBookingTraveler_servlet?bookingId=" + bookingId + "&msg=" + msg);

        } catch (Exception ex) {
            Logger.getLogger(ReviewBookingTraveler_servlet.class.getName())
                  .log(Level.SEVERE, "Error in doPost", ex);
            throw new ServletException(ex);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
