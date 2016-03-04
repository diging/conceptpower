<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<script type="text/javascript">
	$(function() {
		$("#synonymModal").dialog({
			autoOpen : false
		});
		$("#addsynonym").click(function() {
			var addedSynonymsTable = $('#addedSynonyms');
			$('#synonymstable').dataTable().fnClearTable();
			$("#synonymModal").dialog('open');
			$("#synonymsDialogTable").show();
		});
	});
	$(document)
			.ready(
					function() {

						oTable = $('#conceptSearch')
								.dataTable(
										{
											"bJQueryUI" : true,
											"sPaginationType" : "full_numbers",
											"bAutoWidth" : false,

											"aoColumnDefs" : [ {
												"aTargets" : [ 6 ],
												"sType" : "html",
												"fnRender" : function(o, val) {
													return $("<div/>").html(
															o.aData[6]).text();
												}
											} ],

											//set row class to gradeX even if the row is selectable
											"fnRowCallback" : function(nRow,
													aData, iDisplayIndex,
													iDisplayIndexFull) {
												if (aData[2] == aData[3]) {
													$(nRow).removeClass('odd');
													$(nRow).removeClass('even');
													$(nRow).addClass(
															'gradeX even');
												} else {
													$(nRow).removeClass('odd');
													$(nRow).removeClass('even');
													$(nRow).addClass(
															'gradeX odd');
												}

												//alert($("#wrapperids").val());
												var wrapperconcepts = $(
														"#wrapperids").val()
														.split(',');
												var len = wrapperconcepts.length;

												for (var i = 0; i < len; i++) {
													if (aData[2] == wrapperconcepts[i].id)
														$(nRow).addClass(
																'row_selected');
												}

											}
										});

						//ajax call to add the concept selected for wrappping
						$('body')
								.delegate(
										'#conceptSearch tbody tr',
										"click",
										function() {
											console.log("Inside t body");
											var aData = oTable.fnGetData(this); // get datarow
											if (null != aData) // null if we clicked on title row
											{
												var word = aData[0];
												var conceptID = aData[1];
												var wordnetID = aData[2];
												var description = aData[5];
												if (conceptID === wordnetID) {
													console.log(conceptID);
													var wrapperids = '';
													if (!$(this).hasClass(
															'row_selected')) {
														wrapperids = $(
																"#wrapperids")
																.val();
														wrapperids += (conceptID + ',');
														$("#wrapperids").val(
																wrapperids);

														var html = '<div id="'+ wordnetID +'div">';
														html += '<h5>' + word
																+ ' ['
																+ wordnetID
																+ ']' + '</h5>';
														html += '<p>'
																+ description
																+ '</p></div>';

														$("#selectedconcepts")
																.append(html);
													} else {
														wrapperids = $(
																"#wrapperids")
																.val();
														wrapperids = wrapperids
																.replace(
																		wordnetID
																				+ ',',
																		'');
														$("#wrapperids").val(
																wrapperids);

														// remove the div element 
														var x = document
																.getElementById(wordnetID
																		+ 'div');
														x.parentNode
																.removeChild(x);

													}

													var wrapperconcepts = wrapperids
															.split(',');
													var len = wrapperconcepts.length;

													if (len > 1)
														$('#createwrapper')
																.prop(
																		'disabled',
																		false);
													else
														$('#createwrapper')
																.prop(
																		'disabled',
																		true);

													$(this).toggleClass(
															'row_selected');
												}
											}

										});

						$('#detailsModal')
								.on(
										'show.bs.modal',
										function(event) {
											console.log("Inside details Modal");
											var button = $(event.relatedTarget) // Button that triggered the modal
											var conceptid = button
													.data('conceptid') // Extract info from data-* attributes
											// If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
											// Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
											$
													.ajax({
														type : "GET",
														url : "${pageContext.servletContext.contextPath}/conceptDetail",
														data : {
															conceptid : conceptid
														},
														success : function(
																details) {
															$("#conceptTerm")
																	.text(
																			details.name);
															$("#detailsid")
																	.text(
																			details.id);
															$("#detailsuri")
																	.text(
																			details.uri);
															$(
																	"#detailswordnetid")
																	.text(
																			details.wordnetId);
															$("#detailspos")
																	.text(
																			details.pos);
															$(
																	"#detailsconceptlist")
																	.text(
																			details.conceptlist);
															$("#detailstypeid")
																	.text(
																			details.type);
															$("#detailsequalto")
																	.text(
																			details.equalto);
															$(
																	"#detailssimilarto")
																	.text(
																			details.similarto);
															$("#detailscreator")
																	.text(
																			details.creator);
														}
													});
											var modal = $(this)
											/*modal.find('.modal-title').text('New message to ' + recipient)
											modal.find('.modal-body input').val(recipient)*/
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
						description = description != null ? description
								.replace(/"/g, '&quot;') : "";
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
							var addedsynonym = $('#addedSynonnym').val();
							var synonymname = $("#synonymname").val();
							$
									.ajax({
										type : "GET",
										url : "${pageContext.servletContext.contextPath}/conceptAddSynonymView",
										data : {
											synonymname : synonymname,
											addedsynonym : addedsynonym
										},

										success : function(response) {
											var data = jQuery
													.parseJSON(response);
											$('#synonymstable').dataTable()
													.fnClearTable();
											$('#synonymstable').dataTable()
													.fnAddData(data);

										}
									});
						});
	});

	var synonymAdd = function(id, term, description) {
		$("#synonymModal").dialog("close");
		$('#synonymModal').modal('toggle');
		$("#synonymsDialogTable").hide();
		var decodedDescription = unescape(description);
		var x = document.getElementById('addedSynonymsTable');
		var addedsynonym = $('#addedSynonnym').val();

		if (x != null) {

			var new_row = x.rows[0].cloneNode(true);
			new_row.cells[0].innerHTML = '<a onclick="synonymRemove(\''
					+ x.rows.length + '\')">Remove</a>' + '</font></td>'
			new_row.cells[1].innerHTML = term;
			new_row.cells[2].innerHTML = decodedDescription;
			new_row.cells[3].innerHTML = id;
			new_row.cells[3].hidden = true;

			x.appendChild(new_row);

			addedsynonym += ','
			addedsynonym += id;
			$('#addedSynonnym').val(addedsynonym);
		} else {
			var html = '<table border="1" width="400" id="addedSynonymsTable"><thead></thead>';

			html += '<tr><td align="justify"><font size="2">'
					+ '<a onclick="synonymRemove(\'' + 0 + '\')">Remove</a>'
					+ '</font></td>';
			html += '<td align="justify"><font size="2">' + term
					+ '</font></td>';
			html += '<td align="justify"><font size="2">' + decodedDescription
					+ '</font></td>';
			html += '<td align="justify" hidden="true">' + id + '</td></tr>';

			html += '</table>';
			addedsynonym += id;
			$('#addedSynonnym').val(addedsynonym);
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
		$('#addedSynonnym').val('');
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
		$('#addedSynonnym').val(synonyms);
	};
</script>


<h1>Add new Wordnet concept wrapper</h1>
<p>Add a wrapper for a concept in Wordnet. Do that if you for
	example want to attach an "equals to" URI a concept that already exists
	in Wordnet.</p>

<c:if test="${not empty errormsg}">
	<font color="red">${errormsg}</font>
</c:if>

<c:if test="${not empty luceneError}">
	<font color="red">${luceneError}</font>
</c:if>

<h2>1. Search for Wordnet concept</h2>

<form
	action="${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptwrapper/conceptsearch"
	method='post'>
	<table class="greyContent">
		<tr>
			<td>Create wrapper for wordnet concept:</td>
			<td><input type="text" name="name" id="name"></td>
		</tr>
		<tr>
			<td>POS:</td>
			<td><select name="pos" class="form-control">
					<option value="noun">Nouns</option>
					<option value="verb">Verb</option>
					<option value="adverb">Adverb</option>
					<option value="adjective">Adjective</option>
					<option value="other">Other</option>
			</select></td>
		</tr>
	</table>
	<input type="submit" value="Search" class="button">
</form>

<form
	action="${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptwrapper/add"
	method='post'>
	<h2>2. Select Wordnet concept from search results</h2>

	<h4>The following concepts were found:</h4>
	Remember, you can only create a wrapper for concepts in Wordnet!
	<p></p>

	<c:if test="${not empty result}">
		<table cellpadding="0" cellspacing="0"
			class="table table-striped table-bordered" id="conceptSearch">
			<thead>
				<tr>
					<th>Term</th>
					<th>ID</th>
					<th>Wordnet ID</th>
					<th>POS</th>
					<th>Concept List</th>
					<th>Description</th>
					<th>Type</th>
					<th>Synonyms</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="concept" items="${result}">
					<tr title="${concept.uri}">
						<td align="justify">
						<font size="2"> <a
								id="${concept.entry.id}" data-toggle="modal"
								data-target="#detailsModal" data-conceptid="${concept.entry.id}"><c:out
										value="${concept.entry.word}"></c:out></a></font>
						</td>
						<td align="justify"><c:out value="${concept.entry.id}"></c:out></td>
						<td align="justify"><c:out value="${concept.entry.wordnetId}"></c:out></td>
						<td align="justify"><font size="2"><c:out
									value="${concept.entry.pos}"></c:out></font></td>
						<td align="justify"><font size="2"><c:out
									value="${concept.entry.conceptList}"></c:out></font></td>
						<td align="justify"><font size="2">
								${concept.description}</font></td>
						<td align="justify"><font size="2"><c:out
									value="${concept.type.typeName}"></c:out></font></td>
						<td align="justify"><font size="2"><c:forEach
									var="syn" items="${concept.synonyms}">
									<c:out value="-> ${syn.word}"></c:out>
								</c:forEach></font></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</c:if>

	<p />
	<div id="selectedconcepts"></div>

	<h2>3. Enter additional information</h2>
	<p>Selected Wordnet concept:</p>

	<table class="table table-striped table-bordered">

		<tr>
			<td>Concept List</td>

			<td><form:select path="lists" name="lists" class="form-control">
					<form:option value="" label="Select concept list" />
					<form:options items="${lists}" />
				</form:select></td>
			<td />

		</tr>
		<tr>
			<td>Description</td>
			<td><input type="text" name="description" id="description"></td>
			<td />
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
			<td><form:select path="types" name="types" class="form-control">
					<form:option value="" label="Select one" />
					<form:options items="${types}" />
				</form:select></td>
			<td />
		</tr>
		<tr>
			<td>Equals</td>
			<td><input type="text" name="equals" id="equals"></td>
			<td />
		</tr>
		<tr>
			<td>Similar to</td>
			<td><input type="text" name="similar" id="similar"></td>
			<td />
		</tr>
		<tr hidden="true">
			<td><input type="text" name="synonymsids" id="synonymsids"></td>
		</tr>
		<tr hidden="true">
			<td><input type="text" name="wrapperids" id="wrapperids"></td>
		</tr>

	</table>
	<br /> <input type="submit" disabled="disabled" id="createwrapper"
		value="Create Wordnet concept wrapper" class="button">
</form>

<form>

	<!-- Modal -->
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

<div class="modal fade" id="detailsModal" tabindex="-1" role="dialog"
	aria-labelledby="mySmallModalLabel">
	<div class="modal-dialog modal-md">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					Concept Details: <i id="conceptTerm"></i>
				</h4>
			</div>
			<div class="modal-body">
				<div class="row row-odd">
					<div class="col-sm-3">Id:</div>
					<div id="detailsid" class="col-sm-9"></div>
					</tr>
				</div>
				<div class="row row-even">
					<div class="col-sm-3">URI:</div>
					<div id="detailsuri" class="col-sm-9"></div>
				</div>
				<div class="row row-odd">
					<div class="col-sm-3">Wordnet Id:</div>
					<div id="detailswordnetid" class="col-sm-9"></div>
				</div>
				<div class="row row-even">
					<div class="col-sm-3">POS:</div>
					<div id="detailspos" class="col-sm-9"></div>
				</div>
				<div class="row row-odd">
					<div class="col-sm-3">Concept List:</div>
					<div id="detailsconceptlist" class="col-sm-9"></div>
				</div>
				<div class="row row-even">
					<div class="col-sm-3">Type:</div>
					<div id="detailstypeid" class="col-sm-9"></div>
				</div>
				<div class="row row-odd">
					<div class="col-sm-3">Equal to:</div>
					<div id="detailsequalto" class="col-sm-9"></div>
				</div>
				<div class="row row-even">
					<div class="col-sm-3">Similar to:</div>
					<div id="detailssimilarto" class="col-sm-9"></div>
				</div>
				<div class="row row-odd">
					<div class="col-sm-3">Creator:</div>
					<div id="detailscreator" class="col-sm-9"></div>
				</div>

			</div>
		</div>
	</div>
</div>








