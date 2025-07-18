/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.DBContext;
import dal.ReportDao;
import entity.User;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.Properties;
/**
 *
 * @author thang
 */

public class ReportServlet_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet ReportServlet_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReportServlet_servlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 
        private ReportDao dao;

 @Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection conn = new DBContext().getConnection();
            dao = new ReportDao(conn);
        } catch (Exception e) {
            throw new ServletException("Unable to initialize ReportDao", e);
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
        User user = session != null ? (User) session.getAttribute("user") : null;
        System.out.print("dat ka"+user);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
    
       request.setAttribute("msg", request.getParameter("msg"));
        request.getRequestDispatcher("/Views/v1/reportScreen.jsp")
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
        User user = session != null ? (User)session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String feedback = request.getParameter("feedback");
        String adminEmail;
        try {
            adminEmail = dao.getFirstAdminEmail();
        } catch (Exception e) {
            throw new ServletException("Cannot fetch admin email", e);
        }

        boolean sent = false;
        String error = null;
        if (adminEmail != null && !adminEmail.isBlank()) {
            try {
                sendEmail(adminEmail,
                          "Report Website: Connect Traveler and Guide",
                          buildBody(user.getId(), user.getEmail(), feedback));
                sent = true;
            } catch (Exception e) {
                error = e.getMessage();
            }
        } else {
            error = "No admin email found";
        }

        String msg = sent
           ? "reportSent"
           : "reportFail" + (error != null ? ("_"+error) : "");
        response.sendRedirect(request.getContextPath() + "/report_page?msg=" + msg);
    }
  private String buildBody(int userId, String userEmail, String feedback){
        return new StringBuilder()
            .append("User ID: ").append(userId).append("\n")
            .append("User Email: ").append(userEmail).append("\n\n")
            .append("Feedback:\n")
            .append(feedback)
            .toString();
    }
  private void sendEmail(String to, String subject, String body) throws MessagingException {
        // configure your SMTP settings
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session mailSession = Session.getInstance(props, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("quizlet875@gmail.com","fcrg hpnd xcmt hfye");
            }
        });

        Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("quizlet875@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setText(body);

        Transport.send(message);
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
