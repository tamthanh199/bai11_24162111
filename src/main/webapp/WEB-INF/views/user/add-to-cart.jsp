<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:choose>
    <c:when test="${not product.purchasable}">
        <p class="warning">
            Sản phẩm hết hàng hoặc ngừng bán.
        </p>
    </c:when>
    <c:when test="${empty sessionScope.account}">
        <a class="button" href="${pageContext.request.contextPath}/login">
            Đăng nhập để mua hàng
        </a>
    </c:when>
    <c:when test="${sessionScope.account.user}">
        <form class="add-cart-form" action="${pageContext.request.contextPath}/cart/add" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <input type="hidden" name="productId" value="${product.id}">
            <label for="buy-quantity-${product.id}">
                Số lượng
            </label>
            <input id="buy-quantity-${product.id}" type="number" name="quantity" min="1" max="${product.purchaseLimit}" value="1" required>
            <button type="submit">
                Thêm vào giỏ
            </button>
        </form>
        <small class="muted">
            Tối đa ${product.purchaseLimit} sản phẩm mỗi loại.
        </small>
    </c:when>
    <c:otherwise>
        <p class="muted">
            Mua hàng bằng tài khoản USER.
        </p>
    </c:otherwise>
</c:choose>
