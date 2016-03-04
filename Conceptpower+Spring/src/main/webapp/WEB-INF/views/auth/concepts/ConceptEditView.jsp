<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<%@ page import="edu.asu.conceptpower.servlet.core.ConceptEntry"%>

<script>

	$(function() {
		$("#synonymModal").dialog({
			autoOpen : false
		});
	});
	
	
</script>


<form:form
	action="${pageContext.servletContext.contextPath}/auth/conceptlist/editconcept/edit/${conceptId}"
	method='post' modelAttribute="conceptEditBean">

	<h1>Edit concept</h1>
	
	<form:errors path="luceneError" />

	<h2>${word}</h2>
	<p>${conceptEditBean.description}</p>
	<br />
	<br />
	<table>
	
		<tr>
			<td>Concept</td>
			<td><form:input path="word" /></td>
		</tr>
		<tr>
			<td>POS</td>
			<td><form:select path="selectedPosValue" class="form-control">
					<form:option value="" />
					<form:options items="${conceptEditBean.posMap}" />
				</form:select></td>
		</tr>
		<tr>
			<td>Concept List</td>
			<td><form:select path="conceptListValue"
					items="${conceptEditBean.conceptList}" itemValue="conceptListName"
					itemLabel="conceptListName"  class="form-control"/></td>
		</tr>
		<tr>
		</tr>
		<tr>
			<td>Description</td>
			<td><form:textarea path="description" rows="7" cols="50" /></td>
		</tr>
		<tr>
			<td>Synonyms</td>
			<td><div id="addedSynonyms"></div></td>
			<td><input type="button" name="synonym" id="addsynonym"
				data-toggle="modal" data-target="#synonymModal" value="Add Synonym"
				class="button"></td>
		</tr>

		<tr>
			<td>Concept Type</td>
			<td><form:select path="selectedTypeId" class="form-control">
					<form:option value="" />
					<form:options items="${conceptEditBean.types}" itemValue="typeId"
						itemLabel="typeName" />
				</form:select>
		</tr>
		<tr>
			<td>Equals</td>
			<td><form:textarea path="equals" rows="5" cols="25" />
		</tr>

		<tr>
			<td>Similar</td>
			<td><form:input path="similar" /></td>
		</tr>
		<tr>
			<td> <form:hidden path="conceptEntryList"/></td>
		</tr>
		<tr>
			<td><input type="text" name="conceptid" id="conceptid"
				hidden="true" value="${conceptId}"></td>
		</tr>
	</table>

	<table>
		<tr>
			<td><input type="submit" name="edit" id="edit"
				value="Store modified concept" class="button"></td>

			<td>
			
			<c:if test="${conceptEditBean.fromHomeScreen eq true }">
			<a
				href="${pageContext.servletContext.contextPath}/auth/concepts/canceledit?fromHomeScreen=true"><input
					type="button" name="cancel" value="Cancel!" class="button"></a>
			</c:if>
					
			<c:if test="${conceptEditBean.fromHomeScreen eq false }">
				<a
				href="${pageContext.servletContext.contextPath}/auth/concepts/canceledit?fromHomeScreen=false"><input
					type="button" name="cancel" value="Cancel!" class="button"></a>
			</c:if>
			</td>
		
		</tr>
	</table>
	<form:hidden path="fromHomeScreen" />
</form:form>

<form>
	<div class="modal fade" role="dialog" id="synonymModal">


		<div class="modal-dialog" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">

			<div class="vertical-alignment-helper">
				<div class="modal-dialog vertical-align-center">
					<div class="modal-content">

						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">&times;</button>
							<h4 class="modal-title">Search synonym</h4>
							<table id="synonymsDialogTable" hidden="true">
								<tr>
									<td><input type="text" name="synonymname" id="synonymname"></td>
									<td><input type="hidden" name="addedSynonnym"
										id="addedSynonnym" /></td>
									<td><input type="button" name="synsearch"
										id="synonymsearch" value="Search" class="button"></td>
								</tr>
							</table>
						</div>


						<div class="modal-body">
							<div id="synonymViewDiv"
								style="max-width: 1000px; max-height: 500px;" hidden="true">

								<table cellpadding="0" cellspacing="0" id="synonymstable"
									hidden="true" class="table table-striped table-bordered">
									<tbody>
									</tbody>
								</table>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>


</form>