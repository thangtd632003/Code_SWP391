/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.UserDAO_duy;
import entity.Role;
import entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "RegisterServlet_duy", urlPatterns = {"/RegisterServlet_duy"})
public class RegisterServlet_duy extends HttpServlet {
    private final UserDAO_duy userDao = new UserDAO_duy();

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

            // Kiểm tra xem email đã tồn tại chưa
            if (userDao.emailExists(email)) {
                request.setAttribute("error", "Email đã tồn tại. Vui lòng sử dụng email khác.");
                request.getRequestDispatcher("register_duy.jsp").forward(request, response);
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
                request.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại.");
                request.getRequestDispatcher("register_duy.jsp").forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Vai trò không hợp lệ.");
            request.getRequestDispatcher("register_duy.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại sau.");
            request.getRequestDispatcher("register_duy.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Xử lý đăng ký người dùng";
    }
}
