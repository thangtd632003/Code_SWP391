<%-- 
    Document   : tourList
    Created on : May 24, 2025, 8:30:16 AM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entity.Tour" %>
<%@ page import="java.util.List" %>

<%
    // Danh sách đã được servlet setAttribute("tours", tours);
    List<Tour> tours = (List<Tour>) request.getAttribute("tours");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Tour List</title>
  <style>
    body { font-family: Arial, sans-serif; padding: 20px; background: #f4f4f4; }
    .controls { margin-bottom: 20px; display: flex; justify-content: space-between; }
    .controls input[type=text] { padding: 6px; width: 200px; }
    .controls button { padding: 6px 12px; }
    table { width: 100%; border-collapse: collapse; background: #fff; }
    th, td { padding: 8px; border: 1px solid #ddd; text-align: left; }
    th { cursor: pointer; background: #f0f0f0; }
    th.sort-asc::after  { content: " ▲"; }
    th.sort-desc::after { content: " ▼"; }
    .pagination { margin-top: 10px; text-align: center; }
    .pagination span { margin: 0 5px; cursor: pointer; }
    .pagination .active { font-weight: bold; }
    .action-btn { margin-right: 5px; padding: 4px 8px; font-size: 12px; }

    .top-bar {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      background-color: #f0f0f0;
      padding: 10px 20px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      z-index: 1000;
    }

    .top-bar a {
      text-decoration: none;
      color: #007bff;
      font-weight: bold;
      font-size: 16px;
    }

    .top-bar a:hover {
      color: #0056b3;
    }
  </style>
</head>
<body>
<div class="top-bar">
  <a href="/Code_SWP391/login">← Dashboard</a>
</div>
<div style="margin-top:60px;">
  <div class="controls">
    <div>
      <input type="text" id="searchInput" placeholder="Search by name..." onkeyup="applyFilter()" />
      <button onclick="clearFilter()">Clear</button>
    </div>
    <div>
      <button onclick="location.href='${pageContext.request.contextPath}/createTour_thang'">
        + Add New Tour
      </button>
    </div>
  </div>

  <table id="tourTable">
    <thead>
      <tr>
        <th data-field="id" onclick="sortTable('id')">ID</th>
        <th data-field="name" onclick="sortTable('name')">Name</th>
        <th data-field="price" onclick="sortTable('price')">Price</th>
        <th data-field="maxPeoplePerBooking" onclick="sortTable('maxPeoplePerBooking')">Max People</th>
        <th data-field="days" onclick="sortTable('days')">Days</th>
        <th data-field="language" onclick="sortTable('language')">Language</th>
        <th data-field="status" onclick="sortTable('status')">Status</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="t" items="${tours}">
        <tr>
          <td><c:out value="${t.id}" /></td>
          <td><c:out value="${t.name}" /></td>
          <td><c:out value="${t.price}" /></td>
          <td><c:out value="${t.maxPeoplePerBooking}" /></td>
          <td><c:out value="${t.days}" /></td>
          <td><c:out value="${t.language}" /></td>
          <td><c:out value="${t.status}" /></td>
          <td>
            <button class="action-btn" onclick="location.href='${pageContext.request.contextPath}/editTour_thang?id=${t.id}'">Update</button>
            <button class="action-btn" onclick="confirmUpdateStatus(${t.id})">Update status</button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <div class="pagination" id="pagination"></div>
</div>
  <script>
    const rowsPerPage = 10;
    let currentPage = 1;
    let sortField = null;
    let sortAsc = true;

    // Cache toàn bộ rows gốc
    const originalRows = Array.from(document.querySelectorAll('#tourTable tbody tr'));

    function applyFilter() {
      currentPage = 1;
      renderTable();
    }

    function clearFilter() {
      document.getElementById('searchInput').value = '';
      currentPage = 1;
      sortField = null;
      sortAsc = true;
      updateSortIcons();
      renderTable();
    }

    function sortTable(field) {
      if (sortField === field) sortAsc = !sortAsc;
      else { sortField = field; sortAsc = true; }
      renderTable();
      updateSortIcons();
    }

    function updateSortIcons() {
      document.querySelectorAll('th').forEach(th => {
        th.classList.remove('sort-asc','sort-desc');
        if (th.dataset.field === sortField) {
          th.classList.add(sortAsc ? 'sort-asc' : 'sort-desc');
        }
      });
    }

    function confirmUpdateStatus(id) {
      if (confirm('Are you sure to update status tour #' + id + '?')) {
        const form = document.createElement('form');
        form.method = 'post';
        form.action = 'tourList_thang';
        let inputA = document.createElement('input'); inputA.type = 'hidden'; inputA.name = 'action'; inputA.value = 'delete'; form.appendChild(inputA);
        let inputB = document.createElement('input'); inputB.type = 'hidden'; inputB.name = 'id'; inputB.value = id; form.appendChild(inputB);
        document.body.appendChild(form);
        form.submit();
      }
    }

    function renderTable() {
      let allRows = originalRows.slice();
      const filter = document.getElementById('searchInput').value.toLowerCase();

      // Filter
      if (filter) {
        allRows = allRows.filter(row => row.cells[1].innerText.toLowerCase().includes(filter));
      }

      // Sort
      if (sortField) {
        const idxMap = {id:0, name:1, price:2, maxPeoplePerBooking:3, days:4, language:5, status:6};
        const idx = idxMap[sortField];
        allRows.sort((a,b) => {
          let va = a.cells[idx].innerText, vb = b.cells[idx].innerText;
          if (['id','price','maxPeoplePerBooking','days'].includes(sortField)) {
            va = parseFloat(va); vb = parseFloat(vb);
          }
          return sortAsc ? (va>vb?1:va<vb?-1:0) : (va<vb?1:va>vb?-1:0);
        });
      }

      // Paging
      const total = allRows.length;
      const pages = Math.ceil(total/rowsPerPage);
      if (currentPage > pages) currentPage = pages || 1;
      const start = (currentPage-1)*rowsPerPage;
      const end = start + rowsPerPage;
      const pageRows = allRows.slice(start, end);

      const tbody = document.querySelector('#tourTable tbody');
      tbody.innerHTML = '';
      pageRows.forEach(r => tbody.appendChild(r));

      const pg = document.getElementById('pagination'); pg.innerHTML = '';
      for (let i=1; i<=pages; i++) {
        const span = document.createElement('span'); span.innerText = i;
        span.className = (i===currentPage ? 'active' : '');
        span.onclick = () => { currentPage = i; renderTable(); };
        pg.appendChild(span);
      }
    }

    updateSortIcons();
    renderTable();
  </script>
</body>
</html>
