document.addEventListener('DOMContentLoaded', function () {
    initializeDashboard();
    setupEventListeners();
    loadDashboardData();
});

// Dashboard Configuration
const CONFIG = {
    chartColors: {
        primary: '#4f46e5',
        secondary: '#6366f1',
        success: '#10b981',
        danger: '#ef4444',
        warning: '#f59e0b',
        info: '#3b82f6',
        light: '#f3f4f6',
        dark: '#1f2937'
    },
    animationDuration: 300,
    updateInterval: 300000, // 5 minutes
    chartOptions: {
        responsive: true,
        maintainAspectRatio: false
    }
};

// Thêm biến toàn cục để lưu instance chart
let revenueChartInstance = null;
let toursChartInstance = null;

// Dashboard Initialization
function initializeDashboard() {
    setupCharts();
    initializeDataTables();
    setupTheme();
    startAutoRefresh();
}

// Event Listeners Setup
function setupEventListeners() {
    // Sidebar Toggle
    document.getElementById('sidebar-toggle').addEventListener('click', toggleSidebar);

    // User Menu
    document.querySelector('.user-menu-btn').addEventListener('click', toggleUserMenu);

    // Navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', handleNavigation);
    });

    // Notifications & Messages
    setupNotificationHandlers();

    // Search Functionality
    setupSearch();

    // Window Resize Handler
    window.addEventListener('resize', handleResize);

    // Add event listeners to nav items
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', function() {
            // Remove active class from all items
            document.querySelectorAll('.nav-item').forEach(el => {
                el.classList.remove('active');
            });
            
            // Add active class to clicked item
            this.classList.add('active');
        });
    });
}

// Sidebar Functions
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.querySelector('.main-content');

    sidebar.classList.toggle('collapsed');
    mainContent.classList.toggle('expanded');

    // Save preference
    localStorage.setItem('sidebarState', sidebar.classList.contains('collapsed'));
}

// Chart Setup
function setupCharts() {
    // Destroy old charts if they exist
    if (revenueChartInstance) {
        revenueChartInstance.destroy();
    }
    if (toursChartInstance) {
        toursChartInstance.destroy();
    }

    // Revenue Chart
    revenueChartInstance = new Chart(
        document.getElementById('revenueChart').getContext('2d'),
        {
            type: 'line',
            data: {
                labels: getLastNMonths(6),
                datasets: [{
                    label: 'Revenue',
                    data: [12000, 19000, 15000, 25000, 22000, 30000],
                    borderColor: CONFIG.chartColors.primary,
                    backgroundColor: createGradient(CONFIG.chartColors.primary),
                    tension: 0.4,
                    fill: true
                }]
            },
            options: {
                ...CONFIG.chartOptions,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false
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
        }
    );

    // Popular Tours Chart
    toursChartInstance = new Chart(
        document.getElementById('toursChart').getContext('2d'),
        {
            type: 'doughnut',
            data: {
                labels: ['Đà Lạt', 'Phú Quốc', 'Sapa', 'Hạ Long', 'Nha Trang'],
                datasets: [{
                    data: [30, 25, 20, 15, 10],
                    backgroundColor: Object.values(CONFIG.chartColors).slice(0, 5),
                    borderWidth: 0
                }]
            },
            options: {
                ...CONFIG.chartOptions,
                cutout: '75%',
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 20
                        }
                    }
                }
            }
        }
    );
}

// Data Loading & Updates
async function loadDashboardData() {
    try {
        const [bookings, reviews, stats] = await Promise.all([
            fetchData('/api/recent-bookings'),
            fetchData('/api/recent-reviews'),
            fetchData('/api/statistics')
        ]);

        updateDashboard(bookings, reviews, stats);
    } catch (error) {
        handleError(error);
    }
}

async function fetchData(endpoint) {
    try {
        const response = await fetch(endpoint);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return await response.json();
    } catch (error) {
        console.error(`Error fetching data from ${endpoint}:`, error);
        throw error;
    }
}

function updateDashboard(bookings, reviews, stats) {
    updateBookingsTable(bookings);
    updateReviewsList(reviews);
    updateStatistics(stats);
    updateCharts(stats);
}

