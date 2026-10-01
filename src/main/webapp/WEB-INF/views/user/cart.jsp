<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="vi_VN" />
<html>
    <head>
        <title>
            Giỏ hàng
        </title>
    </head>
    <body>
        <div class="title-row">
            <div>
                <h1>
                    Giỏ hàng của bạn
                </h1>
                <p class="muted">
                    Giá và tồn kho được kiểm tra lại khi đặt hàng.
                </p>
            </div>
            <a class="button secondary" href="${pageContext.request.contextPath}/products">
                Tiếp tục mua hàng
            </a>
        </div>
        <%@ include file="flash.jsp" %>
        <c:choose>
            <c:when test="${empty summary.items}">
                <section class="empty-state">
                    <h2>
                        Giỏ hàng đang trống
                    </h2>
                    <p>
                        Chọn sản phẩm để bắt đầu đơn hàng của bạn.
                    </p>
                    <a class="button" href="${pageContext.request.contextPath}/products">
                        Xem sản phẩm
                    </a>
                </section>
            </c:when>
            <c:otherwise>
                <c:if test="${not summary.ready}">
                    <p class="warning" role="alert">
                        Có sản phẩm hết hàng, ngừng bán hoặc vượt tồn kho. Hãy cập nhật số lượng hoặc xóa sản phẩm trước khi thanh toán.
                    </p>
                </c:if>
                <div class="table-scroll">
                    <table>
                        <caption class="sr-only">
                            Các sản phẩm trong giỏ hàng
                        </caption>
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
                                <th>
                                    Thao tác
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${summary.items}">
                                <c:url var="imageUrl" value="/media">
                                    <c:param name="type" value="product" />
                                    <c:param name="name" value="${item.product.images}" />
                                </c:url>
                                <tr>
                                    <td>
                                        <div class="cart-product">
                                            <img class="thumb" src="${fn:escapeXml(imageUrl)}" alt="${fn:escapeXml(item.product.name)}">
                                            <div>
                                                <a href="${pageContext.request.contextPath}/product?id=${item.product.id}">
                                                    <c:out value="${item.product.name}" />
                                                </a>
                                                <small class="muted">
                                                    Mã:
                                                    <c:out value="${item.product.code}" />
                                                    · Tồn kho: ${item.product.stock}
                                                </small>
                                                <c:if test="${not item.product.purchasable}">
                                                    <strong class="danger-link">
                                                        Hết hàng / ngừng bán
                                                    </strong>
                                                </c:if>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="money">
                                        <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.##" />
                                        ₫
                                    </td>
                                    <td>
                                        <form class="quantity-form" action="${pageContext.request.contextPath}/cart/update" method="post">
                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                            <input type="hidden" name="itemId" value="${fn:escapeXml(item.id)}">
                                            <div class="quantity-control">
                                                <button type="button" data-step="-1" aria-label="Giảm số lượng" ${not item.product.purchasable ? 'disabled' : ''}>
                                                    −
                                                </button>
                                                <input aria-label="Số lượng ${fn:escapeXml(item.product.name)}" type="number" name="quantity" min="1" max="${item.product.purchaseLimit}" value="${item.quantity}" required ${not item.product.purchasable ? 'disabled' : ''}>
                                                <button type="button" data-step="1" aria-label="Tăng số lượng" ${not item.product.purchasable ? 'disabled' : ''}>
                                                    +
                                                </button>
                                            </div>
                                            <small class="muted">
                                                Tối đa ${item.product.purchaseLimit} sản phẩm
                                            </small>
                                            <button type="submit" ${not item.product.purchasable ? 'disabled' : ''}>
                                                Cập nhật
                                            </button>
                                        </form>
                                    </td>
                                    <td class="money">
                                        <fmt:formatNumber value="${item.lineTotal}" pattern="#,##0.##" />
                                        ₫
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart/remove" method="post">
                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                            <input type="hidden" name="itemId" value="${fn:escapeXml(item.id)}">
                                            <button class="danger" type="submit">
                                                Xóa
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <section class="cart-bottom">
                    <form action="${pageContext.request.contextPath}/cart/clear" method="post" data-confirm="Xóa toàn bộ sản phẩm khỏi giỏ hàng?">
                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                        <button class="secondary" type="submit">
                            Xóa toàn bộ giỏ
                        </button>
                    </form>
                    <div class="order-summary">
                        <p>
                            ${summary.itemCount} sản phẩm · Phí giao hàng: 0 ₫
                        </p>
                        <h2>
                            Tổng:
                            <fmt:formatNumber value="${summary.total}" pattern="#,##0.##" />
                            ₫
                        </h2>
                        <c:choose>
                            <c:when test="${summary.ready}">
                                <a class="button" href="${pageContext.request.contextPath}/checkout">
                                    Thanh toán COD
                                </a>
                            </c:when>
                            <c:otherwise>
                                <button type="button" disabled>
                                    Thanh toán COD
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </section>
            </c:otherwise>
        </c:choose>
    </body>
</html>
