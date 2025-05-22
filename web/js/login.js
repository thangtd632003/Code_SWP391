const translations = {
    vi: {
        welcomeText: "Chào mừng trở lại",
        welcomeSubtext: "Vui lòng đăng nhập để tiếp tục",
        emailLabel: "Địa chỉ email",
        passwordLabel: "Mật khẩu",
        rememberMe: "Ghi nhớ đăng nhập",
        forgotPassword: "Quên mật khẩu?",
        loginButton: "Đăng nhập",
        socialText: "Hoặc tiếp tục với",
        registerText: "Chưa có tài khoản? <a href='register.jsp'>Đăng ký</a>",
        notice: "Chào mừng đến với hệ thống đặt tour du lịch!",
        infoTitle: "Khám phá Tour Du lịch",
        infoDescription: "Hàng nghìn tour du lịch hấp dẫn đang chờ bạn!",
        tourCaption1: "Tour Du lịch Đà Lạt",
        tourCaption2: "Tour Du lịch Phú Quốc",
        tourCaption3: "Tour Du lịch Sapa",
        featureDestinations: "100+ Điểm đến",
        featureCustomers: "1000+ Khách hài lòng",
        featurePriceGuarantee: "Đảm bảo giá tốt nhất"
    },
    en: {
        welcomeText: "Welcome Back",
        welcomeSubtext: "Please sign in to continue",
        emailLabel: "Email Address",
        passwordLabel: "Password",
        rememberMe: "Remember me",
        forgotPassword: "Forgot Password?",
        loginButton: "Sign In",
        socialText: "Or continue with",
        registerText: "Don't have an account? <a href='register.jsp'>Sign up</a>",
        notice: "Welcome to the tour booking system!",
        infoTitle: "Explore Tours",
        infoDescription: "Thousands of exciting tours are waiting for you!",
        tourCaption1: "Da Lat Tour",
        tourCaption2: "Phu Quoc Tour",
        tourCaption3: "Sapa Tour",
        featureDestinations: "100+ Destinations",
        featureCustomers: "1000+ Happy Customers",
        featurePriceGuarantee: "Best Price Guarantee"
    }
};

// Language handling
function updateLanguage(lang) {
    const t = translations[lang];
    
    // Update text content
    document.getElementById('welcome-text').textContent = t.welcomeText;
    document.getElementById('welcome-subtext').textContent = t.welcomeSubtext;
    document.getElementById('email-label').textContent = t.emailLabel;
    document.getElementById('password-label').textContent = t.passwordLabel;
    document.getElementById('remember-text').textContent = t.rememberMe;
    document.getElementById('forgot-text').textContent = t.forgotPassword;
    document.getElementById('login-btn').textContent = t.loginButton;
    document.getElementById('social-text').textContent = t.socialText;
    document.getElementById('register-text').innerHTML = t.registerText;
    
    // Kiểm tra sự tồn tại của notice-message trước khi cập nhật
    const noticeMessage = document.getElementById('notice-message');
    if (noticeMessage) {
        noticeMessage.textContent = t.notice;
    }

    // Cập nhật info section
    const infoTitle = document.querySelector('.info-content h2');
    const infoDesc = document.querySelector('.info-description');
    if (infoTitle) infoTitle.textContent = t.infoTitle;
    if (infoDesc) infoDesc.textContent = t.infoDescription;
    
    // Cập nhật slide captions
    const slideCaptions = document.querySelectorAll('.slide-caption');
    if (slideCaptions.length >= 3) {
        slideCaptions[0].textContent = t.tourCaption1;
        slideCaptions[1].textContent = t.tourCaption2;
        slideCaptions[2].textContent = t.tourCaption3;
    }
    
    // Cập nhật features
    const features = document.querySelectorAll('.feature-item span');
    if (features.length >= 3) {
        features[0].textContent = t.featureDestinations;
        features[1].textContent = t.featureCustomers;
        features[2].textContent = t.featurePriceGuarantee;
    }

    // Lưu preference
    localStorage.setItem('preferred-language', lang);
}

