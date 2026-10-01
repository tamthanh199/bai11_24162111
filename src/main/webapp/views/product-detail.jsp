<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="vi_VN" />
<html>
    <head>
        <title>
            <c:out value="${product.name}" />
        </title>
    </head>
    <body>
        <h1>
            Chi tiết sản phẩm
        </h1>
        <c:url var="imageUrl" value="/media">
            <c:param name="type" value="product" />
            <c:param name="name" value="${product.images}" />
        </c:url>
        <section class="detail-card">
            <img class="detail-image" src="${fn:escapeXml(imageUrl)}" alt="${fn:escapeXml(product.name)}">
            <div>
                <h2>
                    <c:out value="${product.name}" />
                </h2>
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
                <p class="preserve-lines">
                    <b>
                        Mô tả:
                    </b>
                    <c:out value="${product.description}" />
                </p>
                <%@ include file="/WEB-INF/views/user/add-to-cart.jsp" %>
            </div>
        </section>
    </body>
</html>
