<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
    <head>
        <title>
            Đăng nhập
        </title>
    </head>
    <body>
        <section class="auth-card">
            <h1>
                Đăng nhập
            </h1>
            <c:if test="${param.registered == '1'}">
                <div class="success">
                    Kích hoạt tài khoản thành công. Hãy đăng
                    nhập.
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/login">
                <label>
                    Username hoặc Email
                </label>
                <input name="login" value="${loginValue}" required>
                <label>
                    Mật khẩu
                </label>
                <input type="password" name="password" required>
                <button type="submit">
                    Đăng nhập
                </button>
            </form>
            <p>
                Chưa có tài khoản?
                <a href="${pageContext.request.contextPath}/register">
                    Đăng ký
                </a>
            </p>
        </section>
    </body>
</html>
