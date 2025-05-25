document.addEventListener('DOMContentLoaded', function() {
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