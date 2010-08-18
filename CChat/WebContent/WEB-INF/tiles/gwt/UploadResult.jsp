<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="hasActionErrors() || hasFieldErrors()">
	<script type="text/javascript">
		if (parent.uploadError)  parent.uploadError('${errorFields.picture}');
	</script>
</s:if>
<s:else>
	<script type="text/javascript">
		if (parent.uploadComplete) parent.uploadComplete('${pictureFileName}');
	</script>
</s:else>