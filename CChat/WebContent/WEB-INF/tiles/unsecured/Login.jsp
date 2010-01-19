<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h2 style="text-align:center;"><s:text name="global.login"/></h2>

<s:form action="Login" cssStyle="margin-left:auto; margin-right:auto;">
	<s:textfield name="username" label="%{getText('global.username')}"/>
	<s:password name="password" label="%{getText('global.password')}"/>
	<s:submit value="%{getText('global.login')}"/>
</s:form>

<noscript>
	<div class="areaErrors" style="margin: auto;">
		<s:property value="%{getText('global.noJavascript')}"/>
	</div>
</noscript>
