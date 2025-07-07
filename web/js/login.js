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
        featurePriceGuarantee: "Đảm bảo giá tốt nhất",
        loginError: "Email hoặc mật khẩu không chính xác",
        systemError: "Đã xảy ra lỗi trong quá trình đăng nhập",
        minPasswordLength: "Mật khẩu phải có ít nhất 6 ký tự"
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
        featurePriceGuarantee: "Best Price Guarantee",
        loginError: "Invalid email or password",
        systemError: "An error occurred during login",
        minPasswordLength: "Password must be at least 6 characters"
    }
};

// Function để hiển thị thông báo lỗi
function showError(message) {
    const errorDiv = document.getElementById('password-error');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.classList.add('show');
    }
}

function clearError() {
    const errorDiv = document.getElementById('password-error');
    if (errorDiv) {
        errorDiv.classList.remove('show');
    }
}

// Function để kiểm tra và hiển thị thông báo lỗi
function checkAndShowError() {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    const currentLang = localStorage.getItem('preferred-language') || 'vi';
    
    if (error) {
        const errorDiv = document.getElementById('password-error');
        if (errorDiv) {
            errorDiv.textContent = translations[currentLang].loginError;
            errorDiv.classList.add('show');
        }
    }
}

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
    
    // Update notice text
    const noticeText = document.getElementById('notice-text');
    if (noticeText) {
        noticeText.textContent = t.notice;
    }

    // Update info section
    const infoTitle = document.querySelector('.info-content h2');
    const infoDesc = document.querySelector('.info-description');
    if (infoTitle) infoTitle.textContent = t.infoTitle;
    if (infoDesc) infoDesc.textContent = t.infoDescription;
    
    // Update slide captions
    const slideCaptions = document.querySelectorAll('.slide-caption');
    if (slideCaptions.length >= 3) {
        slideCaptions[0].textContent = t.tourCaption1;
        slideCaptions[1].textContent = t.tourCaption2;
        slideCaptions[2].textContent = t.tourCaption3;
    }
    
    // Update features
    const features = document.querySelectorAll('.feature-item span');
    if (features.length >= 3) {
        features[0].textContent = t.featureDestinations;
        features[1].textContent = t.featureCustomers;
        features[2].textContent = t.featurePriceGuarantee;
    }

    // Cập nhật thông báo lỗi nếu có
    const errorDiv = document.getElementById('password-error');
    if (errorDiv && errorDiv.classList.contains('show')) {
        errorDiv.textContent = t.loginError;
    }

    localStorage.setItem('preferred-language', lang);
}

// Theme handling
function setTheme(theme) {
    const body = document.body;
    const icon = document.querySelector('#theme-toggle i');
    
    if (theme === 'dark') {
        body.classList.add('dark-theme');
        icon.classList.remove('fa-moon');
        icon.classList.add('fa-sun');
    } else {
        body.classList.remove('dark-theme');
        icon.classList.remove('fa-sun');
        icon.classList.add('fa-moon');
    }
    
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
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);
        
        const icon = this.querySelector('i');
        icon.classList.toggle('fa-eye');
        icon.classList.toggle('fa-eye-slash');
    });

    togglePassword.addEventListener('mousedown', function(e) {
        e.preventDefault();
    });
}

// Slideshow functionality
function setupSlideshow() {
    const slides = document.querySelectorAll('.slide');
    let currentSlide = 0;

    function showSlide(index) {
        slides.forEach(slide => {
            slide.classList.remove('active', 'sliding-in', 'sliding-out');
        });

        if (slides[currentSlide]) {
            slides[currentSlide].classList.add('sliding-out');
        }

        currentSlide = index;
        slides[currentSlide].classList.add('active', 'sliding-in');
    }

    function nextSlide() {
        let next = (currentSlide + 1) % slides.length;
        showSlide(next);
    }

    if (slides.length > 0) {
        slides[0].classList.add('active');
        setInterval(nextSlide, 5000);
    }
}

