<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
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
</head>

<body>
	<div align="center">
		<img src="${path}/images/cchat.png" />
	</div>

	<tiles:insertAttribute name="header"/>
	
	<tiles:insertAttribute name="body"/>
	
	<tiles:insertAttribute name="footer"/>

</body>

</html>