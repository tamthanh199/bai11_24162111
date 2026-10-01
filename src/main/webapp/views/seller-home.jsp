<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
    <head>
        <title>
            Trang Seller
        </title>
    </head>
    <body>
        <h1>
            Trang chủ Seller
        </h1>
        <p>
            Seller:
            <strong>
                ${sellerInfo.id} - ${sellerInfo.name}
            </strong>
        </p>
        <div class="product-grid">
            <c:forEach var="product" items="${products}">
                <article class="product-card">
                    <img src="${pageContext.request.contextPath}/media?type=product&amp;name=${product.images}">
                    <div class="card-body">
                        <h3>
                            <a href="${pageContext.request.contextPath}/product?id=${product.id}">
                                ${product.name}
                            </a>
                        </h3>
                        <p>
                            Mã: ${product.code}
                        </p>
                        <p>
                            Giá: ${product.price}
                        </p>
                        <p>
                            Amount: ${product.amount}
                        </p>
                    </div>
                </article>
            </c:forEach>
        </div>
    </body>
</html>
