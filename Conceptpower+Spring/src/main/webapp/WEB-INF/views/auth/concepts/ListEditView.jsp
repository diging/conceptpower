<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>


<h1>Edit concept list</h1>
<p>Edit concept list here.</p>

<font color="red">${luceneError }</font>

<form:form
	action="${pageContext.servletContext.contextPath}/auth/conceptlist/storeeditlist"
	method='post' commandName="conceptListAddForm">
	<table>
		<tr>
			<td>List Name</td>
			<td><form:input path="listName" />
				<form:hidden path="oldListName" /></td>
			<td><form:errors path="listName" class="ui-state-error-text"></form:errors></td>
		</tr>

		<tr>
			<td>List Description</td>
			<td><form:textarea rows="7" cols="50" path="description"></form:textarea></td>
			<td><form:errors path="description" class="ui-state-error-text"></form:errors></td>
		</tr>

		<tr>
			<td><input type="submit" value="Edit list" class="button"></td>
			<td><a
				href="${pageContext.servletContext.contextPath}/auth/conceptlist/editlist/canceledit"><input
					type="button" name="cancel" value="No, cancel!" class="button"></a></td>
		</tr>
	</table>
</form:form>