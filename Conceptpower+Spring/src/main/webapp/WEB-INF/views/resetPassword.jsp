<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>


<h1>Enter new Password</h1>

<p>Please enter a new password below.</p>

<form:form commandName="userBacking"
	action="${pageContext.servletContext.contextPath}/resetComplete"
	method='post' id="editpasswordform">

	<form:hidden path="username" value="${username}" />
	<form:hidden path="token" value="${token}" />
	<form:errors path="" cssClass="error"></form:errors>
	<table style="vertical-align: top">
		<tr>
			<td>Username:</td>
			<td>${userBacking.username}</td>
			<td></td>
		</tr>
		<tr>
			<td>New password:</td>
			<td><form:input type="password" name="password" id="password"
					path="password" /></td>
			<td><form:errors path="password" cssClass="error"></form:errors></td>
		</tr>
		<tr>
			<td>Re-type new password:</td>
			<td><form:input type="password" name="password" id="repassword"
					path="retypedPassword" /></td>
			<td><form:errors path="retypedPassword" cssClass="error"></form:errors></td>
		</tr>
		<tr>
			<td><input type="submit" value="Reset Password" name="submit"
				class="button" /></td>
		</tr>
	</table>

</form:form>


