/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.DBContext;
import dal.tourDao;
import entity.Status;
import entity.Tour;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author thang
 */
public class editTour_servlet extends HttpServlet {
   
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
            out.println("<title>Servlet editTour_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet editTour_servlet at " + request.getContextPath () + "</h1>");
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
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != entity.Role.GUIDE) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/tourList_servlet");
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            tourDao dao = new tourDao(conn);
            int tourId = Integer.parseInt(idParam);
            Tour tour = dao.getTourById(tourId);
            if (tour == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tour not found.");
                return;
            }
            if(tour.getGuideId()!=user.getId()){
                response.sendRedirect(request.getContextPath() + "/tourList_servlet");
            return;
            }
            request.setAttribute("tour", tour);
            // truyền trạng thái update (nếu có)
            String upd = request.getParameter("update");
            request.setAttribute("updateStatus", upd);
            request.getRequestDispatcher("/Views/v1/editTour.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(editTour_servlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            Logger.getLogger(editTour_servlet.class.getName()).log(Level.SEVERE, null, ex);
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
        User user = session != null ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != entity.Role.GUIDE) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String itinerary = request.getParameter("itinerary");
        String priceStr = request.getParameter("price");
        String maxPeopleStr = request.getParameter("maxPeople");
        String daysStr = request.getParameter("days");
        String language = request.getParameter("language");
        String statusParam = request.getParameter("status"); 

        boolean success = false;
        try {
            int tourId = Integer.parseInt(idParam);
            BigDecimal price = new BigDecimal(priceStr);
            int maxPeople = Integer.parseInt(maxPeopleStr);
            int days = Integer.parseInt(daysStr);
            Status status = Status.valueOf(statusParam.toUpperCase());

            Tour tour = new Tour();
            tour.setId(tourId);
            tour.setGuideId(user.getId());
            tour.setName(name);
            tour.setDescription(description);
            tour.setItinerary(itinerary);
            tour.setPrice(price);
            tour.setMaxPeoplePerBooking(maxPeople);
            tour.setDays(days);
            tour.setLanguage(language);
            tour.setStatus(status);

            try (Connection conn = new DBContext().getConnection()) {
                tourDao dao = new tourDao(conn);
                success = dao.updateTour(tour);
            }
        } catch (Exception ex) {
            Logger.getLogger(editTour_servlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // redirect về doGet có param update
        String redirectUrl = String.format("%s/editTour_servlet?id=%s&update=%s", 
            request.getContextPath(), request.getParameter("id"), success ? "success" : "fail");
        response.sendRedirect(redirectUrl);
    
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
