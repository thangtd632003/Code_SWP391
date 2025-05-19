function openModal(id) {
    document.getElementById(id).style.display = "block";
}
function closeModal(id) {
    document.getElementById(id).style.display = "none";
}
// Đóng modal khi click ra ngoài
window.onclick = function(event) {
    ['modalName','modalEmail','modalPassword'].forEach(function(id){
        var modal = document.getElementById(id);
        if (modal && event.target == modal) modal.style.display = "none";
    });
};