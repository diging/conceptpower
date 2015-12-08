<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<h1>Add new concept type</h1>
<p>Add a new concept type here.</p>

<form:form
	action="${pageContext.servletContext.contextPath}/auth/concepts/createconcepttype"
	commandName="conceptTypeAddForm" method='post'>
	<table>
		<tr>
			<td>Type Name</td>
			<td><form:input path="typeName" /></td>
			<td><form:errors path="typeName" class="ui-state-error-text" /></td>
		</tr>

		<tr>
			<td>Type Description</td>
			<td><form:textarea path="typeDescription" rows="7" cols="50" /></td>
			<td><form:errors path="typeDescription"
					class="ui-state-error-text" /></td>
		</tr>
		<tr>
			<td>Matches</td>
			<td><form:input path="matches" /></td>
			<td><form:errors path="matches" class="ui-state-error-text" /></td>
		</tr>

		<tr>
			<td>Super Type</td>
			<td><form:select path="superType" name="superType">
					<form:option value="" label="" />
					<form:options items="${conceptTypeAddForm.types}" />
				</form:select></td>
		</tr>

		<tr>
			<td colspan="2"><input type="submit" value="Create Type"
				class="button"></td>
		</tr>
	</table>
</form:form>