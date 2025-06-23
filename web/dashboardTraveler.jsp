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
                                    <li class="nav-item active">
                                        <a href="#dashboard" class="nav-link">
                                            <i class="fas fa-home"></i>
                                            <span>Dashboard</span>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="/Code_SWP391/ListBookingTraveler_servlet" class="nav-link">
                                            <i class="fas fa-calendar-check"></i>
                                            <span>Bookings</span>
                                            <span class="badge">12</span>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="/Code_SWP391/listTourUser_servlet" class="nav-link">
                                            <i class="fas fa-map-marked-alt"></i>
                                            <span>Tours</span>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#customers" class="nav-link">
                                            <i class="fas fa-users"></i>
                                            <span>Customers</span>
                                        </a>
                                    </li>
                                </ul>
                            </div>

                            <div class="nav-section">
                                <h5 class="nav-section-title">Management</h5>
                                <ul class="nav-list">
                                   
                                    <li class="nav-item">
                                        <a href="#reviews" class="nav-link">
                                            <i class="fas fa-star"></i>
                                            <span>Reviews</span>
                                            <span class="badge">
                                                <c:out value="${reviewCount}" />
                                            </span>
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
                                <input type="text" placeholder="Search...">
                                <i class="fas fa-search"></i>
                            </div>

                            <div class="nav-actions">
                                <button class="nav-action-btn" id="notificationsBtn">
                                    <i class="fas fa-bell"></i>
                                    <span class="badge">3</span>
                                </button>
                                <button class="nav-action-btn" id="messagesBtn">
                                    <i class="fas fa-envelope"></i>
                                    <span class="badge">5</span>
                                </button>
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
                                        <a href="#settings" class="dropdown-item">
                                            <i class="fas fa-cog"></i> Settings
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
                            <div class="stats-grid">
                                <div class="stat-card">
                                    <div class="stat-card-content">
                                        <h3>Total Revenue</h3>
                                        <div class="stat-value">$54,598</div>
                                        <div class="stat-change positive">
                                            <i class="fas fa-arrow-up"></i> 12.5%
                                        </div>
                                    </div>
                                    <div class="stat-icon">
                                        <i class="fas fa-dollar-sign"></i>
                                    </div>
                                </div>

                                <div class="stat-card">
                                    <div class="stat-card-content">
                                        <h3>Total Bookings</h3>
                                        <div class="stat-value">845</div>
                                        <div class="stat-change positive">
                                            <i class="fas fa-arrow-up"></i> 8.2%
                                        </div>
                                    </div>
                                    <div class="stat-icon">
                                        <i class="fas fa-calendar-check"></i>
                                    </div>
                                </div>

                                <div class="stat-card">
                                    <div class="stat-card-content">
                                        <h3>Active Tours</h3>
                                        <div class="stat-value">124</div>
                                        <div class="stat-change negative">
                                            <i class="fas fa-arrow-down"></i> 3.1%
                                        </div>
                                    </div>
                                    <div class="stat-icon">
                                        <i class="fas fa-route"></i>
                                    </div>
                                </div>

                                <div class="stat-card">
                                    <div class="stat-card-content">
                                        <h3>Customer Rating</h3>
                                        <div class="stat-value">4.8</div>
                                        <div class="stat-change positive">
                                            <i class="fas fa-arrow-up"></i> 0.3
                                        </div>
                                    </div>
                                    <div class="stat-icon">
                                        <i class="fas fa-star"></i>
                                    </div>
                                </div>
                            </div>

                            <!-- Charts Section -->
                            <div class="charts-grid">
                                <div class="chart-card main-chart">
                                    <div class="chart-header">
                                        <h3>Revenue Overview</h3>
                                        <div class="chart-actions">
                                            <select class="chart-period-select">
                                                <option>Last 7 Days</option>
                                                <option>Last 30 Days</option>
                                                <option>Last 3 Months</option>
                                                <option>Last Year</option>
                                            </select>
                                            <button class="chart-action-btn">
                                                <i class="fas fa-download"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="chart-body">
                                        <canvas id="revenueChart"></canvas>
                                    </div>
                                </div>

                                <div class="chart-card">
                                    <div class="chart-header">
                                        <h3>Popular Tours</h3>
                                        <button class="chart-action-btn">
                                            <i class="fas fa-ellipsis-v"></i>
                                        </button>
                                    </div>
                                    <div class="chart-body">
                                        <canvas id="toursChart"></canvas>
                                    </div>
                                </div>
                            </div>

                            <!-- Recent Activity -->
                            <div class="activity-grid">
                                <!-- Recent Bookings -->
                                <div class="card recent-bookings">
                                    <div class="card-header">
                                        <h3>Recent Bookings</h3>
                                        <button class="view-all-btn">View All</button>
                                    </div>
                                    <div class="card-body">
                                        <div class="table-responsive">
                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <th>Booking ID</th>
                                                        <th>Customer</th>
                                                        <th>Tour</th>
                                                        <th>Date</th>
                                                        <th>Status</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody id="recentBookingsTable">
                                                    <!-- Dynamic content will be loaded here -->
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>

                                <!-- Recent Reviews -->
                                <div class="card recent-reviews">
                                    <div class="card-header">
                                        <h3>Recent Reviews</h3>
                                        <button class="view-all-btn">View All</button>
                                    </div>
                                    <div class="card-body" id="recentReviews">
                                        <c:choose>
                                            <c:when test="${not empty recentReviews}">
                                                <ul class="list-group list-group-flush">
                                                    <c:forEach var="review" items="${recentReviews}">
                                                        <li class="list-group-item">
                                                            <div>
                                                                <strong>Booking ID:</strong> ${review.bookingId}
                                                                <span class="ms-2"><strong>Guide ID:</strong>
                                                                    ${review.guideId}</span>
                                                                <span class="ms-2"><strong>Rating:</strong>
                                                                    <c:forEach begin="1" end="${review.rating}" var="i">
                                                                        <i class="fas fa-star text-warning"></i>
                                                                    </c:forEach>
                                                                </span>
                                                            </div>
                                                            <div>
                                                                <em>${review.comment}</em>
                                                            </div>
                                                            <div class="text-muted small">
                                                                <fmt:formatDate value="${review.createdAt}"
                                                                    pattern="dd/MM/yyyy HH:mm" />
                                                            </div>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="text-muted">No reviews yet.</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
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