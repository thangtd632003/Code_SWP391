/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.BookingDao_thang;
import dal.DBContext;
import entity.Booking;
import entity.BookingStatus;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
   import java.sql.Connection;


/**
 *
 * @author thang
 */
public class ListBookingTraveler_thang extends HttpServlet {
       private BookingDao_thang bookingDao;
 @Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection conn = new DBContext().getConnection();
            bookingDao = new BookingDao_thang(conn);
           
        } catch (Exception e) {
            throw new ServletException("Cannot initialize DAOs in DetailBookingGuideServlet", e);
        }
    }
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
            out.println("<title>Servlet ListBookingTraveler_thang</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListBookingTraveler_thang at " + request.getContextPath () + "</h1>");
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

        // 2. Kiểm tra role: chỉ cho traveler truy cập
        if (!"traveler".equalsIgnoreCase(user.getRole().name())) {
            // Nếu không phải traveler, redirect về trang profile tương ứng
            response.sendRedirect(request.getContextPath() + "/ProfileGuide_thang");
            return;
        }

        // 3. Lấy danh sách booking theo travelerId
        int travelerId = user.getId();
        try {
            List<Booking> bookings = bookingDao.getBookingsByTravelerId(travelerId);
            request.setAttribute("bookingList", bookings);
            // 4. Forward đến JSP listBookingTraveler_thang.jsp
            request.getRequestDispatcher("/Views/thang/listBookingTraveler_thang.jsp")
                   .forward(request, response);
        } catch (Exception ex) {
            throw new ServletException("Error when retrieving bookings for travelerId=" + travelerId, ex);
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
       HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");

        // 2. Kiểm tra role: chỉ cho traveler truy cập
        if (!"traveler".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileGuide_thang");
            return;
        }

        // 3. Lấy action
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/ListBookingTraveler_thang");
            return;
        }

        switch (action) {
            case "changeStatus":
                // Cho phép traveler đổi trạng thái (vd: hủy booking)
                try {
                    int bookingId = Integer.parseInt(request.getParameter("bookingId"));
                    String statusStr = request.getParameter("newStatus");
                    BookingStatus newStatus = BookingStatus.valueOf(statusStr);
                    bookingDao.updateBookingStatus(bookingId, newStatus);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // Quay lại trang danh sách
                response.sendRedirect(request.getContextPath() + "/ListBookingTraveler_thang");
                break;

            case "detail":
                // Chuyển đến servlet detail với bookingId
                int bookingId = Integer.parseInt(request.getParameter("bookingId"));
                response.sendRedirect(request.getContextPath()
                        + "/DetailBookingTraveler_thang?bookingId=" + bookingId);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/ListBookingTraveler_thang");
                break;
        }
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
