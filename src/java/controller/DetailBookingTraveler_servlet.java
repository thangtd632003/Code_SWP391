/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.BookingDao;
import dal.DBContext;
import dal.tourDao;
import dal.userDao;
import entity.Booking;
import entity.Tour;
import entity.BookingStatus;
import entity.User;
import java.io.IOException;
import java.sql.Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
/**
 *
 * @author thang
 */
public class DetailBookingTraveler_servlet extends HttpServlet {
   private BookingDao bookingDao;
    private tourDao tourDao;
        private userDao userDao;

@Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection conn = new DBContext().getConnection();
            bookingDao = new BookingDao(conn);
           tourDao= new tourDao(conn);
                      userDao = new userDao(conn);

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
            out.println("<title>Servlet DetailBookingTraveler_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DetailBookingTraveler_servlet at " + request.getContextPath () + "</h1>");
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
            response.sendRedirect(request.getContextPath() + "/ProfileGuide_servlet");
            return;
        }

        String bookingIdParam = request.getParameter("bookingId");
        if (bookingIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/ListBookingTraveler_servlet");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdParam);
            Booking booking = bookingDao.getBookingById(bookingId);
            if (booking == null) {
                response.sendRedirect(request.getContextPath() + "/ListBookingTraveler_servlet");
                return;
            }
            Tour tour = tourDao.getTourById(booking.getTourId());
           User userGuideInfor = userDao.getUserById(tour.getGuideId());
            request.setAttribute("booking", booking);
            request.setAttribute("tour", tour);
            request.setAttribute("guideName", userGuideInfor.getFullName());
            request.getRequestDispatcher("/Views/v1/bookingDetailTraveler.jsp")
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

        if (!"traveler".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileGuide_servlet");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            String bookingIdParam = request.getParameter("bookingId");
            response.sendRedirect(request.getContextPath() + "/DetailBookingTraveler_servlet?bookingId=" + bookingIdParam);
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
                String bid = request.getParameter("bookingId");
                response.sendRedirect(request.getContextPath() + "/DetailBookingTraveler_servlet?bookingId=" + bid);
                break;

            default:
                String bookingIdParam = request.getParameter("bookingId");
                response.sendRedirect(request.getContextPath() + "/DetailBookingTraveler_servlet?bookingId=" + bookingIdParam);
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