// Table Updates
function updateBookingsTable(bookings) {
    const tbody = document.querySelector('#recentBookingsTable');
    tbody.innerHTML = bookings.map(booking => `
        <tr class="booking-row" data-id="${booking.id}">
            <td>#${booking.id}</td>
            <td>
                <div class="user-info">
                    <img src="${booking.userAvatar}" alt="${booking.userName}" class="user-avatar-sm">
                    <span>${booking.userName}</span>
                </div>
            </td>
            <td>${booking.tourName}</td>
            <td>${formatDate(booking.date)}</td>
            <td>
                <span class="status-badge ${booking.status.toLowerCase()}">
                    ${booking.status}
                </span>
            </td>
            <td>
                <div class="action-buttons">
                    <button class="action-btn view-btn" title="View Details">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="action-btn edit-btn" title="Edit Booking">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="action-btn delete-btn" title="Cancel Booking">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');

    // Add event listeners to action buttons
    setupTableActionListeners();
}

// Review Management
let reviewModal;
let reviewFormModal;

document.addEventListener('DOMContentLoaded', function() {
    reviewModal = new bootstrap.Modal(document.getElementById('reviewModal'));
    reviewFormModal = new bootstrap.Modal(document.getElementById('reviewFormModal'));
});

function openReviewModal() {
    reviewModal.show();
}

function openAddReviewForm() {
    document.getElementById('reviewFormTitle').textContent = 'Add Review';
    document.getElementById('reviewForm').reset();
    document.getElementById('reviewId').value = '';
    new bootstrap.Modal(document.getElementById('reviewFormModal')).show();
}

function editReview(reviewId) {
    fetch(`api/reviews/${reviewId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Review not found');
            }
            return response.json();
        })
        .then(review => {
            document.getElementById('reviewFormTitle').textContent = 'Edit Review';
            document.getElementById('reviewId').value = review.id;
            document.getElementById('bookingId').value = review.bookingId;
            document.getElementById('guideId').value = review.guideId;
            document.getElementById('rating').value = review.rating;
            document.getElementById('comment').value = review.comment;
            
            // Close the review list modal and show the edit form modal
            const reviewModal = bootstrap.Modal.getInstance(document.getElementById('reviewModal'));
            if (reviewModal) {
                reviewModal.hide();
            }
            const reviewFormModal = new bootstrap.Modal(document.getElementById('reviewFormModal'));
            reviewFormModal.show();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error loading review details');
        });
}

function deleteReview(reviewId) {
    if (confirm('Are you sure you want to delete this review?')) {
        fetch(`api/reviews/${reviewId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                location.reload();
            } else {
                alert('Failed to delete review');
            }
        })
        .catch(error => alert('Error deleting review: ' + error));
    }
}

function saveReview() {
    const form = document.getElementById('reviewForm');
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }

    const formData = new FormData(form);
    const reviewId = formData.get('reviewId');
    
    // Convert FormData to URL-encoded string
    const data = new URLSearchParams(formData).toString();
    
    const method = reviewId ? 'PUT' : 'POST';
    const url = reviewId ? `api/reviews/${reviewId}` : 'api/reviews';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: data
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            // Close the modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('reviewFormModal'));
            modal.hide();
            // Reload the page to show updated data
            location.reload();
        } else {
            alert('Failed to save review');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error saving review: ' + error.message);
    });
}

// Utility Functions
function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString('vi-VN', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

function createGradient(color) {
    const ctx = document.getElementById('revenueChart').getContext('2d');
    const gradient = ctx.createLinearGradient(0, 0, 0, 400);
    gradient.addColorStop(0, `${color}40`);
    gradient.addColorStop(1, `${color}00`);
    return gradient;
}

function getLastNMonths(n) {
    const months = [];
    const date = new Date();
    for (let i = n - 1; i >= 0; i--) {
        const month = new Date(date.getFullYear(), date.getMonth() - i, 1);
        months.push(month.toLocaleDateString('vi-VN', { month: 'short' }));
    }
    return months;
}

// Error Handling
function handleError(error) {
    console.error('Dashboard Error:', error);
    showNotification('Error loading dashboard data', 'error');
}

// Notifications
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <i class="fas fa-${type === 'error' ? 'exclamation-circle' : 'info-circle'}"></i>
        <span>${message}</span>
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.classList.add('show');
        setTimeout(() => {
            notification.classList.remove('show');
            setTimeout(() => notification.remove(), 300);
        }, 3000);
    }, 100);
}

// Auto Refresh
function startAutoRefresh() {
    setInterval(loadDashboardData, CONFIG.updateInterval);
}

// Theme Management
function setupTheme() {
    const savedTheme = localStorage.getItem('dashboardTheme') || 'light';
    document.body.classList.toggle('dark-mode', savedTheme === 'dark');
}

// Search Functionality
function setupSearch() {
    const searchInput = document.querySelector('.search-box input');
    let searchTimeout;

    searchInput.addEventListener('input', (e) => {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            performSearch(e.target.value);
        }, 300);
    });
}

async function performSearch(query) {
    if (!query) return;

    try {
        const results = await fetchData(`/api/search?q=${encodeURIComponent(query)}`);
        showSearchResults(results);
    } catch (error) {
        handleError(error);
    }
}

// Initialize Dashboard
initializeDashboard();

document.addEventListener('DOMContentLoaded', function() {
    // Get current URL hash
    const currentHash = window.location.hash || '#dashboard';
    
    // Add click handlers to all nav links
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', function(e) {
            // Remove active class from all nav items
            document.querySelectorAll('.nav-item').forEach(item => {
                item.classList.remove('active');
            });
            
            // Add active class to parent nav-item
            this.closest('.nav-item').classList.add('active');
        });
        
        // Set initial active state based on URL
        if (link.getAttribute('href') === currentHash) {
            link.closest('.nav-item').classList.add('active');
        }
    });
});