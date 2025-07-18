/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;
import dal.DBContext;
import dal.BookingDao;
import dal.tourDao;
import entity.Booking;
import entity.Tour;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thang
 */
public class UpdateBookingTraveler_servlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

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
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        if (!"traveler".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
            return;
        }
        String bookingIdParam = (String ) request.getParameter("bookingId");
        if (bookingIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
            return;
        }
        int bookingId;
        try {
            bookingId = Integer.parseInt(bookingIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
            return;
        }
        Booking booking = null;
        Tour tour = null;
      try (Connection conn = new DBContext().getConnection()) {
            BookingDao bdao = new BookingDao(conn);
            booking = bdao.getBookingById(bookingId);
            if (booking == null) {
                response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
                return;
            }
            if(booking.getTravelerId()!=user.getId()){
                 response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
                return;
            }
            if (!Integer.valueOf(booking.getTravelerId()).equals(user.getId())){
      
                response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
                return;
              }
            tourDao tdao = new tourDao(conn);
            tour = tdao.getTourById(booking.getTourId());
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UpdateBookingTraveler_servlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (Exception ex) {
            Logger.getLogger(UpdateBookingTraveler_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        String error = (String) request.getAttribute("error");
        request.setAttribute("errorMSG", error);
        request.setAttribute("booking", booking);
        request.setAttribute("tour", tour);
        request.getRequestDispatcher("/Views/v1/UpdateBooking.jsp")
               .forward(request, response);
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
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        if (!"TRAVELER".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
            return;
        }
        String idParam            = request.getParameter("id");
        String numPeopleParam     = request.getParameter("numPeople");
        String contactInfo        = request.getParameter("contactInfo");
        String departureDateParam = request.getParameter("departureDate");
        int bookingId, numPeople;
        java.sql.Date departureDate;
        try {
            bookingId = Integer.parseInt(idParam);
            numPeople = Integer.parseInt(numPeopleParam);
            departureDate = java.sql.Date.valueOf(departureDateParam);
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input data.");
            doGet(request, response);
            return;
        }
        Booking booking = null;
        Tour tour = null;
        try (Connection conn = new DBContext().getConnection()) {
            BookingDao bdao = new BookingDao(conn);
            booking = bdao.getBookingById(bookingId);
            if (booking == null || booking.getTravelerId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
                return;
            }
            tour = new tourDao(conn).getTourById(booking.getTourId());
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UpdateBookingTraveler_servlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (Exception ex) {
            Logger.getLogger(UpdateBookingTraveler_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (numPeople < 1 || numPeople > tour.getMaxPeoplePerBooking()) {
            request.setAttribute("error", "Number of people must be between 1 and " +
                                          tour.getMaxPeoplePerBooking() + ".");
            doGet(request, response);
            return;
        }
        booking.setNumPeople(numPeople);
        booking.setContactInfo(contactInfo);
        booking.setDepartureDate(departureDate);
        boolean success = false;
        try (Connection conn = new DBContext().getConnection()) {
            success = new BookingDao(conn).updateBooking(booking);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UpdateBookingTraveler_servlet.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (Exception ex) {
            Logger.getLogger(UpdateBookingTraveler_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!success) {
            request.setAttribute("error", "Update failed. Please try again.");
            doGet(request, response);
            return;
        }
   
String redirectUrl = request.getContextPath()
    + "/UpdateBookingTraveler_servlet?bookingId=" + bookingId;
response.sendRedirect(redirectUrl);

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
