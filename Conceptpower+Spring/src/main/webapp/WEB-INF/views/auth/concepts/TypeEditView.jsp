<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>


<h1>Edit concept type</h1>
<p>Edit concept type here.</p>

<script>
	$(function() {

		$("#editButton").prop("disabled", "disabled");

		$("#typeName").on("keyup", function() {
			if ($(this).val() != "") {
				$("#editButton").prop("disabled", false);
			} else {
				$("#editButton").prop("disabled", "disabled");
			}
		});

		$("#typeDescription").on("keyup", function() {
			if ($(this).val() != "") {
				$("#editButton").prop("disabled", false);
			} else {
				$("#editButton").prop("disabled", "disabled");
			}
		});

		$("#matches").on("keyup", function() {
			if ($(this).val() != "") {
				$("#editButton").prop("disabled", false);
			} else {
				$("#editButton").prop("disabled", "disabled");
			}
		});

		$("#superType").on("keyup", function() {
				$("#editButton").prop("disabled", false);
		});
	});
</script>

<form:form
	action="${pageContext.servletContext.contextPath}/auth/concepttype/storeedittype"
	commandName="conceptTypeAddForm" method='post'>
	<table>
		<tr>
			<td>Type Name</td>
			<td><form:input path="typeName" class="form-control"/> <form:hidden path="typeid" />
				<form:hidden path="oldTypeName" /></td>
			<td><form:errors path="typeName" class="ui-state-error-text" /></td>
		</tr>

		<tr>
			<td>Type Description</td>
			<td><form:textarea rows="7" cols="50" path="typeDescription"></form:textarea></td>
			<td><form:errors path="typeDescription"
					class="ui-state-error-text" /></td>
		</tr>

		<tr>
			<td>Matches</td>
			<td><form:input path="matches" class="form-control"/></td>
		</tr>

		<tr>
			<td>Super Type</td>
			<td><form:select path="superType">
					<form:option value="" label="Select one" />
					<c:forEach items="${conceptTypeAddForm.types}" var="typesDropDown">
						<form:option value="${typesDropDown.value}">${typesDropDown.value}</form:option>
					</c:forEach>
				</form:select></td>
		</tr>

		<tr>
			<td><input type="submit" value="Edit type" id="editButton"
				name="editButton" class="btn btn-primary" disabled="disabled"></td>
			<td><a
				href="${pageContext.servletContext.contextPath}/auth/concepttype/edittype/canceledit"><input
					type="button" name="cancel" value="No, cancel!" class="btn btn-primary"></a></td>
		</tr>
	</table>
</form:form>