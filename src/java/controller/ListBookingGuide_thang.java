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
/**
 *
 * @author thang
 */
public class ListBookingGuide_thang extends HttpServlet {
       private BookingDao_thang bookingDao;
 @Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection conn = new DBContext().getConnection();
            bookingDao = new BookingDao_thang(conn);
           
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
            out.println("<title>Servlet ListBookingGuide_thang</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListBookingGuide_thang at " + request.getContextPath () + "</h1>");
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
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
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
            BookingDao_thang bookingDao = new BookingDao_thang(conn);
            List<Booking> bookings;

            if (!hasKeyword && !hasSort) {
                // Trường hợp: chưa search, chưa sort → lấy tất cả
                bookings = bookingDao.getBookingsByGuideId(guideId);

            } else if (hasKeyword && !hasSort) {
                // Chỉ search (theo keyword)
                bookings = bookingDao.searchBookingsByTravelerId(guideId, keyword);

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

            request.setAttribute("keyword",         hasKeyword ? keyword.trim() : "");
            request.setAttribute("sortDir",         hasSort ? (sortAsc ? "asc" : "desc") : "");
            request.getRequestDispatcher("/Views/thang/listBookingGuide_thang.jsp")
                   .forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(ListBookingGuide_thang.class.getName())
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
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
            return;
        }

        // Lấy action từ form (hidden input hoặc parameter)
        String action = request.getParameter("action");
        if (action == null) {
            // Nếu không có action, chuyển về danh sách lại
            response.sendRedirect(request.getContextPath() + "/ListBookingGuide_thang");
            return;
        }

        switch (action) {
            case "changeStatus":
                // Thay đổi trạng thái booking
                try {
                    int bookingId = Integer.parseInt(request.getParameter("id"));
                    String statusStr = request.getParameter("newStatus");
                    BookingStatus newStatus = BookingStatus.valueOf(statusStr);
                    bookingDao.updateBookingStatus(bookingId, newStatus);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // Sau khi đổi xong, tải lại trang danh sách
                response.sendRedirect(request.getContextPath() + "/ListBookingGuide_thang");
                break;

            case "detail":
                // Chuyển đến servlet detail với bookingId
                int bookingId = Integer.parseInt(request.getParameter("id"));
                response.sendRedirect(request.getContextPath()
                        + "/DetailBookingGuide_thang?bookingId=" + bookingId);
                break;

            default:
                // Nếu action không hợp lệ, quay lại trang danh sách
                response.sendRedirect(request.getContextPath() + "/ListBookingGuide_thang");
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
