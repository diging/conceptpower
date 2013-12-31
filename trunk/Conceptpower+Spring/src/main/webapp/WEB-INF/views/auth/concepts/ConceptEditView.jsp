<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<script>
	$(function() {
		$("#addsynonym").click(function() {
			$("#dialog").dialog();
			$("#synonymsDialogTable").show();
		});

	});

	$(document).ready(function() {
		$("#synonymstable").dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false
		});

	});

	$(function() {
		$("#synonymsearch")
				.click(
						function() {
							var synonymname = $("#synonymname").val();
							$
									.ajax({
										type : "GET",
										url : "${pageContext.servletContext.contextPath}/conceptEditSynonymView",
										data : {
											synonymname : synonymname
										},
										success : function(response) {

											var html = '<table cellpadding="0" cellspacing="0" class="display dataTable" id="synonymstable"><thead><tr><th>Term</th><th>POS</th><th>Description</th><th><th/></tr></thead><tbody>';
											var len = response.length;
											for (var i = 0; i < len; i++) {
												html += '<tr class="gradeX"><td align="justify"><font size="2">'
														+ response[i].word
														+ '</font></td>';
												html += '<td align="justify"><font size="2">'
														+ response[i].pos
														+ '</font></td>';
												html += '<td align="justify"><font size="2">'
														+ response[i].description
														+ '</font></td>';
												html += '<td align="justify"><font size="2">'
														+ '<a onclick="synonymAdd(\''
														+ response[i].id
														+ '\')">Add</a>'
														+ '</font></td></tr>';
											}
											html += '</tbody></table>';
											$("#synonymViewDiv").html(html);

											$("#synonymstable")
													.dataTable(
															{
																"bJQueryUI" : true,
																"sPaginationType" : "full_numbers",
																"bAutoWidth" : false
															});

										}
									});
						});
	});

	var synonymAdd = function(synonymid) {
		$("#dialog").dialog("close");
		$("#synonymsDialogTable").hide();
		$
				.ajax({
					type : "GET",
					url : "${pageContext.servletContext.contextPath}/conceptEditAddSynonym",
					data : {
						synonymid : synonymid
					},
					success : function(response) {

						var html = '<table border="1" width="400" id="addedSynonymsTable"><thead></thead><tbody>';
						var len = response.length;
						for (var i = 0; i < len; i++) {
							html += '<tr><td align="justify"><font size="2">'
									+ '<a onclick="synonymRemove(\''
									+ response[i].id + '\')">Remove</a>'
									+ '</font></td>';
							html += '<td align="justify"><font size="2">'
									+ response[i].word + '</font></td>';
							html += '<td align="justify"><font size="2">'
									+ response[i].description
									+ '</font></td></tr>';
						}
						html += '</tbody></table>';
						$("#addedSynonyms").html(html);

					}
				});
	};

	var synonymRemove = function(synonymid) {
		$
				.ajax({
					type : "GET",
					url : "${pageContext.servletContext.contextPath}/conceptEditRemoveSynonym",
					data : {
						synonymid : synonymid
					},
					success : function(response) {

						var html = '<table border="1" width="400" id="addedSynonymsTable"><thead></thead><tbody>';
						var len = response.length;
						for (var i = 0; i < len; i++) {
							html += '<tr><td align="justify"><font size="2">'
									+ '<a onclick="synonymRemove(\''
									+ response[i].id + '\')">Remove</a>'
									+ '</font></td>';
							html += '<td align="justify"><font size="2">'
									+ response[i].word + '</font></td>';
							html += '<td align="justify"><font size="2">'
									+ response[i].description
									+ '</font></td></tr>';
						}
						html += '</tbody></table>';
						$("#addedSynonyms").html(html);

					}
				});
	};

	$(document)
			.ready(
					function() {
						$('#synonyms').dataTable({
							"bJQueryUI" : true,
							"sPaginationType" : "full_numbers",
							"bAutoWidth" : false
						});

						$
								.ajax({
									type : "GET",
									url : "${pageContext.servletContext.contextPath}/getConceptEditSynonyms",
									success : function(response) {

										var html = '<table border="1" width="400" id="addedSynonymsTable"><thead></thead><tbody>';
										var len = response.length;
										for (var i = 0; i < len; i++) {
											html += '<tr><td align="justify"><font size="2">'
													+ '<a onclick="synonymRemove(\''
													+ response[i].id
													+ '\')">Remove</a>'
													+ '</font></td>';
											html += '<td align="justify"><font size="2">'
													+ response[i].word
													+ '</font></td>';
											html += '<td align="justify"><font size="2">'
													+ response[i].description
													+ '</font></td></tr>';
										}
										html += '</tbody></table>';
										$("#addedSynonyms").html(html);

									}
								});
					});
</script>


<form
	action="${pageContext.servletContext.contextPath}/auth/conceptlist/editconcept/edit/${id}"
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
			<td><div id="addedSynonyms"></div></td>
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

<form>
	<div id="dialog" title="Search synonym">

		<table id="synonymsDialogTable" hidden="true">
			<tr>
				<td><input type="text" name="synonymname" id="synonymname"></td>
				<td><input type="button" name="synsearch" id="synonymsearch"
					value="Search"></td>
			</tr>
		</table>
		<div id="synonymViewDiv"
			style="max-width: 1000px; max-height: 500px; width: 100%;"></div>

	</div>

</form>