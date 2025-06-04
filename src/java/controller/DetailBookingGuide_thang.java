package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */


import dal.BookingDao_thang;
import dal.DBContext;
import dal.tourDao_thang;
import dal.userDao_thang;
import entity.Booking;
import entity.BookingStatus;
import entity.Tour;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
   import java.sql.Connection;

/**
 *
 * @author thang
 */
public class DetailBookingGuide_thang extends HttpServlet {
    private BookingDao_thang bookingDao;
    private tourDao_thang tourDao;
    private userDao_thang userDao;
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection conn = new DBContext().getConnection();
            bookingDao = new BookingDao_thang(conn);
           tourDao= new tourDao_thang(conn);
           userDao = new userDao_thang(conn);
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
            out.println("<title>Servlet DetailBookingGuide_thang</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DetailBookingGuide_thang at " + request.getContextPath () + "</h1>");
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
        // Kiểm tra role: chỉ cho guide truy cập
        if (!"guide".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
            return;
        }

        // Lấy bookingId từ parameter
        String bookingIdParam = request.getParameter("bookingId");
        if (bookingIdParam == null) {
            // Nếu không có bookingId, quay về danh sách
            response.sendRedirect(request.getContextPath() + "/ListBookingGuide_thang");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdParam);
            Booking booking = bookingDao.getBookingById(bookingId);
            if (booking == null) {
                // Nếu không tìm thấy booking, quay về danh sách
                response.sendRedirect(request.getContextPath() + "/ListBookingGuide_thang");
                return;
            }
            // Lấy thông tin Tour kèm theo
            Tour tour = tourDao.getTourById(booking.getTourId());
         User userTravelerInfo= userDao.getUserById(booking.getTravelerId());
            request.setAttribute("booking", booking);
            request.setAttribute("tour", tour);
            request.setAttribute("travelerName", userTravelerInfo.getFullName());
            request.getRequestDispatcher("/Views/thang/bookingDetailGuide.jsp")
                   .forward(request, response);
        } catch (Exception ex) {
            throw new ServletException("Error when retrieving booking/detail for bookingId=" + bookingIdParam, ex);
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
            // Nếu không có action, quay về trang detail (với bookingId cũ nếu có)
            String bookingIdParam = request.getParameter("bookingId");
            response.sendRedirect(request.getContextPath() + "/DetailBookingGuide_thang?bookingId=" + bookingIdParam);
            return;
        }

        switch (action) {
            case "changeStatus":
                try {
                    int bookingId = Integer.parseInt(request.getParameter("bookingId"));
                    String statusStr = request.getParameter("newStatus");
                    BookingStatus newStatus = BookingStatus.valueOf(statusStr);
                    bookingDao.updateBookingStatus(bookingId, newStatus);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // Sau khi đổi trạng thái xong, quay lại chính trang detail với bookingId
                String bid = request.getParameter("bookingId");
                response.sendRedirect(request.getContextPath() + "/DetailBookingGuide_thang?bookingId=" + bid);
                break;

            default:
                // Trường hợp action không hợp lệ → quay lại trang detail
                String bookingIdParam = request.getParameter("bookingId");
                response.sendRedirect(request.getContextPath() + "/DetailBookingGuide_thang?bookingId=" + bookingIdParam);
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
