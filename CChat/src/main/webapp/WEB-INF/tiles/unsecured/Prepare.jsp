<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:property value="message"/>
<s:url action="Login" namespace="/" var="loginUrl"/>
<br />
<s:a href="%{loginUrl}">Go back to login</s:a>