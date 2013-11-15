<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h2>Edit user: ${user.name}</h2>

<form:form method="POST" commandName="user" action="${pageContext.servletContext.contextPath}/auth/user/edit/store">
	<form:errors path="*" cssClass="errorblock" element="div" />

	<table>
			<tr>
				<td>Username :</td>
				<td>${user.user}</td>
				<td>
				</td>
			</tr>
			<form:hidden path="user" value="${user.user}" />
			<tr>
				<td>Name :</td>
				<td><form:input path="name" />
				</td>
				<td><form:errors path="name" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Is Administrator:</td>
				<td><form:checkbox path="isAdmin" />Yes
				</td>
				<td>
				</td>
			</tr>
			<tr>
				<td colspan="3"><input type="submit" value="Store changes"/></td>
			</tr>
		</table>

</form:form>