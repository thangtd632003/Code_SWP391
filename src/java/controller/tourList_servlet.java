/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DBContext;
import dal.tourDao;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thang
 */
public class tourList_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet tourList_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet tourList_servlet at " + request.getContextPath () + "</h1>");
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
                userId = user.getId();
                userRole = user.getRole().toString();
            }
        }
        if (userId == null || userRole == null || !"GUIDE".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String keyword = request.getParameter("keyword");       
        String sortField = request.getParameter("sortField");   
        String sortDir = request.getParameter("sortDir");       
        boolean sortAsc = !"desc".equalsIgnoreCase(sortDir);

        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        boolean hasSort = (sortField != null && !sortField.trim().isEmpty());

        try (Connection conn = new DBContext().getConnection()) {
            tourDao dao = new tourDao(conn);
            List<Tour> tours;

            if (!hasKeyword && !hasSort) {
                // Trường hợp: CHƯA NHẬP GÌ  lấy tất cả tour của guide
                tours = dao.getToursByGuideId(userId);

            } else if (hasKeyword && !hasSort) {
                // Chỉ Search (có keyword, chưa Sort)
                tours = dao.searchToursByGuideId(userId, keyword.trim());

            } else if (!hasKeyword && hasSort) {
                // Chỉ Sort (chưa Search)
                tours = dao.sortToursByGuideId(userId, sortField.trim(), sortAsc);

            } else {
               
                tours = dao.searchAndSortToursByGuideId(userId, keyword.trim(), sortField.trim(), sortAsc);

            
            }

            //  Đẩy về JSP
         request.setAttribute("tours", tours);
request.setAttribute("keyword", hasKeyword ? keyword.trim() : "");

if (hasSort) {
    String fieldLower = sortField.trim().toLowerCase();
    request.setAttribute("sortField", fieldLower);
    request.setAttribute("sortDir",   sortAsc ? "asc" : "desc");
} else {
    request.setAttribute("sortField", "");
    request.setAttribute("sortDir",   "");
}

            request.getRequestDispatcher("/Views/v1/tourList.jsp")
                   .forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(tourList_servlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            Logger.getLogger(tourList_servlet.class.getName()).log(Level.SEVERE, null, ex);
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
        userId = user.getId();      
        userRole = user.getRole().toString();  
    }
}
        if (userId == null || userRole == null || !"GUIDE".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if ("delete".equalsIgnoreCase(action) && idParam != null) {
            int tourId = Integer.parseInt(idParam);
            try (Connection conn = new DBContext().getConnection()) {
                tourDao dao = new tourDao(conn);
                dao.toggleTourStatus(tourId);
            } catch (Exception ex) {
                Logger.getLogger(tourList_servlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect(request.getContextPath() + "/tourList_servlet");
        } else if ("update".equalsIgnoreCase(action) && idParam != null) {
            response.sendRedirect(request.getContextPath() + "/editTour_servlet?id=" + idParam);
        } else if ("add".equalsIgnoreCase(action)) {
            response.sendRedirect(request.getContextPath() + "/createTour_servlet");
        } else {
            response.sendRedirect(request.getContextPath() + "/tourList_servlet");
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
