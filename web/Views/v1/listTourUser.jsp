<%-- 
    Document   : listTourUser
    Created on : Jun 5, 2025, 4:46:09 PM
    Author     : thang
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>List Tour</title>
  <style>
    /* Toàn trang */
    body {
      font-family: Arial, sans-serif;
      background: #ffffff; /* nền trắng */
      margin: 0; 
      padding: 0;
    }

    /* Thanh cố định trên cùng */
    .top-bar {
      position: fixed;
      top: 0; 
      left: 0; 
      width: 100%;
  
      color: #1565C0;
      background-color:#ffffff; 
    
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 10px 20px; 
      box-shadow: 0 2px 4px rgba(0,0,0,0.2);
      z-index: 1000;
    }
    .top-bar a.dashboard-link {
   color: #1565C0;
      text-decoration: none;
      font-size: 16px;
      font-weight: bold;
      margin-right: 20px;
    }
    .top-bar a.dashboard-link:hover {
        color:  #cce0f0;
    }

    /* Form tìm kiếm bên tay phải */
    .top-bar form {
      display: flex;
      align-items: center;
    }
    .top-bar input[type="text"] {
      padding: 6px; 
      font-size: 14px; 
      width: 200px;
      border: none; 
      border-radius: 4px;
      margin-right: 6px;
    }
    .top-bar button.search-btn {
      padding: 6px 10px; 
      font-size: 14px;
      border: none; 
      border-radius: 4px;
      background: #ff8c00; /* cam tươi */
      color: #fff;
      cursor: pointer;
    }
    .top-bar button.search-btn:hover {
      background: #e57c00;
    }

    .container {
      margin-top: 70px; 
      padding: 20px;
    }
    h2.section-title {
      margin-top: 40px; 
      margin-bottom: 10px; 
      color: #005ea8; 
    }

    .card-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
    }
    .card {
      background: #ffffff;
      border-radius: 8px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
      width: calc(25% - 16px);
      min-width: 220px;
      display: flex;
      flex-direction: column;
      overflow: hidden;
      border: 1px solid #e0e0e0;
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
      color: #003b5c; /* xanh dương đậm hơn cho tên tour */
    }
    .card-body p.price {
      margin: 0 0 12px;
      font-size: 16px;
      color: #ff8c00; /* cam */
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
      color: #fff;
    }
    .btn-detail {
      background: #007bff; 
    }
    .btn-detail:hover {
      background: #006ae0;
    }
    .btn-book {
      background: #28a745;
    }
    .btn-book:hover {
      background: #218838;
    }
 .filter-bar { margin-top:70px; padding:10px 20px; background:#f9f9f9; border-bottom:1px solid #e0e0e0; display:flex; align-items:center; gap:10px; }
    .filter-bar label { font-size:14px; }
    .filter-bar select { padding:6px; font-size:14px; border:1px solid #ccc; border-radius:4px; }
     .pagination { margin:20px 0; display:flex; list-style:none; gap:8px; }
    .pagination li { cursor:pointer; padding:6px 12px; border:1px solid #ccc; border-radius:4px; }
    .pagination li.active { background:#007bff; color:#fff; border-color:#007bff; }
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
    <a href="${pageContext.request.contextPath}/dashboard" class="dashboard-link">← Dashboard</a>

    <form method="get" action="${pageContext.request.contextPath}/listTourUser_servlet">
      <input
        type="text"
        name="keyword"
        placeholder="Search by tour name..."
        value="${keyword != null ? keyword : ''}" />
      <button type="submit" class="search-btn">Search</button>
    </form>
  </div>
  <div class="filter-bar">
    <label for="dayFilter">Filter by days:</label>
    <select id="dayFilter">
      <option value="all">All</option>
      <option value="1-3">1-3 days</option>
      <option value="3-7">3-7 days</option>
      <option value=">7">>7 days</option>
    </select>
  </div>
  <div class="container">
    <c:if test="${not empty userBookedTours}">
      <h2 class="section-title">List tour you booked</h2>
      <div class="card-grid">
        <c:forEach var="t" items="${userBookedTours}">
          <form method="post" action="${pageContext.request.contextPath}/listTourUser_servlet" class="card">
         
            <div class="card-body">
              <h3><c:out value="${t.name}" /></h3>
               <p>Days: <c:out value="${t.days}" /></p>
              <p>Language used by tour guide: <c:out value="${t.language}" /></p>
              <p class="price">${t.price} VND</p>
              <div class="buttons">
                <input type="hidden" name="tourId" value="${t.id}" />
                <button type="submit" name="action" value="detail" class="btn-detail">Detail</button>
                <button type="submit" name="action" value="book" class="btn-book">Book Again</button>
              </div>
            </div>
          </form>
        </c:forEach>
      </div>
    </c:if>

    <h2 class="section-title">Top 10 Most Booked Tours </h2>
    <div class="card-grid">
      <c:forEach var="t" items="${topTours}">
        <form method="post" action="${pageContext.request.contextPath}/listTourUser_servlet" class="card">
          <div class="card-body">
            <h3><c:out value="${t.name}" /></h3>
                          <p>Days: <c:out value="${t.days}" /></p>
              <p>Language used by tour guide: <c:out value="${t.language}" /></p>
            <p class="price">${t.price} USD</p>
            <div class="buttons">
              <input type="hidden" name="tourId" value="${t.id}" />
              <button type="submit" name="action" value="detail" class="btn-detail">Detail</button>
              <button type="submit" name="action" value="book" class="btn-book">Book</button>
            </div>
          </div>
        </form>
      </c:forEach>
    </div>

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
    <div class="card-grid"  id="allTours">
      <c:forEach var="t" items="${allTours}" >
        <form method="post" action="${pageContext.request.contextPath}/listTourUser_servlet" class="card" data-days="${t.days}">
          <div class="card-body">
            <h3><c:out value="${t.name}" /></h3>
                          <p>Days: <c:out value="${t.days}" /></p>
              <p>Language used by tour guide: <c:out value="${t.language}" /></p>
            <p class="price">${t.price} USD</p>
            <div class="buttons">
              <input type="hidden" name="tourId" value="${t.id}" />
              <button type="submit" name="action" value="detail" class="btn-detail">Detail</button>
              <button type="submit" name="action" value="book" class="btn-book">Book</button>
            </div>
             
          </div>
        </form>
      </c:forEach>
      <c:if test="${empty allTours}">
        <p>Not found.</p>
      </c:if>
    </div>
      <ul id="pagination" class="pagination"></ul>
  </div>
      <script>
   document.addEventListener('DOMContentLoaded', function() {
  const filter = document.getElementById('dayFilter');
  const cards = Array.from(document.querySelectorAll('#allTours .card'));
  const pagination = document.getElementById('pagination');
  const perPage = 8;
  let currentPage = 1;
  let filteredCards = [];

  function applyFilter() {
    const val = filter.value;
    filteredCards = cards.filter(card => {
      const days = parseInt(card.dataset.days, 10);
      if (val === 'all') return true;
      if (val === '1-3') return days >= 1 && days <= 3;
      if (val === '3-7') return days >= 3 && days <= 7;
      if (val === '>7')  return days > 7;
      return false;
    });

    currentPage = 1;
    setupPagination();
  }

  function setupPagination() {
    const pageCount = Math.ceil(filteredCards.length / perPage);
    pagination.innerHTML = '';

    if (pageCount <= 1) {
      pagination.style.display = 'none';
    } else {
      pagination.style.display = 'flex';
      for (let i = 1; i <= pageCount; i++) {
        const li = document.createElement('li');
        li.textContent = i;
        if (i === currentPage) li.classList.add('active');
        li.addEventListener('click', () => {
          currentPage = i;
          showPage();
          setupPagination(); // cập nhật lại active
        });
        pagination.appendChild(li);
      }
    }

    showPage();
  }

  function showPage() {
    cards.forEach(card => {
      card.style.display = 'none'; // ẩn tất cả
    });

    const start = (currentPage - 1) * perPage;
    const end = start + perPage;
    filteredCards.slice(start, end).forEach(card => {
      card.style.display = ''; // chỉ hiển thị trang hiện tại
    });
  }

  filter.addEventListener('change', applyFilter);
  applyFilter(); // gọi lần đầu khi load
});
  </script>
</body>

</html>
