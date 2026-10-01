<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
    <head>
        <title>
            ${product.id > 0 ? 'Sửa Product' : 'Thêm Product'}
        </title>
    </head>
    <body>
        <h1>
            ${product.id > 0 ? 'Xem / Cập nhật Product' : 'Tạo Product'}
        </h1>
        <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
        </c:if>
        <form class="form-grid" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/products/save">
            <input type="hidden" name="id" value="${product.id}">
            <label>
                Tên
                sản phẩm
            </label>
            <input name="name" value="${product.name}" required>
            <label>
                Mã
                sản phẩm
            </label>
            <input type="number" name="code" value="${product.code}" required>
            <label>
                Danh mục
            </label>
            <select name="categoryId" required>
                <c:forEach var="c" items="${categories}">
                    <option value="${c.id}" ${product.category != null and product.category.id == c.id ? 'selected' : ''}>
                        ${c.id}
                        - ${c.name}
                    </option>
                </c:forEach>
            </select>
            <label>
                Seller
            </label>
            <select name="sellerId" required>
                <c:forEach var="s" items="${sellers}">
                    <option value="${s.id}" ${product.seller != null and product.seller.id == s.id ? 'selected' : ''}>
                        ${s.id}
                        - ${s.name}
                    </option>
                </c:forEach>
            </select>
            <label>
                Giá
            </label>
            <input type="number" step="0.01" min="0" name="price" value="${product.price}">
            <label>
                Amount
            </label>
            <input type="number" min="0" name="amount" value="${product.amount}">
            <label>
                Stock
            </label>
            <input type="number" min="0" name="stock" value="${product.stock}">
            <label>
                Wishlist
            </label>
            <input type="number" min="0" name="wishlist" value="${product.wishlist}">
            <label>
                Status
            </label>
            <select name="status">
                <option value="1" ${product.status != 0 ? 'selected' : ''}>
                    1 - Active
                </option>
                <option value="0" ${product.status == 0 ? 'selected' : ''}>
                    0
                    - Inactive
                </option>
            </select>
            <label>
                Description
            </label>
            <textarea name="description" rows="5">${product.description}</textarea>
            <label>
                Ảnh sản phẩm
            </label>
            <input type="file" name="imageFile" accept=".jpg,.jpeg,.png,.gif,.webp">
            <c:if test="${product.id >
                0}">
                <img class="preview" src="${pageContext.request.contextPath}/media?type=product&amp;name=${product.images}">
                <small>
                    Không chọn ảnh mới thì giữ ảnh cũ.
                </small>
            </c:if>
            <div>
                <button type="submit">
                    Lưu
                </button>
                <a class="button secondary" href="${pageContext.request.contextPath}/admin/products">
                    Quay
                    lại
                </a>
            </div>
        </form>
    </body>
</html>
