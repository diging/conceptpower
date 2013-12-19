<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<form
	action="${pageContext.servletContext.contextPath}/auth/concepts/editconceptconfirm/${id}"
	method='post'>

	<h1>Delete concept</h1>
	<p>Do you really want to delete the following concept?</p>


	<h2>${word}</h2>
	<p>${description}</p>
	<br /> <br />
	<table>

		<tr>
			<td>Concept</td>
			<td><input type="text" name="name" id="name" value="${word}"></td>
		</tr>
		<tr>
			<td>POS</td>
			<td><form:select path="poss" name="poss">
					<form:option value="${selectedposvalue}" label="${selectedposname}"
						selected="selected" />
					<form:options items="${poss}" />
				</form:select></td>
		</tr>
		<tr>
			<td>Concept List</td>
			<td><form:select path="lists" name="lists">
					<form:option value="" label="Select concept list" />
					<form:option value="${selectedlistname}"
						label="${selectedlistname}" selected="selected" />
					<form:options items="${lists}" />
				</form:select></td>
		</tr>
		<tr>
			<td>Description</td>
			<td><input type="text" name="description" id="description"
				value="${description}"></td>
		</tr>
		<tr>
			<td>Synonyms</td>
			<td></td>
			<td><input type="button" name="synonym" id="addsynonym"
				value="Add Synonym"></td>
		</tr>

		<tr>
			<td>Concept Type</td>
			<td><form:select path="types" name="types">
					<form:option value="" label="Select one" />
					<form:option value="${selectedtypeid}" label="${selectedtypename}"
						selected="selected" />
					<form:options items="${types}" />
				</form:select></td>
		</tr>
		<tr>
			<td>Equals</td>
			<td><input type="text" name="equal" id="equal" value="${equal}"></td>
		</tr>
		<tr>
			<td>Similar</td>
			<td><input type="text" name="similar" id="similar"
				value="${similar}"></td>
		</tr>

	</table>

	<p />
	<h4>Do you want to proceed and delete this concept?</h4>
	<br />

	<table>
		<tr>
			<td><input type="submit" name="edit" id="edit"
				value="Store modified concept"></td>

			<td><a
				href="${pageContext.servletContext.contextPath}/auth/concepts/canceledit/${conceptList}"><input
					type="button" name="cancel" value="Cancel!"></a></td>
		</tr>
	</table>
</form>