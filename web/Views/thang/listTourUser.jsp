<%-- 
    Document   : listTourUser
    Created on : Jun 5, 2025, 4:46:09 PM
    Author     : thang
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entity.Tour" %>
<%@ page import="java.util.List" %>

<%
    // Lấy ba danh sách từ servlet:
    List<Tour> allTours         = (List<Tour>) request.getAttribute("allTours");
    List<Tour> topTours         = (List<Tour>) request.getAttribute("topTours");
    List<Tour> userBookedTours  = (List<Tour>) request.getAttribute("userBookedTours");
    String   keyword            = (String) request.getAttribute("keyword");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Danh sách Tour</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #f4f4f4;
      margin: 0; padding: 0;
    }
    .top-bar {
      position: fixed; top: 0; left: 0; width: 100%;
      background-color: #333; color: #fff;
      padding: 10px 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.2);
      z-index: 1000;
    }
    .top-bar form {
      display: inline-block;
    }
    .top-bar input[type="text"] {
      padding: 6px; font-size: 14px; width: 200px;
      border: none; border-radius: 4px;
    }
    .top-bar button {
      padding: 6px 10px; font-size: 14px; margin-left: 4px;
      border: none; border-radius: 4px;
      background: #ff8c00; color: #fff;
      cursor: pointer;
    }
    .top-bar button:hover {
      background: #e57c00;
    }
    .container {
      margin-top: 70px; /* để nhường chỗ cho thanh tìm kiếm */
      padding: 20px;
    }
    h2.section-title {
      margin-top: 40px; margin-bottom: 10px; color: #333;
    }
    .card-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
    }
    .card {
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
      width: calc(25% - 16px);
      min-width: 220px;
      display: flex;
      flex-direction: column;
      overflow: hidden;
    }
    .card img {
      width: 100%;
      height: 140px;
      object-fit: cover;
    }
    .card-body {
      flex: 1;
      padding: 12px;
      display: flex;
      flex-direction: column;
    }
    .card-body h3 {
      margin: 0 0 8px;
      font-size: 18px;
      color: #333;
    }
    .card-body p.price {
      margin: 0 0 12px;
      font-size: 16px;
      color: #ff8c00;
      font-weight: bold;
    }
    .card-body .buttons {
      margin-top: auto;
      display: flex;
      gap: 6px;
    }
    .card-body .buttons button {
      flex: 1;
      padding: 6px 8px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
    }
    .btn-detail {
      background: #007bff;
      color: #fff;
    }
    .btn-detail:hover {
      background: #006ae0;
    }
    .btn-book {
      background: #28a745;
      color: #fff;
    }
    .btn-book:hover {
      background: #218838;
    }

    /* Responsive */
    @media (max-width: 1024px) {
      .card { width: calc(33.333% - 16px); }
    }
    @media (max-width: 768px) {
      .card { width: calc(50% - 16px); }
    }
    @media (max-width: 480px) {
      .card { width: 100%; }
    }
  </style>
</head>
<body>
  <div class="top-bar">
    <!-- Form tìm kiếm chạy lại chính servlet -->
    <form method="get" action="${pageContext.request.contextPath}/listTourUser_thang">
      <input
        type="text"
        name="keyword"
        placeholder="Search by tour name..."
        value="${keyword != null ? keyword : ''}" />
      <button type="submit">Search</button>
    </form>
  </div>

  <div class="container">
    <!-- 1. Section: Các tour user đã booking -->
    <c:if test="${not empty userBookedTours}">
      <h2 class="section-title">List tour you booking</h2>
      <div class="card-grid">
        <c:forEach var="t" items="${userBookedTours}">
          <form method="post" action="${pageContext.request.contextPath}/listTourUser_thang" class="card">
            <!-- Ảnh tour -->
          
            <div class="card-body">
              <h3><c:out value="${t.name}" /></h3>
              <p class="price">${t.price} USD</p>
              <div class="buttons">
                <input type="hidden" name="tourId" value="${t.id}" />
                <button type="submit" name="action" value="detail" class="btn-detail">
                  Detail
                </button>
                <button type="submit" name="action" value="book" class="btn-book">
                  Book Again
                </button>
              </div>
            </div>
          </form>
        </c:forEach>
      </div>
    </c:if>

    <!-- 2. Section: Top 10 tour được booking nhiều nhất -->
    <h2 class="section-title">Top 10 Tour Phổ biến</h2>
    <div class="card-grid">
      <c:forEach var="t" items="${topTours}">
        <form method="post" action="${pageContext.request.contextPath}/listTourUser_thang" class="card">
        
          <div class="card-body">
            <h3><c:out value="${t.name}" /></h3>
            <p class="price">${t.price} VND</p>
            <div class="buttons">
              <input type="hidden" name="tourId" value="${t.id}" />
              <button type="submit" name="action" value="detail" class="btn-detail">Detail</button>
              <button type="submit" name="action" value="book" class="btn-book">Book</button>
            </div>
          </div>
        </form>
      </c:forEach>
    </div>

    <!-- 3. Section: Tất cả Tour (hoặc kết quả search) -->
    <h2 class="section-title">
      <c:choose>
        <c:when test="${not empty keyword}">
          Result for "<c:out value="${keyword}" />"
        </c:when>
        <c:otherwise>
          List tour
        </c:otherwise>
      </c:choose>
    </h2>
    <div class="card-grid">
      <c:forEach var="t" items="${allTours}">
        <form method="post" action="${pageContext.request.contextPath}/listTourUser_thang" class="card">
        
          <div class="card-body">
            <h3><c:out value="${t.name}" /></h3>
            <p class="price">${t.price} VND </p>
            <div class="buttons">
              <input type="hidden" name="tourId" value="${t.id}" />
              <button type="submit" name="action" value="detail" class="btn-detail">
                Detail
              </button>
              <button type="submit" name="action" value="book" class="btn-book">
                Book
              </button>
            </div>
          </div>
        </form>
      </c:forEach>
      <c:if test="${empty allTours}">
        <p>Not found.</p>
      </c:if>
    </div>
  </div>
</body>
</html>
