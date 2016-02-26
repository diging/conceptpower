<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<h1>Delete List</h1>
<p>Do you really want to delete the following List?</p>

<table class="table table-striped table-bordered">
	<tr>
		<td>List Name:</td>
		<td>${listName}</td>
	</tr>
	<tr>
		<td>List Description:</td>
		<td>${description}</td>
	</tr>
</table>

<p />
<c:if test="${enabledelete}">
	<h4>Do you want to proceed and delete this list?</h4>
</c:if>

<c:if test="${not enabledelete}">
	<h4>You can not delete this list since some of the existing
		concepts are using this list !!</h4>
	<h4>Delete all the existing Concepts which are using this list to
		enable delete !!</h4>
</c:if>
<br />

<table>
	<tr>
		<c:if test="${enabledelete}">
			<td><a
				href="${pageContext.servletContext.contextPath}/auth/conceptlist/deleteconceptlistconfirm/${listName}"><input
					type="button" name="delete" id="delete" value="Yes, delete list"
					class="button"></a></td>
			<td><a
				href="${pageContext.servletContext.contextPath}/auth/conceptlist/canceldelete/"><input
					type="button" name="cancel" value="No, cancel!" class="button"></a></td>
		</c:if>
		<c:if test="${not enabledelete}">
			<td><a
				href="${pageContext.servletContext.contextPath}/auth/conceptlist/canceldelete/"><input
					type="button" name="cancel" value="Cancel!" class="button"></a></td>
		</c:if>

	</tr>
</table>