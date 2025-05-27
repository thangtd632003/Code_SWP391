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

            // Lấy các thông tin từ request
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");
            String birthDate = request.getParameter("birthDate");
            String newPassword = request.getParameter("newPassword");
            
            // Validate fullName
            if (fullName == null || fullName.trim().isEmpty()) {
                out.write("{\"success\": false, \"message\": \"Name cannot be empty\"}");
                return;
            }

            // Cập nhật thông tin user
            currentUser.setFullName(fullName.trim());
            currentUser.setEmail(email);
            currentUser.setPhone(phone);
            currentUser.setGender(Gender.valueOf(gender));
            currentUser.setBirthDate(Date.valueOf(birthDate));
            
            // Lưu vào database
            UserDAO_Long userDAO = new UserDAO_Long();
            boolean updated = userDAO.updateUser(currentUser, null, newPassword);
            
            if (updated) {
                // Cập nhật session với thông tin mới
                session.setAttribute("user", currentUser);
                out.write("{\"success\": true, \"message\": \"Settings updated successfully\"}");
            } else {
                out.write("{\"success\": false, \"message\": \"Failed to update settings\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace(); // In ra console để debug
            out.write("{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }
}