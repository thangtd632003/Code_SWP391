<%-- 
    Document   : dashboardTraveler.jsp
    Created on : May 27, 2025, 4:03:44 PM
    Author     : thang
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Tour Management Dashboard</title>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="css/dashboard.css">
                <style>
    /* Toàn trang */
    body {
      font-family: Arial, sans-serif;
      background: #ffffff; /* nền trắng */
      margin: 0; 
      padding: 0;
    }

    /* Thanh cố định trên cùng */
    .top-bar {
      position: fixed;
      top: 0; 
      left: 0; 
      width: 100%;
  
      color: #1565C0;
      background-color:#ffffff; 
    
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 10px 20px; 
      box-shadow: 0 2px 4px rgba(0,0,0,0.2);
      z-index: 1000;
    }
    .top-bar a.dashboard-link {
   color: #1565C0;
      text-decoration: none;
      font-size: 16px;
      font-weight: bold;
      margin-right: 20px;
    }
    .top-bar a.dashboard-link:hover {
        color:  #cce0f0;
    }

    /* Form tìm kiếm bên tay phải */
    .top-bar form {
      display: flex;
      align-items: center;
    }
    .top-bar input[type="text"] {
      padding: 6px; 
      font-size: 14px; 
      width: 200px;
      border: none; 
      border-radius: 4px;
      margin-right: 6px;
    }
    .top-bar button.search-btn {
      padding: 6px 10px; 
      font-size: 14px;
      border: none; 
      border-radius: 4px;
      background: #ff8c00; /* cam tươi */
      color: #fff;
      cursor: pointer;
    }
    .top-bar button.search-btn:hover {
      background: #e57c00;
    }

    /* Khu vực nội dung chính */
    .container {
      margin-top: 70px; /* chừa chỗ cho thanh top-bar */
      padding: 20px;
    }
    h2.section-title {
      margin-top: 40px; 
      margin-bottom: 10px; 
      color: #005ea8; /* tiêu đề màu xanh dương */
    }

    /* Lưới hiển thị các card */
    .card-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
    }
    .card {
      background: #ffffff;
      border-radius: 8px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
      width: calc(25% - 16px);
      min-width: 220px;
      display: flex;
      flex-direction: column;
      overflow: hidden;
      border: 1px solid #e0e0e0;
    }
    .card img {
      width: 100%;
      height: 140px;
      object-fit: cover;
    }
    .card-body {
      flex: 1;
      padding: 12px;
      display: flex;
      flex-direction: column;
    }
    .card-body h3 {
      margin: 0 0 8px;
      font-size: 18px;
      color: #003b5c; /* xanh dương đậm hơn cho tên tour */
    }
    .card-body p.price {
      margin: 0 0 12px;
      font-size: 16px;
      color: #ff8c00; /* cam */
      font-weight: bold;
    }
    .card-body .buttons {
      margin-top: auto;
      display: flex;
      gap: 6px;
    }
    .card-body .buttons button {
      flex: 1;
      padding: 6px 8px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      color: #fff;
    }
    .btn-detail {
      background: #007bff; /* xanh dương */
    }
    .btn-detail:hover {
      background: #006ae0;
    }
    .btn-book {
      background: #28a745; /* xanh lá */
    }
    .btn-book:hover {
      background: #218838;
    }

    /* Responsive */
    @media (max-width: 1024px) {
      .card { width: calc(33.333% - 16px); }
    }
    @media (max-width: 768px) {
      .card { width: calc(50% - 16px); }
    }
    @media (max-width: 480px) {
      .card { width: 100%; }
    }
  </style>
            </head>

            <body class="dashboard-body">

                <div class="dashboard-container">
                    <!-- Sidebar -->
                    <aside class="sidebar" id="sidebar">
                        <div class="sidebar-header">
                            <img src="img/logo.png" alt="Logo" class="logo">
                            <h2 class="brand-text">Tour System</h2>
                            <button id="sidebar-close" class="sidebar-close">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>

                        <nav class="sidebar-nav">
                            <div class="nav-section">
                                <h5 class="nav-section-title">Main Menu</h5>
                                <ul class="nav-list">
                                   
                                    <li class="nav-item">
                                        <a href="/Code_SWP391/ListBookingTraveler_servlet" class="nav-link">
                                            <i class="fas fa-calendar-check"></i>
                                            <span>My Bookings</span>
                                          
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="/Code_SWP391/listTourUser_servlet" class="nav-link">
                                            <i class="fas fa-map-marked-alt"></i>
                                            <span>All Tours of Website</span>
                                        </a>
                                    </li>
                                     
                                </ul>
                            </div>

                        

                            <div class="nav-section">
                                <h5 class="nav-section-title">Settings</h5>
                                <ul class="nav-list">
                                  
                                    <li class="nav-item">
                                        <a href="/Code_SWP391/ProfileTravler_servlet" class="nav-link">
                                            <i class="fas fa-user-circle"></i>
                                            <span>Account information</span>
                                        </a>
                                    </li>
                                     <li class="nav-item">
                                        <a href="/Code_SWP391/report_page" class="nav-link">
                                               <i class="fas fa-file"></i>
                                            <span>Send report to admin</span>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </nav>
                    </aside>

                    <!-- Main Content -->
                    <main class="main-content">
                        <!-- Top Navigation -->
                        <nav class="top-nav">
                            <button id="sidebar-toggle" class="sidebar-toggle">
                                <i class="fas fa-bars"></i>
                            </button>

                            <div class="search-box">
                             
                            </div>

                            <div class="nav-actions">
                               
                                <div class="user-menu">
                                    <button class="user-menu-btn">
                                        <img src="img/logi.jpg" alt="User Avatar" class="user-avatar" hidden>
                                        <span class="user-name">
                                            <c:out value="${sessionScope.user.fullName}" />
                                        </span>
                                        <i class="fas fa-chevron-down"></i>
                                    </button>
                                    <div class="dropdown-menu">
                                        <a href="/Code_SWP391/ProfileTravler_servlet" class="dropdown-item">
                                            <i class="fas fa-user"></i> Account information
                                        </a>
                                   
                                        <div class="dropdown-divider"></div>
                                        <a href="/Code_SWP391/logout" class="dropdown-item text-danger">
                                            <i class="fas fa-sign-out-alt"></i> Logout
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </nav>

                        <!-- Dashboard Content -->
                        <div class="dashboard-content">
                            <!-- Statistics Cards -->
                           >

                                <div class="stat-card">
                                    <div class="stat-card-content">
                                        <h3>Total Bookings</h3>
                                        <div class="stat-value">${totalBookings}</div>
                                        
                                    </div>
                                    <div class="stat-icon">
                                        <i class="fas fa-calendar-check"></i>
                                    </div>
                                </div>

                                <div class="stat-card">
                                    <div class="stat-card-content">
                                        <h3>Total booking Approved</h3>
                                        <div class="stat-value">${approvedBookings}</div>
                                       
                                    </div>
                                    <div class="stat-icon">
                                        <i class="fas fa-route"></i>
                                    </div>
                                </div>
   <h2 class="section-title">Tour booked</h2>
    <div class="card-grid">
      <c:forEach var="t" items="${tours}">
        <form method="post" action="${pageContext.request.contextPath}/listTourUser_servlet" class="card">
          <div class="card-body">
            <h3><c:out value="${t.name}" /></h3>
                          <p>Days: <c:out value="${t.days}" /></p>
              <p>Language used by tour guide: <c:out value="${t.language}" /></p>
            <p class="price">${t.price} USD</p>
            <div class="buttons">
              <input type="hidden" name="tourId" value="${t.id}" />
              <button type="submit" name="action" value="detail" class="btn-detail">Detail</button>
              <button type="submit" name="action" value="book" class="btn-book">Book Again</button>
            </div>
          </div>
        </form>
      </c:forEach>
    </div>


                          
                            </div>
                        </div>
                    </main>
                </div>

                <!-- Notification Panel -->
                <div class="notifications-panel" id="notificationsPanel">
                    <!-- Will be populated dynamically -->
                </div>

                <!-- Messages Panel -->
                <div class="messages-panel" id="messagesPanel">
                    <!-- Will be populated dynamically -->
                </div>

                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                <script src="js/dashboard.js"></script>
            </body>

            </html>