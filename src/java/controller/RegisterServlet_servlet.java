/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.UserDAO_2;
import entity.Role;
import entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "RegisterServlet_duy", urlPatterns = {"/RegisterServlet_duy"})
public class RegisterServlet_servlet extends HttpServlet {
    private final UserDAO_2 userDao = new UserDAO_2();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register_duy.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String roleParam = request.getParameter("role");

            if (userDao.emailExists(email)) {
                request.setAttribute("error", "Email already exists. Please use another email.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            Role role = Role.valueOf(roleParam.toUpperCase());

            User user = new User();
            user.setEmail(email);
            user.setPasswordHash(password); // Bạn có thể mã hóa mật khẩu tại đây
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setRole(role);

            boolean registered = userDao.registerUser(user);

            if (registered) {
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid role.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred. Please try again later.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Xử lý đăng ký người dùng";
    }
}
