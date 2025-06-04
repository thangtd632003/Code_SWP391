/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.DBContext;
import dal.tourDao_thang;
import entity.Tour;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author thang
 */
public class listTourAdmin_thang extends HttpServlet {
   
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
            out.println("<title>Servlet listTourAdmin_thang</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet listTourAdmin_thang at " + request.getContextPath () + "</h1>");
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
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        if (!"ADMIN".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTravler_thang");
            return;
        }

        // 2. Đọc param search + sort + page (nếu cần phân trang)
        String keyword   = request.getParameter("keyword");     // có thể null hoặc rỗng
        String sortField = request.getParameter("sortField");   // id, name, price, ...
        String sortDir   = request.getParameter("sortDir");     // asc hoặc desc
        boolean sortAsc  = !"desc".equalsIgnoreCase(sortDir);   // nếu sortDir=desc thì false, ngược lại true

        // 3. Gọi DAO để lấy danh sách search + sort
        List<Tour> tours = null;
        try (Connection conn = new DBContext().getConnection()) {
            tourDao_thang dao = new tourDao_thang(conn);
            tours = dao.searchAndSortTours(keyword, sortField, sortAsc);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(listTourAdmin_thang.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (Exception ex) {
            Logger.getLogger(listTourAdmin_thang.class.getName()).log(Level.SEVERE, null, ex);
        }

        // 4. Set lại attribute để JSP biết đang search/sort gì
        request.setAttribute("tours", tours);
        request.setAttribute("keyword",   (keyword == null) ? "" : keyword.trim());
        request.setAttribute("sortField", (sortField == null) ? "" : sortField.trim().toLowerCase());
        request.setAttribute("sortDir",   sortAsc ? "asc" : "desc");

        // 5. Forward về JSP
        request.getRequestDispatcher("/Views/thang/listTourAdmin.jsp")
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

        if (!user.getRole().name().toString().equalsIgnoreCase("ADMIN")) {
            response.sendRedirect(request.getContextPath() + "/ProfileTravler_thang");
            return;
        }

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if ("changeStatus".equalsIgnoreCase(action) && idParam != null) {
            try {
                int tourId = Integer.parseInt(idParam);
                try (Connection conn = new DBContext().getConnection()) {
                    tourDao_thang dao = new tourDao_thang(conn);
                    dao.toggleTourStatus(tourId);
                } catch (Exception ex) {
                    Logger.getLogger(listTourAdmin_thang.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(listTourAdmin_thang.class.getName()).log(Level.SEVERE, null, ex);
                // Không đặt sonRedirect ngay; vẫn quay lại list để hiện lỗi (nếu có)
            }
            // Sau khi đổi status, về lại doGet để load lại
            response.sendRedirect(request.getContextPath() + "/listTourAdmin_thang");
        }
        else if ("view".equalsIgnoreCase(action) && idParam != null) {
            // Xem chi tiết tour, chuyển sang servlet tourDetailAdmin_thang
            response.sendRedirect(request.getContextPath() + "/tourDetail_thang?id=" + idParam);
        }
        else {
            // Nếu action không hợp lệ, quay lại list
            response.sendRedirect(request.getContextPath() + "/listTourAdmin_thang");
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
