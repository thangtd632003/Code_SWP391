/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;
import dal.DBContext;
import dal.ReviewDao;
import entity.Review;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thang
 */
public class ListReviewGuide_servlet extends HttpServlet {
    private ReviewDao reviewDao;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection conn = new DBContext().getConnection();
            reviewDao = new ReviewDao(conn);
        } catch (Exception e) {
            throw new ServletException("Unable to initialize ReviewDao", e);
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
            out.println("<title>Servlet ListReviewGuide_servlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ListReviewGuide_servlet at " + request.getContextPath () + "</h1>");
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
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!"guide".equalsIgnoreCase(user.getRole().name())) {
            response.sendRedirect(request.getContextPath() + "/ProfileTraveler_thang");
            return;
        }

        int guideId = user.getId();

        String keyword   = request.getParameter("keyword");       
        String sortDir   = request.getParameter("sortDir");     
        String sortField = request.getParameter("sortField");     
        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        boolean hasSort    = (sortDir != null && (sortDir.equalsIgnoreCase("asc") || sortDir.equalsIgnoreCase("desc")));
        boolean sortAsc    = !"desc".equalsIgnoreCase(sortDir);

        try {
            List<Review> reviews;
            if (!hasKeyword && !hasSort) {
                // lấy tất cả
                reviews = reviewDao.getReviewsByGuideId(guideId);
            } else if (hasKeyword && !hasSort) {
                // chỉ search
                reviews = reviewDao.searchReviewsByGuideId(guideId, keyword.trim());
            } else if (!hasKeyword && hasSort) {
                // chỉ sort
                reviews = reviewDao.sortReviewsByGuideId(guideId, sortAsc);
            } else {
                // search + sort
                reviews = reviewDao.searchAndSortReviewsByGuideId(guideId, keyword.trim(), sortAsc);
            }

            request.setAttribute("reviews",   reviews);
            request.setAttribute("keyword",   hasKeyword ? keyword.trim() : "");
            request.setAttribute("sortField", hasSort ? sortField : "");
            request.setAttribute("sortDir",   hasSort ? (sortAsc ? "asc" : "desc") : "");

            request.getRequestDispatcher("/Views/v1/listReviewGuide.jsp")
                   .forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(ListReviewGuide_servlet.class.getName())
                  .log(Level.SEVERE, "Error when retrieving reviews for guideId=" + guideId, ex);
            throw new ServletException("Error when retrieving reviews", ex);
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
        processRequest(request, response);
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
