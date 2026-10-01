<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
    <head>
        <title>
            Xác thực OTP
        </title>
    </head>
    <body>
        <section class="auth-card">
            <h1>
                Kích hoạt bằng OTP
            </h1>
            <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
            </c:if>
            <c:choose>
                <c:when test="${sessionScope.otpMailSent}">
                    <p>
                        OTP đã được gửi tới email. Mã có hiệu lực 5 phút.
                    </p>
                </c:when>
                <c:otherwise>
                    <div class="warning">
                        Chưa cấu hình MAIL_USERNAME /
                        MAIL_PASSWORD nên email chưa được gửi. Khi demo OTP thật hãy cấu
                        hình App Password Gmail.
                    </div>
                </c:otherwise>
            </c:choose>
            <form method="post" action="${pageContext.request.contextPath}/verify-otp">
                <label>
                    Mã OTP
                </label>
                <input name="otp" maxlength="6" required>
                <button type="submit">
                    Kích hoạt
                </button>
            </form>
        </section>
    </body>
</html>
