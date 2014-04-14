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
						var description = aData.description;
						description = description.replace(/"/g, '&quot;');
						$('td:eq(3)', nRow).html(
								'<a onclick="synonymAdd(\'' + aData.id
										+ '\',\'' + aData.word + '\',\''
										+ description + '\')">Add</a>');
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
													.fnClearTable();
											$('#synonymstable').dataTable()
													.fnAddData(response);

										}
									});
						});
	});

	var synonymAdd = function(id, term, description) {
		$("#dialog").dialog("close");
		$("#synonymsDialogTable").hide();

		var x = document.getElementById('addedSynonymsTable');

		if (x != null) {

			var new_row = x.rows[0].cloneNode(true);
			new_row.cells[0].innerHTML = '<a onclick="synonymRemove(\''
					+ x.rows.length + '\')">Remove</a>' + '</font></td>'
			new_row.cells[1].innerHTML = term;
			new_row.cells[2].innerHTML = description;
			new_row.cells[3].innerHTML = id;
			new_row.cells[3].hidden = true;

			x.appendChild(new_row);
		} else {
			var html = '<table border="1" width="400" id="addedSynonymsTable"><thead></thead>';

			html += '<tr><td align="justify"><font size="2">'
					+ '<a onclick="synonymRemove(\'' + 0 + '\')">Remove</a>'
					+ '</font></td>';
			html += '<td align="justify"><font size="2">' + term
					+ '</font></td>';
			html += '<td align="justify"><font size="2">' + description
					+ '</font></td>';
			html += '<td align="justify" hidden="true">' + id + '</td></tr>';

			html += '</table>';
			$("#addedSynonyms").html(html);

		}

		var synonyms = " ";
		var table = document.getElementById('addedSynonymsTable');
		for (var r = 0, n = table.rows.length; r < n; r++) {
			synonyms += table.rows[r].cells[3].innerHTML + ',';
		}
		$("#synonymsids").val(synonyms);
	};

	var synonymRemove = function(row) {
		var x = document.getElementById('addedSynonymsTable');
		x.deleteRow(row);
		if (!(x.rows.length > 0))
			x.parentNode.removeChild(x);
		var synonyms = " ";
		var table = document.getElementById('addedSynonymsTable');
		for (var r = 0, n = table.rows.length; r < n; r++) {
			synonyms += table.rows[r].cells[3].innerHTML + ',';
		}
		$("#synonymsids").val(synonyms);
	};

	//service 
	$(function() {
		$("#serviceSearch")
				.click(
						function() {
							var serviceterm = $("#serviceterm").val();
							var serviceid = $(
									"#serviceNameIdMap option:selected").val();

							$
									.ajax({
										type : "GET",
										url : "${pageContext.servletContext.contextPath}/serviceSearch",
										data : {
											serviceterm : serviceterm,
											serviceid : serviceid
										},
										success : function(response) {
											if (response.length > 0) {
												$('#serviceResultTable')
														.dataTable()
														.fnClearTable();
												$('#serviceResultTable')
														.dataTable().fnAddData(
																response);
												$('#showServiceResult').show();
											} else {
												$('#serviceResultTable')
														.dataTable()
														.fnClearTable();
												$('#showServiceResult').show();
											}
										}
									});
						});
	});

	$(function() {
		$("#showServiceResult").click(function() {
			$('#serviceResult').toggle();
			$('#serviceResultTable').toggle();
		});
	});

	$(document).ready(defineServicedatatable);

	function defineServicedatatable() {

		$('#serviceResultTable')
				.dataTable(
						{
							"bJQueryUI" : true,
							"sPaginationType" : "full_numbers",
							"bAutoWidth" : false,
							"aoColumns" : [ {
								"sTitle" : "Select",
								"mDataProp" : "isChecked",
							}, {
								"sTitle" : "Word",
								"mDataProp" : "word",
							}, {
								"sTitle" : "ID",
								"mDataProp" : "id",
							}, {
								"sTitle" : "Description",
								"mDataProp" : "description",
							} ],
							aoColumnDefs : [ {
								aTargets : [ 0 ],
								fnRender : function(o, v) {
									var check = '';
									if ($('#equals').val().indexOf(
											o.aData["id"]) != -1) {
										check = 'checked=\'checked\'';
									}
									return '<input type="checkbox" id="isChecked" '
											+ check
											+ ' name="isChecked" onclick="serviceConceptAdd(\''
											+ o.aData["id"] + '\')"></input>';
								},
							} ]
						});
	};

	var serviceConceptAdd = function(serviceConceptID) {
		var equals = $('#equals').val();
		if (equals == "")
			equals = serviceConceptID;
		else if (equals.indexOf(serviceConceptID) != -1) {
			equals = equals.replace(serviceConceptID + ',', '');
			equals = equals.replace(serviceConceptID, '');
		} else
			equals = equals + "," + serviceConceptID;

		$('#equals').val(equals);
	};

	var form = $('#createconceptform');
	$(document).on('submit', 'form', function() {

		var ret = true;
		if ($("#name").val() === "") {
			$("#nameerror").html('Please enter a name for concept !');
			ret = false;
		} else {
			$("#nameerror").html('');
		}
		if ($("#lists option:selected").text() === "") {
			$("#listerror").html('Please select a concpet list !');
			ret = false;
		} else {
			$("#listerror").html('');
		}
		if ($("#types option:selected").text() === "") {
			$("#typeerror").html('Please select a concept type !');
			ret = false;
		} else {
			$("#typeerror").html('');
		}

		return ret;
	});

	$(document).ready(showAjaxProcessing);

	function showAjaxProcessing() {
		var $loading = $('#loadingDiv').hide();
		$(document).ajaxStart(function() {
			$loading.show();
		}).ajaxStop(function() {
			$loading.hide();
		});
	}

	$(document).ready(hideFormProcessing);

	function hideFormProcessing() {
		$('#loadingDiv').hide();
	}

	function showFormProcessing() {
		$('#loadingDiv').show();
	}
