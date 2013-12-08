<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<h1>Add new concept list</h1>
<p>Add a new concept list here.</p>

<form
	action="${pageContext.servletContext.contextPath}/auth/concepts/createconceptlist"
	method='post'>
	<table>
		<tr>
			<td>List Name</td>
			<td><input type="text" name="name" id="name"></td>
		</tr>

		<tr>
			<td>List Description</td>
			<td><input type="text" name="description" id="description"></td>
		</tr>

		<tr>
			<td colspan="2"><input type="submit" value="Create List"></td>
		</tr>
	</table>
</form>