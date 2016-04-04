<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<h1>Add new user</h1>
<p>Add a new user here.</p>
<form:form commandName="user"
	action="${pageContext.servletContext.contextPath}/auth/user/createuser"
	method='post'>
	<table>
		<tr>
			<td>UserName:</td>
			<td><form:input type="text" name="username" id="username"
					path="username" class="form-control"></form:input></td>
			<td><form:errors path="username"></form:errors></td>
		</tr>
		<tr>
			<td>Full Name:</td>
			<td><form:input type="text" name="fullname" id="fullname"
					path="fullname" class="form-control"></form:input></td>
			<td><form:errors path="fullname"></form:errors></td>
		</tr>
		<tr>
			<td>Email:</td>
			<td><form:input type="text" name="email" id="email" path="email"
					class="form-control"></form:input></td>
			<td><form:errors path="email"></form:errors></td>
		</tr>
		<tr>
			<td>Is Administrator:</td>
			<td><form:checkbox path="isAdmin" id="isadmin"
					checked="${user.isAdmin == true ? 'checked' : ''}" /> Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><form:input type="password" name="password" id="password"
					path="password" class="form-control"></form:input></td>
			<td><form:errors path="password"></form:errors></td>
		</tr>
		<tr>
			<td>Retype Password:</td>
			<td><form:input type="password" name="repassword" id="password"
					path="retypedPassword" class="form-control"></form:input></td>
			<td><form:errors path="retypedPassword"></form:errors></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Create user"
				class="btn btn-primary"></td>
		</tr>
	</table>

</form:form>