<%-- 
    Document   : dashboard
    Created on : May 19, 2025, 3:55:00 PM
    Author     : Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="javax.servlet.http.*,javax.servlet.*,java.util.*"%>
<%
    String name = (String) session.getAttribute("name");
    String email = (String) session.getAttribute("email");
    String phone = (String) session.getAttribute("phone");
    Double balance = (Double) request.getAttribute("balance");
    String errorMsg = (String) request.getAttribute("errorMsg");
    Integer tourCount = (Integer) request.getAttribute("tourCount");
    if (tourCount == null) tourCount = 0;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Traveler Dashboard</title>
        <link rel="stylesheet" type="text/css" href="css/dashboard.css">
    </head>
    <body>
        <div class="dashboard-container">
            <h2>Traveler Dashboard</h2>
            <ul>
                <li>
                    <strong>Name:</strong> <%= name != null ? name : "N/A" %>
                    <a href="#" onclick="openModal('modalName')" style="margin-left:10px;">Edit</a>
                </li>
                <li>
                    <strong>Email:</strong> <%= email != null ? email : "N/A" %>
                    <a href="#" onclick="openModal('modalEmail')" style="margin-left:10px;">Edit</a>
                </li>
                <li>
                    <strong>Phone:</strong> <%= phone != null ? phone : "N/A" %>
                    <a href="#" onclick="openModal('modalPhone')" style="margin-left:10px;">Edit</a>
                    <a href="#" onclick="openModal('modalAddPhone')" style="margin-left:10px;color:green;">Add</a>
                    <a href="#" onclick="openModal('modalDeletePhone')" style="margin-left:10px;color:red;">Delete</a>
                </li>
                <li>
                    <strong>Current Balance:</strong> <%= balance != null ? String.format("%,.2f", balance) : "N/A" %>
                    <a href="#" onclick="openModal('modalTopup')" style="margin-left:10px;color:#28a745;">Top Up</a>
                </li>
                <li>
                    <strong>Tours Traveled:</strong> <%= tourCount %>
                </li>
            </ul>
            <a href="#" onclick="openModal('modalPassword')">Change Password</a>
        </div>

        <% if (errorMsg != null) { %>
        <div class="modal" id="modalError" style="display:block;">
            <div class="modal-content">
                <span class="close" onclick="closeModal('modalError')">&times;</span>
                <p style="color:red;"><%= errorMsg %></p>
            </div>
        </div>
        <% } %>

        <!-- Modal đổi tên -->
        <div id="modalName" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('modalName')">&times;</span>
                <form action="change-name" method="post">
                    <label for="newName">New Name:</label>
                    <input type="text" id="newName" name="newName" required>
                    <label for="passwordName">Current Password:</label>
                    <input type="password" id="passwordName" name="password" required>
                    <button type="submit">Save</button>
                </form>
            </div>
        </div>
        <!-- Modal đổi email -->
        <div id="modalEmail" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('modalEmail')">&times;</span>
                <form action="change-email" method="post">
                    <label for="newEmail">New Email:</label>
                    <input type="email" id="newEmail" name="newEmail" required>
                    <label for="passwordEmail">Current Password:</label>
                    <input type="password" id="passwordEmail" name="password" required>
                    <button type="submit">Save</button>
                </form>
            </div>
        </div>
        <!-- Modal đổi số điện thoại -->
        <div id="modalPhone" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('modalPhone')">&times;</span>
                <form action="change-phone" method="post">
                    <label for="oldPhone">Current Phone:</label>
                    <input type="text" id="oldPhone" name="oldPhone" required pattern="[0-9]{9,15}">
                    <label for="newPhone">New Phone:</label>
                    <input type="text" id="newPhone" name="newPhone" required pattern="[0-9]{9,15}">
                    <label for="passwordPhone">Current Password:</label>
                    <input type="password" id="passwordPhone" name="password" required>
                    <button type="submit">Save</button>
                </form>
            </div>
        </div>
        <!-- Modal thêm số điện thoại -->
        <div id="modalAddPhone" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('modalAddPhone')">&times;</span>
                <form action="add-phone" method="post">
                    <label for="addPhone">New Phone:</label>
                    <input type="text" id="addPhone" name="addPhone" required pattern="[0-9]{9,15}">
                    <label for="passwordAddPhone">Current Password:</label>
                    <input type="password" id="passwordAddPhone" name="password" required>
                    <button type="submit">Add</button>
                </form>
            </div>
        </div>
        <!-- Modal xóa số điện thoại -->
        <div id="modalDeletePhone" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('modalDeletePhone')">&times;</span>
                <form action="delete-phone" method="post">
                    <p>Bạn có chắc chắn muốn xóa số điện thoại này?</p>
                    <label for="passwordDeletePhone">Current Password:</label>
                    <input type="password" id="passwordDeletePhone" name="password" required>
                    <button type="submit" style="background:#dc3545;">Delete</button>
                </form>
            </div>
        </div>
        <!-- Modal đổi mật khẩu -->
        <div id="modalPassword" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('modalPassword')">&times;</span>
                <form action="change-password" method="post">
                    <label for="oldPassword">Old Password:</label>
                    <input type="password" id="oldPassword" name="oldPassword" required>
                    <label for="newPassword">New Password:</label>
                    <input type="password" id="newPassword" name="newPassword" required>
                    <button type="submit">Save</button>
                </form>
            </div>
        </div>
        <!-- Modal nạp tiền -->
        <div id="modalTopup" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('modalTopup')">&times;</span>
                <form action="member-register" method="post">
                    <label for="price">Chọn số tiền nạp:</label>
                    <select id="price" name="price" required>
                        <option value="1">100,000 VNĐ</option>
                        <option value="2">200,000 VNĐ</option>
                        <option value="5">500,000 VNĐ</option>
                        <option value="10">1,000,000 VNĐ</option>
                    </select>
                    <label for="language">Ngôn ngữ:</label>
                    <select id="language" name="language">
                        <option value="vn">Tiếng Việt</option>
                        <option value="en">English</option>
                    </select>
                    <button type="submit" style="background:#28a745;">Nạp tiền</button>
                </form>
            </div>
        </div>
        <script src="js/dashboard.js"></script>
    </body>
</html>
