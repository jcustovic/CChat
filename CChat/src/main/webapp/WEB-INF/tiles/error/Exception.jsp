<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h3><s:property value="%{getText('global.exceptionDescription')}"/>: </h3> <s:property value="exception" />
<h3><s:property value="%{getText('global.exceptionStack')}"/>: </h3> <s:property value="exceptionStack" />