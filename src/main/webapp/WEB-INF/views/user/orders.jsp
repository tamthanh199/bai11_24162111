<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="vi_VN" />
<html>
    <head>
        <title>
            Lịch sử đặt hàng
        </title>
    </head>
    <body>
        <div class="title-row">
            <div>
                <h1>
                    Lịch sử đặt hàng
                </h1>
                <p class="muted">
                    Theo dõi trạng thái các đơn hàng của bạn.
                </p>
            </div>
            <a class="button" href="${pageContext.request.contextPath}/products">
                Mua thêm sản phẩm
            </a>
        </div>
        <nav class="status-filters" aria-label="Lọc trạng thái đơn hàng">
            <a class="${empty selectedStatus ? 'active' : ''}" href="${pageContext.request.contextPath}/orders">
                Tất cả
            </a>
            <c:forEach var="status" items="${statuses}">
                <a class="${selectedStatus == status.code ? 'active' : ''}" href="${pageContext.request.contextPath}/orders?status=${status.code}">
                    <c:out value="${status.label}" />
                    (${counts[status.code]})
                </a>
            </c:forEach>
        </nav>
        <p class="muted">
            ${total} đơn hàng phù hợp · Trang ${page}/${totalPages}
        </p>
        <c:choose>
            <c:when test="${empty orders}">
                <section class="empty-state">
                    <h2>
                        Chưa có đơn hàng trong bộ lọc này
                    </h2>
                    <p>
                        Chọn trạng thái khác hoặc tiếp tục mua sắm.
                    </p>
                </section>
            </c:when>
            <c:otherwise>
                <div class="order-list">
                    <c:forEach var="order" items="${orders}">
                        <article class="panel order-card">
                            <div>
                                <h2 class="order-id">
                                    <c:out value="${order.id}" />
                                </h2>
                                <p>
                                    Ngày đặt:
                                    <c:out value="${order.formattedBuyDate}" />
                                </p>
                                <p>
                                    Người nhận:
                                    <c:out value="${order.recipientName}" />
                                </p>
                                <p>
                                    COD · Tổng tiền:
                                    <strong>
                                        <fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.##" />
                                        ₫
                                    </strong>
                                </p>
                            </div>
                            <div class="order-actions">
                                <span class="status-badge status-${order.status}">
                                    <c:out value="${order.statusLabel}" />
                                </span>
                                <a class="button" href="${pageContext.request.contextPath}/orders/detail?id=${fn:escapeXml(order.id)}">
                                    Xem chi tiết
                                </a>
                            </div>
                        </article>
                    </c:forEach>
                </div>
                <nav class="pagination" aria-label="Phân trang lịch sử">
                    <c:forEach var="number" begin="1" end="${totalPages}">
                        <c:url var="pageUrl" value="/orders">
                            <c:param name="page" value="${number}" />
                            <c:if test="${not empty selectedStatus}">
                                <c:param name="status" value="${selectedStatus}" />
                            </c:if>
                        </c:url>
                        <a class="${number == page ? 'active' : ''}" href="${fn:escapeXml(pageUrl)}">
                            ${number}
                        </a>
                    </c:forEach>
                </nav>
            </c:otherwise>
        </c:choose>
    </body>
</html>