// Theme handling
function setTheme(theme) {
    const body = document.body;
    const icon = document.querySelector('#theme-toggle i');
    
    if (theme === 'dark') {
        body.classList.add('dark-mode');
        icon.classList.remove('fa-moon');
        icon.classList.add('fa-sun');
    } else {
        body.classList.remove('dark-mode');
        icon.classList.remove('fa-sun');
        icon.classList.add('fa-moon');
    }
    
    // Lưu theme preference
    localStorage.setItem('theme', theme);
}

function toggleTheme() {
    const currentTheme = localStorage.getItem('theme') || 'light';
    const newTheme = currentTheme === 'light' ? 'dark' : 'light';
    setTheme(newTheme);
}

// Password visibility toggle
function setupPasswordToggle() {
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');

    togglePassword.addEventListener('click', function() {
        // Toggle type between password and text
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);
        
        // Toggle icon between eye and eye-slash
        const icon = this.querySelector('i');
        icon.classList.toggle('fa-eye');
        icon.classList.toggle('fa-eye-slash');
    });

    // Prevent form submission when clicking the toggle button
    togglePassword.addEventListener('mousedown', function(e) {
        e.preventDefault();
    });
}

// Slideshow functionality
function setupSlideshow() {
    const slides = document.querySelectorAll('.slide');
    let currentSlide = 0;

    function showSlide(n) {
        slides.forEach(slide => slide.style.display = 'none');
        slides[n].style.display = 'block';
    }

    function nextSlide() {
        currentSlide = (currentSlide + 1) % slides.length;
        showSlide(currentSlide);
    }

    // Initialize first slide
    showSlide(0);

    // Auto advance slides
    setInterval(nextSlide, 5000);
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    // Set up language switcher
    const langSwitch = document.getElementById('lang-switcher');
    langSwitch.addEventListener('change', (e) => {
        updateLanguage(e.target.value);
    });

    // Set up theme toggle
    const themeToggle = document.getElementById('theme-toggle');
    themeToggle.addEventListener('click', toggleTheme);

    // Load saved theme
    const savedTheme = localStorage.getItem('theme') || 'light';
    setTheme(savedTheme);

    // Load saved preferences
    const savedLang = localStorage.getItem('preferred-language') || 'vi';

    // Apply saved language
    langSwitch.value = savedLang;
    updateLanguage(savedLang);

    // Handle form submission
    const loginForm = document.getElementById('loginForm');
    loginForm.addEventListener('submit', function(e) {
        const remember = document.getElementById('remember').checked;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        if (remember) {
            localStorage.setItem('savedEmail', email);
            // Note: Storing passwords in localStorage is not secure
            // This is just for demonstration
            localStorage.setItem('savedPassword', password);
        } else {
            localStorage.removeItem('savedEmail');
            localStorage.removeItem('savedPassword');
        }
    });

    // Load saved credentials
    const savedEmail = localStorage.getItem('savedEmail');
    const savedPassword = localStorage.getItem('savedPassword');
    if (savedEmail && savedPassword) {
        document.getElementById('email').value = savedEmail;
        document.getElementById('password').value = savedPassword;
        document.getElementById('remember').checked = true;
    }
    
    // Setup password toggle
    setupPasswordToggle();
    
    // Setup slideshow
    setupSlideshow();
});

document.addEventListener('DOMContentLoaded', function() {
    const noticeContainer = document.querySelector('.notice-container');
    
    if (noticeContainer) {
        // Hiển thị notice
        noticeContainer.classList.add('show');
        
        // Ẩn notice sau 5 giây
        setTimeout(() => {
            noticeContainer.classList.remove('show');
            noticeContainer.classList.add('hide');
            
            // Xóa notice sau khi animation kết thúc
            setTimeout(() => {
                noticeContainer.remove();
            }, 500);
        }, 5000);
    }
});