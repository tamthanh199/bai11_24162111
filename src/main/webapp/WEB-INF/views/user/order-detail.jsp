<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="vi_VN" />
<html>
    <head>
        <title>
            Chi tiết đơn hàng
        </title>
    </head>
    <body>
        <div class="title-row">
            <div>
                <h1>
                    Chi tiết đơn hàng
                </h1>
                <p class="order-id">
                    <c:out value="${order.id}" />
                </p>
            </div>
            <a class="button secondary" href="${pageContext.request.contextPath}/orders">
                Lịch sử đặt hàng
            </a>
        </div>
        <%@ include file="flash.jsp" %>
        <section class="panel">
            <div class="title-row no-top-margin">
                <h2>
                    Trạng thái hiện tại
                </h2>
                <span class="status-badge status-${order.status}">
                    <c:out value="${order.statusLabel}" />
                </span>
            </div>
            <c:choose>
                <c:when test="${order.status lt 6}">
                    <ol class="order-progress">
                        <c:forEach var="status" items="${statuses}">
                            <c:if test="${status.code lt 6}">
                                <li class="${status.code le order.status ? 'reached' : ''} ${status.code == order.status ? 'current' : ''}">
                                    <c:out value="${status.label}" />
                                </li>
                            </c:if>
                        </c:forEach>
                    </ol>
                </c:when>
                <c:otherwise>
                    <p class="warning">
                        Đơn hàng hiện ở trạng thái:
                        <c:out value="${order.statusLabel}" />
                        .
                    </p>
                </c:otherwise>
            </c:choose>
        </section>
        <div class="checkout-grid">
            <section class="panel">
                <h2>
                    Thông tin người nhận
                </h2>
                <p>
                    <strong>
                        Họ tên:
                    </strong>
                    <c:out value="${order.recipientName}" />
                </p>
                <p>
                    <strong>
                        Điện thoại:
                    </strong>
                    <c:out value="${order.recipientPhone}" />
                </p>
                <p class="preserve-lines">
                    <strong>
                        Địa chỉ:
                    </strong>
                    <c:out value="${order.shippingAddress}" />
                </p>
                <c:if test="${not empty order.orderNote}">
                    <p class="preserve-lines">
                        <strong>
                            Ghi chú:
                        </strong>
                        <c:out value="${order.orderNote}" />
                    </p>
                </c:if>
            </section>
            <section class="panel">
                <h2>
                    Thông tin thanh toán
                </h2>
                <p>
                    Ngày đặt:
                    <c:out value="${order.formattedBuyDate}" />
                </p>
                <p>
                    Phương thức:
                    <strong>
                        COD — Tiền mặt khi nhận hàng
                    </strong>
                </p>
                <p>
                    Phí giao hàng: 0 ₫
                </p>
                <p class="summary-total">
                    Tổng tiền:
                    <strong>
                        <fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.##" />
                        ₫
                    </strong>
                </p>
            </section>
        </div>
        <section class="panel">
            <h2>
                Sản phẩm đã đặt
            </h2>
            <p class="muted">
                Đơn giá được lưu tại thời điểm đặt hàng.
            </p>
            <div class="table-scroll">
                <table>
                    <thead>
                        <tr>
                            <th>
                                Sản phẩm
                            </th>
                            <th>
                                Đơn giá
                            </th>
                            <th>
                                Số lượng
                            </th>
                            <th>
                                Thành tiền
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${items}">
                            <c:url var="imageUrl" value="/media">
                                <c:param name="type" value="product" />
                                <c:param name="name" value="${item.productImage}" />
                            </c:url>
                            <tr>
                                <td>
                                    <div class="cart-product">
                                        <img class="thumb" src="${fn:escapeXml(imageUrl)}" alt="${fn:escapeXml(item.productName)}">
                                        <div>
                                            <c:out value="${item.productName}" />
                                            <small class="muted">
                                                Mã:
                                                <c:out value="${item.productCode}" />
                                            </small>
                                        </div>
                                    </div>
                                </td>
                                <td class="money">
                                    <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.##" />
                                    ₫
                                </td>
                                <td>
                                    ${item.quantity}
                                </td>
                                <td class="money">
                                    <fmt:formatNumber value="${item.lineTotal}" pattern="#,##0.##" />
                                    ₫
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>
    </body>
</html>
