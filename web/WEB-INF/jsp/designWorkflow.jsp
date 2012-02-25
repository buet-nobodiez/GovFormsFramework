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
        
        <style type="text/css">
              table
            {
            border-collapse:collapse;
            }
            table,th,thead, td
            {
            border: 1px solid black;
            }
        </style>
        
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
        
        <script type="text/javascript">
        $(document).ready(function() {
            $("#form1").validate();

            $('#result').load('formBuilder/fieldTypes', function() {
                alert('Load was performed.');
            });
        });

        function newWorkflowDetail(workflowId, indx, order) {
            window.location = "newWorkflowDetail.htm?workflowId=" + workflowId + "&type=" + type + "&order=" + order;
        }
    </script>
                
    </head>

    <body>

        <c:url var="saveWorkflowDetailUrl" value="/userMgt/${formAction}.htm?workflowId=${workflowId}"/>
        <form:form modelAttribute="workflowDetail" method="POST" action="${saveWorkflowDetailUrl}" id="form1" enctype="multipart/form-data">
            

            
            <div class="label">
                Milestone Name : 
            </div>
            <div class="field">
                <form:select path="milestoneId" items="${milestoneOption}" cssClass=""/>
                <form:errors path="milestoneId" cssClass="error"/>
            </div>
            <div class="clear"></div>
            
            
            <div class="label">
                Accept Milestone : 
            </div>
            <div class="field">
                <form:select path="acceptMilestoneId" items="${milestoneOption}" cssClass=""/>
                <form:errors path="acceptMilestoneId" cssClass="error"/>
            </div>
            <div class="clear"></div>
            
            
            
            <div class="label">
                Decline Milestone : 
            </div>
            <div class="field">
                <form:select path="declineMilestoneId" items="${milestoneOption}" cssClass=""/>
                <form:errors path="declineMilestoneId" cssClass="error"/>
            </div>
            <div class="clear"></div>
            
            
           
            <br>
            
            <c:if test="${ !empty workflowDetailList}">
                <table >
                    <th>Milestone Name</th>
                    <th>Accept Milestone Name</th>
                    <th>Decline Milestone Name</th>
                    
                    <c:forEach var="f" items="${workflowDetailList}"  varStatus="counter1">
                        <tr>
                            <td>${f.milestoneName}</td>
                            <td>${f.acceptMilestoneName}</td>
                            <td>${f.declineMilestoneName}</td>
                        </tr>
                    </c:forEach>
                    
                </table>
            </c:if>
            
             

            



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
