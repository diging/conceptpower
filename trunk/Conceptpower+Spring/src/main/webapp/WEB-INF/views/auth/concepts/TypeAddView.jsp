<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<h1>Add new concept type</h1>
<p>Add a new concept type here.</p>

<form
	action="${pageContext.servletContext.contextPath}/auth/concepts/createconcepttype"
	method='post'>
	<table>
		<tr>
			<td>Type Name</td>
			<td><input type="text" name="name" id="name"></td>
		</tr>

		<tr>
			<td>Type Description</td>
			<td><input type="text" name="description" id="description"></td>
		</tr>

		<tr>
			<td>Matches</td>
			<td><input type="text" name="match" id="match"></td>
		</tr>

		<tr>
			<td>Super Type</td>
			<td><form:select path="types">
					<form:option value="" label="" />
					<form:options items="${types}" />
				</form:select></td>
		</tr>

		<tr>
			<td colspan="2"><input type="submit" value="Create List"></td>
		</tr>
	</table>
</form>