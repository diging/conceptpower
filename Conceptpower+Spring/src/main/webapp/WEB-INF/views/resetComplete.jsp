<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>




<h1>Password successfully Reset</h1>

<p>You have successfully reset your password. You can now login to
	Conceptpower.</p>

<form name='f'
	action="<c:url value='${pageContext.servletContext.contextPath}/j_spring_security_check' />"
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
			<td></td>
			<td>
				<p>
					<input name="submit" type="submit" value="Login" class="btn btn-primary" />
				</p>
			</td>
		</tr>
	</table>
</form>



