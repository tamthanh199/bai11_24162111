<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<header class="site-header">
    <div class="container header-inner">
        <a class="brand" href="${pageContext.request.contextPath}/home">
            DE05
            Shop
        </a>
        <nav>
            <a href="${pageContext.request.contextPath}/home">
                Trang Chủ
            </a>
            <a href="${pageContext.request.contextPath}/products">
                Sản phẩm
            </a>
            <c:if test="${not empty sessionScope.account and sessionScope.account.seller}">
                <a href="${pageContext.request.contextPath}/seller">
                    Trang Seller
                </a>
            </c:if>
            <c:if test="${not empty sessionScope.account and sessionScope.account.admin}">
                <a href="${pageContext.request.contextPath}/admin">
                    Trang quản
                    trị
                </a>
            </c:if>
            <c:if test="${not empty sessionScope.account and sessionScope.account.user}">
                <a href="${pageContext.request.contextPath}/cart">
                    Giỏ hàng
                </a>
                <a href="${pageContext.request.contextPath}/orders">
                    Đơn hàng của tôi
                </a>
            </c:if>
            <c:choose>
                <c:when test="${empty sessionScope.account}">
                    <a href="${pageContext.request.contextPath}/login">
                        Đăng nhập
                    </a>
                    <a href="${pageContext.request.contextPath}/register">
                        Đăng ký
                    </a>
                </c:when>
                <c:otherwise>
                    <span class="hello">
                        Xin chào,
                        <c:out value="${sessionScope.account.displayName}" />
                    </span>
                    <a href="${pageContext.request.contextPath}/logout">
                        Đăng xuất
                    </a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
