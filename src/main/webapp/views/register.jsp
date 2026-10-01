<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
    <head>
        <title>
            Đăng ký
        </title>
    </head>
    <body>
        <section class="auth-card">
            <h1>
                Đăng ký tài khoản User
            </h1>
            <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/register">
                <label>
                    Username
                </label>
                <input name="username" value="${username}" required>
                <label>
                    Họ tên
                </label>
                <input name="fullName" value="${fullName}" required>
                <label>
                    Email
                </label>
                <input type="email" name="email" value="${email}" required>
                <label>
                    Điện
                    thoại
                </label>
                <input name="phone" value="${phone}">
                <label>
                    Mật
                    khẩu
                </label>
                <input type="password" name="password" required>
                <button type="submit">
                    Gửi OTP và đăng ký
                </button>
            </form>
        </section>
    </body>
</html>
