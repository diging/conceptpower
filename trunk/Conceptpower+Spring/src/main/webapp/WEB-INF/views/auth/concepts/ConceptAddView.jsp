<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<h1>Add new concept</h1>
<p>Add a new concept here.</p>

<form
	action="${pageContext.servletContext.contextPath}/auth/concepts/ConceptAddView"
	method='post'>
	<table>
		<tr>
			<td>Concept</td>
			<td><input type="text" name="name" id="name"></td>
		</tr>
		<tr>
			<td>POS</td>
			<td><select name="pos">
					<option value="noun">Nouns</option>
					<option value="verb">Verb</option>
					<option value="adverb">Adverb</option>
					<option value="adjective">Adjective</option>
					<option value="other">Other</option>
			</select></td>
		</tr>
		<tr>
			<td>Concept List</td>
			<td><select name="conceptList">
			</select></td>
		</tr>
		<tr>
			<td>Description</td>
			<td><input type="text" name="description" id="description"></td>
		</tr>
		<tr>
			<td>Synonyms</td>
			<td></td>
			<td><input type="submit" value="Search"></td>
		</tr>
		<tr>
			<td>Concept Type</td>
			<td><select name="conceptType">
			</select></td>
		</tr>
		<tr>
			<td>Equals</td>
			<td><input type="text" name="equals" id="equals"></td>
		</tr>
		<tr>
			<td>Similar to</td>
			<td><input type="text" name="similar" id="similar"></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Add Concept"></td>
		</tr>
	</table>
</form>