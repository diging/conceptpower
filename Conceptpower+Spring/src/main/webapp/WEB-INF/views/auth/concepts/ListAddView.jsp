<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h1>Add new concept list</h1>
<p>Add a new concept list here.</p>

<form:form
	action="${pageContext.servletContext.contextPath}/auth/concepts/createconceptlist"
	commandName="conceptListAddForm" method='post'>
	<table>
		<tr>
			<td>List Name</td>
			<td><form:input path="listName" class="form-control"/></td>
			<td><form:errors path="listName" class="ui-state-error-text"></form:errors></td>
		</tr>

		<tr>
			<td>List Description</td>
			<td><form:textarea rows="7" cols="50" path="description" class="form-control"></form:textarea></td>
			<td><form:errors path="description" class="ui-state-error-text"></form:errors></td>
		</tr>

		<tr>
			<td colspan="2"><input type="submit" value="Create List"
				class="btn btn-primary"></td>
		</tr>
	</table>
</form:form>