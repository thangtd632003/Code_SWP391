<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>
    <!-- Header Controls -->
    <div class="header-controls">
        <select id="lang-switcher">
            <option value="vi">Tiếng Việt</option>
            <option value="en">English</option>
        </select>
        <button id="theme-toggle" class="theme-btn">
            <i class="fas fa-moon"></i>
        </button>
    </div>

    <!-- Notice -->
    <div class="notice-container">
        <div class="db-notice" id="notice-message">
            <%= request.getAttribute("notice") != null ? request.getAttribute("notice") : "Chào mừng đến với hệ thống đặt tour du lịch!" %>
        </div>
    </div>

    <!-- Thay thế phần page-container hiện tại -->
<div class="page-container">
    <!-- Left side - Login Form -->
    <div class="login-section">
        <div class="login-container">
            <!-- Giữ nguyên nội dung login-card hiện tại -->
            <div class="login-card">
                <div class="login-header">
                    <h1 id="welcome-text">Chào mừng trở lại</h1>
                    <p id="welcome-subtext">Vui lòng đăng nhập để tiếp tục</p>
                </div>

                <form action="login" method="post" id="loginForm">
                    <div class="form-group">
                        <label for="email" id="email-label">Địa chỉ email</label>
                        <input type="email" id="email" name="email" class="form-input" required>
                    </div>

                    <div class="form-group">
                        <label for="password" id="password-label">Mật khẩu</label>
                        <div class="password-input-container">
                            <input type="password" id="password" name="password" class="form-input" required>
                            <button type="button" id="togglePassword" class="password-toggle">
                                <i class="far fa-eye"></i>
                            </button>
                        </div>
                    </div>

                    <div class="form-footer">
                        <label class="remember-me">
                            <input type="checkbox" name="remember" id="remember">
                            <span id="remember-text">Ghi nhớ đăng nhập</span>
                        </label>
                        <a href="#" class="forgot-password" id="forgot-text">Quên mật khẩu?</a>
                    </div>

                    <button type="submit" class="btn btn-primary" id="login-btn">
                        Đăng nhập
                    </button>

                    <div class="social-login">
                        <p class="social-login-text" id="social-text">Hoặc tiếp tục với</p>
                        <div class="social-buttons">
                            <button type="button" class="btn btn-social">
                                <i class="fab fa-google"></i>
                            </button>
                            <button type="button" class="btn btn-social">
                                <i class="fab fa-facebook"></i>
                            </button>
                            <button type="button" class="btn btn-social">
                                <i class="fab fa-github"></i>
                            </button>
                        </div>
                    </div>

                    <div class="register-link" id="register-text">
                        Chưa có tài khoản? <a href="register.jsp">Đăng ký</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Right side - Advertisement/Info Section -->
    <div class="info-section">
        <div class="info-content">
            <h2>Khám phá Tour Du lịch</h2>
            <p class="info-description">Hàng nghìn tour du lịch hấp dẫn đang chờ bạn!</p>
            
            <!-- Slideshow container -->
<div class="slideshow-container">
    <div class="slide fade">
        <img src="img/dalat.jpg" alt="Tour Đà Lạt">
        <div class="slide-caption">Tour Du lịch Đà Lạt</div>
    </div>
    <div class="slide fade">
        <img src="img/phuquoc.jpg" alt="Tour Phú Quốc">
        <div class="slide-caption">Tour Du lịch Phú Quốc</div>
    </div>
    <div class="slide fade">
        <img src="img/sapa.jpg" alt="Tour Sapa">
        <div class="slide-caption">Tour Du lịch Sapa</div>
    </div>
</div>

            <!-- Điểm nhấn tính năng -->
            <div class="features">
                <div class="feature-item">
                    <i class="fas fa-map-marked-alt"></i>
                    <span>100+ Điểm đến</span>
                </div>
                <div class="feature-item">
                    <i class="fas fa-users"></i>
                    <span>1000+ Khách hài lòng</span>
                </div>
                <div class="feature-item">
                    <i class="fas fa-shield-alt"></i>
                    <span>Đảm bảo giá tốt nhất</span>
                </div>
            </div>
        </div>
    </div>
</div>

    <script src="js/login.js"></script>
</body>
</html>