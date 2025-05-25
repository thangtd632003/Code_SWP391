/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DBContext;
import dal.tourDao_thang;
import entity.Tour;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thang
 */
public class tourList_thang extends HttpServlet {
   
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
            out.println("<title>Servlet tourList_thang</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet tourList_thang at " + request.getContextPath () + "</h1>");
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
Integer userId = null;
String userRole = null;

if (session != null) {
    User user = (User) session.getAttribute("user");
    if (user != null) {
        userId = user.getId();      // hoặc user.getUserId() tùy thuộc vào định nghĩa của class User
        userRole = user.getRole().toString();   // hoặc user.getUserRole()
    }
}
        if (userId == null || userRole == null || !"GUIDE".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            tourDao_thang dao = new tourDao_thang(conn);
            List<Tour> tours = dao.getToursByGuideId(userId);
            request.setAttribute("tours", tours);
            request.getRequestDispatcher("/Views/thang/tourList.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(tourList_thang.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            Logger.getLogger(tourList_thang.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
Integer userId = null;
String userRole = null;

if (session != null) {
    User user = (User) session.getAttribute("user");
    if (user != null) {
        userId = user.getId();      // hoặc user.getUserId() tùy thuộc vào định nghĩa của class User
        userRole = user.getRole().toString();   // hoặc user.getUserRole()
    }
}
        if (userId == null || userRole == null || !"GUIDE".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if ("delete".equalsIgnoreCase(action) && idParam != null) {
            int tourId = Integer.parseInt(idParam);
            try (Connection conn = new DBContext().getConnection()) {
                tourDao_thang dao = new tourDao_thang(conn);
                dao.deleteTour(tourId);
            } catch (Exception ex) {
                Logger.getLogger(tourList_thang.class.getName()).log(Level.SEVERE, null, ex);
                // bạn có thể set thông báo lỗi vào request/session nếu muốn
            }
            // Sau khi xóa, redirect về chính servlet list
            response.sendRedirect(request.getContextPath() + "/tourList_thang");
        } else if ("update".equalsIgnoreCase(action) && idParam != null) {
            // chuyển sang servlet hoặc JSP xử lý update tour
            response.sendRedirect(request.getContextPath() + "/editTour_thang?id=" + idParam);
        } else if ("add".equalsIgnoreCase(action)) {
            // chuyển sang servlet hoặc JSP xử lý tạo mới tour
            response.sendRedirect(request.getContextPath() + "/createTour_thang");
        } else {
            // action không hợp lệ, quay lại list
            response.sendRedirect(request.getContextPath() + "/tourList_thang");
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
