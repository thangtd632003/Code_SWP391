/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;
import com.cloudinary.http5.UploaderStrategy;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dal.guideDao;
import dal.DBContext;
import entity.GuideProfile;
import entity.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;


import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thang
 */
@WebServlet(name="GuideProfile", urlPatterns={"/guideProfile_servlet"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB limit


public class guideProfile_servlet extends HttpServlet {
  
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
            out.println("<title>Servlet guideProfile_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet guideProfile_servlet at " + request.getContextPath () + "</h1>");
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
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (!"GUIDE".equals(userRole.toUpperCase())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTravler_servlet");
            System.out.println("THIS WON'T PRINT if redirect worked");

            return;
        }
DBContext dbContext = new DBContext();
        try (Connection conn =  dbContext.getConnection()) {
            guideDao dao = new guideDao(conn);
            GuideProfile profile = dao.getGuideProfileById(userId);
              String update = request.getParameter("update");
    request.setAttribute("updateStatus", update); 
            request.setAttribute("guideProfile", profile);
            request.getRequestDispatcher("/Views/v1/guideProfile.jsp").forward(request, response);
        } catch (SQLException e) {
            Logger.getLogger(guideProfile_servlet.class.getName())
              .log(Level.SEVERE, "SQL error khi đổi mật khẩu", e);
            response.sendError(500, "Internal Server Error");
        } catch (Exception ex) {
            Logger.getLogger(guideProfile_servlet.class.getName()).log(Level.SEVERE, null, ex);
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error");
        return;
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


Map<String, String> config = new HashMap<>();
config.put("cloud_name", "drmuz8amc");
config.put("api_key",
"516948638473583");
config.put("api_secret", "40cUiErsEb9g_KiLRclptLFRKac");

Cloudinary cloudinary = new Cloudinary(config);

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
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String bio = request.getParameter("bio");
        Part avatarPart = request.getPart("avatar");

        String avatarUrl = null;
     if (avatarPart != null && avatarPart.getSize() > 0) {
    // Tạo file tạm
    File tempFile = File.createTempFile("avatar-", ".tmp");
    avatarPart.write(tempFile.getAbsolutePath());
    try {
        Map uploadResult = cloudinary.uploader()
            .upload(tempFile, ObjectUtils.emptyMap());
        avatarUrl = (String) uploadResult.get("secure_url");
    } finally {
        tempFile.delete();  // xóa file tạm
    }
}
DBContext dbContext = new DBContext();
        try (Connection conn = dbContext.getConnection()) {
            guideDao dao = new guideDao(conn);
            GuideProfile profile = dao.getGuideProfileById(userId);
            boolean result;

            if (profile == null) {
                profile = new GuideProfile();
                profile.setGuideId(userId);
                profile.setBio(bio);
                profile.setAvatarUrl(avatarUrl != null ? avatarUrl : ""); // fallback empty
                result = dao.addGuideProfile(profile);
            } else {
                if (avatarUrl != null) {
                    profile.setAvatarUrl(avatarUrl);
                }
                profile.setBio(bio);
                result = dao.updateGuideProfile(profile);
            }

           response.sendRedirect(request.getContextPath()
    + "/guideProfile_servlet?update=" + (result ? "success" : "fail"));
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Internal Server Error");
        } catch (Exception ex) {
            Logger.getLogger(guideProfile_servlet.class.getName()).log(Level.SEVERE, null, ex);
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
