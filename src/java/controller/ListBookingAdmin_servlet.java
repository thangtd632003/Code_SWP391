/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.BookingAdminDao;
import dal.DBContext;
import entity.Booking;
import entity.Role;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;

/**
 *
 * @author thang
 */
public class ListBookingAdmin_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet ListBookingAdmin_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListBookingAdmin_servlet at " + request.getContextPath () + "</h1>");
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
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User current = (User) session.getAttribute("user");
        if (!current.getRole().name().equalsIgnoreCase("ADMIN") ) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
            return;
        }

        String keyword   = request.getParameter("keyword");
        String sortField = request.getParameter("sortField");
        String sortDir   = request.getParameter("sortDir");
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasSort    = sortField   != null && !sortField.trim().isEmpty();
        boolean sortAsc    = !"desc".equalsIgnoreCase(sortDir);

        try (Connection conn = new DBContext().getConnection()) {
            BookingAdminDao dao = new BookingAdminDao(conn);
            List<Booking> bookings;

            if (!hasKeyword && !hasSort) {
                bookings = dao.getAllBookings();
            } else if (hasKeyword && !hasSort) {
                bookings = dao.searchBookings(keyword.trim());
            } else if (!hasKeyword && hasSort) {
                bookings = dao.sortBookingsByField(sortField.trim(), sortAsc);
            } else {
                bookings = dao.searchAndSortBookings(keyword.trim(), sortField.trim(), sortAsc);
            }

            // 5. Push to JSP
            request.setAttribute("bookings", bookings);
            request.setAttribute("keyword",   hasKeyword ? keyword.trim() : "");
            request.setAttribute("sortField", hasSort    ? sortField.trim() : "");
            request.setAttribute("sortDir",   hasSort    ? (sortAsc ? "asc" : "desc") : "");

            request.getRequestDispatcher("/Views/listBookingAdmin.jsp")
                   .forward(request, response);
        }
        catch (SQLException e) {
            throw new ServletException("Cannot query bookings", e);
        } catch (Exception ex) {
            Logger.getLogger(ListBookingAdmin_servlet.class.getName()).log(Level.SEVERE, null, ex);
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
        processRequest(request, response);
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
