/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;
import dal.BookingDao;
import dal.DBContext;
import entity.Booking;
import entity.BookingStatus;
import entity.User;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
   import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import dal.tourDao;
import entity.Tour;
/**
 *
 * @author thang
 */
public class ListBookingGuide_servlet extends HttpServlet {
       private BookingDao bookingDao;
       private tourDao tourDao;
 @Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection conn = new DBContext().getConnection();
            bookingDao = new BookingDao(conn);
           tourDao = new tourDao(conn);
        } catch (Exception e) {
            throw new ServletException("Cannot initialize DAOs in DetailBookingGuideServlet", e);
        }}
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
            out.println("<title>Servlet ListBookingGuide_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListBookingGuide_servlet at " + request.getContextPath () + "</h1>");
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
        if (!"guide".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
            return;
        }
        int guideId = user.getId();

        // 2. Đọc tham số tìm kiếm & sắp xếp (nếu có)
        String keyword   = request.getParameter("keyword");    // tìm kiếm theo contact_info
        String sortDir   = request.getParameter("sortDir");  // "asc" hoặc "desc" 
                String sortField = request.getParameter("sortField");

        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        boolean hasSort    = (sortDir != null && (sortDir.equalsIgnoreCase("asc") || sortDir.equalsIgnoreCase("desc")));
        boolean sortAsc    = !"desc".equalsIgnoreCase(sortDir);

        try (Connection conn = new DBContext().getConnection()) {
            BookingDao bookingDao = new BookingDao(conn);
            List<Booking> bookings;

            if (!hasKeyword && !hasSort) {
                // Trường hợp: chưa search, chưa sort → lấy tất cả
                bookings = bookingDao.getBookingsByGuideId(guideId);

            } else if (hasKeyword && !hasSort) {
                // Chỉ search (theo keyword)
                bookings = bookingDao.searchBookingsByGuideId(guideId, keyword);

            } else if (!hasKeyword && hasSort) {
                // Chỉ sort (theo updated_at)
                bookings = bookingDao.sortBookingsByGuideId(guideId, sortField, sortAsc);

            } else {
                // Có cả search + sort
                bookings = bookingDao.searchAndSortBookingsByGuideId(guideId, keyword, sortField, sortAsc);
            }

            // 3. Đẩy dữ liệu về JSP
            request.setAttribute("bookings",       bookings);
            request.setAttribute("BookingStatus",   BookingStatus.class);
                        request.setAttribute("sortField", sortField);
request.setAttribute("message", request.getAttribute("message")); // Giữ message nếu có

            request.setAttribute("keyword",         hasKeyword ? keyword.trim() : "");
            request.setAttribute("sortDir",         hasSort ? (sortAsc ? "asc" : "desc") : "");
            request.getRequestDispatcher("/Views/v1/listBookingGuide.jsp")
                   .forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(ListBookingGuide_servlet.class.getName())
                  .log(Level.SEVERE, "Error when retrieving bookings for guideId=" + guideId, ex);
            throw new ServletException("Error when retrieving bookings for guideId=" + guideId, ex);
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
        // Kiểm tra role: chỉ cho guide truy cập
        if (!"guide".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
            return;
        }

        // Lấy action từ form (hidden input hoặc parameter)
        String action = request.getParameter("action");
        if (action == null) {
            // Nếu không có action, chuyển về danh sách lại
            response.sendRedirect(request.getContextPath() + "/ListBookingGuide_servlet");
            return;
        }

        switch (action) {
          case "changeStatus":
                try {
                    int bookingId = Integer.parseInt(request.getParameter("id"));
                    String statusStr = request.getParameter("newStatus");
                    BookingStatus newStatus = BookingStatus.valueOf(statusStr);

                    // 1) If we're approving, we need to run our conflict checks
                    if (newStatus == BookingStatus.APPROVED) {
                        // a) Is departure date already in the past?
                        boolean past = bookingDao.isDepartureInPast(bookingId);

                        if (past) {
                            request.setAttribute("message",
                                "❌ Cannot approve a booking whose departure date has already passed.");
                            // call doGet to re‐display with the message
                            doGet(request, response);
                            return;
                        }

                        // b) Load the booking’s departure_date and tour days:
                        Booking booking = bookingDao.getBookingById(bookingId);
                        java.util.Date departureDate = booking.getDepartureDate();
                        // You need a method in DAO to fetch tour.days for this booking:
                        Tour t = tourDao.getTourById(bookingId);
                     Integer   tourDays = t.getDays();

                        // c) Check conflict with other APPROVED bookings of the same guide
                        boolean conflictApproved = bookingDao.hasApprovedConflict(
                                bookingId, departureDate, tourDays);

                        if (conflictApproved) {
                            request.setAttribute("message",
                                " This booking conflicts with another already‐approved booking on your schedule.");
                            doGet(request, response);
                            return;
                        }

                        // d) Check conflict with other PENDING bookings (so you can warn the guide)
                        List<Booking> overlappingPending =
                            bookingDao.getOverlappingPendingForGuide(bookingId, departureDate, tourDays);

                        if (!overlappingPending.isEmpty()) {
                            request.setAttribute("message",
                                "️ This booking overlaps with other pending bookings. Please review them before approving.");
                            doGet(request, response);
                            return;
                        }

                        // e) No conflicts → safe to mark as APPROVED
                        bookingDao.updateBookingStatus(bookingId, newStatus);
                        request.setAttribute("message",
                            " Booking has been approved successfully.");
                        doGet(request, response);
                        return;
                    }

                    // 2) If newStatus is REJECTED or CANCELLED, just update directly
                    bookingDao.updateBookingStatus(bookingId, newStatus);
                    request.setAttribute("message",
                        "✅ Booking status updated successfully.");
                    doGet(request, response);
                    return;

                } catch (Exception ex) {
                    ex.printStackTrace();
                    request.setAttribute("message", "❌ An error occurred: " + ex.getMessage());
                    doGet(request, response);
                    return;
                }
             


            case "detail":
                // Chuyển đến servlet detail với bookingId
                int bookingId = Integer.parseInt(request.getParameter("id"));
                response.sendRedirect(request.getContextPath()
                        + "/DetailBookingGuide_servlet?bookingId=" + bookingId);
                break;

            default:
                // Nếu action không hợp lệ, quay lại trang danh sách
                response.sendRedirect(request.getContextPath() + "/ListBookingGuide_servlet");
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
