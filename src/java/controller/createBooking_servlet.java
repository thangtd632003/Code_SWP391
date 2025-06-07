/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import dal.BookingDao;
import dal.DBContext;
import dal.tourDao;
import entity.Booking;
import entity.BookingStatus;
import entity.Tour;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thang
 */
public class createBooking_servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Định dạng ngày nhận từ form, ví dụ "yyyy-MM-dd"
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
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
            out.println("<title>Servlet createBooking_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet createBooking_servlet at " + request.getContextPath () + "</h1>");
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
            // Nếu chưa đăng nhập, chuyển hướng về login
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");

        // 2. Lấy tourId từ query string
        String tourIdParam = request.getParameter("tourId");
        if (tourIdParam == null) {
            // Nếu không có tourId, quay về trang danh sách tour user
            response.sendRedirect(request.getContextPath() + "/listTourUser_servlet");
            return;
        }

        int tourId;
        try {
            tourId = Integer.parseInt(tourIdParam);
        } catch (NumberFormatException e) {
            // tourId không hợp lệ → quay về list
            response.sendRedirect(request.getContextPath() + "/listTourUser_servlet");
            return;
        }

        // 3. Nạp thông tin tour để hiển thị lên form (nếu cần)
        try (Connection conn = new DBContext().getConnection()) {
            tourDao tourDao = new tourDao(conn);
            Tour tour = tourDao.getTourById(tourId);
            if (tour == null) {
                // Nếu không tìm thấy tour, quay về list
                response.sendRedirect(request.getContextPath() + "/listTourUser_servlet");
                return;
            }
            request.setAttribute("tour", tour);
        } catch (SQLException ex) {
            throw new ServletException("Lỗi khi nạp thông tin tour.", ex);
        } catch (Exception ex) {
            Logger.getLogger(createBooking_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // 4. Chuyển đến JSP bookingTour.jsp để hiển thị form
        // Nếu có parameter message (như error hoặc success), giữ lại để hiển thị
        String message = request.getParameter("message");
        if (message != null) {
            request.setAttribute("message", message);
        }
        request.getRequestDispatcher("/Views/v1/bookingTour.jsp")
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
      HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");

        // 2. Đọc tham số từ form
        String tourIdParam      = request.getParameter("tourId");
        String departureParam   = request.getParameter("departureDate"); // định dạng "yyyy-MM-dd"
        String numPeopleParam   = request.getParameter("numPeople");
        String contactInfo      = request.getParameter("contactInfo");

        int tourId;
        int numPeople;
        Date departureDate;
        try {
            tourId = Integer.parseInt(tourIdParam);
            numPeople = Integer.parseInt(numPeopleParam);
            departureDate = DATE_FORMAT.parse(departureParam);
        } catch (NumberFormatException | ParseException e) {
            // Nếu parse lỗi, quay về form với thông báo lỗi
            response.sendRedirect(request.getContextPath() + "/createBooking_servlet?tourId=" 
                    + tourIdParam + "&message=invalidInput");
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            // Nạp DAO
            BookingDao bookingDao = new BookingDao(conn);
            tourDao tourDao = new tourDao(conn);

            // Lấy thông tin tour (để biết số ngày)
            Tour tour = tourDao.getTourById(tourId);
            if (tour == null) {
                response.sendRedirect(request.getContextPath() + "/listTourUser_servlet");
                return;
            }
            int tourDays = tour.getDays();

            // 3. Kiểm tra xung đột với booking của chính user
            boolean sameDateConflict = bookingDao.isSameDateConflictForUser(
                    user.getId(), departureDate);
            if (sameDateConflict) {
                String m = "Date confict,check booking list";
                response.sendRedirect(request.getContextPath() 
                        + "/createBooking_servlet?tourId=" + tourId 
                        + "&message=" + m);
                return;
            }
            boolean periodConflictUser = bookingDao.isPeriodConflictForUser(
                    user.getId(), departureDate, tourDays);
            if (periodConflictUser) {
                  String m1 = "Date confict with other traveler";
                response.sendRedirect(request.getContextPath()
                        + "/createBooking_servlet?tourId=" + tourId
                        + "&message=" +m1 );
                return;
            }

            // 4. Kiểm tra xung đột với booking APPROVED của guide
            boolean periodConflictGuide = bookingDao.isPeriodConflictForGuide(
                    tour.getGuideId(), departureDate, tourDays);
            if (periodConflictGuide) {
                  String m2 = "Date confict with guide schedule booking";
                response.sendRedirect(request.getContextPath()
                        + "/createBooking_servlet?tourId=" + tourId
                        + "&message=" + m2);
                return;
            }

            // 5. Thực hiện tạo booking mới (mặc định status = PENDING)
            Booking newBooking = new Booking();
            newBooking.setTravelerId(user.getId());
            newBooking.setTourId(tourId);
            newBooking.setNumPeople(numPeople);
            newBooking.setContactInfo(contactInfo);
            newBooking.setStatus(BookingStatus.PENDING);
            // departureDate là java.util.Date, nhưng BookingDao sẽ dùng java.sql.Date bên trong
            newBooking.setDepartureDate(departureDate);

            boolean created = bookingDao.addBooking(newBooking);
            if (!created) {
                // Nếu insertion thất bại
                response.sendRedirect(request.getContextPath()
                        + "/createBooking_servlet?tourId=" + tourId
                        + "&message=creationFailed");
                return;
            }
        } catch (SQLException ex) {
            throw new ServletException("Lỗi khi tạo booking mới.", ex);
        } catch (Exception ex) {
            Logger.getLogger(createBooking_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // 6. Nếu thành công, chuyển về trang xác nhận hoặc danh sách booking
        response.sendRedirect(request.getContextPath() + "/createBooking_servlet?tourId=" + tourId+"&message=bookingSuccess");
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
