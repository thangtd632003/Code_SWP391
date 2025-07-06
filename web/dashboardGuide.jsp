<%-- 
    Document   : guide-dashboard
    Created on : June 22, 2025
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
    <title>Tour Guide Dashboard</title>
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
                <h2 class="brand-text">Website connection with travelers and tour guides</h2>
                <button id="sidebar-close" class="sidebar-close">
                    <i class="fas fa-times"></i>
                </button>
            </div>

            <nav class="sidebar-nav">
                

                <div class="nav-section">
                    <h5 class="nav-section-title">Management</h5>
                    <ul class="nav-list">
                        <li class="nav-item">
                            <a href="tourList_servlet" class="nav-link">
                                <i class="fas fa-map-marked-alt"></i>
                                <span>My Tours</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="/Code_SWP391/ListBookingGuide_servlet" class="nav-link">
                                <i class="fas fa-calendar-check"></i>
                                <span>Bookings</span>
                                <span class="badge">
                                    <c:out value="${pendingBookings}" />
                                </span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="#reviews" class="nav-link">
                                <i class="fas fa-star"></i>
                                <span>Reviews</span>
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
                            <a href="/Code_SWP391/guideProfile_servlet" class="nav-link">
                                <i class="fas fa-user-circle"></i>
                                <span>Guide Profile</span>
                            </a>
                        </li>
                        <li class="nav-item">
                                                       <a href="/Code_SWP391/report_page" class="nav-link">

                                <i class="fas fa-file"></i>
                                <span>Send report to admin </span>
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
                           
                            <span class="user-name">
                                <c:out value="${currentUser.fullName}" />
                            </span>
                            <i class="fas fa-chevron-down"></i>
                        </button>
                        <div class="dropdown-menu">
                            <a href="/Code_SWP391/ProfileTravler_servlet" class="dropdown-item">
                                <i class="fas fa-user"></i> Account information
                            </a>
                            <a href="/Code_SWP391/guideProfile_servlet" class="dropdown-item">
                                <i class="fas fa-user"></i> Profile
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
                <!-- Guide Profile Card -->
                <div class="card mb-4">
                    <div class="card-body d-flex align-items-center p-4">
                        
                        <div class="guide-info">
                            <h2 class="mb-1">${currentUser.fullName}</h2>
                            <p class="text-muted mb-2"><i class="fas fa-envelope me-2"></i>${currentUser.email}</p>
                            <div class="d-flex align-items-center">
                                <div class="me-3">
                                    <span class="fw-bold">${averageRating}</span>
                                    <span class="text-warning">
                                        <c:forEach begin="1" end="5" var="i">
                                            <c:choose>
                                                <c:when test="${i <= averageRating}">
                                                    <i class="fas fa-star"></i>
                                                </c:when>
                                                <c:when test="${i > averageRating && i <= averageRating + 0.5}">
                                                    <i class="fas fa-star-half-alt"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="far fa-star"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </span>
                                </div>
                                <span class="badge bg-primary me-2">Tour Guide</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Statistics Cards -->
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-card-content">
                            <h3>Total Tours</h3>
                            <div class="stat-value">${totalTours}</div>
                            <div class="stat-change positive">
                                <i class="fas fa-route"></i> Active Tours
                            </div>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-map-marked-alt"></i>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-card-content">
                            <h3>Total Bookings</h3>
                            <div class="stat-value">${totalBookings}</div>
                            <div class="stat-change positive">
                                <i class="fas fa-users"></i> Travelers
                            </div>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-card-content">
                            <h3>Pending Bookings</h3>
                            <div class="stat-value">${pendingBookings}</div>
                            <div class="stat-change neutral">
                                <i class="fas fa-clock"></i> Waiting Approval
                            </div>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-hourglass-half"></i>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-card-content">
                            <h3>Average Rating</h3>
                            <div class="stat-value">${averageRating}/5.0</div>
                            <div class="stat-change positive">
                                <i class="fas fa-thumbs-up"></i> Customer Satisfaction
                            </div>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-star"></i>
                        </div>
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
    
    <script>
      
        
        // Initialize charts when the page loads
        document.addEventListener('DOMContentLoaded', function() {
            // Bookings Chart
            const bookingsCtx = document.getElementById('bookingsChart').getContext('2d');
            const bookingsChart = new Chart(bookingsCtx, {
                type: 'line',
                data: {
                    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
                    datasets: [{
                        label: 'Bookings',
                        data: [12, 19, 3, 5, 2, 3, 7],
                        borderColor: '#4e73df',
                        backgroundColor: 'rgba(78, 115, 223, 0.05)',
                        tension: 0.3,
                        fill: true
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {
                                drawBorder: false
                            }
                        },
                        x: {
                            grid: {
                                display: false
                            }
                        }
                    }
                }
            });
            
            // Ratings Chart
            const ratingsCtx = document.getElementById('ratingsChart').getContext('2d');
            const ratingsChart = new Chart(ratingsCtx, {
                type: 'doughnut',
                data: {
                    labels: ['5 Stars', '4 Stars', '3 Stars', '2 Stars', '1 Star'],
                    datasets: [{
                        data: [15, 8, 3, 1, 0],
                        backgroundColor: [
                            '#1cc88a', '#36b9cc', '#f6c23e', '#e74a3b', '#858796'
                        ],
                        hoverBackgroundColor: [
                            '#17a673', '#2c9faf', '#dda20a', '#be2617', '#6e707e'
                        ],
                        hoverBorderColor: "rgba(234, 236, 244, 1)",
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'right'
                        }
                    },
                    cutout: '70%'
                }
            });
        });
    </script>
</body>

</html>
