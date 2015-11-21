<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<h2>Edit user: ${fullname}</h2>

<form:form commandName="user"
	action="${pageContext.servletContext.contextPath}/auth/user/editpassword/store"
	method='post' id="editpasswordform">

	<table style="vertical-align: top">
		<tr>
			<td>Username :</td>
			<form:hidden path="username" value="${username}" />
			<td>${username}</td>
			<td></td>
		</tr>
		<tr>
			<td>New password:</td>
			<td><form:input type="password" name="password" id="password"
					path="password" /></td>
			<td><form:errors name="password" id="password" path="password" /></td>
		</tr>
		<tr>
			<td>Re-type new password:</td>
			<td><form:input type="password" name="password" id="repassword"
					path="retypedPassword" /></td>
			<td><form:errors name="retypedPassword" id="retypedPassword" path="retypedPassword" /></td>
		</tr>
		<tr>
			<td><input type="submit" value="Store changes" name="submit"
				class="button" /></td>
			<td><input type="button" value="Cancel" name="submit"
				onclick="window.location.replace('${pageContext.servletContext.contextPath}/auth/user/canceledit')"
				class="button" /></td>
		</tr>
	</table>

</form:form>