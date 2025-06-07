/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;


import dal.DBContext;
import dal.userDao;
import entity.User;
import entity.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author thang
 */
public class userDetail_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet userDetail_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet userDetail_servlet at " + request.getContextPath () + "</h1>");
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

        String idParam =  request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/userList_servlet");
            return;
        }
        int uid = Integer.parseInt(idParam);

        try (Connection conn = new DBContext().getConnection()) {
            userDao dao = new userDao(conn);
            User u = dao.getUserById(uid);
            if (u == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
                return;
            }
            request.setAttribute("userDetail", u);
            request.getRequestDispatcher("/Views/v1/userDetail.jsp")
                   .forward(request, response);
        } catch (Exception ex) {
            throw new ServletException("Lỗi khi lấy chi tiết User", ex);
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
