<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<script type="text/javascript">
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

						//ajax call to get already selected concepts for wrapping 
						$
								.ajax({
									type : "GET",
									url : "${pageContext.servletContext.contextPath}/getSelectedConceptsFroWrapping",
									success : function(response) {

										var html = "";
										var len = response.length;

										if (len > 0)
											$('#createwrapper').prop(
													'disabled', false);
										else
											$('#createwrapper').prop(
													'disabled', true);

										for (var i = 0; i < len; i++) {
											html += '<h5>' + response[i].word
													+ ' ['
													+ response[i].wordnetId
													+ ']' + '</h5>';
											html += '<p>'
													+ response[i].description
													+ '</p>';
										}
										html += '</p>';
										$("#selectedconcepts").html(html);
									}
								});

						//ajax call to add the concept selected for wrappping
						$('body')
								.delegate(
										'#conceptSearch tbody tr',
										"click",
										function() {

											var aData = oTable.fnGetData(this); // get datarow
											if (null != aData) // null if we clicked on title row
											{
												var word = aData[1];
												var conceptID = aData[2];
												var wordnetID = aData[3];
												var description = aData[6];
												if (conceptID === wordnetID) {

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
					});

	$(function() {
		$("#addsynonym").click(function() {
			$("#dialog").dialog({
				width : "auto"
			});
			$("#synonymsDialogTable").show();
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
										url : "${pageContext.servletContext.contextPath}/conceptWrapperAddSynonymView",
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

	function detailsView(concept) {
		var conceptid = concept.id;
		$.ajax({
			type : "GET",
			url : "${pageContext.servletContext.contextPath}/conceptDetail",
			data : {
				conceptid : conceptid
			},
			success : function(details) {
				$("#detailsid").text(details.id);
				$("#detailsuri").text(details.uri);
				$("#detailswordnetid").text(details.wordnetId);
				$("#detailspos").text(details.pos);
				$("#detailsconceptlist").text(details.conceptlist);
				$("#detailstypeid").text(details.type);
				$("#detailsequalto").text(details.equalto);
				$("#detailssimilarto").text(details.similarto);
				$("#detailscreator").text(details.creator);

				$("#detailsdiv").dialog({
					title : details.name,
					width : 'auto'
				});
				$("#detailstable").show();
			}
		});
	}
</script>


<h1>Add new Wordnet concept wrapper</h1>
<p>Add a wrapper for a concept in Wordnet. Do that if you for
	example want to attach an "equals to" URI a concept that already exists
	in Wordnet.</p>

<c:if test="${not empty errormsg}">
	<p style="text-color: red;">${errormsg}</p>
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
			<td><select name="pos">
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
		<table cellpadding="0" cellspacing="0" class="display dataTable"
			id="conceptSearch">
			<thead>
				<tr>
					<th></th>
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
					<tr>
						<td align="justify"><font size="2"><a
								onclick="detailsView(this);" id="${concept.entry.id}">Details</a></font></td>
						<td align="justify"><font size="2"><c:out
									value="${concept.entry.word}"></c:out></font></td>
						<td align="justify"><c:out value="${concept.entry.id}"></c:out></td>
						<td align="justify"><c:out value="${concept.entry.wordnetId}"></c:out></td>
						<td align="justify"><font size="2"><c:out
									value="${concept.entry.pos}"></c:out></font></td>
						<td align="justify"><font size="2"><c:out
									value="${concept.entry.conceptList}"></c:out></font></td>
						<td align="justify"><font size="2"><c:out
									value="${concept.description}"></c:out></font></td>
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

	<table class="greyContent">

		<tr>
			<td>Concept List</td>

			<td><form:select path="lists" name="lists">
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
				value="Add Synonym" class="button"></td>
		</tr>
		<tr>
			<td>Concept Type</td>
			<td><form:select path="types" name="types">
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

	<div id="detailsdiv" style="max-width: 600px; max-height: 500px;">
		<table id="detailstable" class="greyContent" hidden="true">
			<tr>
				<td>Id:</td>
				<td id="detailsid"></td>
			</tr>
			<tr>
				<td>URI:</td>
				<td id="detailsuri"></td>
			</tr>
			<tr>
				<td>Wordnet Id:</td>
				<td id="detailswordnetid"></td>
			</tr>
			<tr>
				<td>POS:</td>
				<td id="detailspos"></td>
			</tr>
			<tr>
				<td>Concept List:</td>
				<td id="detailsconceptlist"></td>
			</tr>
			<tr>
				<td>Type:</td>
				<td id="detailstypeid"></td>
			</tr>
			<tr>
				<td>Equal to:</td>
				<td id="detailsequalto"></td>
			</tr>
			<tr>
				<td>Similar to:</td>
				<td id="detailssimilarto"></td>
			</tr>
			<tr>
				<td>Creator:</td>
				<td id="detailscreator"></td>
			</tr>
		</table>
	</div>

</form>









