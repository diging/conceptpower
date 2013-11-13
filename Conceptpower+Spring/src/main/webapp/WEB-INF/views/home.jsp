<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>


<h1>
	Hello world!  
</h1>

<sec:authorize access="isAuthenticated()">You're logged in!</sec:authorize>

<sec:authorize access="isAnonymous()">
<form name='f' action="<c:url value='j_spring_security_check' />"
	method='post'>

	<table>
		<tr>
			<td>Username:</td>
			<td><input type='text' name='j_username' value=''></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><input type='password' name='j_password' /></td>
		</tr>
		<tr>
	</table>
	<p>
		<input name="submit" type="submit" value="Login" class="button" />
	</p>


</form>
</sec:authorize>

