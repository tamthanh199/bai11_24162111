<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>
            Dashboard
        </title>
    </head>
    <body>
        <h1>
            Trang quản trị - Đề 05
        </h1>
        <div class="dashboard">
            <a class="dashboard-card" href="${pageContext.request.contextPath}/admin/categories">
                <strong>
                    CRUD
                    Category
                </strong>
                <span>
                    Tạo, xem, cập nhật, xóa + phân trang + upload
                    ảnh
                </span>
            </a>
            <a class="dashboard-card" href="${pageContext.request.contextPath}/admin/products">
                <strong>
                    CRUD
                    Product
                </strong>
                <span>
                    Tạo, xem, cập nhật, xóa + phân trang + upload
                    ảnh
                </span>
            </a>
        </div>
    </body>
</html>
