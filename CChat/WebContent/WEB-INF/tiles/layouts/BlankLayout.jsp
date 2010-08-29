<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<tiles:useAttribute name="title"/>

<html>

<c:set var="path" value="${pageContext.request.contextPath}"/>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- <meta name="gwt:property" content="locale=hr_HR">  -->
	<title>
		<s:property value="getText(#attr.title)"/>
	</title>
	<!-- <link rel="favicon" href="${path}/images/favicon.ico" type="image/x-icon" /> --> 
	<link rel="stylesheet" href="${path}/style/style.css" type="text/css"/>
	
	 <style type="text/css">
        body { overflow:hidden }
        #loading {
            border: 1px solid #ccc;
            position: absolute;
            left: 45%;
            top: 40%;
            padding: 2px;
            z-index: 20001;
            height: auto;
            width: 10%;
        }

        #loading a {
            color: #225588;
        }

        #loading .loadingIndicator {
            background: white;
            font: bold 13px tahoma, arial, helvetica;
            padding: 10px;
            margin: 0;
            height: auto;
            color: #444;
        }

        #loadingMsg {
            font: normal 10px arial, tahoma, sans-serif;
        }
    </style>
</head>

<body>
	<tiles:insertAttribute name="body"/>
</body>

</html>