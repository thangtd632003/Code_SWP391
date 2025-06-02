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
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard"); // Đúng: chuyển sang servlet
            return;
        } 
        handleCookieLogin(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String remember = request.getParameter("remember");

            UserDAO_Long dao = new UserDAO_Long();
            User user = dao.login(email, password);

            if (user == null) {
                response.getWriter().write("{\"success\": false}");
                return;
            }

            // Tạo session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            // Cập nhật last login
            dao.updateLastLogin(user.getId());

            // Xử lý remember me
            if ("on".equals(remember)) {
                addCookie(response, "email", email, 30 * 24 * 60 * 60);
                addCookie(response, "password", password, 30 * 24 * 60 * 60);
            } else {
                addCookie(response, "email", "", 0);
                addCookie(response, "password", "", 0);
            }

            // Đúng: chuyển sang servlet dashboard
            response.getWriter().write("{\"success\": true, \"redirect\": \"" + request.getContextPath() + "/dashboard\"}");
            
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false}");
        }
    }

    private void handleCookieLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String savedEmail = null;
            String savedPassword = null;
            
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("email")) savedEmail = cookie.getValue();
                if (cookie.getName().equals("password")) savedPassword = cookie.getValue();
            }
            
            if (savedEmail != null && savedPassword != null) {
                UserDAO_Long dao = new UserDAO_Long();
                User user = dao.login(savedEmail, savedPassword);
                if (user != null) {
                    // Tạo session cho auto login
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    
                    // Cập nhật last login
                    dao.updateLastLogin(user.getId());
                    
                    response.sendRedirect(request.getContextPath() + "/dashboard"); // Đúng: chuyển sang servlet
                    return;
                }
            }
        }
        
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}