<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}" />

<div id="loadingWrapper">
<div id="loading">
    <div class="loadingIndicator">
        <img src="${path}/images/ajax-loader.gif" width="16" height="16" style="margin-right:8px;float:left;vertical-align:top;"/>CChat<br/>
        <span id="loadingMsg">Loading styles and images...</span></div>
</div>
</div>

<script type="text/javascript">document.getElementById('loadingMsg').innerHTML = '<center>Loading CChat application<br> Please wait... </center>';</script>
<script type="text/javascript" language="javascript" src="${path}/admin/admin.nocache.js"></script>
<script type="text/javascript">document.getElementById('loadingWrapper').innerHTML = '';</script>