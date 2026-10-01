<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
    <head>
        <title>
            ${category.id > 0 ? 'Sửa Category' : 'Thêm Category'}
        </title>
    </head>
    <body>
        <h1>
            ${category.id > 0 ? 'Xem / Cập nhật Category' : 'Tạo Category'}
        </h1>
        <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
        </c:if>
        <form class="form-grid" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/categories/save">
            <input type="hidden" name="id" value="${category.id}">
            <label>
                Tên
                danh mục
            </label>
            <input name="name" value="${category.name}" required>
            <label>
                Status
            </label>
            <select name="status">
                <option value="1" ${category.status != 0 ? 'selected' : ''}>
                    1 - Active
                </option>
                <option value="0" ${category.status == 0 ? 'selected' : ''}>
                    0
                    - Inactive
                </option>
            </select>
            <label>
                Ảnh
            </label>
            <input type="file" name="imageFile" accept=".jpg,.jpeg,.png,.gif,.webp">
            <c:if test="${category.id >
                0}">
                <img class="preview" src="${pageContext.request.contextPath}/media?type=category&amp;name=${category.images}">
                <small>
                    Không chọn ảnh mới thì giữ ảnh cũ.
                </small>
            </c:if>
            <div>
                <button type="submit">
                    Lưu
                </button>
                <a class="button secondary" href="${pageContext.request.contextPath}/admin/categories">
                    Quay
                    lại
                </a>
            </div>
        </form>
    </body>
</html>
