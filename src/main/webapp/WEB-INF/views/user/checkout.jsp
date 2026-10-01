<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="vi_VN" />
<html>
    <head>
        <title>
            Đặt hàng COD
        </title>
    </head>
    <body>
        <div class="title-row">
            <div>
                <h1>
                    Đặt hàng COD
                </h1>
                <p class="muted">
                    Kiểm tra đơn hàng và thông tin người nhận trước khi xác nhận.
                </p>
            </div>
            <a class="button secondary" href="${pageContext.request.contextPath}/cart">
                Quay lại giỏ hàng
            </a>
        </div>
        <c:if test="${not empty error}">
            <p class="error" role="alert">
                <c:out value="${error}" />
            </p>
        </c:if>
        <c:if test="${not summary.ready}">
            <p class="warning" role="alert">
                Giỏ hàng có sản phẩm không đủ tồn kho hoặc ngừng bán. Vui lòng cập nhật giỏ hàng.
            </p>
        </c:if>
        <div class="checkout-grid">
            <section class="panel">
                <h2>
                    Thông tin giao hàng
                </h2>
                <form class="form-grid" action="${pageContext.request.contextPath}/checkout" method="post" data-checkout>
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                    <input type="hidden" name="cartId" value="${fn:escapeXml(summary.cart.id)}">
                    <input type="hidden" name="fingerprint" value="${summary.fingerprint}">
                    <label for="recipientName">
                        Họ tên người nhận *
                    </label>
                    <input id="recipientName" name="recipientName" autocomplete="name" value="${fn:escapeXml(shipping.name)}" minlength="2" maxlength="100" required>
                    <label for="recipientPhone">
                        Số điện thoại *
                    </label>
                    <input id="recipientPhone" name="recipientPhone" type="tel" autocomplete="tel" value="${fn:escapeXml(shipping.phone)}" pattern="0[0-9]{9}|[+]84[0-9]{9}" maxlength="20" title="10 số bắt đầu bằng 0, hoặc +84 và 9 số" required>
                    <label for="shippingAddress">
                        Địa chỉ giao hàng *
                    </label>
                    <textarea id="shippingAddress" name="shippingAddress" autocomplete="street-address" rows="3" minlength="10" maxlength="500" required><c:out value="${shipping.address}" /></textarea>
                    <small class="muted">
                        Ghi rõ số nhà, đường, phường/xã và tỉnh/thành phố.
                    </small>
                    <label for="orderNote">
                        Ghi chú (tối đa 500 ký tự)
                    </label>
                    <textarea id="orderNote" name="orderNote" rows="3" maxlength="500"><c:out value="${shipping.note}" /></textarea>
                    <div class="payment-box">
                        <strong>
                            COD — Thanh toán khi nhận hàng
                        </strong>
                        <p>
                            Bạn thanh toán tiền mặt cho đơn vị giao hàng. Phí giao hàng: 0 ₫.
                        </p>
                    </div>
                    <button type="submit" ${not summary.ready ? 'disabled' : ''}>
                        Xác nhận đặt hàng
                    </button>
                    <p class="muted">
                        Tồn kho chỉ được trừ khi đơn hàng được tạo thành công.
                    </p>
                </form>
            </section>
            <aside class="panel">
                <h2>
                    Đơn hàng của bạn
                </h2>
                <ul class="checkout-items">
                    <c:forEach var="item" items="${summary.items}">
                        <li>
                            <div>
                                <strong>
                                    <c:out value="${item.product.name}" />
                                </strong>
                                <small>
                                    ${item.quantity} ×
                                    <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.##" />
                                    ₫
                                </small>
                            </div>
                            <span class="money">
                                <fmt:formatNumber value="${item.lineTotal}" pattern="#,##0.##" />
                                ₫
                            </span>
                        </li>
                    </c:forEach>
                </ul>
                <p class="summary-total">
                    Tổng thanh toán:
                    <strong>
                        <fmt:formatNumber value="${summary.total}" pattern="#,##0.##" />
                        ₫
                    </strong>
                </p>
            </aside>
        </div>
    </body>
</html>
