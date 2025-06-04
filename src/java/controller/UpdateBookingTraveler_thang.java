/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;
import dal.DBContext;
import dal.BookingDao_thang;
import dal.tourDao_thang;
import entity.Booking;
import entity.Tour;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thang
 */
public class UpdateBookingTraveler_thang extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

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
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // 2. Kiểm role (chỉ TRAVELER mới được cập nhật booking)
        if (!"traveler".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
            return;
        }
        // 3. Lấy bookingId từ request parameter (form trước có truyền ?bookingId=…)
        Integer bookingIdParam =  (Integer) request.getAttribute("bookingId");
        if (bookingIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
            return;
        }
        int bookingId;
        try {
            bookingId = bookingIdParam;
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
            return;
        }
        // 4. Lấy thông tin booking & tour tương ứng
        Booking booking = null;
        Tour tour = null;
      try (Connection conn = new DBContext().getConnection()) {
            BookingDao_thang bdao = new BookingDao_thang(conn);
            booking = bdao.getBookingById(bookingId);
            if (booking == null) {
                response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
                return;
            }
            // Chỉ owner (traveler) của booking mới được sửa
            if (booking.getTravelerId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
                return;
            }
            tourDao_thang tdao = new tourDao_thang(conn);
            tour = tdao.getTourById(booking.getTourId());
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UpdateBookingTraveler_thang.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (Exception ex) {
            Logger.getLogger(UpdateBookingTraveler_thang.class.getName()).log(Level.SEVERE, null, ex);
        }
        // 5. Đưa data vào request và forward sang JSP form
        request.setAttribute("booking", booking);
        request.setAttribute("tour", tour);
        request.getRequestDispatcher("/Views/thang/UpdateBooking.jsp")
               .forward(request, response);
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
      // 1. Kiểm session + login
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // 2. Kiểm role
        if (!"TRAVELER".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
            return;
        }
        // 3. Đọc form data
        String idParam            = request.getParameter("id");
        String numPeopleParam     = request.getParameter("numPeople");
        String contactInfo        = request.getParameter("contactInfo");
        String departureDateParam = request.getParameter("departureDate");
        // 4. Chuyển về kiểu dữ liệu, kiểm tra dữ liệu hợp lệ
        int bookingId, numPeople;
        java.sql.Date departureDate;
        try {
            bookingId = Integer.parseInt(idParam);
            numPeople = Integer.parseInt(numPeopleParam);
            departureDate = java.sql.Date.valueOf(departureDateParam);
        } catch (Exception e) {
            // Nếu parse lỗi, trả lại doGet kèm thông báo lỗi
            request.setAttribute("error", "Invalid input data.");
            doGet(request, response);
            return;
        }
        // 5. Lấy booking gốc để so sánh
        Booking booking = null;
        Tour tour = null;
        try (Connection conn = new DBContext().getConnection()) {
            BookingDao_thang bdao = new BookingDao_thang(conn);
            booking = bdao.getBookingById(bookingId);
            if (booking == null || booking.getTravelerId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
                return;
            }
            // Lấy tour để biết maxPeoplePerBooking
            tour = new tourDao_thang(conn).getTourById(booking.getTourId());
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UpdateBookingTraveler_thang.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (Exception ex) {
            Logger.getLogger(UpdateBookingTraveler_thang.class.getName()).log(Level.SEVERE, null, ex);
        }
        // 6. Kiểm numPeople không vượt quá maxPeoplePerBooking
        if (numPeople < 1 || numPeople > tour.getMaxPeoplePerBooking()) {
            request.setAttribute("error", "Number of people must be between 1 and " +
                                          tour.getMaxPeoplePerBooking() + ".");
            doGet(request, response);
            return;
        }
        // 7. Cập nhật booking với dao
        booking.setNumPeople(numPeople);
        booking.setContactInfo(contactInfo);
        booking.setDepartureDate(departureDate);
        boolean success = false;
        try (Connection conn = new DBContext().getConnection()) {
            success = new BookingDao_thang(conn).updateBooking(booking);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(UpdateBookingTraveler_thang.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (Exception ex) {
            Logger.getLogger(UpdateBookingTraveler_thang.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!success) {
            request.setAttribute("error", "Update failed. Please try again.");
            doGet(request, response);
            return;
        }
        // 8. Nếu thành công, chuyển lại doGet (reload form) hoặc redirect nơi khác
        // … sau khi update thành công
request.setAttribute("bookingId", bookingId);
doGet(request, response);

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
