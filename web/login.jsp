<%-- Document : login Created on : May 18, 2025, 12:15:51 AM Author : Long --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
    <link rel="icon" type="image/png" href="https://cdn.jsdelivr.net/gh/simple-icons/simple-icons/icons/login.svg">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body>
    <!-- Language Switcher & Theme Switcher -->
    <div style="display: flex; justify-content: flex-end; align-items: center; margin: 10px 30px 0 0; gap: 16px;">
        <select id="lang-switcher" name="language" onchange="switchLanguage()">
            <option value="vi">Tiếng Việt</option>
            <option value="en">English</option>
        </select>
        <button id="theme-toggle" title="Chuyển đổi giao diện"
            style="padding: 6px 12px; border-radius: 6px; border: 1px solid #ccc; background: #fff; cursor: pointer;">
            <i class="fa fa-moon"></i>
        </button>
    </div>
    <% String notice=(String) request.getAttribute("notice"); if (notice==null || notice.isEmpty()) {
        notice="Không thể lấy thông báo từ hệ thống. Vui lòng liên hệ quản trị viên!" ; } %>
    <div class="db-notice"
        style="background:#fff3cd;color:#856404;padding:10px 15px;border-radius:5px;border:1px solid #ffeeba;margin:20px 30px 0 30px;">
        <i class="fa fa-bell" style="margin-right:7px;color:#ffc107;"></i>
        <%= notice %>
    </div>
    <div style="display: flex; align-items: flex-start;">
        <div class="login-container">
            <h2 id="login-title">
                <i class="fa-solid fa-right-to-bracket" style="margin-right:8px;color:#007bff;"></i>
                Login
            </h2>
            <form action="login" method="post">
                <label for="username" id="label-username">
                    <i class="fa fa-user" style="margin-right:5px;color:#007bff;"></i>Username:
                </label>
                <input type="text" id="username" name="username" required>
                <label for="password" id="label-password">
                    <i class="fa fa-lock" style="margin-right:5px;color:#007bff;"></i>Password:
                </label>
                <input type="password" id="password" name="password" required>
                <label>
                    <input type="checkbox" name="remember" id="remember"> Remember Me
                </label>
                <label for="captcha" id="label-captcha">
                    <i class="fa fa-shield-halved" style="margin-right:5px;color:#007bff;"></i>Captcha:
                </label>
                <input type="text" id="captcha" name="captcha" required>
                <input type="submit" id="login-btn" value="Login">
                <div class="login-options">
                    <a href="register.jsp" class="register-link btn-effect" id="register-link">
                        <i class="fa fa-user-plus" style="margin-right:5px;"></i>Register
                    </a>
                    <a href="forgot-password.jsp" class="btn-effect" id="forgot-link" style="margin-left:10px;">
                        <i class="fa fa-question-circle" style="margin-right:5px;"></i>Forgot Password
                    </a>
                    <hr>
                    <a href="https://accounts.google.com/o/oauth2/auth" class="google-login btn-effect" id="google-link" style="background:#fff; color:#db4437; border:1px solid #db4437; margin-bottom:5px;">
                        <i class="fab fa-google" style="margin-right:5px;"></i>Login with Google
                    </a>
                    <a href="https://www.facebook.com/v2.0/dialog/oauth" class="facebook-login btn-effect" id="facebook-link" style="background:#fff; color:#1877f3; border:1px solid #1877f3;">
                        <i class="fab fa-facebook" style="margin-right:5px;"></i>Login with Facebook
                    </a>
                </div>
            </form>
        </div>
        <div class="login-side-image" style="margin-left: 40px; margin-top: 30px;">
            <img src="img/nofication.png" alt="Login Image" class="side-rect-img">
        </div>
    </div>
    <script src="js/login.js"></script>
</body>

</html>