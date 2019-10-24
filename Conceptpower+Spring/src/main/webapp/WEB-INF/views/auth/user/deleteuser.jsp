<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
	
</script>

<h2>Delete user:</h2>

<form:form modelAttribute="user"
	action="${pageContext.servletContext.contextPath}/auth/user/confirmdeleteuser/"
	method='post' id="edituserform">

	<table class="table table-striped table-bordered" width="500">
		<tr>
			<td width="120"><form:hidden id="name" path="username" />Username:</td>
			<td>${user.username}</td>

		</tr>
		<tr>
			<td width="120"><form:hidden id="fullname" path="fullname" />Name:</td>
			<td>${user.fullname}</td>
		</tr>
		<tr>
			<td width="120">Is Administrator:</td>
			<td><form:checkbox path="isAdmin" id="isadmin"
					checked="${user.isAdmin == true ? 'checked' : ''}" disabled="true" />
				Yes</td>
		</tr>
	</table>

	<input type="submit" value="Delete User" name="submit" class="btn btn-primary" />
	<input type="button" value="Cancel" name="submit"
		onclick="window.location.replace('${pageContext.servletContext.contextPath}/auth/user/canceldelete')"
		class="btn btn-primary" />

</form:form>