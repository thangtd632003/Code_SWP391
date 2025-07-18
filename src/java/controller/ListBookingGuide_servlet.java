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
import dal.userDao;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
/**
 *
 * @author thang
 */
public class ListBookingGuide_servlet extends HttpServlet {
       private BookingDao bookingDao;
       private tourDao tourDao;
       private userDao userDao;
 @Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection conn = new DBContext().getConnection();
            bookingDao = new BookingDao(conn);
           tourDao = new tourDao(conn);
           userDao = new userDao(conn);
        } catch (Exception e) {
            throw new ServletException("Cannot initialize DAOs in DetailBookingGuideServlet", e);
        }}
    
     private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USERNAME = "quizlet875@gmail.com";
    private static final String SMTP_PASSWORD = "fcrg hpnd xcmt hfye";

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

        String keyword   = request.getParameter("keyword");    
        String sortDir   = request.getParameter("sortDir");  
                String sortField = request.getParameter("sortField");

        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        boolean hasSort    = (sortDir != null && (sortDir.equalsIgnoreCase("asc") || sortDir.equalsIgnoreCase("desc")));
        boolean sortAsc    = !"desc".equalsIgnoreCase(sortDir);

        try (Connection conn = new DBContext().getConnection()) {
            BookingDao bookingDao = new BookingDao(conn);
            List<Booking> bookings;

            if (!hasKeyword && !hasSort) {
                bookings = bookingDao.getBookingsByGuideId(guideId);

            } else if (hasKeyword && !hasSort) {
                bookings = bookingDao.searchBookingsByGuideId(guideId, keyword);

            } else if (!hasKeyword && hasSort) {
                bookings = bookingDao.sortBookingsByGuideId(guideId, sortField, sortAsc);

            } else {
                bookings = bookingDao.searchAndSortBookingsByGuideId(guideId, keyword, sortField, sortAsc);
            }

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
        if (!"guide".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_servlet");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/ListBookingGuide_servlet");
            return;
        }

        switch (action) {
          case "changeStatus":
                try {
                    int bookingId = Integer.parseInt(request.getParameter("id"));
                    String statusStr = request.getParameter("newStatus");
                    BookingStatus newStatus = BookingStatus.valueOf(statusStr);

                    if (newStatus == BookingStatus.APPROVED) {
                        boolean past = bookingDao.isDepartureInPast(bookingId);

                        if (past) {
                            request.setAttribute("message",
                                "❌ Cannot approve a booking whose departure date has already passed.");
                            doGet(request, response);
                            return;
                        }

                        Booking booking = bookingDao.getBookingById(bookingId);
                        java.util.Date departureDate = booking.getDepartureDate();
                        Tour t = tourDao.getTourById(bookingId);
                     Integer   tourDays = t.getDays();

                        boolean conflictApproved = bookingDao.hasApprovedConflict(
                                bookingId, departureDate, tourDays);

                        if (conflictApproved) {
                            request.setAttribute("message",
                                " This booking conflicts with another already‐approved booking on your schedule.");
                            doGet(request, response);
                            return;
                        }

                        List<Booking> overlappingPending =
                            bookingDao.getOverlappingPendingForGuide(bookingId, departureDate, tourDays);

                        if (!overlappingPending.isEmpty()) {
                            request.setAttribute("message",
                                "️ This booking overlaps with other pending bookings. Please review them before approving.");
                            doGet(request, response);
                            return;
                        }
 User traveler = userDao.getUserById(booking.getTravelerId());
                        bookingDao.updateBookingStatus(bookingId, newStatus);
                              boolean mailSent = sendNewBookingToTraveler(traveler.getEmail(),bookingId);
                        request.setAttribute("message",
                            " Booking has been approved successfully.");
                        doGet(request, response);
                        return;
                    }
                    Booking b = bookingDao.getBookingById(bookingId);
                  User traveler = userDao.getUserById(b.getTravelerId());
                    bookingDao.updateBookingStatus(bookingId, newStatus);
                    
            boolean mailSent = sendNewBookingToTraveler(traveler.getEmail(),bookingId);

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
                int bookingId = Integer.parseInt(request.getParameter("id"));
                response.sendRedirect(request.getContextPath()
                        + "/DetailBookingGuide_servlet?bookingId=" + bookingId);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/ListBookingGuide_servlet");
                break;
        }
    }
private boolean sendNewBookingToTraveler(String travelerEmail,int bookingID) {
    try {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.starttls.required", "true");

        Session mailSession = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
        mailSession.setDebug(true);

        Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(SMTP_USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(travelerEmail));
        message.setSubject("Have a new status booking for your booking");

        String content = "Hi,This is mail from website connection traveler and guide.\n\n"
                + "Booking you just updated. Check booking list.\n\n"
          + "Booking ID     : " + bookingID + "\n"
                
                + "Thank you.";

        message.setText(content);
        Transport.send(message);
        return true;

    } catch (Exception e) {
       Logger.getLogger(ListBookingGuide_servlet.class.getName())
      .log(Level.SEVERE, "An error occurred", e);
        return false;
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
