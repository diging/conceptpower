<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<script>
	$(function() {

		$("#addsynonym").click(function() {
			$("#dialog").dialog({
				width : 'auto'
			});
			$("#synonymsDialogTable").show();
		});

		$("#name")
				.on(
						"change paste",
						function() {
							$
									.ajax({
										type : "GET",
										url : "${pageContext.servletContext.contextPath}/getExistingConcepts",
										data : {
											conceptname : $(this).val()
										},
										success : function(response) {
											if (response.length > 0) {
												var text = "Following concepts have the same name : \n";
												var len = response.length;
												for (var i = 0; i < len; i++) {
													text += (response[i].word
															+ " - "
															+ response[i].id + "\n");
												}
												$("#warning").show();
												$('#warning').prop('title',
														text);
											} else {
												$("#warning").hide();
												$('#warning').prop('title', '');
											}
										}
									});
						});
	});

	$(document).ready(definedatatable);

	function definedatatable() {
		$('#synonymstable').dataTable(
				{
					"bJQueryUI" : true,
					"sPaginationType" : "full_numbers",
					"bAutoWidth" : false,
					"bStateSave" : true,
					"aoColumns" : [ {
						"sTitle" : "Term",
						"mDataProp" : "word",
					}, {
						"sTitle" : "POS",
						"mDataProp" : "pos",
					}, {
						"sTitle" : "Description",
						"mDataProp" : "description",
					}, {
						"sTitle" : "Add",
						"mDataProp" : "id"
					} ],
					"fnRowCallback" : function(nRow, aData, iDisplayIndex) {
						$('td:eq(3)', nRow).html(
								'<a onclick="synonymAdd(\'' + aData.id
										+ '\')">Add</a>');
						return nRow;
					}
				});
	};

	$(function() {
		$("#synonymsearch")
				.click(
						function() {
							$("#synonymViewDiv").show();
							$("#synonymstable").show();
							var synonymname = $("#synonymname").val();
							$
									.ajax({
										type : "GET",
										url : "${pageContext.servletContext.contextPath}/conceptAddSynonymView",
										data : {
											synonymname : synonymname
										},
										success : function(response) {
											$('#synonymstable').dataTable()
													.fnAddData(response);
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
					url : "${pageContext.servletContext.contextPath}/conceptAddAddSynonym",
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
					url : "${pageContext.servletContext.contextPath}/conceptAddRemoveSynonym",
					data : {
						synonymid : synonymid
					},
					success : function(response) {
						var border = response.length > 0 ? 1 : 0;
						var html = '<table border="'+ border +'" width="400" id="addedSynonymsTable"><thead></thead><tbody>';
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
</script>

<h1>Add new concept</h1>
<p>Add a new concept here.</p>

<form
	action="${pageContext.servletContext.contextPath}/auth/conceptlist/addconcept/add"
	method='post'>
	<table>
		<tr>
			<td>Concept</td>

			<td><input type="text" name="name" id="name"><b> </b><img
				alt=""
				src="${pageContext.servletContext.contextPath}/resources/img/warning.png"
				class="none" name="warning" id="warning" hidden="true"></td>
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

			<td><form:select path="lists" name="lists">
					<form:option value="" label="Select concept list" />
					<form:options items="${lists}" />
				</form:select></td>

		</tr>
		<tr>
			<td>Description</td>
			<td><textarea rows="7" cols="50" name="description"
					id="description"></textarea></td>
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
					<form:options items="${types}" />
				</form:select></td>
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
<form>
	<div id="dialog" title="Search synonym">

		<table id="synonymsDialogTable" hidden="true">
			<tr>
				<td><input type="text" name="synonymname" id="synonymname"></td>
				<td><input type="button" name="synsearch" id="synonymsearch"
					value="Search"></td>
			</tr>
		</table>

		<div id="synonymViewDiv" style="max-width: 1000px; max-height: 500px;"
			hidden="true">

			<table cellpadding="0" cellspacing="0" class="display dataTable"
				id="synonymstable" hidden="true">
				<tbody>
				</tbody>
			</table>

		</div>

	</div>

</form>

