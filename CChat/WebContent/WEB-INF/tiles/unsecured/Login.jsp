<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<c:set var="locale" value="${session.WW_TRANS_I18N_LOCALE}"/>
<c:if test="${empty locale}"> 
	<c:set var="locale" value="hr_HR"/>
</c:if>

<h2 style="text-align:center;"><s:text name="global.login"/></h2>

<s:form action="Login" cssStyle="text-align:center; margin-left:auto; margin-right:auto;">
	<s:textfield name="username" label="%{getText('global.username')}" size="20"/>
	<s:password name="password" label="%{getText('global.password')}" size="20"/>
	<s:select label="%{getText('global.language')}" name="request_locale" list="#{'en':'English', 'hr_HR':'Hrvatski'}" value="%{locale}" />

	<s:submit value="%{getText('global.login')}"/>
</s:form>


<noscript>
	<div class="areaErrors" style="margin: auto;">
		<s:property value="%{getText('global.noJavascript')}"/>
	</div>
</noscript>
