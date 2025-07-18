/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.sql.Connection;

import dal.DBContext;
import dal.userDao;
import entity.Gender;
import entity.Role;
import entity.Status;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;

/**
 *
 * @author thang
 */
public class ProfileTravler_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet ProfileTravler_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileTravler_servlet at " + request.getContextPath () + "</h1>");
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
       String email=null;
       HttpSession session = request.getSession();
       User userSession=(User) session.getAttribute("user");
       if(userSession !=null){
        email= userSession.getEmail();}

        if (email == null) {
            response.sendRedirect("login.jsp");
           return;
       }
    
       try (Connection conn = new DBContext().getConnection()) {
        userDao userDAO = new userDao(conn);
        User user = userDAO.getUserByEmail(email);
 String upd = request.getParameter("update");
        if ("success".equals(upd)) {
            request.setAttribute("message", "Cập nhật thành công!");
            request.setAttribute("msgType", "success");
        } else if ("fail".equals(upd)) {
            request.setAttribute("message", "Cập nhật thất bại.");
            request.setAttribute("msgType", "error");
        }
        if (user != null) {
            request.setAttribute("user", user);
            request.getRequestDispatcher("Views/v1/profile.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
        }
    } catch (Exception e) {
        throw new ServletException(e);
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
   String email=null;
       HttpSession session = request.getSession();
       User userSession=(User) session.getAttribute("user");
       if(userSession !=null){
        email= userSession.getEmail();}

        if (email == null) {
            response.sendRedirect("login.jsp");
           return;
       }
    
     
         User updatedUser = new User();
    updatedUser.setFullName(request.getParameter("fullName"));
    updatedUser.setPhone(request.getParameter("phone"));
    updatedUser.setRole(Role.valueOf(request.getParameter("role").toUpperCase()));
    updatedUser.setStatus(Status.valueOf(request.getParameter("status").toUpperCase()));
String genderParam = request.getParameter("gender");
if (genderParam != null && !genderParam.isEmpty()) {
    updatedUser.setGender(Gender.valueOf(genderParam.toUpperCase()));
} else {
    updatedUser.setGender(null); // nếu không chọn
}

String birthDateParam = request.getParameter("birthDate");
if (birthDateParam != null && !birthDateParam.isEmpty()) {
    updatedUser.setBirthDate(Date.valueOf(birthDateParam)); // java.sql.Date.valueOf
} else {
    updatedUser.setBirthDate(null);
}
    try (Connection conn = new DBContext().getConnection()) {
        userDao userDAO = new userDao(conn);
        boolean success = userDAO.updateUserByEmail(email, updatedUser);

    if (success) {
    response.sendRedirect(request.getContextPath() 
        + "/ProfileTravler_servlet?update=success");
} else {
    response.sendRedirect(request.getContextPath() 
        + "/ProfileTravler_servlet?update=fail");
}


    } catch (Exception e) {
        throw new ServletException(e);
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
