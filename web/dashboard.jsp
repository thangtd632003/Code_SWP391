<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Page manage website connection with travelers and tour guides</title>
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
            <h4>Website Management</h4>
        </div>
        <ul class="nav flex-column">
         
            <li class="nav-item">
                <a class="nav-link" href="/Code_SWP391/listTourAdmin_servlet">
                    <i class="fas fa-map-marked-alt"></i> Tours
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/Code_SWP391/ListBookingAdmin_servlet">
                    <i class="fas fa-calendar-check"></i> Bookings
                </a>
            </li>
          
             <li class="nav-item">
                <a class="nav-link" href="/Code_SWP391/userList_servlet">
                    <i class="fas fa-map-marked-alt"></i> Users
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/Code_SWP391/ProfileTraveler_servlet">
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
            <div class="welcome-text">
                <h4>Hello Admin</h4>
               
            </div>
        </div>

        <!-- Stats Cards -->
   <div class="row mb-4">
    <div class="col-md-3">
        <div class="stat-card blue">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <div class="stat-value">${totalGuides}</div>
                    <div class="stat-label">Guide</div>
                </div>
                <div class="stat-icon">
                    <i class="fas fa-user-tie"></i>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="stat-card green">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <div class="stat-value">${totalTravelers}</div>
                    <div class="stat-label">Traveler</div>
                </div>
                <div class="stat-icon">
                    <i class="fas fa-users"></i>
                </div>
            </div>
        </div>
    </div>
    
    <div class="col-md-3">
        <div class="stat-card red">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <div class="stat-value">${averageRating}/5</div>
                    <div class="stat-label">Review Average</div>
                </div>
                <div class="stat-icon">
                    <i class="fas fa-star"></i>
                </div>
            </div>
        </div>
    </div>
                     <div class="col-md-3">
        <div class="stat-card blue">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <div class="stat-value">${toursNumber}</div>
                    <div class="stat-label">Total tour of website</div>
                </div>
                <div class="stat-icon">
                    <i class="fas fa-user-tie"></i>
                </div>
            </div>
        </div>
    </div>


       
   </div>    
                     <div class="col-md-3">
        <div class="stat-card green">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <div class="stat-value">${bookingNumber}</div>
                    <div class="stat-label">Total approved booking of website</div>
                </div>
                <div class="stat-icon">
                    <i class="fas fa-user-tie"></i>
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
     
    </script>
</body>
</html>