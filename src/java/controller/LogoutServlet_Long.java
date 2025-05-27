package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet_Long extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy session hiện tại
            HttpSession session = request.getSession(false);
            
            // Kiểm tra session có tồn tại không
            if (session != null) {
                // Xóa các attribute trong session
                session.removeAttribute("user");
                // Hủy session
                session.invalidate();
            }
            
            // Chuyển hướng về trang login
            response.sendRedirect("login.jsp");
            
        } catch (Exception e) {
            // Log lỗi nếu có
            e.printStackTrace();
            // Chuyển hướng đến trang error hoặc login trong trường hợp có lỗi
            response.sendRedirect("login.jsp");
        }
    }
}