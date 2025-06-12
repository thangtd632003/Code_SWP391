/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;
import dal.DBContext;
import dal.userDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import entity.User;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author thang
 */
public class ForgotPassword_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet ForgotPassword_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ForgotPassword_servlet at " + request.getContextPath () + "</h1>");
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
     private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    // Thay bằng email + mật khẩu ứng dụng (app password) của bạn:
    private static final String SMTP_USERNAME = "quizlet875@gmail.com";
    private static final String SMTP_PASSWORD = "fcrg hpnd xcmt hfye";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
      HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user != null) {
            response.sendRedirect(request.getContextPath() + "/ProfileTravler_servlet");
            return;
        }

        // Nếu user đã login, forward đến trang nhập email
        request.getRequestDispatcher("/Views/v1/forgotPassword.jsp").forward(request, response);
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

        if (user != null) {
            response.sendRedirect(request.getContextPath() + "/ProfileTravler_servlet");
            return;
        }

        String email = request.getParameter("email");
        if (email == null || email.isEmpty()) {
            request.setAttribute("error", "Email not null.");
            request.getRequestDispatcher("/Views/v1/forgotPassword.jsp").forward(request, response);
            return;
        }

        // Kiểm tra email có tồn tại không
        try (Connection conn = new DBContext().getConnection()) {
            userDao dao = new userDao(conn);
            boolean exists = dao.emailExists(email);

            if (!exists) {
                request.setAttribute("error", "Email not exits.");
                request.getRequestDispatcher("/Views/v1/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Nếu email tồn tại -> tạo mã OTP ngẫu nhiên (6 chữ số)
            String otp = generateOTP(6);

            session.setAttribute("resetEmail", email);
            session.setAttribute("resetOTP", otp);

            // Gửi mã OTP qua email
            boolean mailSent = sendOtpEmail(email, otp);
            if (!mailSent) {
                request.setAttribute("error", "Send email error. Try again.");
                request.getRequestDispatcher("/Views/v1/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Điều hướng sang trang nhập OTP & mật khẩu mới
            response.sendRedirect(request.getContextPath() + "/ChangePassword_servlet");
        } catch (Exception e) {
           Logger.getLogger(ForgotPassword_servlet.class.getName())
              .log(Level.SEVERE, "SQL error khi đổi mật khẩu", e);
            response.sendRedirect(request.getContextPath() + "/forgotPassword?error=server");
        }
    }

    // Hàm sinh OTP (chuỗi gồm 'length' chữ số ngẫu nhiên)
    private String generateOTP(int length) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10)); // 0-9
        }
        return sb.toString();
    }

    // Hàm dùng Jakarta Mail (JavaMail) để gửi email có mã OTP
    private boolean sendOtpEmail(String toEmail, String otp) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
props.put("mail.smtp.starttls.required", "true");
            Session mailSession = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });
mailSession.setDebug(true);
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(SMTP_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã xác thực khôi phục mật khẩu");
            message.setText("Mã xác thực của bạn là: " + otp + "\n\n"
                    + "Vui lòng không cung cấp mã này cho bất cứ ai.\n"
                    + "Nếu bạn không yêu cầu khôi phục mật khẩu, có thể bỏ qua email này.");

            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
