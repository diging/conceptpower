<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<h1>Add new user</h1>
<p>Add a new user here.</p>
<form
	action="${pageContext.servletContext.contextPath}/auth/user/createuser"
	method='post'>
	<table>
		<tr>
			<td>UserName:</td>
			<td><input type="text" name="username" id="username"></td>
		</tr>

		<tr>
			<td>Full Name:</td>
			<td><input type="text" name="fullname" id="fullname"></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><input type="password" name="password" id="password"></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Create user"
				class="button"></td>
		</tr>
	</table>

</form>