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
			<td><form:input type="text" name="username" id="username" path="user"></form:input></td>
			<td>${errUserName}</td>
		</tr>

		<tr>
			<td>Full Name:</td>
			<td><form:input type="text" name="fullname" id="fullname" path="name"></form:input></td>
		</tr>
		<tr>
			<td>Email:</td>
			<td><form:input type="text" name="email" id="email" path="email"></form:input></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><form:input type="password" name="password" id="password" path="pw"></form:input></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Create user"
				class="button"></td>
		</tr>
	</table>

</form:form>