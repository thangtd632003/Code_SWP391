<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="entity.Booking" %>
<%@ page import="entity.BookingStatus" %>
<%@ page import="java.util.List" %>

<%
    // Các attribute được thiết lập bởi servlet:
    //   "bookings"  : List<Booking> (đã được search và sort ở servlet)
    //   "keyword"   : String (tìm theo contact)
    //   "sortField" : String (tên trường sort)
    //   "sortDir"   : String ("asc" hoặc "desc")
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
  
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
        <!-- Form tìm kiếm theo contact -->
        <div class="controls">
            <form method="get" action="${pageContext.request.contextPath}/ListBookingTraveler_thang">
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

        <!-- Bảng hiển thị các booking đã được search/sort ở servlet -->
        <table id="bookingTable">
            <thead>
                <tr>
                    <!-- Cột ID -->
                    <th
                      <c:if test="${sortField eq 'id'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_thang'>
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

                    <!-- Cột Tour ID -->
                    <th
                      <c:if test="${sortField eq 'tour_id'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_thang'>
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

                    <!-- Cột Traveler ID -->
                    <th
                      <c:if test="${sortField eq 'traveler_id'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_thang'>
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
                      <a href="<c:url value='/ListBookingTraveler_thang'>
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

                    <!-- Cột Contact -->
                    <th
                      <c:if test="${sortField eq 'contact_info'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_thang'>
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

                    <!-- Cột Status -->
                    <th
                      <c:if test="${sortField eq 'status'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_thang'>
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

                    <!-- Cột Departure -->
                    <th
                      <c:if test="${sortField eq 'departure_date'}">
                        class="${sortDir eq 'asc' ? 'sorted-asc' : 'sorted-desc'}"
                      </c:if>
                    >
                      <a href="<c:url value='/ListBookingTraveler_thang'>
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
                            <!-- Nút Detail -->
                            <form method="post" action="${pageContext.request.contextPath}/ListBookingTraveler_thang">
                                <input type="hidden" name="action" value="detail"/>
                                <input type="hidden" name="id" value="${b.id}"/>
                                <button class="action-btn" type="submit">Detail</button>
                            </form>

                            <!-- Nút Cancel nếu status = PENDING -->
                            <c:choose>
                                <c:when test="${b.status.name() == 'PENDING'}">
                                    <form method="post" action="${pageContext.request.contextPath}/ListBookingTraveler_thang">
                                        <input type="hidden" name="action" value="changeStatus"/>
                                        <input type="hidden" name="id" value="${b.id}"/>
                                        <input type="hidden" name="newStatus" value="CANCELLED"/>
                                        <button type="submit" class="action-btn">Cancel</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <span>-</span>
                                </c:otherwise>
                            </c:choose>

                            <!-- Nút Update nếu status = PENDING -->
                            <c:choose>
                                <c:when test="${b.status.name() == 'PENDING'}">
                                    <form method="post" action="${pageContext.request.contextPath}/ListBookingTraveler_thang">
                                        <input type="hidden" name="action" value="update"/>
                                        <input type="hidden" name="id" value="${b.id}"/>
                                        <button class="action-btn" type="submit">Update</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <span>-</span>
                                </c:otherwise>
                            </c:choose>
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
        const rowsPerPage = 10;                 // số dòng hiển thị mỗi trang
        let currentPage = 1;                    // trang hiện tại

        // Lấy mảng tất cả row (tr) trong tbody
        const allRows = Array.from(document.querySelectorAll('#bookingTable tbody tr'));

        function renderTable() {
            // Tính tổng pages
            const totalRows = allRows.length;
            const totalPages = Math.ceil(totalRows / rowsPerPage);
            if (currentPage > totalPages) currentPage = totalPages || 1;

            // Tính index đầu và cuối cho trang hiện tại
            const start = (currentPage - 1) * rowsPerPage;
            const end = start + rowsPerPage;

            // Xóa hết tbody, rồi thêm lại hàng thuộc trang hiện tại
            const tbody = document.querySelector('#bookingTable tbody');
            tbody.innerHTML = '';
            const pageRows = allRows.slice(start, end);
            pageRows.forEach(row => tbody.appendChild(row));

            // Xây phần pagination
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

        // Khi load trang lần đầu, in bảng và pagination
        renderTable();
    </script>
</body>
</html>
