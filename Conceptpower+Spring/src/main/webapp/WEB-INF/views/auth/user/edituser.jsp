 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h2>Edit user:</h2>

<form:form commandName="user"
	action="${pageContext.servletContext.contextPath}/auth/user/edituser/store"
	method='post' id="edituserform">

	<table>
		<tr>
			<td>Username:</td>
			<td><form:hidden path="username" />${user.username}</td>
			<td></td>
		</tr>
		<tr>
			<td>Name:</td>
			<td><form:input type="text" id="fullname" path="fullname" /></td>
			<td><form:errors type="text" id="fullname" path="fullname" /></td>
		</tr>
		<tr>
			<td>Email:</td>
			<td><form:input type="email" id="email" path="email" /></td>
			<td><form:errors type="email" id="email" path="email" /></td>
		</tr>
		<tr>
			<td>Is Administrator:</td>
			<td><form:checkbox path="isAdmin" id="isadmin"
					checked="${user.isAdmin == true ? 'checked' : ''}" /> Yes</td>
			<td></td>
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