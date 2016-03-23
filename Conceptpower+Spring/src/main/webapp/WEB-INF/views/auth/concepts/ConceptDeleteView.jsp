<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<h1>Delete concept</h1>
<p>Do you really want to delete the following concept?</p>

<font color="red">${luceneError }</font>
<h2>${word}</h2>
<p>${description}</p>
<br />
<br />
<table  class="table table-striped table-bordered">
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
			href="${pageContext.servletContext.contextPath}/auth/conceptlist/deleteconceptconfirm/${conceptId}?fromHomeScreenDelete=${fromHomeScreenDelete}"><input
				type="button" name="delete" id="delete" value="Yes, delete concept!" class="btn btn-primary"></a></td>
	
		<c:if test="${fromHomeScreenDelete eq 'false'}">
		<td><a
			href="${pageContext.servletContext.contextPath}/auth/concepts/canceldelete/${conceptList}"><input
				type="button" name="cancel" value="No, cancel!" class="btn btn-primary"></a></td>
		</c:if>
		
		<c:if test="${fromHomeScreenDelete eq 'true'}">
		<td><a
			href="${pageContext.servletContext.contextPath}/login"><input
				type="button" name="cancel" value="No, cancel!" class="btn btn-primary"></a></td>
		</c:if>
		
	</tr>
</table>