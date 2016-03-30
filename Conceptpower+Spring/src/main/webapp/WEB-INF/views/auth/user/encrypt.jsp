<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1>Encrypt Password</h1>
<p>Are you sure you want to encrypt the password for user "${userfullname}"? This cannot be undone.</p>

<form:form 
	action="${pageContext.servletContext.contextPath}/auth/user/encryptpassword"
	method='post'>
	
	<input type="hidden" name="username" value="${username}">
	
	<c:if test="${not isEncrypted}">
	<input type="submit" value="Encrypt" class="button">
	</c:if>
	<c:if test="${isEncrypted}">
	Password is already encrypted.
	</c:if>
	
	<a href="${pageContext.servletContext.contextPath}/auth/user/list">Cancel</a>

</form:form>