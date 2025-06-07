<%-- 
    Document   : userList
    Created on : Jun 5, 2025, 10:09:52 AM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Manage User</title>
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
    .action-btn { margin-right: 5px; padding: 4px 8px; font-size: 12px; }
    .message { margin-bottom: 12px; padding: 8px; }
    .success { background: #d4edda; color: #155724; }
    .fail    { background: #f8d7da; color: #721c24; }
    .top-bar {
      position: fixed; top: 0; left: 0; width: 100%;
      background-color: #f0f0f0; padding: 10px 20px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1); z-index: 1000;
    }
    .top-bar a {
      text-decoration: none; color: #007bff; font-weight: bold; font-size: 16px;
    }
    .top-bar a:hover { color: #0056b3; }
    /* Style cho phân trang */
    #pagination { margin-top: 20px; text-align: center; }
    #pagination button {
      margin: 0 3px;
      padding: 5px 10px;
      border: 1px solid #007bff;
      background: #fff;
      color: #007bff;
      cursor: pointer;
    }
    #pagination button.active {
      background: #007bff;
      color: #fff;
    }
  </style>
</head>
<body>

  <div class="top-bar">
    <a href="${pageContext.request.contextPath}/dashboard">← Dashboard</a>
  </div>

  <div style="margin-top: 60px;">
    <h2>List User</h2>

    <!-- Thông báo kết quả nếu có -->
    <c:if test="${param.msg == 'statusOk'}">
      <div class="message success">Change status succes!</div>
    </c:if>
    <c:if test="${param.msg == 'statusFail'}">
      <div class="message fail">Change status fail!</div>
    </c:if>

    <!-- Controls: Search + nút Add -->
    <div class="controls">
      <form method="get" action="${pageContext.request.contextPath}/userList_thang">
        <input type="text"
               name="keyword"
               placeholder="Search name or email..."
               value="${fn:escapeXml(keyword)}" />
        <input type="hidden" name="sortField" value="${sortField}" />
        <input type="hidden" name="sortDir"   value="${sortDir}" />
        <button type="submit">Search</button>
      </form>

      <!-- Nếu có trang tạo user mới, bạn có thể thêm button ở đây -->
      <!--
      <a href="${pageContext.request.contextPath}/createUser_thang">
        <button type="button">Add New User</button>
      </a>
      -->
    </div>

    <!-- Bảng User -->
    <table id="userTable">
      <thead>
        <tr>
          <!-- Cột ID -->
          <th
            <c:if test="${sortField eq 'id'}">
              class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
            </c:if>
          >
            <a href="<c:url value='/userList_thang'>
                       <c:param name='keyword'   value='${keyword}'/>
                       <c:param name='sortField' value='id'/>
                       <c:choose>
                         <c:when test='${sortField eq "id" && sortDir eq "asc"}'>
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

          <!-- Cột Full Name -->
          <th
            <c:if test='${sortField eq "full_name"}'>
              class='${sortDir eq "asc" ? "sorted-asc" : "sorted-desc"}'
            </c:if>
          >
            <a href='<c:url value="/userList_thang">
                       <c:param name="keyword"   value="${keyword}"/>
                       <c:param name="sortField" value="full_name"/>
                       <c:choose>
                         <c:when test="${sortField eq 'full_name' && sortDir eq 'asc'}">
                           <c:param name="sortDir" value="desc"/>
                         </c:when>
                         <c:otherwise>
                           <c:param name="sortDir" value="asc"/>
                         </c:otherwise>
                       </c:choose>
                     </c:url>'>
              Full Name
            </a>
          </th>

          <!-- Cột Email -->
          <th
            <c:if test='${sortField eq "email"}'>
              class='${sortDir eq "asc" ? "sorted-asc" : "sorted-desc"}'
            </c:if>
          >
            <a href='<c:url value="/userList_thang">
                       <c:param name="keyword"   value="${keyword}"/>
                       <c:param name="sortField" value="email"/>
                       <c:choose>
                         <c:when test="${sortField eq 'email' && sortDir eq 'asc'}">
                           <c:param name="sortDir" value="desc"/>
                         </c:when>
                         <c:otherwise>
                           <c:param name="sortDir" value="asc"/>
                         </c:otherwise>
                       </c:choose>
                     </c:url>'>
              Email
            </a>
          </th>

          <th>Phone</th>

          <!-- Cột Status -->
          <th
            <c:if test='${sortField eq "status"}'>
              class='${sortDir eq "asc" ? "sorted-asc" : "sorted-desc"}'
            </c:if>
          >
            <a href='<c:url value="/userList_thang">
                       <c:param name="keyword"   value="${keyword}"/>
                       <c:param name="sortField" value="status"/>
                       <c:choose>
                         <c:when test="${sortField eq 'status' && sortDir eq 'asc'}">
                           <c:param name="sortDir" value="desc"/>
                         </c:when>
                         <c:otherwise>
                           <c:param name="sortDir" value="asc"/>
                         </c:otherwise>
                       </c:choose>
                     </c:url>'>
              Status
            </a>
          </th>

          <!-- Cột Updated At -->
          <th
            <c:if test='${sortField eq "updated_at"}'>
              class='${sortDir eq "asc" ? "sorted-asc" : "sorted-desc"}'
            </c:if>
          >
            <a href='<c:url value="/userList_thang">
                       <c:param name="keyword"   value="${keyword}"/>
                       <c:param name="sortField" value="updated_at"/>
                       <c:choose>
                         <c:when test="${sortField eq 'updated_at' && sortDir eq 'asc'}">
                           <c:param name="sortDir" value="desc"/>
                         </c:when>
                         <c:otherwise>
                           <c:param name="sortDir" value="asc"/>
                         </c:otherwise>
                       </c:choose>
                     </c:url>'>
              Updated At
            </a>
          </th>

          <th>Actions</th>
        </tr>
      </thead>

      <tbody>
        <c:forEach var="u" items="${users}">
          <tr>
            <td><c:out value="${u.id}"/></td>
            <td><c:out value="${u.fullName}"/></td>
            <td><c:out value="${u.email}"/></td>
            <td><c:out value="${u.phone}"/></td>
            <td><c:out value="${u.status}"/></td>
            <td><c:out value="${u.updatedAt}"/></td>
            <td>
              <!-- Nút Detail -->
              <form method="post"
                    action="${pageContext.request.contextPath}/userList_thang"
                    style="display:inline;">
                <input type="hidden" name="action" value="detail"/>
                <input type="hidden" name="id"     value="${u.id}"/>
                <button type="submit" class="action-btn">Detail</button>
              </form>

              <!-- Nút Change Status (chỉ hiển thị nếu currentRole == "ADMIN") -->
              <c:if test="${currentRole == 'ADMIN' && u.role != 'ADMIN'}">
  <form method="post"
        action="${pageContext.request.contextPath}/userList_thang"
        style="display:inline;">
    <input type="hidden" name="action" value="changeStatus"/>
    <input type="hidden" name="id"     value="${u.id}"/>
    <input type="hidden" name="newStatus"
          value="${u.status == 'ACTIVE' ? 'LOCKED' : 'ACTIVE'}"/>
    <button type="submit" class="action-btn">
      <c:out value="${u.status == 'ACTIVE' ? 'Lock' : 'Activate'}"/>
    </button>
  </form>
</c:if>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>

    <c:if test="${fn:length(users) == 0}">
      <p>Not Founds.</p>
    </c:if>

    <!-- Phân trang sẽ được chèn tại đây -->
    <div id="pagination"></div>
  </div>

  <!-- JavaScript phân trang client-side -->
  <script>
    (function() {
      const rowsPerPage = 10;
      const table = document.getElementById("userTable");
      const tbody = table.querySelector("tbody");
      const rows = Array.from(tbody.querySelectorAll("tr"));
      const pagination = document.getElementById("pagination");

      if (rows.length === 0) {
        return; // Nếu không có dòng, không cần phân trang
      }

      const totalPages = Math.ceil(rows.length / rowsPerPage);

      function showPage(page) {
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        rows.forEach((row, index) => {
          row.style.display = (index >= start && index < end) ? "" : "none";
        });

        // Highlight nút hiện tại
        const btns = pagination.querySelectorAll("button");
        btns.forEach(btn => {
          btn.classList.toggle("active", Number(btn.textContent) === page);
        });
      }

      function setupPagination() {
        for (let i = 1; i <= totalPages; i++) {
          const btn = document.createElement("button");
          btn.textContent = i;
          btn.addEventListener("click", function() {
            showPage(i);
          });
          pagination.appendChild(btn);
        }
      }

      setupPagination();
      showPage(1); // Mặc định hiển thị trang 1
    })();
  </script>
</body>
</html>
