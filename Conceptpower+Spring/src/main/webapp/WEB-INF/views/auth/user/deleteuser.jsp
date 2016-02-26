<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
	
</script>

<h2>Delete user:</h2>

<form:form commandName="user"
	action="${pageContext.servletContext.contextPath}/auth/user/confirmdeleteuser/"
	method='post' id="edituserform">

	<table class="table table-striped table-bordered">
		<tr>
			<td><form:hidden id="name" path="username" />Username :</td>
			<td>${user.username}</td>

		</tr>
		<tr>
			<td><form:hidden id="fullname" path="fullname" />Name</td>
			<td>:${user.fullname}</td>
		</tr>
		<tr>
			<td>Is Administrator:</td>
			<td><form:checkbox path="isAdmin" id="isadmin"
					checked="${user.isAdmin == true ? 'checked' : ''}" disabled="true" />
				Yes</td>
		</tr>
	</table>

	<input type="submit" value="Delete User" name="submit" class="button" />
	<input type="button" value="Cancel" name="submit"
		onclick="window.location.replace('${pageContext.servletContext.contextPath}/auth/user/canceldelete')"
		class="button" />

</form:form>