const translations = {
    vi: {
        login: "Đăng nhập",
        username: '<i class="fa fa-user" style="margin-right:5px;color:#007bff;"></i>Tên đăng nhập:',
        password: '<i class="fa fa-lock" style="margin-right:5px;color:#007bff;"></i>Mật khẩu:',
        loginBtn: "Đăng nhập",
        register: '<i class="fa fa-user-plus" style="margin-right:5px;"></i>Tạo tài khoản',
        forgot: '<i class="fa fa-question-circle" style="margin-right:5px;"></i>Quên mật khẩu?',
        google: '<i class="fab fa-google" style="margin-right:5px; color:#db4437;"></i>Đăng nhập bằng Google',
        facebook: '<i class="fab fa-facebook" style="margin-right:5px; color:#1877f3;"></i>Đăng nhập bằng Facebook'
    },
    en: {
        login: "Login",
        username: '<i class="fa fa-user" style="margin-right:5px;color:#007bff;"></i>Username:',
        password: '<i class="fa fa-lock" style="margin-right:5px;color:#007bff;"></i>Password:',
        loginBtn: "Login",
        register: '<i class="fa fa-user-plus" style="margin-right:5px;"></i>Register',
        forgot: '<i class="fa fa-question-circle" style="margin-right:5px;"></i>Forgot password?',
        google: '<i class="fab fa-google" style="margin-right:5px; color:#db4437;"></i>Login with Google',
        facebook: '<i class="fab fa-facebook" style="margin-right:5px; color:#1877f3;"></i>Login with Facebook'
    }
};

function switchLanguage() {
    const lang = document.getElementById('lang-switcher').value;
    document.getElementById('login-title').innerHTML = translations[lang].login;
    document.getElementById('label-username').innerHTML = translations[lang].username;
    document.getElementById('label-password').innerHTML = translations[lang].password;
    document.getElementById('login-btn').value = translations[lang].loginBtn;
    document.getElementById('register-link').innerHTML = translations[lang].register;
    document.getElementById('forgot-link').innerHTML = translations[lang].forgot;
    document.getElementById('google-link').innerHTML = translations[lang].google;
    document.getElementById('facebook-link').innerHTML = translations[lang].facebook;
}

// Đặt ngôn ngữ mặc định là tiếng Việt khi trang vừa load
window.onload = function() {
    document.getElementById('lang-switcher').value = 'vi';
    switchLanguage();

    // Theme toggle logic
    const themeBtn = document.getElementById('theme-toggle');
    themeBtn.onclick = function() {
        document.body.classList.toggle('dark-mode');
        // Đổi icon
        const icon = themeBtn.querySelector('i');
        if(document.body.classList.contains('dark-mode')) {
            icon.classList.remove('fa-moon');
            icon.classList.add('fa-sun');
        } else {
            icon.classList.remove('fa-sun');
            icon.classList.add('fa-moon');
        }
    };
};