<%-- 
    Document   : listBookingAdmin
    Created on : Jul 1, 2025, 3:50:39 PM
    Author     : thang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Booking Management - Admin</title>
  <style>
    body { font-family: Arial, sans-serif; padding: 20px; background: #f4f4f4; }
    .controls { margin-bottom: 20px; display: flex; justify-content: space-between; }
    .controls input[type=text] { padding: 6px; width: 200px; }
    .controls button { padding: 6px 12px; }
    table { width: 100%; border-collapse: collapse; background: #fff; }
    th, td { padding: 8px; border: 1px solid #ddd; text-align: left; }
    th a { color: inherit; text-decoration: none; display: block; }
    th a:hover { text-decoration: underline; }
    th.sorted-asc::after  { content: " ▲"; }
    th.sorted-desc::after { content: " ▼"; }
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

    /* Client‑side pagination */
    .pagination {
      margin-top: 15px;
      text-align: center;
    }
    .pagination span {
      margin: 0 5px;
      cursor: pointer;
      padding: 4px 8px;
      border: 1px solid #ddd;
      border-radius: 4px;
    }
    .pagination span.active {
      font-weight: bold;
      background-color: #007bff;
      color: #fff;
      border-color: #007bff;
    }
    .pagination span:hover:not(.active) {
      background-color: #f0f0f0;
    }
  </style>
</head>
<body>
  <div class="top-bar">
    <a href="${pageContext.request.contextPath}/dashboard">← Admin Dashboard</a>
  </div>

  <div style="margin-top:60px;">
    <!-- Search form -->
    <div class="controls">
      <form method="get" action="${pageContext.request.contextPath}/ListBookingAdmin_servlet">
        <input type="text"
               name="keyword"
               placeholder="Search by contact..."
               value="${fn:escapeXml(keyword)}" />
        <button type="submit" class="search-btn">Search</button>
      </form>
    </div>

    <!-- bookings table -->
    <table id="bookingTable">
      <thead>
        <tr>
          <th
            <c:if test="${sortField eq 'id'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/ListBookingAdmin_servlet'>
                <c:param name='keyword'   value='${fn:escapeXml(keyword)}'/>
                <c:param name='sortField' value='id'/>
                <c:choose>
                  <c:when test="${sortField eq 'id' and sortDir eq 'asc'}">
                    <c:param name='sortDir' value='desc'/>
                  </c:when>
                  <c:otherwise>
                    <c:param name='sortDir' value='asc'/>
                  </c:otherwise>
                </c:choose>
             </c:url>">
      ID
    </a>
          </th>
          <th
            <c:if test="${sortField eq 'tour_id'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
           <a href="<c:url value='/ListBookingAdmin_servlet'>
                <c:param name='keyword'   value='${fn:escapeXml(keyword)}'/>
                <c:param name='sortField' value='tour_id'/>
                <c:choose>
                  <c:when test="${sortField eq 'tour_id' and sortDir eq 'asc'}">
                    <c:param name='sortDir' value='desc'/>
                  </c:when>
                  <c:otherwise>
                    <c:param name='sortDir' value='asc'/>
                  </c:otherwise>
                </c:choose>
             </c:url>">
      Tour ID
    </a>
          </th>
          <th
            <c:if test="${sortField eq 'traveler_id'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
          <a href="<c:url value='/ListBookingAdmin_servlet'>
               <c:param name='keyword'      value='${fn:escapeXml(keyword)}'/>
               <c:param name='sortField'    value='traveler_id'/>
               <c:choose>
                 <c:when test="${sortField eq 'traveler_id' and sortDir eq 'asc'}">
                   <c:param name='sortDir' value='desc'/>
                 </c:when>
                 <c:otherwise>
                   <c:param name='sortDir' value='asc'/>
                 </c:otherwise>
               </c:choose>
             </c:url>">
      Traveler ID
    </a>
          </th>
          <th>Contact</th>
          <th
            <c:if test="${sortField eq 'status'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/ListBookingAdmin_servlet'>
               <c:param name='keyword'   value='${fn:escapeXml(keyword)}'/>
               <c:param name='sortField' value='status'/>
               <c:choose>
                 <c:when test="${sortField eq 'status' and sortDir eq 'asc'}">
                   <c:param name='sortDir' value='desc'/>
                 </c:when>
                 <c:otherwise>
                   <c:param name='sortDir' value='asc'/>
                 </c:otherwise>
               </c:choose>
             </c:url>">
      Status
    </a>
          </th>
          <th
            <c:if test="${sortField eq 'departure_date'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
           <a href="<c:url value='/ListBookingAdmin_servlet'>
               <c:param name='keyword'   value='${fn:escapeXml(keyword)}'/>
               <c:param name='sortField' value='departure_date'/>
               <c:choose>
                 <c:when test="${sortField eq 'departure_date' and sortDir eq 'asc'}">
                   <c:param name='sortDir' value='desc'/>
                 </c:when>
                 <c:otherwise>
                   <c:param name='sortDir' value='asc'/>
                 </c:otherwise>
               </c:choose>
             </c:url>">
      Departure
    </a>
          </th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="b" items="${bookings}">
          <tr>
            <td><c:out value="${b.id}"/></td>
            <td><c:out value="${b.tourId}"/></td>
            <td><c:out value="${b.travelerId}"/></td>
            <td><c:out value="${b.contactInfo}"/></td>
            <td><c:out value="${b.status}"/></td>
            <td><c:out value="${b.departureDate}"/></td>
            <td>
            <form method="get" style="display:inline;"
      action="${pageContext.request.contextPath}/DetailBookingAdmin_servlet">
    <input type="hidden" name="bookingId" value="${b.id}"/>
    <button class="action-btn" type="submit">Detail</button>
</form>
              
            </td>
          </tr>
        </c:forEach>
        <c:if test="${fn:length(bookings) == 0}">
          <tr><td colspan="7">No bookings found.</td></tr>
        </c:if>
      </tbody>
    </table>

    <!-- client‑side pagination -->
    <div class="pagination" id="pagination"></div>
  </div>

  <script>
    const rowsPerPage = 10;
    let   currentPage = 1;

    const allRows = Array.from(
      document.querySelectorAll('#bookingTable tbody tr')
    );

    function renderTable() {
      const totalRows  = allRows.length;
      const totalPages = Math.ceil(totalRows / rowsPerPage);
      if (currentPage > totalPages) currentPage = totalPages || 1;

      const start = (currentPage - 1) * rowsPerPage;
      const end   = start + rowsPerPage;

      const tbody = document.querySelector('#bookingTable tbody');
      tbody.innerHTML = '';
      allRows.slice(start, end).forEach(r => tbody.appendChild(r));

      const pg = document.getElementById('pagination');
      pg.innerHTML = '';
      for (let i = 1; i <= totalPages; i++) {
        const span = document.createElement('span');
        span.innerText = i;
        span.className = i === currentPage ? 'active' : '';
        span.onclick = () => { currentPage = i; renderTable(); };
        pg.appendChild(span);
      }
    }

    renderTable();
  </script>
</body>
</html>