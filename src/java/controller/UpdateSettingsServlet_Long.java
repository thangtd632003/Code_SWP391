package controller;

import dal.UserDAO_Long;
import entity.User;
import entity.Gender;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

@WebServlet("/update-settings")
public class UpdateSettingsServlet_Long extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            
            if (currentUser == null) {
                out.write("{\"success\": false, \"message\": \"User not logged in\"}");
                return;
            }

            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            
            // Cập nhật thông tin user
            currentUser.setFullName(request.getParameter("fullName"));
            currentUser.setEmail(request.getParameter("email"));
            currentUser.setPhone(request.getParameter("phone"));
            currentUser.setGender(Gender.valueOf(request.getParameter("gender")));
            currentUser.setBirthDate(Date.valueOf(request.getParameter("birthDate")));
            
            // Lưu vào database
            UserDAO_Long userDAO = new UserDAO_Long();
            boolean updated = userDAO.updateUser(currentUser, currentPassword, newPassword);
            
            if (updated) {
                session.setAttribute("user", currentUser);
                out.write("{\"success\": true}");
            } else {
                out.write("{\"success\": false, \"message\": \"Failed to update settings. Please check your current password.\"}");
            }
            
        } catch (Exception e) {
            out.write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}