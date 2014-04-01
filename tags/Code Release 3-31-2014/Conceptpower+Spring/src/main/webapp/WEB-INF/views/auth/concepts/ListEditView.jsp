<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>


<h1>Edit concept list</h1>
<p>Edit concept list here.</p>

<form
	action="${pageContext.servletContext.contextPath}/auth/conceptlist/storeeditlist/${listname}"
	method='post'>
	<table>
		<tr>
			<td>List Name</td>
			<td><input type="text" name="newlistname" id="newlistname"
				value="${newlistname}"></td>
		</tr>

		<tr>
			<td>List Description</td>
			<td><textarea rows="7" cols="50" name="description"
					id="description"> ${description}</textarea></td>
		</tr>

		<tr>
			<td><input type="submit" value="Edit list" class="button"></td>
			<td><a
				href="${pageContext.servletContext.contextPath}/auth/conceptlist/editlist/canceledit"><input
					type="button" name="cancel" value="No, cancel!" class="button"></a></td>
		</tr>
	</table>
</form>