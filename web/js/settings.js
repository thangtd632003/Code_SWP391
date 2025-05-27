document.addEventListener('DOMContentLoaded', function() {
    // Xử lý khi click vào Settings
    document.querySelector('a[href="#settings"]').addEventListener('click', function(e) {
        e.preventDefault();
        const settingsModal = new bootstrap.Modal(document.getElementById('settingsModal'));
        settingsModal.show();
    });
});

// Hàm validate và lưu settings
function saveSettings() {
    const form = document.getElementById('settingsForm');
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }

    // Kiểm tra password mới nếu có nhập
    const currentPassword = document.getElementById('currentPassword').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (newPassword) {
        if (!currentPassword) {
            alert('Please enter your current password');
            return;
        }
        if (newPassword !== confirmPassword) {
            alert('New passwords do not match');
            return;
        }
    }

    // Tạo FormData object
    const formData = new FormData(form);

    // Gửi request
    fetch('update-settings', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
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