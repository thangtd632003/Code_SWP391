<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Tour List - Admin</title>
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

    /* Phân trang client */
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
    <div class="controls">
      <form method="get" action="${pageContext.request.contextPath}/listTourAdmin_servlet">
        <input type="text"
               name="keyword"
               placeholder="Search by name..."
               value="${fn:escapeXml(keyword)}" />
        <button type="submit" class="search-btn">Search</button>
      </form>
    </div>

    <table id="tourTable">
      <thead>
        <tr>
          <th
            <c:if test="${sortField eq 'id'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/listTourAdmin_servlet'>
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
                     </c:url>">
              ID
            </a>
          </th>

          <th
            <c:if test="${sortField eq 'name'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/listTourAdmin_servlet'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='name'/>
                       <c:choose>
                         <c:when test="${sortField eq 'name' && sortDir eq 'asc'}">
                           <c:param name='sortDir' value='desc'/>
                         </c:when>
                         <c:otherwise>
                           <c:param name='sortDir' value='asc'/>
                         </c:otherwise>
                       </c:choose>
                     </c:url>">
              Name
            </a>
          </th>

          <th
            <c:if test="${sortField eq 'price'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/listTourAdmin_servlet'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='price'/>
                       <c:choose>
                         <c:when test="${sortField eq 'price' && sortDir eq 'asc'}">
                           <c:param name='sortDir' value='desc'/>
                         </c:when>
                         <c:otherwise>
                           <c:param name='sortDir' value='asc'/>
                         </c:otherwise>
                       </c:choose>
                     </c:url>">
              Price(USD)
            </a>
          </th>

          <th
            <c:if test="${sortField eq 'max_people_per_booking'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/listTourAdmin_servlet'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='max_people_per_booking'/>
                       <c:choose>
                         <c:when test="${sortField eq 'max_people_per_booking' && sortDir eq 'asc'}">
                           <c:param name='sortDir' value='desc'/>
                         </c:when>
                         <c:otherwise>
                           <c:param name='sortDir' value='asc'/>
                         </c:otherwise>
                       </c:choose>
                     </c:url>">
              Max People
            </a>
          </th>

          <th
            <c:if test="${sortField eq 'days'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/listTourAdmin_servlet'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='days'/>
                       <c:choose>
                         <c:when test="${sortField eq 'days' && sortDir eq 'asc'}">
                           <c:param name='sortDir' value='desc'/>
                         </c:when>
                         <c:otherwise>
                           <c:param name='sortDir' value='asc'/>
                         </c:otherwise>
                       </c:choose>
                     </c:url>">
              Days
            </a>
          </th>

          <th
            <c:if test="${sortField eq 'language'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/listTourAdmin_servlet'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='language'/>
                       <c:choose>
                         <c:when test="${sortField eq 'language' && sortDir eq 'asc'}">
                           <c:param name='sortDir' value='desc'/>
                         </c:when>
                         <c:otherwise>
                           <c:param name='sortDir' value='asc'/>
                         </c:otherwise>
                       </c:choose>
                     </c:url>">
              Language
            </a>
          </th>

          <th
            <c:if test="${sortField eq 'status'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/listTourAdmin_servlet'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='status'/>
                       <c:choose>
                         <c:when test="${sortField eq 'status' && sortDir eq 'asc'}">
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

          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="t" items="${tours}">
          <tr>
            <td><c:out value="${t.id}"/></td>
            <td><c:out value="${t.name}"/></td>
            <td><c:out value="${t.price}"/></td>
            <td><c:out value="${t.maxPeoplePerBooking}"/></td>
            <td><c:out value="${t.days}"/></td>
            <td><c:out value="${t.language}"/></td>
            <td>
  <c:choose>
    <c:when test="${t.status == 'ACTIVE'}">
      Unlocked
    </c:when>
    <c:otherwise>
      Locked
    </c:otherwise>
  </c:choose>
</td>
            <td>
              <form method="post" style="display:inline;" action="${pageContext.request.contextPath}/listTourAdmin_servlet">
                <input type="hidden" name="action" value="view"/>
                <input type="hidden" name="id" value="${t.id}"/>
                <button class="action-btn" type="submit">Detail</button>
              </form>
              <form method="post" style="display:inline;" action="${pageContext.request.contextPath}/listTourAdmin_servlet"
                    onsubmit="return confirm('Are you sure to change status of tour #${t.id}?')">
                <input type="hidden" name="action" value="changeStatus"/>
                <input type="hidden" name="id" value="${t.id}"/>
                <button class="action-btn" type="submit">Change Status</button>
              </form>
            </td>
          </tr>
        </c:forEach>

        <c:if test="${fn:length(tours) == 0}">
          <tr><td colspan="8">No tours found.</td></tr>
        </c:if>
      </tbody>
    </table>

    <div class="pagination" id="pagination"></div>
  </div>

  <script>
    const rowsPerPage = 10;                 
    let currentPage = 1;                   

    const allRows = Array.from(document.querySelectorAll('#tourTable tbody tr'));

    function renderTable() {
      const totalRows = allRows.length;
      const totalPages = Math.ceil(totalRows / rowsPerPage);
      if (currentPage > totalPages) currentPage = totalPages || 1;

      const start = (currentPage - 1) * rowsPerPage;
      const end = start + rowsPerPage;

      const tbody = document.querySelector('#tourTable tbody');
      tbody.innerHTML = '';
      const pageRows = allRows.slice(start, end);
      pageRows.forEach(row => tbody.appendChild(row));

      const paginationDiv = document.getElementById('pagination');
      paginationDiv.innerHTML = '';
      for (let i = 1; i <= totalPages; i++) {
        const span = document.createElement('span');
        span.innerText = i;
        span.className = (i === currentPage ? 'active' : '');
        span.onclick = () => {
          currentPage = i;
          renderTable();
        };
        paginationDiv.appendChild(span);
      }
    }

    renderTable();
  </script>
</body>
</html>
