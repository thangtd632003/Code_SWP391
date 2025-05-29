document.addEventListener('DOMContentLoaded', function() {
    // Event cho nút Settings ở sidebar
    document.getElementById('settingsLink').addEventListener('click', function(e) {
        e.preventDefault();
        openSettingsModal();
    });

    // Event cho nút Settings ở dropdown
    document.querySelector('a[href="#settings"]').addEventListener('click', function(e) {
        e.preventDefault();
        openSettingsModal();
    });
});

function openSettingsModal() {
    const settingsModal = document.getElementById('settingsModal');
    const modal = new bootstrap.Modal(settingsModal, {
        keyboard: true,
        backdrop: true,
        focus: true
    });
    
    // Remove aria-hidden when showing modal
    settingsModal.addEventListener('shown.bs.modal', function () {
        settingsModal.removeAttribute('aria-hidden');
    });
    
    modal.show();
}

// Trong file settings.js, sửa lại hàm saveSettings()
function saveSettings() {
    const form = document.getElementById('settingsForm');
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }

    const fullName = document.getElementById('fullName').value;
    if (!fullName || fullName.trim() === '') {
        alert('Full name cannot be empty');
        return;
    }

    // Thay đổi cách gửi dữ liệu
    const data = new URLSearchParams();
    data.append('fullName', fullName.trim());
    data.append('email', document.getElementById('email').value);
    data.append('phone', document.getElementById('phone').value);
    data.append('gender', document.getElementById('gender').value);
    data.append('birthDate', document.getElementById('birthDate').value);

    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (newPassword) {
        if (newPassword !== confirmPassword) {
            alert('New passwords do not match');
            return;
        }
        data.append('newPassword', newPassword);
    }

    // Log data trước khi gửi
    console.log('Sending data:', Object.fromEntries(data));

    // Gửi request với headers và body mới
    fetch('update-settings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data
    })
    .then(response => {
        console.log('Response status:', response.status);
        return response.json();
    })
    .then(data => {
        console.log('Response data:', data);
        if (data.success) {
            alert('Settings updated successfully!');
            location.reload();
        } else {
            alert(data.message || 'Error updating settings');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error updating settings');
    });
}

function showUserProfile() {
    // Ẩn tất cả các section khác
    document.querySelectorAll('.dashboard-content > div').forEach(section => {
        if (section.id !== 'userProfileSection') {
            section.style.display = 'none';
        }
    });

    // Hiển thị section profile
    document.getElementById('userProfileSection').style.display = 'block';
}
document.addEventListener('DOMContentLoaded', function() {
    // Thêm event listeners cho các nút profile
    const profileLinks = document.querySelectorAll('a[href="#profile"]');
    profileLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            showUserProfile();
        });
    });
});