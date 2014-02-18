<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>


<h1>Edit concept type</h1>
<p>Edit concept type here.</p>

<form
	action="${pageContext.servletContext.contextPath}/auth/concepttype/storeedittype/${typeid}"
	method='post'>
	<table>
		<tr>
			<td>Type Name</td>
			<td><input type="text" name="name" id="name" value="${typeName}"></td>
		</tr>

		<tr>
			<td>Type Description</td>
			<td><textarea rows="7" cols="50" name="description"
					id="description"> ${description}</textarea></td>
		</tr>

		<tr>
			<td>Matches</td>
			<td><input type="text" name="match" id="match"
				value="${matches}"></td>
		</tr>

		<tr>
			<td>Super Type</td>
			<td><form:select path="supertypes" name="supertypes">
					<form:option value="" label="Select one" />
					<form:option value="${selectedtypeid}" label="${selectedtypename}"
						selected="selected" />
					<form:options items="${supertypes}" />
				</form:select></td>
		</tr>

		<tr>
			<td colspan="2"><input type="submit" value="Edit type"></td>
		</tr>
	</table>
</form>