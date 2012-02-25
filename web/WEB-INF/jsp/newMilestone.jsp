<%--
        Copyright (C) 2011 Therap (BD) Ltd.
        
        This code is free software: you can redistribute it and/or modify it
        under the terms of the GNU Lesser General Public License as published
        by the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.
        
        This code is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
        GNU Lesser General Public License for more details.
        
        You should have received a copy of the GNU Lesser General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
--%>
<%--
    Author: Asif Ali
    Email: asif@therapbd.com
    Company: Therap (BD) Ltd.
--%>
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
        <title><fmt:message key="title.userForm"/></title>

        <script type="text/javascript">
            $(document).ready(function() {
            <c:if test="${userCmd.sysId != null}">
                        $("#form1").validate();
            </c:if>
            <c:if test="${userCmd.sysId == null}">
                        $("#form1").validate({
                        rules: { confirmPassword: {equalTo: "#password"}, userName: {remote:"uniq    ueUserName.htm"} },
                            messages: { userName: { remote: jQuery.format("<fmt:message key='validate.user.alreadyExists'/>")} }
                        });
            </c:if>
                    });
        </script>
    </head>

    <body>

        <c:url var="createMilestoneUrl" value="/userMgt/${formAction}.htm"/>
        <form:form modelAttribute="milestoneCmd" method="POST" action="${createMilestoneUrl}" id="form1" enctype="multipart/form-data">
            

            <div class="label">
                <form:label path="milestoneName"><fmt:message key="label.milestoneName"/></form:label>
                    <span class="required">*</span>
            </div>
            <div class="field">
                <form:input path="milestoneName" cssClass="required"/>
                <form:errors path="milestoneName" cssClass="error"/>
            </div>
            <div class="clear"></div>

            
            <div class="label">
                <form:label path="milestoneDescription"><fmt:message key="label.description"/></form:label>
            </div>
            <div class="field">
                <form:textarea path="milestoneDescription" cssClass=""/>
                <form:errors path="milestoneDescription" cssClass="error"/>
            </div>
            <div class="clear"></div>
            
            
            <div class="label">
            <form:label path="role"><fmt:message key="Role"/></form:label>
            <span class="required">*</span>
        </div>
        <div class="field">
            <form:radiobuttons path="role" items="${customOption}" cssClass="required"/>
            <form:errors path="role" cssClass="error"/>
        </div>
        <div class="clear"></div>

            
          
           
            
            <div class="label">
                <form:label path="ministryName"><fmt:message key="label.ministry"/></form:label>
                </div>
            <div class="field">
                <form:select path="ministryName" items="${ministryOption}" cssClass=""/>
                <form:errors path="ministryName" cssClass="error"/>
            </div>
            <div class="clear"></div>
             

            



            <div class="buttonDivLeft">
                <input type="button" value="<fmt:message key='button.back'/>" onclick="window.location='milestoneList.htm';"/>
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
