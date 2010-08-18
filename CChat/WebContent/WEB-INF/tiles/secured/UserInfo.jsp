<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<b><s:property value="%{getText('global.username')}"/>:</b> <s:property value="operator.username"/> (<a href="<s:url action="Logout" namespace="/"/>"><s:property value="%{getText('global.logout')}"/></a>) 
, <b><s:property value="%{getText('global.role')}"/>:</b> <s:property value="operator.role.name"/>