</script>

<table style="padding: 0px;">
	<tr>
		<td><h1>Add new concept</h1></td>
	</tr>
	<tr>
		<td><div>
				Add a new concept here.
			</div></td>
	</tr>
</table>

<table>
	<tr>
		<td style="vertical-align: top;">

			<table>
				<tr>
					<td>Service</td>
					<td><form:select path="serviceNameIdMap"
							name="serviceNameIdMap" id="serviceNameIdMap">
							<form:options items="${serviceNameIdMap}" />
						</form:select></td>
					<td>Term</td>
					<td><input type="text" name="serviceterm" id="serviceterm"></td>
					<td><input type="submit" id="serviceSearch" value="search"
						class="button" /> <img alt="" id="loadingDiv" width="16px"
					height="16px"
					src="${pageContext.servletContext.contextPath}/resources/img/ajax_process_16x16.gif"
					class="none"></td>
				</tr>
			</table>
		</td>

	</tr>
</table>

<div style="padding: 15px;" id="showServiceResult" hidden="true">
	<a>Show Results</a>
</div>
<div id="serviceResult"
	style="max-width: 1000px; max-height: 500px; padding: 15px;"
	hidden="true">

	<table cellpadding="0" cellspacing="0" class="display dataTable"
		id="serviceResultTable" hidden="true">
		<tbody>
		</tbody>
	</table>

</div>

<table>
	<tr>
		<td><form
				action="${pageContext.servletContext.contextPath}/auth/conceptlist/addconcept/add"
				method='post' id="createconceptform">
				<table>
					<tr>
						<td>Concept &nbsp &nbsp &nbsp &nbsp</td>

						<td><input type="text" name="name" id="name"><b>
						</b><img alt=""
							src="${pageContext.servletContext.contextPath}/resources/img/warning.png"
							class="none" name="warning" id="warning" hidden="true"></td>
						<td><div id="nameerror" style="color: red"></div></td>
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

						<td><form:select path="lists" name="lists" id="lists">
								<form:options items="${lists}" />
							</form:select></td>
						<td><div id="listerror" style="color: red"></div></td>
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
							value="Add Synonym" class="button"></td>
					</tr>
					<tr>
						<td>Concept Type</td>
						<td><form:select path="types" name="types" id="types">
								<form:options items="${types}" />
							</form:select></td>
						<td><div id="typeerror" style="color: red"></div></td>
					</tr>
					<tr>
						<td>Equals</td>
						<td><textarea rows="5" cols="25" name="equals" id="equals"></textarea></td>
					</tr>
					<tr>
						<td>Similar to</td>
						<td><input type="text" name="similar" id="similar"></td>
					</tr>
					<tr>
						<td><input type="text" name="synonymsids" id="synonymsids"
							hidden="true"></td>
					</tr>
					<tr>
						<td colspan="2"><input type="submit" value="Add Concept"
							class="button" onclick="showFormProcessing()"
							onsubmit="hideFormProcessing()"></td>
					</tr>
				</table>

			</form></td>

	</tr>

</table>


<form>
	<div id="dialog" title="Search synonym">

		<table id="synonymsDialogTable" hidden="true">
			<tr>
				<td><input type="text" name="synonymname" id="synonymname"></td>
				<td><input type="button" name="synsearch" id="synonymsearch"
					value="Search" class="button"></td>
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

