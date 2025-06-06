/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import dal.DBContext;
import dal.tourDao_thang;
import dal.userDao_thang;
import dal.guideDao_thang;
import entity.Tour;
import entity.User;
import entity.GuideProfile;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author thang
 */
public class detailTourUser_thang extends HttpServlet {
   
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
            out.println("<title>Servlet detailTourUser_thang</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet detailTourUser_thang at " + request.getContextPath () + "</h1>");
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
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (!user.getRole().name().equalsIgnoreCase("traveler")) {
            // Nếu không phải TRAVELER, chuyển hướng về trang profile tương ứng
          
                response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
            
            return;
        }

        // 2. Lấy tourId từ param và kiểm tra
        String tourIdParam = request.getParameter("tourId");
        if (tourIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/listTourUser_thang");
            return;
        }

        int tourId;
        try {
            tourId = Integer.parseInt(tourIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/listTourUser_thang");
            return;
        }

        // 3. Kết nối DB, lấy thông tin tour và guide
        try (Connection conn = new DBContext().getConnection()) {
            tourDao_thang tourDao = new tourDao_thang(conn);
            Tour tour = tourDao.getTourById(tourId);
            if (tour == null) {
                response.sendRedirect(request.getContextPath() + "/listTourUser_thang");
                return;
            }

            // Lấy thông tin user (guide) theo guideId
            userDao_thang userDao = new userDao_thang(conn);
            User guideUser = userDao.getUserById(tour.getGuideId());
            if (guideUser == null) {
                response.sendRedirect(request.getContextPath() + "/listTourUser_thang");
                return;
            }

            // Lấy thêm thông tin profile (bio, imageUrl) nếu có
            guideDao_thang guideDao = new guideDao_thang(conn);
            GuideProfile guideProfile = guideDao.getGuideProfileById(tour.getGuideId());
            // Nếu chưa có profile, guideProfile có thể null – xử lý phía JSP

            // 4. Đưa vào request và forward sang JSP hiển thị
            request.setAttribute("tour", tour);
            request.setAttribute("guideUser", guideUser);
            request.setAttribute("guideProfile", guideProfile);
            request.getRequestDispatcher("/Views/thang/detailTourUser_thang.jsp")
                   .forward(request, response);

        } catch (SQLException ex) {
            throw new ServletException("Không thể truy vấn thông tin Tour/Guide", ex);
        } catch (Exception ex) {
            throw new ServletException("Lỗi không xác định", ex);
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
        processRequest(request, response);
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
