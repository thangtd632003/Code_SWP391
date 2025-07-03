/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.BookingDao;
import dal.DBContext;
import dal.tourDao;
import dal.userDao;
import entity.Booking;
import entity.Tour;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;

/**
 *
 * @author thang
 */
public class detailBookingAdmin_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet detailBookingAdmin_thang</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet detailBookingAdmin_thang at " + request.getContextPath () + "</h1>");
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
        User current = session != null ? (User) session.getAttribute("user") : null;
        if (current == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!current.getRole().name().equalsIgnoreCase("ADMIN")) {
            response.sendRedirect(request.getContextPath() + "/profileTraveler_servlet");
            return;
        }

    
        String idParam = request.getParameter("bookingId");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/ListBookingAdmin_servlet");
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            int bookingId = Integer.parseInt(idParam);

            BookingDao bookingDao = new BookingDao(conn);
            Booking booking = bookingDao.getBookingById(bookingId);
            if (booking == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found");
                return;
            }

            tourDao tourDao = new tourDao(conn);
            Tour tour = tourDao.getTourById(booking.getTourId());

            userDao userDao = new userDao(conn);
            User traveler = userDao.getUserById(booking.getTravelerId());
            User guide    = tour != null
                           ? userDao.getUserById(tour.getGuideId())
                           : null;
System.out.println("Booking: " + booking);
System.out.println("Tour: " + tour);
System.out.println("Traveler: " + traveler);
System.out.println("Guide: " + guide);

            request.setAttribute("booking",  booking);
            request.setAttribute("tour",      tour);
            request.setAttribute("traveler",  traveler);
            request.setAttribute("guide",     guide);
            
            request.getRequestDispatcher("/Views/v1/detailBookingAdmin.jsp")
                   .forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(detailBookingAdmin_servlet.class.getName())
                  .log(Level.SEVERE, "Error fetching detail for bookingId=" + idParam, ex);
            throw new ServletException("Database error", ex);
        } catch (NumberFormatException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bookingId");
        } catch (Exception ex) {
            Logger.getLogger(detailBookingAdmin_servlet.class.getName()).log(Level.SEVERE, null, ex);
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
