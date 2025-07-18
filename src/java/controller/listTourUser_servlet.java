/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;

import dal.DBContext;
import dal.tourDao;
import entity.Tour;
import entity.User;
import entity.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author thang
 */
public class listTourUser_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet listTourUser_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet listTourUser_servlet at " + request.getContextPath () + "</h1>");
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
        if (current.getRole() != Role.TRAVELER) {
            if (current.getRole() == Role.GUIDE) {
                response.sendRedirect(request.getContextPath() + "/ProfileGuide_servlet");
            } else {
                response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
            }
            return;
        }

        String keyword = request.getParameter("keyword");
        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());

        try (Connection conn = new DBContext().getConnection()) {
            tourDao dao = new tourDao(conn);

            List<Tour> allTours = hasKeyword
                    ? dao.searchToursUsers(keyword.trim())
                    : dao.getAllToursUsers();

            List<Tour> topTours = dao.getTop10ByBookings();

            List<Tour> userBookedTours = dao.getToursBookedByUser(current.getId());

            request.setAttribute("allTours", allTours);
            request.setAttribute("topTours", topTours);
            request.setAttribute("userBookedTours", userBookedTours);
            request.setAttribute("keyword", hasKeyword ? keyword.trim() : "");

            request.getRequestDispatcher("/Views/v1/listTourUser.jsp")
                   .forward(request, response);
        }
        catch (SQLException ex) {
            throw new ServletException("Không thể truy vấn Tour.", ex);
        } catch (Exception ex) {
            throw new ServletException("Lỗi không xác định.", ex);
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
          String action = request.getParameter("action");
        String tourIdStr = request.getParameter("tourId");
        if (tourIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/listTourUser_servlet");
            return;
        }
        int tourId = Integer.parseInt(tourIdStr);

        if ("detail".equalsIgnoreCase(action)) {
            // Chuyển hướng sang trang chi tiết tour
            response.sendRedirect(request.getContextPath() + "/detailTourUser_servlet?tourId=" + tourId);
        }
        else if ("book".equalsIgnoreCase(action)) {
            // Chuyển hướng sang trang tạo Booking (giả sử gọi servlet CreateBooking_servlet)
            response.sendRedirect(request.getContextPath() + "/createBooking_servlet?tourId=" + tourId);
        }
        else {
            response.sendRedirect(request.getContextPath() + "/listTourUser_servlet");
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
