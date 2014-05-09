<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
	
</script>

<h2>Delete user:</h2>

<form:form commandName="user"
	action="${pageContext.servletContext.contextPath}/auth/user/confirmdeleteuser/"
	method='post' id="edituserform">

	<table>
		<tr>
			<td><form:hidden id="name" path="username" />Username :
				${user.username}</td>

		</tr>
		<tr>
			<td><form:hidden id="fullname" path="name" />Name :${user.name}</td>
		</tr>
		<tr>
			<td>Is Administrator: <form:checkbox path="isAdmin" id="isadmin"
					checked="${user.isAdmin == true ? 'checked' : ''}" disabled="true" />
				Yes
			</td>
		</tr>
		<tr>
			<td><input type="submit" value="Delete User" name="submit"
				class="button" /> <input type="button" value="Cancel" name="submit"
				onclick="window.location.replace('${pageContext.servletContext.contextPath}/auth/user/canceldelete')"
				class="button" /></td>
		</tr>
	</table>

</form:form>