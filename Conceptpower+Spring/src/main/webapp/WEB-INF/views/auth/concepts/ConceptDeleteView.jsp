<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<h1>Delete concept</h1>
<p>Do you really want to delete the following concept?</p>


<h2>${word}</h2>
<p>${description}</p>
<br />
<br />
<table class="greyContent">
	<tr>
		<td>Id:</td>
		<td>${conceptId}</td>
	</tr>
	<tr>
		<td>Wordnet Id:</td>
		<td>${wordnetId}</td>
	</tr>
	<tr>
		<td>POS:</td>
		<td>${pos}</td>
	</tr>
	<tr>
		<td>Concept List:</td>
		<td>${conceptList}</td>
	</tr>
	<tr>
		<td>Type:</td>
		<td>${type}</td>
	</tr>
	<tr>
		<td>Synonyms:</td>
		<td>${synonyms}</td>
	</tr>
	<tr>
		<td>Equal to:</td>
		<td>${equal}</td>
	</tr>
	<tr>
		<td>Similar to:</td>
		<td>${similar}</td>
	</tr>
	<tr>
		<td>Creator:</td>
		<td>${user}</td>
	</tr>
	<tr>
		<td>Modified:</td>
		<td>${modified}</td>
	</tr>
</table>

<p />
<h4>Do you want to proceed and delete this concept?</h4>
<br />

<table>
	<tr>
		<td><a
			href="${pageContext.servletContext.contextPath}/auth/conceptlist/deleteconceptconfirm/${conceptId}"><input
				type="button" name="delete" id="delete" value="Yes, delete concept!" class="button"></a></td>

		<td><a
			href="${pageContext.servletContext.contextPath}/auth/concepts/canceldelete/${conceptList}"><input
				type="button" name="cancel" value="No, cancel!" class="button"></a></td>
	</tr>
</table>