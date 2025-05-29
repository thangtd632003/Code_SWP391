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
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/dashboard" class="nav-link">
                            <i class="fas fa-home"></i>
                            <span>Dashboard</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#bookings" class="nav-link">
                            <i class="fas fa-calendar-check"></i>
                            <span>Bookings</span>
                            <span class="badge">12</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#tours" class="nav-link">
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
                        <a href="#guides" class="nav-link" onclick="openGuidesModal()">
                            <i class="fas fa-user-tie"></i>
                            <span>Tour Guides</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#" class="nav-link" onclick="openReviewModal()">
                            <i class="fas fa-star"></i>
                            <span>Reviews</span>
                            <span class="badge">
                                                <c:out value="${reviewCount}"/>
                                            </span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#reports" class="nav-link">
                            <i class="fas fa-chart-bar"></i>
                            <span>Reports</span>
                        </a>
                    </li>
                </ul>
            </div>

            <div class="nav-section">
                <h5 class="nav-section-title">Settings</h5>
                <ul class="nav-list">
                    <li class="nav-item">
                        <a href="javascript:void(0)" class="nav-link" id="settingsLink">
                            <i class="fas fa-cog"></i>
                            <span>Settings</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#profile" class="nav-link">
                            <i class="fas fa-user-circle"></i>
                            <span>Profile</span>
                        </a>
                    </li>
                </ul>
            </div>
        </nav>
    </aside>
                            <div class="nav-section">
                                <h5 class="nav-section-title">Settings</h5>
                                <ul class="nav-list">
                                    <li class="nav-item">
                                        <a href="#settings" class="nav-link">
                                            <i class="fas fa-cog"></i>
                                            <span>Settings</span>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="/Code_SWP391/ProfileTravler_thang" class="nav-link">
                                            <i class="fas fa-user-circle"></i>
                                            <span>Profile</span>
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
                        <img src="img/logi.jpg" alt="User Avatar" class="user-avatar">
                        <span class="user-name">
                                            <c:out value="${sessionScope.user.fullName}"/>
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
                                        <a href="/Code_SWP391/ProfileTravler_thang" class="dropdown-item">
                                            <i class="fas fa-user"></i> Profile
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
                        <i class="fas fa-chevron-down"></i>
                    </button>
                    <div class="dropdown-menu">
                        <a href="#profile" class="dropdown-item">
                            <i class="fas fa-user"></i> Profile
                        </a>
                        <a href="#settings" class="dropdown-item">
                            <i class="fas fa-cog"></i> Settings
                        </a>
                        <div class="dropdown-divider"></div>
                        <a href="logout" class="dropdown-item text-danger">
                            <i class="fas fa-sign-out-alt"></i> Logout
                        </a>
                    </div>
                </div>
            </div>
        </nav>

        <!-- Dashboard Content -->
        <div class="dashboard-content">
            <!-- User Information Section -->
            <div id="userProfileSection" class="card mb-4" style="display: none;">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h3 class="mb-0">User Information</h3>
                    <button class="btn btn-primary" onclick="openSettingsModal()">
                        <i class="fas fa-edit"></i> Edit Profile
                    </button>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-3 text-center mb-3">
                            <img src="img/logi.jpg" alt="User Avatar" class="rounded-circle img-fluid mb-2"
                                 style="max-width: 150px;">
                            <h4>${sessionScope.user.fullName}</h4>
                            <p class="text-muted">${sessionScope.user.role}</p>
                        </div>
                        <div class="col-md-9">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="fw-bold">Full Name:</label>
                                    <c:out value="${sessionScope.user.fullName}"/>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="fw-bold">Email:</label>
                                    <p>${sessionScope.user.email}</p>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="fw-bold">Phone Number:</label>
                                    <p>${sessionScope.user.phone}</p>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="fw-bold">Gender:</label>
                                    <p>${sessionScope.user.gender}</p>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="fw-bold">Birth Date:</label>
                                    <p><fmt:formatDate value="${sessionScope.user.birthDate}" pattern="dd/MM/yyyy"/></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
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
                        <h3>Total Guide</h3>
                        <div class="stat-value">${totalGuides}</div>
                        <div class="stat-change positive">
                            <i class="fas fa-user-tie"></i> Active Guides
                        </div>
                    </div>
                    <div class="stat-icon">
                        <i class="fas fa-user-tie"></i>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-content">
                        <h3>Total Traveler</h3>
                        <div class="stat-value">${totalTravelers}</div>
                        <div class="stat-change positive">
                            <i class="fas fa-users"></i> Active Travelers
                        </div>
                    </div>
                    <div class="stat-icon">
                        <i class="fas fa-users"></i>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-content">
                        <h3>Customer Rating</h3>
                        <div class="stat-value">${averageRating}</div>
                        <div class="stat-change positive">
                            <i class="fas fa-star"></i> Average Rating
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
                                                                pattern="dd/MM/yyyy HH:mm"/>
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

