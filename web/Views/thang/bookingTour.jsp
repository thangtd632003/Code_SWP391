<%-- 
    Document   : bookingTour
    Created on : Jun 5, 2025, 7:43:31 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entity.Tour" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<%
  
    
    // Định dạng ngày hiện tại để giới hạn ngày đặt
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String todayStr = sdf.format(new Date());
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Booking Tour</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #E3F2FD; /* Xanh dương nhạt */
      margin: 0; padding: 0;
    }
    .top-bar {
      position: fixed; top: 0; left: 0; width: 100%;
      background-color: #fff; /* Xanh đậm */
      color:  #1565C0;
      display: flex;
      align-items: center;
      padding: 10px 20px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.2);
      z-index: 1000;
    }
    .top-bar .back-btn {
      background: #fff;
      color: #1565C0;
      border: none;
      border-radius: 4px;
      padding: 6px 12px;
      cursor: pointer;
      font-weight: bold;
      margin-right: 20px;
    }
    .top-bar .back-btn:hover {
      background: #f0f0f0;
    }
    .top-bar h2 {
      margin: 0;
      font-size: 18px;
      flex-grow: 1;
    }
    .container {
      max-width: 500px;
      margin: 80px auto; /* Đẩy xuống 80px để tránh thanh navbar */
      background: #ffffff;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
      padding: 20px;
    }
    .tour-info {
      background: #BBDEFB; /* Xanh nhạt hơn */
      padding: 12px;
      border-radius: 6px;
      margin-bottom: 20px;
    }
    .tour-info p {
      margin: 6px 0;
      color: #0D47A1;
    }
    form label {
      display: block;
      margin-top: 12px;
      font-weight: bold;
      color: #0D47A1;
    }
    form input[type="date"],
    form input[type="number"],
    form textarea {
      width: 100%;
      padding: 8px;
      margin-top: 4px;
      border: 1px solid #90CAF9;
      border-radius: 4px;
      box-sizing: border-box;
    }
    form textarea {
      resize: vertical;
      height: 80px;
    }
    .buttons {
      margin-top: 20px;
      text-align: center;
    }
    .buttons button {
      background: #1976D2; /* Xanh đậm */
      color: #fff;
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
    }
    .buttons button:hover {
      background: #1565C0;
    }
    .message {
      margin-bottom: 16px;
      padding: 10px;
      border-radius: 4px;
      text-align: center;
    }
    .message.error {
      background: #FFCDD2; /* Đỏ nhạt */
      color: #B71C1C;
    }
    .message.success {
      background: #C8E6C9; /* Xanh lá nhạt */
      color: #1B5E20;
    }
    .message{
        color:#1565C0
    }
  </style>
</head>
<body>
  <div class="top-bar">
    <!-- Nút Back to Tour List -->
    <button class="back-btn" onclick="location.href='${pageContext.request.contextPath}/listTourUser_thang'">
      ← Back to Tours
    </button>
    
  </div>
  <div class="container">
    <h2>Booking Tour</h2>
    
    <!-- Hiển thị thông báo lỗi hoặc thành công nếu có -->
    <c:if test="${not empty errorMessage}">
      <div class="message error">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
      <div class="message success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty message}">
      <div class="message">${message}</div>
    </c:if>
    <!-- Thông tin tóm tắt của tour -->
    <c:if test="${not empty tour}">
      <div class="tour-info">
        <p><strong>Name tour:</strong> <c:out value="${tour.name}" /></p>
        <p><strong>Price(USD):</strong> <c:out value="${tour.price}" /> VND</p>
        <p><strong>Days of tour:</strong> <c:out value="${tour.days}" /></p>
        <p><strong>Language used by tour guide :</strong> <c:out value="${tour.language}" /></p>
      </div>
    </c:if>
    
    <!-- Form đặt tour -->
    <form method="post" action="${pageContext.request.contextPath}/createBooking_thang">
      <!-- Giữ lại tourId -->
      <input type="hidden" name="tourId" value="${tour.id}" />

      <!-- Ngày khởi hành -->
      <label for="departureDate">Depature date:</label>
      <input type="date"
             id="departureDate"
             name="departureDate"
             required
             min="<%= todayStr %>" />

      <!-- Số khách -->
      <label for="numPeople">Number people of tour:</label>
      <input type="number"
             id="numPeople"
             name="numPeople"
             required
             min="1"
             max="${tour.maxPeoplePerBooking}"
             value="1" />

      <!-- Thông tin liên hệ -->
      <label for="contactInfo">Contact info:</label>
      <textarea id="contactInfo"
                name="contactInfo"
                placeholder="Nhập email hoặc số điện thoại..."
                required></textarea>

      <div class="buttons">
        <button type="submit">Confirm</button>
      </div>
    </form>
  </div>
</body>
</html>
