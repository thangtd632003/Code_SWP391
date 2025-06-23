<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Hệ thống quản lý tour</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <style>
        :root {
            --primary-color: #3498db;
            --secondary-color: #2ecc71;
            --dark-color: #2c3e50;
            --light-color: #ecf0f1;
            --danger-color: #e74c3c;
            --warning-color: #f39c12;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            color: #333;
        }
        
        .sidebar {
            background-color: var(--dark-color);
            color: white;
            min-height: 100vh;
            padding-top: 20px;
            position: fixed;
            width: 250px;
        }
        
        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.8);
            padding: 12px 20px;
            margin-bottom: 5px;
            border-radius: 5px;
            transition: all 0.3s;
        }
        
        .sidebar .nav-link:hover,
        .sidebar .nav-link.active {
            background-color: rgba(255, 255, 255, 0.1);
            color: white;
        }
        
        .sidebar .nav-link i {
            margin-right: 10px;
            width: 20px;
            text-align: center;
        }
        
        .main-content {
            margin-left: 250px;
            padding: 20px;
        }
        
        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
            transition: transform 0.3s;
        }
        
        .card:hover {
            transform: translateY(-5px);
        }
        
        .stat-card {
            padding: 20px;
            border-radius: 10px;
            color: white;
        }
        
        .stat-card.blue {
            background: linear-gradient(135deg, #3498db, #2980b9);
        }
        
        .stat-card.green {
            background: linear-gradient(135deg, #2ecc71, #27ae60);
        }
        
        .stat-card.orange {
            background: linear-gradient(135deg, #f39c12, #e67e22);
        }
        
        .stat-card.red {
            background: linear-gradient(135deg, #e74c3c, #c0392b);
        }
        
        .stat-card .stat-icon {
            font-size: 48px;
            opacity: 0.6;
        }
        
        .stat-card .stat-value {
            font-size: 24px;
            font-weight: bold;
        }
        
        .stat-card .stat-label {
            font-size: 14px;
            opacity: 0.8;
        }
        
        .table {
            background-color: white;
            border-radius: 10px;
            overflow: hidden;
        }
        
        .table th {
            background-color: var(--dark-color);
            color: white;
            font-weight: 500;
        }
        
        .badge.status-pending {
            background-color: #f39c12;
        }
        
        .badge.status-approved {
            background-color: #2ecc71;
        }
        
        .badge.status-rejected {
            background-color: #e74c3c;
        }
        
        .badge.status-cancelled {
            background-color: #95a5a6;
        }
        
        .profile-header {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
        }
        
        .profile-pic {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            object-fit: cover;
            margin-right: 20px;
        }
        
        .welcome-text h4 {
            margin: 0;
            color: var(--dark-color);
        }
        
        .welcome-text p {
            margin: 5px 0 0 0;
            color: #7f8c8d;
        }
        
        .chart-container {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            height: 300px;
        }
        
        .action-btn {
            padding: 5px 10px;
            font-size: 12px;
        }
        
        .nav-tabs .nav-link {
            color: var(--dark-color);
            font-weight: 500;
        }
        
        .nav-tabs .nav-link.active {
            color: var(--primary-color);
            font-weight: 600;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="text-center mb-4">
            <h4>Tour Management</h4>
        </div>
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link active" href="dashboard">
                    <i class="fas fa-tachometer-alt"></i> Dashboard
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="tours">
                    <i class="fas fa-map-marked-alt"></i> Tours
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="bookings">
                    <i class="fas fa-calendar-check"></i> Bookings
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="reviews">
                    <i class="fas fa-star"></i> Reviews
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="profile">
                    <i class="fas fa-user"></i> Profile
                </a>
            </li>
            <li class="nav-item mt-5">
                <a class="nav-link" href="logout">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            </li>
        </ul>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Profile Header -->
        <div class="profile-header">
            <img src="${currentUser.profilePicture != null ? currentUser.profilePicture : 'assets/img/default-avatar.png'}" alt="Profile Picture" class="profile-pic">
            <div class="welcome-text">
                <h4>Xin chào, ${currentUser.fullName}</h4>
                <p>Chào mừng trở lại với hệ thống quản lý tour</p>
            </div>
        </div>

        <!-- Stats Cards -->
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="stat-card blue">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-value">${totalTours}</div>
                            <div class="stat-label">Tours</div>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-map-marked-alt"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card green">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-value">${totalBookings}</div>
                            <div class="stat-label">Đặt tour</div>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card orange">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-value">${pendingBookings}</div>
                            <div class="stat-label">Chờ xử lý</div>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-clock"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card red">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="stat-value">${averageRating}/5</div>
                            <div class="stat-label">Đánh giá trung bình</div>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-star"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Recent Bookings -->
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-white">
                        <h5 class="card-title mb-0">Đặt tour gần đây</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Tour</th>
                                        <th>Khách hàng</th>
                                        <th>Ngày khởi hành</th>
                                        <th>Số người</th>
                                        <th>Trạng thái</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="booking" items="${recentBookings}">
                                        <tr>
                                            <td>${booking.id}</td>
                                            <td>
                                                <c:forEach var="tour" items="${tours}">
                                                    <c:if test="${tour.id == booking.tourId}">
                                                        ${tour.title}
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                <c:forEach var="traveler" items="${travelers}">
                                                    <c:if test="${traveler.id == booking.travelerId}">
                                                        ${traveler.fullName}
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                            <td><fmt:formatDate value="${booking.departureDate}" pattern="dd/MM/yyyy" /></td>
                                            <td>${booking.numPeople}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${booking.status == 'PENDING'}">
                                                        <span class="badge status-pending">Chờ xử lý</span>
                                                    </c:when>
                                                    <c:when test="${booking.status == 'APPROVED'}">
                                                        <span class="badge status-approved">Đã duyệt</span>
                                                    </c:when>
                                                    <c:when test="${booking.status == 'REJECTED'}">
                                                        <span class="badge status-rejected">Từ chối</span>
                                                    </c:when>
                                                    <c:when test="${booking.status == 'CANCELLED'}">
                                                        <span class="badge status-cancelled">Đã hủy</span>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="btn-group">
                                                    <a href="booking-detail?id=${booking.id}" class="btn btn-sm btn-primary action-btn">
                                                        <i class="fas fa-eye"></i>
                                                    </a>
                                                    <c:if test="${booking.status == 'PENDING'}">
                                                        <button class="btn btn-sm btn-success action-btn" onclick="updateBookingStatus(${booking.id}, 'APPROVED')">
                                                            <i class="fas fa-check"></i>
                                                        </button>
                                                        <button class="btn btn-sm btn-danger action-btn" onclick="updateBookingStatus(${booking.id}, 'REJECTED')">
                                                            <i class="fas fa-times"></i>
                                                        </button>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Reviews -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header bg-white">
                        <h5 class="card-title mb-0">Đánh giá gần đây</h5>
                    </div>
                    <div class="card-body p-0">
                        <div class="list-group list-group-flush">
                            <c:forEach var="review" items="${recentReviews}">
                                <div class="list-group-item">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h6 class="mb-1">
                                            <c:forEach var="traveler" items="${travelers}">
                                                <c:if test="${traveler.id == review.travelerId}">
                                                    ${traveler.fullName}
                                                </c:if>
                                            </c:forEach>
                                        </h6>
                                        <small class="text-muted">
                                            <fmt:formatDate value="${review.createdAt}" pattern="dd/MM/yyyy" />
                                        </small>
                                    </div>
                                    <div class="mb-2">
                                        <c:forEach begin="1" end="5" var="i">
                                            <c:choose>
                                                <c:when test="${i <= review.rating}">
                                                    <i class="fas fa-star text-warning"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="far fa-star text-warning"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                    <p class="mb-1">${review.comment}</p>
                                    <small>
                                        Tour: 
                                        <c:forEach var="tour" items="${tours}">
                                            <c:if test="${tour.id == review.tourId}">
                                                ${tour.title}
                                            </c:if>
                                        </c:forEach>
                                    </small>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Upcoming Tours -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-white">
                        <h5 class="card-title mb-0">Tour sắp tới</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Tên tour</th>
                                        <th>Điểm đến</th>
                                        <th>Ngày khởi hành</th>
                                        <th>Số lượng đặt</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="tour" items="${upcomingTours}">
                                        <tr>
                                            <td>${tour.id}</td>
                                            <td>${tour.title}</td>
                                            <td>${tour.destination}</td>
                                            <td><fmt:formatDate value="${tour.startDate}" pattern="dd/MM/yyyy" /></td>
                                            <td>${tour.bookingCount}/${tour.capacity}</td>
                                            <td>
                                                <a href="tour-detail?id=${tour.id}" class="btn btn-sm btn-primary action-btn">
                                                    <i class="fas fa-eye"></i> Chi tiết
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap & jQuery JS -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    
    <script>
        // Hàm cập nhật trạng thái đặt tour
        function updateBookingStatus(bookingId, status) {
            if (confirm('Bạn có chắc chắn muốn ' + (status === 'APPROVED' ? 'duyệt' : 'từ chối') + ' đặt tour này?')) {
                $.ajax({
                    url: 'update-booking-status',
                    type: 'POST',
                    data: {
                        bookingId: bookingId,
                        status: status
                    },
                    success: function(response) {
                        if (response.success) {
                            alert('Cập nhật trạng thái thành công!');
                            location.reload();
                        } else {
                            alert('Có lỗi xảy ra: ' + response.message);
                        }
                    },
                    error: function() {
                        alert('Có lỗi xảy ra khi kết nối đến server');
                    }
                });
            }
        }
    </script>
</body>
</html>