<!-- Review Modal -->
<div class="modal fade" id="reviewModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Manage Reviews</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <!-- Review Actions -->
                <div class="mb-3">
                    <button class="btn btn-primary" onclick="openAddReviewForm()">
                        <i class="fas fa-plus"></i> Add Review
                    </button>
                </div>

                <!-- Reviews Table -->
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>Guide ID</th>
                            <th>Rating</th>
                            <th>Comment</th>
                            <th>Date</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody id="reviewsTableBody">
                        <c:choose>
                            <c:when test="${not empty recentReviews}">
                                <c:forEach var="review" items="${recentReviews}">
                                    <tr>
                                        <td>${review.bookingId}</td>
                                        <td>${review.guideId}</td>
                                        <td class="rating-cell">
                                            <c:forEach begin="1" end="${review.rating}">
                                                <i class="fas fa-star text-warning"></i>
                                            </c:forEach>
                                        </td>
                                        <td>${review.comment}</td>
                                        <td><fmt:formatDate value="${review.createdAt}"
                                                            pattern="dd/MM/yyyy HH:mm"/></td>
                                        <td>
                                            <button class="btn btn-sm btn-info" onclick="editReview(${review.id})">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <button class="btn btn-sm btn-danger" onclick="deleteReview(${review.id})">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="text-center">No reviews available</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Add/Edit Review Form Modal -->
<div class="modal fade" id="reviewFormModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="reviewFormTitle">Add Review</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="reviewForm" class="needs-validation" novalidate>
                    <input type="hidden" id="reviewId" name="reviewId">
                    <div class="mb-3">
                        <label for="bookingId" class="form-label">Booking ID</label>
                        <input type="number" class="form-control" id="bookingId" name="bookingId" required>
                        <div class="invalid-feedback">Please enter a valid Booking ID</div>
                    </div>
                    <div class="mb-3">
                        <label for="guideId" class="form-label">Guide ID</label>
                        <input type="number" class="form-control" id="guideId" name="guideId" required>
                        <div class="invalid-feedback">Please enter a valid Guide ID</div>
                    </div>
                    <div class="mb-3">
                        <label for="rating" class="form-label">Rating</label>
                        <input type="number" class="form-control" id="rating" name="rating" min="1" max="5" required>
                        <div class="invalid-feedback">Please enter a rating between 1 and 5</div>
                    </div>
                    <div class="mb-3">
                        <label for="comment" class="form-label">Comment</label>
                        <textarea class="form-control" id="comment" name="comment" rows="3" required></textarea>
                        <div class="invalid-feedback">Please enter a comment</div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" onclick="saveReview()">Save</button>
            </div>
        </div>
    </div>
</div>

<!-- Settings Modal -->
<div class="modal fade"
     id="settingsModal"
     tabindex="-1"
     role="dialog"
     aria-labelledby="settingsModalLabel"
     aria-modal="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="settingsModalLabel">User Settings</h5>
                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Close">
                </button>
            </div>
            <div class="modal-body">
                <form id="settingsForm" class="needs-validation" novalidate>
                    <div class="mb-3">
                        <label for="fullName" class="form-label">Full Name</label>
                        <input type="text" class="form-control" id="fullName" name="fullName"
                               value="${sessionScope.user.fullName}" required>
                        <div class="invalid-feedback">Please enter your full name</div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email"
                               value="${sessionScope.user.email}" required>
                        <div class="invalid-feedback">Please enter a valid email</div>
                    </div>

                    <div class="mb-3">
                        <label for="phone" class="form-label">Phone Number</label>
                        <input type="tel" class="form-control" id="phone" name="phone"
                               value="${sessionScope.user.phone}" required>
                        <div class="invalid-feedback">Please enter your phone number</div>
                    </div>

                    <div class="mb-3">
                        <label for="gender" class="form-label">Gender</label>
                        <select class="form-select" id="gender" name="gender" required>
                            <option value="MALE" ${sessionScope.user.gender == 'MALE' ? 'selected' : ''}>Male</option>
                            <option value="FEMALE" ${sessionScope.user.gender == 'FEMALE' ? 'selected' : ''}>Female
                            </option>
                            <option value="OTHER" ${sessionScope.user.gender == 'OTHER' ? 'selected' : ''}>Other
                            </option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="birthDate" class="form-label">Birth Date</label>
                        <input type="date" class="form-control" id="birthDate" name="birthDate"
                               value="${sessionScope.user.birthDate}" required>
                        <div class="invalid-feedback">Please enter your birth date</div>
                    </div>

                    <div class="mb-3">
                        <label for="newPassword" class="form-label">New Password (leave blank to keep current)</label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword">
                        <div class="invalid-feedback">Password must be at least 6 characters</div>
                    </div>

                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Confirm New Password</label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword">
                        <div class="invalid-feedback">Passwords do not match</div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button"
                        class="btn btn-secondary"
                        data-bs-dismiss="modal">Cancel
                </button>
                <button type="button"
                        class="btn btn-primary"
                        onclick="saveSettings()">Save Changes
                </button>
            </div>
        </div>
    </div>
</div>

<!-- Tour Guides Modal -->
<div class="modal fade" id="guidesModal" tabindex="-1">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Tour Guides Management</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Rating</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="guidesTableBody">
                            <!-- Data will be loaded dynamically -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Guide Details Modal -->
<div class="modal fade" id="guideDetailsModal" tabindex="-1" aria-labelledby="guideDetailsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="guideDetailsModalLabel">Guide Details</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div id="guideDetailsContent">
                    <!-- Content will be loaded dynamically -->
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/dashboard.js"></script>
<script src="js/settings.js"></script>
</body>

</html>