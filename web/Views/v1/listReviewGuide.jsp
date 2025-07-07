<%-- 
    Document   : listReviewGuide
    Created on : Jul 7, 2025, 6:44:49 AM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Guide - Review List</title>
  <style>
    body { font-family: Arial, sans-serif; padding: 20px; background: #f4f4f4; }
    .controls { margin-bottom: 20px; display: flex; justify-content: space-between; }
    .controls input[type=text] { padding: 6px; width: 200px; }
    .controls button { padding: 6px 12px; }
    table { width: 100%; border-collapse: collapse; background: #fff; }
    th, td { padding: 8px; border: 1px solid #ddd; text-align: left; }
    th a { color: inherit; text-decoration: none; display: block; }
    th.sorted-asc::after  { content: " ▲"; }
    th.sorted-desc::after { content: " ▼"; }
    .pagination { margin-top: 10px; text-align: center; }
    .pagination span {
      margin: 0 5px; cursor: pointer; padding: 4px 8px;
      border: 1px solid #ddd; border-radius: 4px;
    }
    .pagination span.active {
      font-weight: bold; background-color: #007bff; color: #fff; border-color: #007bff;
    }
    .pagination span:hover:not(.active) {
      background-color: #f0f0f0;
    }
    .action-btn { margin-right: 5px; padding: 4px 8px; font-size: 12px; }
    .top-bar {
      position: fixed; top: 0; left: 0; width: 100%;
      background-color: #f0f0f0; padding: 10px 20px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1); z-index: 1000;
    }
    .top-bar a {
      text-decoration: none; color: #007bff; font-weight: bold; font-size: 16px;
    }
    .top-bar a:hover { color: #0056b3; }
    .search-btn { margin-left: 8px; }
  </style>
</head>
<body>
  <div class="top-bar">
    <a href="${pageContext.request.contextPath}/dashboard">← Back to Dashboard</a>
  </div>

  <div style="margin-top:60px;">
    <!-- Search form -->
    <div class="controls">
      <form method="get" action="${pageContext.request.contextPath}/ListReviewGuide_servlet">
        <input type="text"
               name="keyword"
               placeholder="Search by comment..."
               value="${fn:escapeXml(keyword)}" />
        <button type="submit" class="search-btn">Search</button>
        <input type="hidden" name="sortField" value="${sortField}" />
        <input type="hidden" name="sortDir"   value="${sortDir}" />
        <input type="hidden" name="page"      value="1" />
      </form>
    </div>

    <!-- Reviews table -->
    <table id="reviewTable">
      <thead>
        <tr>
          <th
            <c:if test="${sortField eq 'id'}">
              class="${sortDir eq 'asc' ? 'sorted-asc':'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/ListReviewGuide_servlet'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='id'/>
                       <c:choose>
                         <c:when test="${sortField eq 'id' && sortDir eq 'asc'}">
                           <c:param name='sortDir' value='desc'/>
                         </c:when>
                         <c:otherwise>
                           <c:param name='sortDir' value='asc'/>
                         </c:otherwise>
                       </c:choose>
                       <c:param name='page' value='1'/>
                     </c:url>">
              ID
            </a>
          </th>

          <th
            <c:if test="${sortField eq 'booking_id'}">
              class="${sortDir eq 'asc' ? 'sorted-asc':'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/ListReviewGuide_servlet'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='booking_id'/>
                       <c:choose>
                         <c:when test="${sortField eq 'booking_id' && sortDir eq 'asc'}">
                           <c:param name='sortDir' value='desc'/>
                         </c:when>
                         <c:otherwise>
                           <c:param name='sortDir' value='asc'/>
                         </c:otherwise>
                       </c:choose>
                       <c:param name='page' value='1'/>
                     </c:url>">
              Booking ID
            </a>
          </th>

          <th
            <c:if test="${sortField eq 'rating'}">
              class="${sortDir eq 'asc' ? 'sorted-asc':'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/ListReviewGuide_servlet'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='rating'/>
                       <c:choose>
                         <c:when test="${sortField eq 'rating' && sortDir eq 'asc'}">
                           <c:param name='sortDir' value='desc'/>
                         </c:when>
                         <c:otherwise>
                           <c:param name='sortDir' value='asc'/>
                         </c:otherwise>
                       </c:choose>
                       <c:param name='page' value='1'/>
                     </c:url>">
              Rating
            </a>
          </th>

          <th
            <c:if test="${sortField eq 'comment'}">
              class="${sortDir eq 'asc' ? 'sorted-asc':'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/ListReviewGuide_servlet'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='comment'/>
                       <c:choose>
                         <c:when test="${sortField eq 'comment' && sortDir eq 'asc'}">
                           <c:param name='sortDir' value='desc'/>
                         </c:when>
                         <c:otherwise>
                           <c:param name='sortDir' value='asc'/>
                         </c:otherwise>
                       </c:choose>
                       <c:param name='page' value='1'/>
                     </c:url>">
              Comment
            </a>
          </th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="r" items="${reviews}">
          <tr>
            <td>${r.id}</td>
            <td>${r.bookingId}</td>
            <td>${r.rating}</td>
            <td><c:out value="${r.comment}"/></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>

    <c:if test="${fn:length(reviews) == 0}">
      <p>No reviews found.</p>
    </c:if>

    <!-- pagination -->
    <div class="pagination" id="pagination"></div>
  </div>

  <script>
    const rowsPerPage = 10;
    let currentPage = 1;
    const allRows = Array.from(document.querySelectorAll('#reviewTable tbody tr'));

    function renderTable() {
      const total = allRows.length;
      const pages = Math.ceil(total / rowsPerPage);
      if (currentPage > pages) currentPage = pages || 1;
      const start = (currentPage - 1) * rowsPerPage;
      const end = start + rowsPerPage;
      const tbody = document.querySelector('#reviewTable tbody');
      tbody.innerHTML = '';
      allRows.slice(start, end).forEach(r => tbody.appendChild(r));
      const pg = document.getElementById('pagination');
      pg.innerHTML = '';
      for (let i = 1; i <= pages; i++) {
        const s = document.createElement('span');
        s.innerText = i;
        s.className = (i === currentPage ? 'active' : '');
        s.onclick = () => { currentPage = i; renderTable(); };
        pg.appendChild(s);
      }
    }

    renderTable();
  </script>
</body>
</html>
