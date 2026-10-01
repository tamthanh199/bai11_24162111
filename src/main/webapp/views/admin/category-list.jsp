<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
    <head>
        <title>
            CRUD Category
        </title>
    </head>
    <body>
        <div class="title-row">
            <h1>
                Quản lý Category
            </h1>
            <a class="button" href="${pageContext.request.contextPath}/admin/categories/form">
                +
                Thêm Category
            </a>
        </div>
        <c:if test="${param.error == 'used'}">
            <div class="error">
                Không thể xóa Category đang được Product sử
                dụng.
            </div>
        </c:if>
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
                        Status
                    </th>
                    <th>
                        Thao tác
                    </th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${categories}">
                    <tr>
                        <td>
                            ${c.id}
                        </td>
                        <td>
                            <img class="thumb" src="${pageContext.request.contextPath}/media?type=category&amp;name=${c.images}">
                        </td>
                        <td>
                            ${c.name}
                        </td>
                        <td>
                            ${c.status}
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/categories/form?id=${c.id}">
                                Sửa/Xem
                            </a>
                            |
                            <a class="danger-link" onclick="return confirm('Xóa Category?')" href="${pageContext.request.contextPath}/admin/categories/delete?id=${c.id}">
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
