<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<table width="100%">
	<tr>
		<td width="30%" align="left">
			<s:property value="date" />
		</td>
		<td width="30%" align="center">
			CChat
		</td>
		<td width="30%" align="right">
			<b><s:property value="%{getText('global.username')}"/>:</b> <s:property value="operator.username"/> (<a href="<s:url action="Logout" namespace="/"/>"><s:property value="%{getText('global.logout')}"/></a>) 
			<br/><b><s:property value="%{getText('global.role')}"/>:</b> <s:property value="operator.role.name"/>
		</td>
	</tr>
</table>