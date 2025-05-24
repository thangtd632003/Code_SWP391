/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.DBContext;
import dal.tourDao_thang;
import entity.Tour;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
/**
 *
 * @author thang
 */
public class createTour_thang extends HttpServlet {
   
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
            out.println("<title>Servlet createTour_thang</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet createTour_thang at " + request.getContextPath () + "</h1>");
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
       //  HttpSession session = request.getSession(false);
      //  Integer userId = session != null ? (Integer) session.getAttribute("userId") : null;
        //String userRole = session != null ? (String) session.getAttribute("userRole") : null;
Integer userId=1;
String userRole="guide";
        if (userId == null || userRole == null || !"GUIDE".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Forward đến JSP tạo tour
        request.getRequestDispatcher("/Views/thang/createTour.jsp").forward(request, response);
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
       //  HttpSession session = request.getSession(false);
       // Integer guideId = session != null ? (Integer) session.getAttribute("userId") : null;
       // String userRole = session != null ? (String) session.getAttribute("userRole") : null;
Integer guideId=1;
String userRole="guide";
        if (guideId == null || userRole == null || !"GUIDE".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Đọc dữ liệu form
        String name        = request.getParameter("name");
        String description = request.getParameter("description");
        String itinerary   = request.getParameter("itinerary");
        String priceStr    = request.getParameter("price");
        String maxPeopleStr= request.getParameter("maxPeople");

        // Chuyển kiểu
        BigDecimal price = BigDecimal.ZERO;
        int maxPeople   = 0;
        try {
            price     = new BigDecimal(priceStr);
            maxPeople = Integer.parseInt(maxPeopleStr);
        } catch (NumberFormatException e) {
            // nếu parse lỗi, chuyển về form với báo lỗi
            response.sendRedirect(request.getContextPath() + "/createTour_thang?create=fail");
            return;
        }

        // Build Tour entity
        Tour tour = new Tour();
        tour.setGuideId(guideId);
        tour.setName(name);
        tour.setDescription(description);
        tour.setItinerary(itinerary);
        tour.setPrice(price);
        tour.setMaxPeoplePerBooking(maxPeople);

        // Gọi DAO
        boolean created = false;
        try (Connection conn = new DBContext().getConnection()) {
            tourDao_thang dao = new tourDao_thang(conn);
            created = dao.createTour(tour);
        } catch (SQLException ex) {
            Logger.getLogger(createTour_thang.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(createTour_thang.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Redirect tùy kết quả
        if (created) {
            response.sendRedirect(request.getContextPath() + "/tourList_thang?create=success");
        } else {
            response.sendRedirect(request.getContextPath() + "/createTour_thang?create=fail");
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
