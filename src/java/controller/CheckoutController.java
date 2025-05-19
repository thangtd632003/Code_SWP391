/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author long
 */
@WebServlet(name = "CheckoutController", urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            HttpSession session = request.getSession();
            Account acc = (Account) session.getAttribute("acc");
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_Amount = request.getParameter("vnp_Amount");

            if (acc != null && "00".equals(vnp_ResponseCode) && vnp_Amount != null) {
                // Số tiền VNPAY trả về là x100, cần chia cho 100
                int amount = Integer.parseInt(vnp_Amount) / 100;

                // Cộng tiền vào tài khoản
                AccountDAO accountDAO = new AccountDAO();
                accountDAO.addBalance(acc.getId(), amount);

                // Cập nhật lại balance trong session nếu cần
                acc.setBalance(acc.getBalance() + amount);
                session.setAttribute("acc", acc);

                request.setAttribute("successMsg", "Nạp tiền thành công: " + amount + " VNĐ");
            } else if (acc == null) {
                request.setAttribute("errorMsg", "Bạn cần đăng nhập để nạp tiền.");
            } else {
                request.setAttribute("errorMsg", "Nạp tiền thất bại hoặc bị hủy!");
            }
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMsg", "Có lỗi xảy ra khi xử lý nạp tiền.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Checkout nạp tiền và cộng vào tài khoản";
    }
}
