<%--
Document : registration
Created on : Jan 26, 2012, 3:40:47 PM
Author : anik
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Registration Page</title>
    </head>
    <body>











    <c:url var="createUserUrl" value="/userMgt/${formAction}.htm"/>
    <form:form modelAttribute="userCmd" method="POST" action="${createUserUrl}" id="form1" enctype="multipart/form-data">
        <form:hidden path="sysId"/>

        <div>
            <form:errors cssClass="error"/>
        </div>

        <div class="label">
            <form:label path="userName"><fmt:message key="label.userName"/></form:label>
            <span class="required">*</span>
        </div>
        <div class="field">
            <c:if test="${userCmd.sysId == null}">
                <form:input path="userName" cssClass="required"/>
            </c:if>
            <c:if test="${userCmd.sysId != null}">
                <form:hidden path="userName"/> ${userCmd.userName}
            </c:if>
            <form:errors path="userName" cssClass="error"/>
        </div>
        <div class="clear"></div>

        <c:if test="${userCmd.sysId == null}">
            <div class="label">
                <form:label path="password"><fmt:message key="label.password"/></form:label>
                <span class="required">*</span>
            </div>
            <div class="field">
                <form:password path="password" cssClass="required"/>
                <form:errors path="password" cssClass="error"/>
            </div>
            <div class="clear"></div>

            <div class="label">
                <form:label path="confirmPassword"><fmt:message key="label.password.confirm"/></form:label>
                <span class="required">*</span>
            </div>
            <div class="field">
                <form:password path="confirmPassword" cssClass="required"/>
                <form:errors path="confirmPassword" cssClass="error"/>
            </div>
            <div class="clear"></div>
        </c:if>



        <div class="clear"></div>

        <div class="label">
            <form:label path="name"><fmt:message key="label.fullName"/></form:label>
            <span class="required">*</span>
        </div>
        <div class="field">
            <form:input path="name" cssClass="required"/>
            <form:errors path="name" cssClass="error"/>
        </div>
        <div class="clear"></div>

        <div class="label">
            <form:label path="title"><fmt:message key="label.designation"/></form:label>
            <span class="required">*</span>
        </div>
        <div class="field">
            <form:input path="title" cssClass="required"/>
            <form:errors path="title" cssClass="error"/>
        </div>
        <div class="clear"></div>

        <div class="label">
            <form:label path="email"><fmt:message key="label.email"/></form:label>
            <span class="required">*</span>
        </div>
        <div class="field">
            <form:input path="email" cssClass="required"/>
            <form:errors path="email" cssClass="error"/>
        </div>
        <div class="clear"></div>

        <div class="label">
            <form:label path="mobile"><fmt:message key="label.mobile"/></form:label>
            <span class="required">*</span>
        </div>
        <div class="field">
            <form:input path="mobile" cssClass=""/>
            <form:errors path="mobile" cssClass="error"/>
        </div>
        <div class="clear"></div>

        <div class="buttonDivLeft">
            <input type="button" value="<fmt:message key='button.back'/>" onclick="window.location='userList.htm';"/>
        </div>
        <div class="buttonDiv">
            <c:if test="${userCmd.sysId == null}">
                <input type="submit" value="<fmt:message key='button.submit'/>"/>
            </c:if>
            <c:if test="${userCmd.sysId != null }">
                <input type="submit" value="<fmt:message key='button.update'/>"/>
            </c:if>
        </div>
    </form:form>

















</body>
</html>
