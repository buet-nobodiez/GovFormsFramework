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
    <title><fmt:message key="title.formInfo"/>: ${form.title}</title>

    <style type="text/css">
        td {
            border: 1px solid #D3E5DA;
            padding: 5px;
            font-size: 10pt;
            font-weight: normal;
        }

        h3 {
            font-size: 12pt;
        }
    </style>


    <style type="text/css">
                /*
                Blue Dream
                Written by Teylor Feliz  http://www.admixweb.com
        */

        table { background:#D3E4E5;
         border:1px solid gray;
         border-collapse:collapse;
         color:#fff;
         font:normal 12px verdana, arial, helvetica, sans-serif;
        }
        caption { border:1px solid #5C443A;
         color:#5C443A;
         font-weight:bold;
         letter-spacing:20px;
         padding:6px 4px 8px 0px;
         text-align:center;
         text-transform:uppercase;
        }
        td, th { color:#363636;
         padding:.4em;
        }
        tr { border:1px dotted gray;
        }
        thead th, tfoot th { background:#5C443A;
         color:#FFFFFF;
         padding:3px 10px 3px 10px;
         text-align:left;
         text-transform:uppercase;
        }
        tbody td a { color:#363636;
         text-decoration:none;
        }
        tbody td a:visited { color:gray;
         text-decoration:line-through;
        }
        tbody td a:hover { text-decoration:underline;
        }
        tbody th a { color:#363636;
         font-weight:normal;
         text-decoration:none;
        }
        tbody th a:hover { color:#363636;
        }
        tbody td+td+td+td a { background-image:url('bullet_blue.png');
         background-position:left center;
         background-repeat:no-repeat;
         color:#03476F;
         padding-left:15px;
        }
        tbody td+td+td+td a:visited { background-image:url('bullet_white.png');
         background-position:left center;
         background-repeat:no-repeat;
        }
        tbody th, tbody td { text-align:left;
         vertical-align:top;
        }
        tfoot td { background:#5C443A;
         color:#FFFFFF;
         padding-top:3px;
        }
        .odd { background:#fff;
        }
        tbody tr:hover { background:#99BCBF;
         border:1px solid #03476F;
         color:#000000;
        }
    </style>


</head>

<body>

<h3><fmt:message key="label.tableName"/>: ${form.tableName}</h3>

<table width="100%" style="border:1px solid #D3E5DA;" cellspacing="0">
    <thead>
        <tr>
        <th scope="col"><fmt:message key="label.fieldName"/></th>
        <th scope="col"><fmt:message key="label.colName"/></th>
        </tr>
    </thead>

    

    <tbody>
        <c:forEach var="f" items="${form.fields}"  varStatus="st">
            <c:if test="${f.type != 'file' && f.type != 'section' && f.type != 'note' }">
                <tr <c:if test="${st.count % 2 == 1}">class="odd"</c:if>>
                    <th id="r100" scope="row">${f.label}</th>
                    <td>${f.colName}</td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>


   

</table>





</body>
</html>
