package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            
            if (session != null) {
                session.removeAttribute("user");
                session.invalidate();
            }
            
            response.sendRedirect("login.jsp");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp");
        }
    }
}