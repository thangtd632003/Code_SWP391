/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import dal.DBContext;
import dal.userDao;
import entity.User;
import entity.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
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
public class userList_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet userList_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet userList_servlet at " + request.getContextPath () + "</h1>");
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
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        User current = (User) session.getAttribute("user");
        if (current.getRole() != Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/profileTraveler_servlet");
            return;
        }

        //  Đọc các tham số search / sort
        String keyword   = request.getParameter("keyword");      
        String sortField = request.getParameter("sortField");    
        String sortDir   = request.getParameter("sortDir");      
        boolean sortAsc  = !"desc".equalsIgnoreCase(sortDir);

        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        boolean hasSort    = (sortField != null && !sortField.trim().isEmpty());

        try (Connection conn = new DBContext().getConnection()) {
            userDao dao = new userDao(conn);
            List<User> users;

            if (!hasKeyword && !hasSort) {
                // Lấy toàn bộ, mặc định sort theo updated_at DESC
                users = dao.sortUsersByUpdatedAt(false);
            }
            else if (hasKeyword && !hasSort) {
                users = dao.searchUsers(keyword.trim());
            }
            else if (!hasKeyword && hasSort) {
                // Vì chỉ hỗ trợ sort theo updated_at, ta bỏ qua sortField khác
                users = dao.sortUsersByUpdatedAt(sortAsc);
            }
            else {
                // Có cả search + sort
                users = dao.searchAndSortUsers(keyword.trim(), sortAsc);
            }

            //  Truyền sang JSP
            request.setAttribute("users", users);
            request.setAttribute("keyword", hasKeyword ? keyword.trim() : "");
            request.setAttribute("sortField", hasSort ? sortField : "");
            request.setAttribute("sortDir", hasSort ? (sortAsc ? "asc" : "desc") : "");
            request.setAttribute("currentRole", current.getRole().name());

            request.getRequestDispatcher("/Views/v1/userList.jsp")
                   .forward(request, response);
        }
        catch (SQLException ex) {
            throw new ServletException("Không thể lấy danh sách User", ex);
        }
        catch (Exception ex) {
            throw new ServletException("Lỗi không xác định", ex);
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
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        User current = (User) session.getAttribute("user");
        if (current.getRole() != Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/profileTraveler_servlet");
            return;
        }

        // Lấy action
        String action = request.getParameter("action");
        try (Connection conn = new DBContext().getConnection()) {
            userDao dao = new userDao(conn);

            if ("changeStatus".equalsIgnoreCase(action)) {
                int uid = Integer.parseInt(request.getParameter("id"));
                String statusParam = request.getParameter("newStatus"); // "ACTIVE" hoặc "LOCKED"
                boolean ok = dao.changeStatusById(uid, entity.Status.valueOf(statusParam));
                response.sendRedirect(request.getContextPath()
                    + "/userList_servlet?msg=" + (ok ? "statusOk" : "statusFail"));
            }
            else if ("detail".equalsIgnoreCase(action)) {
                // Chỉ đơn giản redirect tới servlet detail, hoặc bạn có thể forward luôn
                int id = Integer.parseInt(request.getParameter("id"));
         
    response.sendRedirect(request.getContextPath() + "/userDetail_servlet?id=" + id);



            }
            else {
                response.sendRedirect(request.getContextPath() + "/userList_servlet");
            }
        }
        catch (SQLException ex) {
            throw new ServletException("Lỗi thao tác với User", ex);
        } catch (Exception ex) {
            Logger.getLogger(userList_servlet.class.getName()).log(Level.SEVERE, null, ex);
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
