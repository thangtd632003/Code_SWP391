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
import dal.userDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * @author thang
 */
public class createBooking_servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

     private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    // Thay báº±ng email + máº­t kháº©u á»©ng dá»¥ng (app password) cá»§a báº¡n:
    private static final String SMTP_USERNAME = "quizlet875@gmail.com";
    private static final String SMTP_PASSWORD = "fcrg hpnd xcmt hfye";

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

        //  Lấy tourId từ query string
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
            // tourId không hợp lệ  quay về list
            response.sendRedirect(request.getContextPath() + "/listTourUser_servlet");
            return;
        }

        //  Nạp thông tin tour để hiển thị lên form (nếu cần)
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

        //  Chuyển đến JSP bookingTour.jsp để hiển thị form
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

        //  Đọc tham số từ form
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
userDao userDao = new userDao(conn);
            // Lấy thông tin tour (để biết số ngày)
            Tour tour = tourDao.getTourById(tourId);
            //lấy guide account
            User guide = userDao.getUserById(tour.getGuideId());
            if (tour == null) {
                response.sendRedirect(request.getContextPath() + "/listTourUser_servlet");
                return;
            }
            int tourDays = tour.getDays();

            //  Kiểm tra xung đột với booking của chính user
            boolean sameDateConflict = bookingDao.isSameDateConflictForUser(
                    user.getId(), departureDate);
            if (sameDateConflict) {
                String m = "Booking Fail.Departure date confict,check booking list";
                response.sendRedirect(request.getContextPath() 
                        + "/createBooking_servlet?tourId=" + tourId 
                        + "&message=" + m);
                return;
            }
            boolean periodConflictUser = bookingDao.isPeriodConflictForUser(
                    user.getId(), departureDate, tourDays);
            if (periodConflictUser) {
                  String m1 = "Booking Fail.Time of booking overlap with orther booking, check bookinglist";
                response.sendRedirect(request.getContextPath()
                        + "/createBooking_servlet?tourId=" + tourId
                        + "&message=" +m1 );
                return;
            }

            //  Kiểm tra xung đột với booking APPROVED của guide
            boolean periodConflictGuide = bookingDao.isPeriodConflictForGuide(
                    tour.getGuideId(), departureDate, tourDays);
            if (periodConflictGuide) {
                  String m2 = "Date confict with guide schedule booking";
                response.sendRedirect(request.getContextPath()
                        + "/createBooking_servlet?tourId=" + tourId
                        + "&message=" + m2);
                return;
            }

            //  Thực hiện tạo booking mới (mặc định status = PENDING)
            Booking newBooking = new Booking();
            newBooking.setTravelerId(user.getId());
            newBooking.setTourId(tourId);
            newBooking.setNumPeople(numPeople);
            newBooking.setContactInfo(contactInfo);
            newBooking.setStatus(BookingStatus.PENDING);
            newBooking.setDepartureDate(departureDate);
            newBooking.setTourName(tour.getName());
            newBooking.setTourDays(tour.getDays());
            newBooking.setTourLanguage(tour.getLanguage());
            newBooking.setTourPrice(tour.getPrice());
            newBooking.setTourItinerary(tour.getItinerary());
            boolean created = bookingDao.addBooking(newBooking);
            
            if (!created) {
                
                // Nếu insertion thất bại
                response.sendRedirect(request.getContextPath()
                        + "/createBooking_servlet?tourId=" + tourId
                        + "&message=creationFailed");
                return;
            }
            
            boolean mailSent = sendNewBookingToGuide(guide.getEmail());

        } catch (SQLException ex) {
            throw new ServletException("Lỗi khi tạo booking mới.", ex);
        } catch (Exception ex) {
            Logger.getLogger(createBooking_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  Nếu thành công, chuyển về trang xác nhận hoặc danh sách booking
        response.sendRedirect(request.getContextPath() + "/createBooking_servlet?tourId=" + tourId+"&message=bookingSuccess");
    }
private boolean sendNewBookingToGuide(String guideEmail) {
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
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(guideEmail));
        message.setSubject("You have a booking for tour");

        String content = "Hi,\n\n"
                + "This is mail from website connection traveler and guideYou have just received a new booking for the tour you are in charge of. Check booking list.\n\n"
          
                + "Please be prepared and contact customers as needed..\n"
                + "Thank.";

        message.setText(content);
        Transport.send(message);
        return true;

    } catch (Exception e) {
      Logger.getLogger(createBooking_servlet.class.getName())
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
