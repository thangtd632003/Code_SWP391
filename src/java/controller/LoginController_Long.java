package controller;

import dal.UserDAO_Long;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController_Long extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra xem user đã đăng nhập chưa
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp"); 
            return;
        }

        // Kiểm tra cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String savedEmail = null;
            String savedPassword = null;
            
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("email")) {
                    savedEmail = cookie.getValue();
                }
                if (cookie.getName().equals("password")) {
                    savedPassword = cookie.getValue();
                }
            }
            
            if (savedEmail != null && savedPassword != null) {
                UserDAO_Long dao = new UserDAO_Long();
                User user = dao.login(savedEmail, savedPassword);
                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    dao.updateLastLogin(user.getId());
                    

                    // Sửa phần chuyển hướng trong switch case
                    switch (user.getRole()) {
                        case ADMIN:
                            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
                            break;
                        case GUIDE:
                            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
                            break;
                        default:
                            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
                    }
                    return;
                }
            }
        }
        
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String remember = request.getParameter("remember");

            UserDAO_Long dao = new UserDAO_Long();
            User user = dao.login(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                dao.updateLastLogin(user.getId());

                if ("on".equals(remember)) {
                    Cookie emailCookie = new Cookie("email", email);
                    Cookie passwordCookie = new Cookie("password", password);
                    
                    emailCookie.setMaxAge(30 * 24 * 60 * 60);
                    passwordCookie.setMaxAge(30 * 24 * 60 * 60);
                    
                    emailCookie.setHttpOnly(true);
                    passwordCookie.setHttpOnly(true);
                    
                    response.addCookie(emailCookie);
                    response.addCookie(passwordCookie);
                } else {
                    Cookie emailCookie = new Cookie("email", "");
                    Cookie passwordCookie = new Cookie("password", "");
                    
                    emailCookie.setMaxAge(0);
                    passwordCookie.setMaxAge(0);
                    
                    response.addCookie(emailCookie);
                    response.addCookie(passwordCookie);
                }

                // Chuyển hướng dựa trên role
                // Sửa phần chuyển hướng trong switch case
                switch (user.getRole()) {
                    case ADMIN:
                        response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
                        break;
                    case GUIDE:
                        response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
                        break;
                    default:
                        response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
                }
            } else {
                request.setAttribute("notice", "Email hoặc mật khẩu không chính xác!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Thêm logging
            e.printStackTrace(); 
            request.setAttribute("notice", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}