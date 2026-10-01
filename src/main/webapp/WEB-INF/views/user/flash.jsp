<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:if test="${not empty flashMessage}">
    <p class="${fn:escapeXml(flashType)}" role="status">
        <c:out value="${flashMessage}" />
    </p>
</c:if>
