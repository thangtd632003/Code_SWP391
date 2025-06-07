/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.DBContext;
import dal.userDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author thang
 */
public class ChangePassword_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet ChangePassword</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangePassword at " + request.getContextPath () + "</h1>");
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
        String email = (session != null) ? (String) session.getAttribute("resetEmail") : null;
        String otp = (session != null) ? (String) session.getAttribute("resetOTP") : null;

        if (email == null || otp == null) {
            response.sendRedirect(request.getContextPath() + "/ForgotPassword");
            return;
        }

        // Forward sang JSP nhập OTP + mật khẩu mới
        request.getRequestDispatcher("Views/v1/changePassword.jsp").forward(request, response);
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
        String sessionEmail = (session != null) ? (String) session.getAttribute("resetEmail") : null;
        String sessionOtp = (session != null) ? (String) session.getAttribute("resetOTP") : null;

        if (sessionEmail == null || sessionOtp == null) {
            response.sendRedirect(request.getContextPath() + "/ForgotPassword_servlet");
            return;
        }

        String inputOtp = request.getParameter("otp");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validations
        if (inputOtp == null || !inputOtp.equals(sessionOtp)) {
            request.setAttribute("error", "Code OTP not correct");
            request.getRequestDispatcher("Views/v1/changePassword.jsp").forward(request, response);
            return;
        }
        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New password not match");
            request.getRequestDispatcher("Views/v1/changePassword.jsp").forward(request, response);
            return;
        }

        // Hash mật khẩu mới (ví dụ dùng BCrypt)
      

        // Cập nhật lại password trong DB
        try (Connection conn = new DBContext().getConnection()) {
            userDao dao = new userDao(conn);
            boolean updated = dao.updatePasswordByEmail(sessionEmail, newPassword);

            if (updated) {
                // Xóa thông tin tạm (otp & email) khỏi session
                session.removeAttribute("resetEmail");
                session.removeAttribute("resetOTP");

                // Chuyển về trang login hoặc gửi thông báo thành công
                response.sendRedirect(request.getContextPath() + "/login.jsp?reset=success");
            } else {
                request.setAttribute("error", "Can not update, try againt.");
                request.getRequestDispatcher("Views/v1/changePassword.jsp").forward(request, response);
            }
        } catch (SQLException e) {
             Logger.getLogger(ChangePassword_servlet.class.getName())
              .log(Level.SEVERE, "SQL error khi đổi mật khẩu", e);
            request.setAttribute("error", "Error system, try againt.");
            request.getRequestDispatcher("Views/v1/changePassword.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ChangePassword_servlet.class.getName()).log(Level.SEVERE, null, ex);
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
