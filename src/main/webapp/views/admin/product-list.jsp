<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
    <head>
        <title>
            CRUD Product
        </title>
    </head>
    <body>
        <div class="title-row">
            <h1>
                Quản lý Product
            </h1>
            <a class="button" href="${pageContext.request.contextPath}/admin/products/form">
                +
                Thêm Product
            </a>
        </div>
        <table>
            <thead>
                <tr>
                    <th>
                        ID
                    </th>
                    <th>
                        Ảnh
                    </th>
                    <th>
                        Tên
                    </th>
                    <th>
                        Mã
                    </th>
                    <th>
                        Category
                    </th>
                    <th>
                        Seller
                    </th>
                    <th>
                        Giá
                    </th>
                    <th>
                        Amount
                    </th>
                    <th>
                        Thao tác
                    </th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td>
                            ${p.id}
                        </td>
                        <td>
                            <img class="thumb" src="${pageContext.request.contextPath}/media?type=product&amp;name=${p.images}">
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/product?id=${p.id}">
                                ${p.name}
                            </a>
                        </td>
                        <td>
                            ${p.code}
                        </td>
                        <td>
                            ${p.category.name}
                        </td>
                        <td>
                            ${p.seller.id}- ${p.seller.name}
                        </td>
                        <td>
                            ${p.price}
                        </td>
                        <td>
                            ${p.amount}
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/products/form?id=${p.id}">
                                Sửa/Xem
                            </a>
                            |
                            <a class="danger-link" onclick="return confirm('Xóa Product?')" href="${pageContext.request.contextPath}/admin/products/delete?id=${p.id}">
                                Xóa
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="pagination">
            <c:if test="${page >
                1}">
                <a href="?page=${page-1}">
                    «
                </a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <a class="${i==page?'active':''}" href="?page=${i}">
                    ${i}
                </a>
            </c:forEach>
            <c:if test="${page < totalPages}">
                <a href="?page=${page+1}">
                    »
                </a>
            </c:if>
        </div>
    </body>
</html>
