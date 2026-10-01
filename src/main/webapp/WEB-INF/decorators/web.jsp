<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width,initial-scale=1.0">
        <title>
            <sitemesh:write property="title" />
        </title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
        <script src="${pageContext.request.contextPath}/assets/shop.js" defer>
        </script>
        <sitemesh:write property="head" />
    </head>
    <body>
        <%@ include file="/commons/web/header.jsp"%>
        <main class="container">
            <sitemesh:write property="body" />
        </main>
        <%@ include file="/commons/web/footer.jsp"%>
    </body>
</html>
