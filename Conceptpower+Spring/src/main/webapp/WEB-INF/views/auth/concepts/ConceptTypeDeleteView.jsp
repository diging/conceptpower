<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<h1>Delete Type</h1>
<p>Do you really want to delete the following Type?</p>
<h2>${typeName}</h2>
<p>${description}</p>

<table class="table table-striped table-bordered">
	<tr>
		<td>Type Name:</td>
		<td>${typeName}</td>
	</tr>
	<tr>
		<td>Type Description:</td>
		<td>${description}</td>
	</tr>
	<tr>
		<td>Matches:</td>
		<td>${matches}</td>
	</tr>
	<tr>
		<td>Super Type:</td>
		<td>${supertype}</td>
	</tr>
</table>

<p />
<c:if test="${enabledelete}">
	<h4>Do you want to proceed and delete this type?</h4>
</c:if>

<c:if test="${not enabledelete}">
	<h4>You cannot delete this type since some concepts are using this type.</h4>
	<h4>Delete all existing concepts that use this type to enable delete.</h4>
</c:if>
<br />

<table>
	<tr>
		<c:if test="${enabledelete}">
			<td><a
				href="${pageContext.servletContext.contextPath}/auth/concepttype/deleteconcepttypeconfirm/${typeid}"><input
					type="button" name="delete" id="delete" value="Yes, delete type"
					class="button"></a></td>
			<td><a
				href="${pageContext.servletContext.contextPath}/auth/concepttype/canceldelete/"><input
					type="button" name="cancel" value="No, cancel!" class="button"></a></td>
		</c:if>
		<c:if test="${not enabledelete}">
			<td><a
				href="${pageContext.servletContext.contextPath}/auth/concepttype/canceldelete/"><input
					type="button" name="cancel" value="Cancel!" class="button"></a></td>
		</c:if>

	</tr>
</table>