<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="vi_VN" />
<html>
    <head>
        <title>
            Trang chủ
        </title>
    </head>
    <body>
        <h1>
            Sản phẩm theo từng Seller
        </h1>
        <div id="products">
            <c:forEach var="section" items="${sections}">
                <section class="seller-section">
                    <h2>
                        Mã cửa hàng: ${section.seller.id} —
                        <c:out value="${section.seller.name}" />
                    </h2>
                    <c:choose>
                        <c:when test="${empty section.products}">
                            <p>
                                Seller này chưa có sản phẩm.
                            </p>
                        </c:when>
                        <c:otherwise>
                            <div class="product-grid">
                                <c:forEach var="product" items="${section.products}">
                                    <c:url var="imageUrl" value="/media">
                                        <c:param name="type" value="product" />
                                        <c:param name="name" value="${product.images}" />
                                    </c:url>
                                    <article class="product-card">
                                        <img src="${fn:escapeXml(imageUrl)}" alt="${fn:escapeXml(product.name)}" loading="lazy">
                                        <div class="card-body">
                                            <h3>
                                                <a href="${pageContext.request.contextPath}/product?id=${product.id}">
                                                    <c:out value="${product.name}" />
                                                </a>
                                            </h3>
                                            <p>
                                                <b>
                                                    Mã sản phẩm:
                                                </b>
                                                <c:out value="${product.code}" />
                                            </p>
                                            <p>
                                                <b>
                                                    Danh mục:
                                                </b>
                                                <c:out value="${product.category.name}" />
                                            </p>
                                            <p>
                                                <b>
                                                    Giá:
                                                </b>
                                                <fmt:formatNumber value="${product.price}" pattern="#,##0.##" />
                                                ₫
                                            </p>
                                            <p>
                                                <b>
                                                    Tồn kho:
                                                </b>
                                                ${product.stock}
                                            </p>
                                            <%@ include file="/WEB-INF/views/user/add-to-cart.jsp" %>
                                        </div>
                                    </article>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </section>
            </c:forEach>
        </div>
    </body>
</html>
