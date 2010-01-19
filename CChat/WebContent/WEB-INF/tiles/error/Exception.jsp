<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h3>Opis greške: </h3> <s:property value="exception" />
<h3>Exception stack: </h3> <s:property value="exceptionStack" />