// Welcome notice handling
function showWelcomeNotice() {
    const welcomeNotice = document.getElementById('welcome-notice');
    const noticeText = document.getElementById('notice-text');
    const currentLang = localStorage.getItem('preferred-language') || 'vi';

    if (welcomeNotice && noticeText) {
        noticeText.textContent = translations[currentLang].notice;
        welcomeNotice.style.display = 'flex';
        
        setTimeout(() => {
            welcomeNotice.classList.add('hide');
            setTimeout(() => {
                welcomeNotice.remove();
            }, 500);
        }, 5000);
    }
}

// Cập nhật setupFormValidation
function setupFormValidation() {
    const passwordInput = document.getElementById('password');
    const errorDiv = document.getElementById('password-error');
    
    if (passwordInput && errorDiv) {
        passwordInput.addEventListener('input', function() {
            errorDiv.classList.remove('show');
        });
    }
}

async function handleLogin(e) {
    e.preventDefault();
    
    clearError();
    
    const form = e.target;
    const email = form.querySelector('#email').value;
    const password = form.querySelector('#password').value;
    const remember = form.querySelector('#remember').checked;
    
    try {
        const response = await fetch('login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}&remember=${remember ? 'on' : 'off'}`
        });

        const data = await response.json();

        if (data.success) {
            // Hiển thị thông báo thành công (tùy chọn)
            const noticeContainer = document.querySelector('.notice-container');
            if (noticeContainer) {
                const notice = document.createElement('div');
                notice.className = 'db-notice success';
                notice.innerHTML = `
                    <i class="fas fa-check-circle"></i>
                    <span>Đăng nhập thành công! Đang chuyển hướng...</span>
                `;
                noticeContainer.innerHTML = '';
                noticeContainer.appendChild(notice);
            }

            // Đợi 1 giây trước khi chuyển hướng để người dùng thấy thông báo
            setTimeout(() => {
                window.location.href = data.redirect;
            }, 1000);
        } else {
             console.log('Login failed, error code =', data.error);  // debug

    const errorDiv = document.getElementById('password-error');
    const currentLang = localStorage.getItem('preferred-language') || 'vi';
    let message;
const raw = data.error;
const trimmed = (raw || '').trim();

const code = (data.error || '')
  .normalize('NFKC')                               // chuẩn hóa unicode
  .replace(/[\u200B-\u200D\uFEFF]/g, '')           // bỏ zero-width space
  .replace(/[\u0000-\u001F\u007F-\u009F]/g, '')    // bỏ control char ASCII
  .replace(/\s+/g, '')                             // bỏ mọi khoảng trắng
  .toUpperCase();
    switch (code) {
      
      case 'invalid_credentials':
        message = translations[currentLang].loginError;
        break;
      case 'system_error':
        message = translations[currentLang].systemError;
        break;
        case 'LOCKED':
        message = "Your account has been locked by admin";
        break;
      default:
        message = translations[currentLang].loginError;
    }


   showError(message);

  
        }
    } catch (error) {
        const currentLang = localStorage.getItem('preferred-language') || 'vi';
        showError(translations[currentLang].systemError);
    }
}

// Trong phần khởi tạo
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');

    // Nếu có lỗi từ server
    if (error) {
         showError(error); 
    }

    // Set up language switcher
    const langSwitch = document.getElementById('lang-switcher');
    langSwitch.addEventListener('change', (e) => {
        updateLanguage(e.target.value);
        // Nếu đang có lỗi hiển thị, cập nhật lại thông báo lỗi
        const errorDiv = document.getElementById('password-error');
        if (errorDiv && errorDiv.classList.contains('show')) {
            showError();
        }
    });

    // Set up theme toggle
    const themeToggle = document.getElementById('theme-toggle');
    themeToggle.addEventListener('click', toggleTheme);

    // Load saved preferences
    const savedTheme = localStorage.getItem('theme') || 'light';
    const savedLang = localStorage.getItem('preferred-language') || 'vi';

    // Apply saved preferences
    setTheme(savedTheme);
    langSwitch.value = savedLang;
    updateLanguage(savedLang);

    // Kiểm tra và hiển thị lỗi ngay khi trang được tải
    checkAndShowError();

    // Set up all features
    setupPasswordToggle();
    setupSlideshow();
    showWelcomeNotice();
    setupFormValidation();
    // ... other initialization code ...

    // Set up form submission
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});