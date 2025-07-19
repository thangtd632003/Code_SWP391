<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.Date" %>
<%
long nowTime = System.currentTimeMillis();
request.setAttribute("nowTime", nowTime);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Booking List</title>
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
        <a href="${pageContext.request.contextPath}/dashboard">← Back to Dashboard</a>
    </div>

    <div style="margin-top:60px;">
        <div class="controls">
            <form method="get" action="${pageContext.request.contextPath}/ListBookingTraveler_servlet">
                <input type="text"
                       name="keyword"
                       placeholder="Search by contact..."
                       value="${fn:escapeXml(keyword)}" />
                <button type="submit" class="search-btn">Search</button>
                <!-- Khi tìm kiếm hoặc sort, luôn gửi kèm sortField, sortDir, page=1 -->
                <input type="hidden" name="sortField" value="${sortField}" />
                <input type="hidden" name="sortDir" value="${sortDir}" />
                <input type="hidden" name="page" value="1" />
            </form>
        </div>

        <table id="bookingTable">
            <thead>
                <tr>
                    <th
                      <c:if test="${sortField eq 'id'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_servlet'>
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
                      <c:if test="${sortField eq 'tour_id'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_servlet'>
                                 <c:param name='keyword'   value='${keyword}'/>
                                 <c:param name='sortField' value='tour_id'/>
                                 <c:choose>
                                     <c:when test="${sortField eq 'tour_id' && sortDir eq 'asc'}">
                                        <c:param name='sortDir' value='desc'/>
                                     </c:when>
                                     <c:otherwise>
                                        <c:param name='sortDir' value='asc'/>
                                     </c:otherwise>
                                 </c:choose>
                                 <c:param name='page' value='1'/>
                              </c:url>">
                        Tour ID
                      </a>
                    </th>

                    <th
                      <c:if test="${sortField eq 'traveler_id'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_servlet'>
                                 <c:param name='keyword'   value='${keyword}'/>
                                 <c:param name='sortField' value='traveler_id'/>
                                 <c:choose>
                                     <c:when test="${sortField eq 'traveler_id' && sortDir eq 'asc'}">
                                        <c:param name='sortDir' value='desc'/>
                                     </c:when>
                                     <c:otherwise>
                                        <c:param name='sortDir' value='asc'/>
                                     </c:otherwise>
                                 </c:choose>
                                 <c:param name='page' value='1'/>
                              </c:url>">
                        Traveler ID
                      </a>
                    </th>

                    <!-- Cột People -->
                    <th
                      <c:if test="${sortField eq 'num_people'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_servlet'>
                                 <c:param name='keyword'   value='${keyword}'/>
                                 <c:param name='sortField' value='num_people'/>
                                 <c:choose>
                                     <c:when test="${sortField eq 'num_people' && sortDir eq 'asc'}">
                                        <c:param name='sortDir' value='desc'/>
                                     </c:when>
                                     <c:otherwise>
                                        <c:param name='sortDir' value='asc'/>
                                     </c:otherwise>
                                 </c:choose>
                                 <c:param name='page' value='1'/>
                              </c:url>">
                        People
                      </a>
                    </th>

                    <th
                      <c:if test="${sortField eq 'contact_info'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_servlet'>
                                 <c:param name='keyword'   value='${keyword}'/>
                                 <c:param name='sortField' value='contact_info'/>
                                 <c:choose>
                                     <c:when test="${sortField eq 'contact_info' && sortDir eq 'asc'}">
                                        <c:param name='sortDir' value='desc'/>
                                     </c:when>
                                     <c:otherwise>
                                        <c:param name='sortDir' value='asc'/>
                                     </c:otherwise>
                                 </c:choose>
                                 <c:param name='page' value='1'/>
                              </c:url>">
                        Contact
                      </a>
                    </th>

                    <th
                      <c:if test="${sortField eq 'status'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_servlet'>
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
                                 <c:param name='page' value='1'/>
                              </c:url>">
                        Status
                      </a>
                    </th>

                    <th
                      <c:if test="${sortField eq 'departure_date'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_servlet'>
                                 <c:param name='keyword'   value='${keyword}'/>
                                 <c:param name='sortField' value='departure_date'/>
                                 <c:choose>
                                     <c:when test="${sortField eq 'departure_date' && sortDir eq 'asc'}">
                                        <c:param name='sortDir' value='desc'/>
                                     </c:when>
                                     <c:otherwise>
                                        <c:param name='sortDir' value='asc'/>
                                     </c:otherwise>
                                 </c:choose>
                                 <c:param name='page' value='1'/>
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
                        <td><c:out value="${b.id}" /></td>
                        <td><c:out value="${b.tourId}" /></td>
                        <td><c:out value="${b.travelerId}" /></td>
                        <td><c:out value="${b.numPeople}" /></td>
                        <td><c:out value="${b.contactInfo}" /></td>
                        <td><c:out value="${b.status}" /></td>
                        <td><c:out value="${b.departureDate}" /></td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/ListBookingTraveler_servlet">
                                <input type="hidden" name="action" value="detail"/>
                                <input type="hidden" name="id" value="${b.id}"/>
                                <button class="action-btn" type="submit">Detail</button>
                            </form>

                            <c:choose>
                                <c:when test="${b.status.name() == 'PENDING'}">
                                    <form method="post" action="${pageContext.request.contextPath}/ListBookingTraveler_servlet">
                                        <input type="hidden" name="action" value="changeStatus"/>
                                        <input type="hidden" name="id" value="${b.id}"/>
                                        <input type="hidden" name="newStatus" value="CANCELLED"/>
                                        <button type="submit" class="action-btn">Cancel</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <span></span>
                                </c:otherwise>
                            </c:choose>

                            <c:choose>
                                <c:when test="${b.status.name() == 'PENDING'}">
                                    <form method="post" action="${pageContext.request.contextPath}/ListBookingTraveler_servlet">
                                        <input type="hidden" name="action" value="update"/>
                                        <input type="hidden" name="id" value="${b.id}"/>
                                        <button class="action-btn" type="submit">Update</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <span></span>
                                </c:otherwise>
                            </c:choose>
                                    <c:if test="${b.status.name() == 'APPROVED' 
             and b.departureDate.time lt nowTime}">
  <form method="get" action="${pageContext.request.contextPath}/ReviewBookingTraveler_servlet"
        style="display:inline;">
    <input type="hidden" name="bookingId" value="${b.id}"/>
    <button type="submit" class="action-btn">Review</button>
  </form>
</c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <c:if test="${fn:length(bookings) == 0}">
            <p>No bookings found.</p>
        </c:if>

        <!-- PHẦN PHÂN TRANG CLIENT -->
        <div class="pagination" id="pagination"></div>
    </div>

    <script>
        const rowsPerPage = 10;                 
        let currentPage = 1;                 

        const allRows = Array.from(document.querySelectorAll('#bookingTable tbody tr'));

        function renderTable() {
            const totalRows = allRows.length;
            const totalPages = Math.ceil(totalRows / rowsPerPage);
            if (currentPage > totalPages) currentPage = totalPages || 1;

            const start = (currentPage - 1) * rowsPerPage;
            const end = start + rowsPerPage;

            const tbody = document.querySelector('#bookingTable tbody');
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
