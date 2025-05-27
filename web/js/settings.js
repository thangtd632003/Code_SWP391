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
    const settingsModal = new bootstrap.Modal(document.getElementById('settingsModal'));
    settingsModal.show();
}

function saveSettings() {
    const form = document.getElementById('settingsForm');
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }

    // Tạo đối tượng FormData
    const formData = new FormData();
    formData.append('fullName', document.getElementById('fullName').value);
    formData.append('email', document.getElementById('email').value);
    formData.append('phone', document.getElementById('phone').value);
    formData.append('gender', document.getElementById('gender').value);
    formData.append('birthDate', document.getElementById('birthDate').value);

    // Xử lý password nếu được nhập
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (newPassword) {
        if (newPassword !== confirmPassword) {
            alert('New passwords do not match');
            return;
        }
        formData.append('newPassword', newPassword);
    }

    // Gửi request
    fetch('update-settings', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('Settings updated successfully!');
            // Refresh trang để cập nhật thông tin mới
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