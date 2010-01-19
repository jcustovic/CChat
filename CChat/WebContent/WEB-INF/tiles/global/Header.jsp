<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="#session['userSession'].role.name.equals('admin')">

</s:if>
<s:elseif test="#session['userSession'].role.name.equals('operator')">
	<a href="<s:url action="EnterChat"/>"><s:text name="menu.operator.Chat"/></a> | 
	<a href="<s:url action="OperatorStatistics"/>"><s:text name="admin.statistics.title"/></a> |
	<a href="<s:url action="OperatorOptions"/>"><s:text name="menu.operator.options"/></a>
</s:elseif>
<s:elseif test="#session['userSession'].role.name.equals('moderator')">
	<a href="<s:url action="EnterChat"/>"><s:text name="menu.operator.Chat"/></a> | 
	<a href="<s:url action="OperatorStatistics"/>"><s:text name="admin.statistics.title"/></a> |
	<a href="<s:url action="OperatorOptions"/>"><s:text name="menu.operator.options"/></a>
</s:elseif>