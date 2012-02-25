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
    <title>Dashboard</title>

    <script type="text/javascript">
        function search() {
            var colVal = document.forms[0].elements["colVal"].value;
            var colName = document.forms[0].elements["colName"].value;

            window.location = "entryList.htm?formId=${formId}&page=" + ${page}
                    + "&colName=" + colName + "&colVal=" + colVal;
        }

        function popitup(url) {
            window.open(url, 'name', 'height=600,width=800');
        }
    </script>

    <style type="text/css">
        tr.odd {
            background-color: #D3E5DA;
        }

        td {
            border: 1px solid #D3E5DA;
            padding: 5px;
            font-size: 10pt;
            font-weight: normal;
        }

        td a {
            font-size: 10pt;
            font-weight: normal;
            color: #ff0000;
        }
    </style>

    <style type="text/css">
        .cssguycomments {background:#eee;border:#ddd;padding:8px;margin-bottom:40px;}
        .cssguycomments p {font:normal 12px/18px verdana;}
        table {border-collapse:collapse;}
        thead th {
        font:bold 13px/18px georgia;
        text-align:left;
        background:#fff4c6;
        color:#333;
        padding:8px 16px 8px 8px;
        border-right:1px solid #fff;
        border-bottom:1px solid #fff;
        }
        thead th.null {background:#fff;}
        tbody th {
        font:bold 12px/15px georgia;
        text-align:left;
        background:#fff9e1;
        color:#333;
        padding:8px;
        border-bottom:1px solid #f3f0e4;
        border-right:1px solid #fff;
        }
        tbody td {
        font:normal 12px/15px georgia;
        color:#333;
        padding:8px;
        border-right:1px solid #f3f0e4;
        border-bottom:1px solid #f3f0e4;
        }
        tbody td.on {background:#f3f0e4;}
        thead th.on {background:#ffe068;}
        tbody th.on {background:#ffe068;}
    </style>

    <script type="text/javascript">
        /*
        For functions getElementsByClassName, addClassName, and removeClassName
        Copyright Robert Nyman, http://www.robertnyman.com
        Free to use if this text is included
        */
        function addLoadEvent(func) {
        var oldonload = window.onload;
        if (typeof window.onload != 'function') {
        window.onload = func;
        } else {
        window.onload = function() {
        oldonload();
        func();
        }
        }
        }
        function getElementsByClassName(className, tag, elm){
        var testClass = new RegExp("(^|\\s)" + className + "(\\s|$)");
        var tag = tag || "*";
        var elm = elm || document;
        var elements = (tag == "*" && elm.all)? elm.all : elm.getElementsByTagName(tag);
        var returnElements = [];
        var current;
        var length = elements.length;
        for(var i=0; i<length; i++){
        current = elements[i];
        if(testClass.test(current.className)){
        returnElements.push(current);
        }
        }
        return returnElements;
        }
        function addClassName(elm, className){
        var currentClass = elm.className;
        if(!new RegExp(("(^|\\s)" + className + "(\\s|$)"), "i").test(currentClass)){
        elm.className = currentClass + ((currentClass.length > 0)? " " : "") + className;
        }
        return elm.className;
        }
        function removeClassName(elm, className){
        var classToRemove = new RegExp(("(^|\\s)" + className + "(\\s|$)"), "i");
        elm.className = elm.className.replace(classToRemove, "").replace(/^\s+|\s+$/g, "");
        return elm.className;
        }
        function makeTheTableHeadsHighlight() {
        // get all the td's in the heart of the table...
        var table = document.getElementById('rockartists');
        var tbody = table.getElementsByTagName('tbody');
        var tbodytds = table.getElementsByTagName('td');
        // and loop through them...
        for (var i=0; i<tbodytds.length; i++) {
        // take note of their default class name
        tbodytds[i].oldClassName = tbodytds[i].className;
        // when someone moves their mouse over one of those cells...
        tbodytds[i].onmouseover = function() {
        // attach 'on' to the pointed cell
        addClassName(this,'on');
        // attach 'on' to the th in the thead with the same class name
        var topheading = getElementsByClassName(this.oldClassName,'th',table);
        addClassName(topheading[0],'on');
        // attach 'on' to the far left th in the same row as this cell
        var row = this.parentNode;
        var rowheading = row.getElementsByTagName('th')[0];
        addClassName(rowheading,'on');
        }
        // ok, now when someone moves their mouse away, undo everything we just did.
        tbodytds[i].onmouseout = function() {
        // remove 'on' from this cell
        removeClassName(this,'on');
        // remove 'on' from the th in the thead
        var topheading = getElementsByClassName(this.oldClassName,'th',table);
        removeClassName(topheading[0],'on');
        // remove 'on' to the far left th in the same row as this cell
        var row = this.parentNode;
        var rowheading = row.getElementsByTagName('th')[0];
        removeClassName(rowheading,'on');
        }
        }
        }
        addLoadEvent(makeTheTableHeadsHighlight);
    </script>




</head>

<body>



    <c:if test="${sessionScope.user.admin != 0}" >




    <table width="100%"  cellspacing="0" id="rockartists">

	<thead>
	<tr>
            <td>Serial No.</td>
            <td>Date</td>
            <td>Tracking Id</td>
            <td>Form Name</td>
            <td>Task</td>
            <td>Earlier Message</td>
            <td>Message Source</td>
        </tr>
	</thead>


	<tbody>

      <c:forEach var="e" items="${trackList}" varStatus="st">
        <tr <c:if test="${st.count % 2 == 1}">class="odd"</c:if>>
            <th class=""><c:out value="${st.count}" /></th>
            
      <form action="saveHistory.htm" method="POST">
            
            <td class="r1">${e.workflowHistory.date}</td>
            <td class="r2">${e.trackId}</td>
            <td class="r3">
                ${e.formName} <br>
                <a href="#">Export PDF</a>
            </td>
            
            <td class="r4">
                <textarea name="comment"></textarea>
                <br/>
                <input type="hidden" name="trackId" value="${e.trackId}"/>
                <input type="hidden" name="milestoneId" value="${e.currentMilestoneId}"/>
                <input type="hidden" name="formId" value="${e.formId}"/>
                
                <input type="submit" name="button" value="Approve"/> <br/>
                <input type="submit" name="button" value="Decline"/> <br/>
            </td>
            
            <td class="r5">${e.workflowHistory.comment}</td>
            <td class="r6">
                <c:if test="${e.milestone.role == 10}">
                    Manager
                </c:if>
                <c:if test="${e.milestone.role == 1}">
                    Secretary
                </c:if>
                <br/>
                ${e.milestone.ministryName} Ministry          
            </td>
            
       </form>
      
        </tr>
    </c:forEach>
	</tbody>
        
        
</table>


</c:if>
    
    <c:if test="${sessionScope.user.admin == 0}" >

    <table width="100%"  cellspacing="0" id="rockartists">

	<thead>
	<tr>
            <td>Serial No.</td>
            <td>Tracking Id</td>
            <td>Form Name</td>
            <td>Status</td>
        </tr>
	</thead>


	<tbody>

      <c:forEach var="e" items="${trackList}" varStatus="st">
        <tr <c:if test="${st.count % 2 == 1}">class="odd"</c:if>>
            <th class=""><c:out value="${st.count}" /></th>
            
            
            <td class="r1">${e.trackId}</td>
            <td class="r2">
                ${e.formName} <br>
            </td>
            
             <td class="r3">
                 <c:if test="${e.currentMilestoneId == 1}">
                     Workflow Completed
                 </c:if>
                 
                 <c:if test="${e.currentMilestoneId != 1}">
                     Processing
                 </c:if>    
                     
             </td>
            
            
       </form>
      
        </tr>
    </c:forEach>
	</tbody>
        
        
</table>


</c:if>



</body>
</html>
