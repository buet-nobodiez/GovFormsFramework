
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><fmt:message key="title.previousversion"/></title>
</head>

<body>

<c:if test="${fn:length(parents) == 0}">
    <fmt:message key="index.no.forms.yet"/>
</c:if>

<c:forEach var="f" items="${parents}" varStatus="status">
    <div>
        <h2>
            <a href="<c:url value="newEntry.htm?formId=${f.formId}"/>"><c:out escapeXml="true" value="${f.title}"/></a>
        </h2>
    </div>

    <div class="clear"></div>
</c:forEach>

</body>
</html>
