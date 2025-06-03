<%-- 
    Document   : listBookingTraveler_thang
    Created on : Jun 3, 2025, 8:13:25 PM
    Author     : thang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entity.Booking" %>
<%@ page import="java.util.List" %>
<%
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Guide - Booking List</title>
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
            position: fixed; top: 0; left: 0; width: 100%;
            background-color: #f0f0f0; padding: 10px 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1); z-index: 1000;
        }
        .top-bar a {
            text-decoration: none; color: #007bff;
            font-weight: bold; font-size: 16px;
        }
        .top-bar a:hover { color: #0056b3; }
    </style>
</head>
<body>
<div class="top-bar">
    <a href="${pageContext.request.contextPath}/dashboard">← Back to Dashboard</a>
</div>

<div style="margin-top:60px;">
    <div class="controls">
        <div>
            <input type="text" id="searchInput" placeholder="Search by contact..." onkeyup="applyFilter()" />
            <button onclick="clearFilter()">Clear</button>
        </div>
    </div>

    <table id="bookingTable">
        <thead>
        <tr>
            <th data-field="id" onclick="sortTable('id')">ID</th>
            <th data-field="tourId" onclick="sortTable('tourId')">Tour ID</th>
            <th data-field="travelerId" onclick="sortTable('travelerId')">Traveler ID</th>
            <th data-field="numPeople" onclick="sortTable('numPeople')">People</th>
            <th data-field="contact" onclick="sortTable('contact')">Contact</th>
            <th data-field="status" onclick="sortTable('status')">Status</th>
            <th data-field="departure" onclick="sortTable('departure')">Departure</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="b" items="${bookings}">
            <tr>
                <td>${b.id}</td>
                <td>${b.tourId}</td>
                <td>${b.travelerId}</td>
                <td>${b.numPeople}</td>
                <td>${b.contactInfo}</td>
                <td>${b.status}</td>
                <td>${b.departureDate}</td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/listBookingGuide_thang">
                        <input type="hidden" name="action" value="detail"/>
                        <input type="hidden" name="id" value="${b.id}"/>
                        <button class="action-btn" type="submit">Detail</button>
                    </form>
             
  <c:choose>
    <c:when test="${booking.status eq 'PENDING'}">
      <form method="post" action="${pageContext.request.contextPath}/listBookingTraveler_thang">
        <input type="hidden" name="action" value="changeStatus" />
        <input type="hidden" name="id" value="${booking.id}" />
        <input type="hidden" name="newStatus" value="CANCELLED" />
        <button type="submit" class="action-btn">Cancel</button>
      </form>
    </c:when>
    <c:otherwise>
      <span>-</span>
    </c:otherwise>
  </c:choose>
 <form method="post" action="${pageContext.request.contextPath}/listBookingGuide_thang">
                        <input type="hidden" name="action" value="update"/>
                        <input type="hidden" name="id" value="${b.id}"/>
                        <button class="action-btn" type="submit">Update</button>
                    </form>
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

    const originalRows = Array.from(document.querySelectorAll('#bookingTable tbody tr'));

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

    function renderTable() {
        let allRows = originalRows.slice();
        const filter = document.getElementById('searchInput').value.toLowerCase();

        if (filter) {
            allRows = allRows.filter(row => row.cells[4].innerText.toLowerCase().includes(filter));
        }

        if (sortField) {
            const idxMap = {id:0, tourId:1, travelerId:2, numPeople:3, contact:4, status:5, departure:6};
            const idx = idxMap[sortField];
            allRows.sort((a, b) => {
                let va = a.cells[idx].innerText, vb = b.cells[idx].innerText;
                if (['id', 'tourId', 'travelerId', 'numPeople'].includes(sortField)) {
                    va = parseFloat(va); vb = parseFloat(vb);
                }
                return sortAsc ? (va > vb ? 1 : va < vb ? -1 : 0) : (va < vb ? 1 : va > vb ? -1 : 0);
            });
        }

        const total = allRows.length;
        const pages = Math.ceil(total / rowsPerPage);
        if (currentPage > pages) currentPage = pages || 1;
        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        const pageRows = allRows.slice(start, end);

        const tbody = document.querySelector('#bookingTable tbody');
        tbody.innerHTML = '';
        pageRows.forEach(r => tbody.appendChild(r));

        const pg = document.getElementById('pagination'); pg.innerHTML = '';
        for (let i = 1; i <= pages; i++) {
            const span = document.createElement('span');
            span.innerText = i;
            span.className = (i === currentPage ? 'active' : '');
            span.onclick = () => { currentPage = i; renderTable(); };
            pg.appendChild(span);
        }
    }

    updateSortIcons();
    renderTable();
</script>
</body>
</html>

