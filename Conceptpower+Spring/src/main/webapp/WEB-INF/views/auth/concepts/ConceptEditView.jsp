<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<%@ page import="edu.asu.conceptpower.core.ConceptEntry"%>

<script>
$(function() {
	$("#synonymModal").dialog({
		autoOpen : false
	});
});

$(document).ready(function() {
    $("#searchconcept").click(function() {
        console.log("Search clicked");
        console.log($("name").val());
        $.ajax({
            type : "GET",
            url : "${pageContext.servletContext.contextPath}/conceptEdit/conceptSearch",
            data : {
                concept : $("#name").val(),
                pos : $("#pos").val()
            },
            success : function(response) {
                console.log(data);
                var data = jQuery.parseJSON(response);
            },
    
            error : function(httpStatus, response) {
               console.log("try again later");
            }
        });
    });
});


    $(function() {
        $("#addsynonym").click(function() {
        	var addedSynonymsTable = $('#addedSynonyms');
        	console.log(addedSynonymsTable);
			$('#synonymstable').dataTable().fnClearTable();
			$("#synonymModal").dialog('open');
			$("#synonymsDialogTable").show();
        });
    });
    $(document).ready(definedatatable);
    $(document).ready(definedatatableForSynonymAdd);
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
                            $('#synonymstable').dataTable().fnClearTable();
                            $("#synonymstable").show();
                            
	var addedsynonym = $('#synonymsids').val();
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
										},
										error : function(httpStatus, response) {
											if (httpStatus.status == 409) {
												var errorMessage = "<i class=\"fa fa-exclamation-triangle\">"
														+ httpStatus.responseText
														+ "</i>";
												$("#synonymModal").dialog(
														"close");
												$('#synonymModal').modal(
														'toggle');
												$('#errorMessage').show();
												$('#error_alert_msg').html(
														errorMessage);
											}
										}
									});
						});
	});
	var synonymAdd = function(idValue, wrd, desc) {
		$("#synonymModal").dialog("close");
		$('#synonymModal').modal('toggle');
		$("#synonymsDialogTable").hide();
		var eachSynonym = {
			Id : idValue,
			Description : desc,
			Word : wrd
		};
		//Adding synonymId to the existing list
		var list = document.getElementById("synonymsids").value;
		list += eachSynonym.Id + ',';
		$("#synonymsids").val(list);
		$("#addedSynonyms").show();
		$("#addedSynonymsTable").show();
		$('#addedSynonymsTable').dataTable().fnAddData(eachSynonym);
	}
	function definedatatableForSynonymAdd() {
		$('#addedSynonymsTable').dataTable(
				{
					"bJQueryUI" : true,
					"bAutoWidth" : false,
					"bStateSave" : true,
					"aoColumns" : [ {
						"sTitle" : "Remove",
						"mDataProp" : "Id"
					}, {
						"sTitle" : "Word",
						"mDataProp" : "Word",
					}, {
						"sTitle" : "Description",
						"mDataProp" : "Description",
					} ],
					"fnRowCallback" : function(nRow, aData, iDisplayIndex) {
						$('td:eq(0)', nRow).html(
								'<a onclick="synonymTemporaryRemove(\''
										+ aData.Id + '\')">Remove</a>');
						return nRow;
					},
					"fnCreatedRow" : function(nRow, aData, iDataIndex) {
						$(nRow).attr('id', aData.Id);
						$(nRow).attr('class', '');
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
						var conceptid = $('#conceptid').val();

						$
								.ajax({
									type : "GET",
									url : "${pageContext.servletContext.contextPath}/getConceptEditSynonyms",
									data : {
										conceptid : conceptid
									},
									success : function(response) {
										var border = response.length > 0 ? 1
												: 0;
										var synonym = JSON.parse(response);
										var total = synonym.Total;
										// var arr = new Array();
										for (var i = 0; i < total; i++) {
											$('#addedSynonymsTable')
													.dataTable()
													.fnAddData(
															synonym.synonyms[i]);
										}
										if (total > 0) {
											$("#addedSynonyms").show();
											$("#addedSynonymsTable").show();
										}
									}
								});
					});
	
	var synonymTemporaryRemove = function(synonymid) {
		var count = $('#addedSynonymsTable tr').length;
		var synonymTable = $("#addedSynonymsTable").dataTable();
		var tableRow = $('#' + synonymid);
		synonymTable.fnDeleteRow(synonymTable
				.fnGetPosition(tableRow.find('td')[0])[0]);
		//Removing synonymId from existing list
		var list = document.getElementById("synonymsids").value;
		document.getElementById("synonymsids").value = removeList(list,
				synonymid, ',');
	};
	var removeList = function(list, synonymid, separator) {
		separator = separator || ",";
		var values = list.split(separator);
		for (var i = 0; i < values.length; i++) {
			if (values[i] == synonymid) {
				values.splice(i, 1);
				return values.join(separator);
			}
		}
		return list;
	};
</script>


<form:form
	action="${pageContext.servletContext.contextPath}/auth/conceptlist/editconcept/edit/${conceptId}"
	method='post' modelAttribute="conceptEditBean">

	<h1>Edit concept</h1>

	<h2>${word}</h2>
	<p>${conceptEditBean.description}</p>
	<br />
	<br />
	<table>

		<tr>
			<td>Concept</td>
			<td><form:input path="word" class="form-control"/></td>
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
			<td><form:select path="conceptListValue" class="form-control"
					items="${conceptEditBean.conceptList}" itemValue="conceptListName"
					itemLabel="conceptListName" /></td>
		</tr>
		<tr>
		</tr>
		<tr>
			<td>Description</td>
			<td><form:textarea path="description" rows="7" cols="50" class="form-control"/></td>
		</tr>
		<tr>
			<td>Synonyms</td>
			<td>
				<div id="addedSynonyms" hidden="true">
					<table border="1" width="400" id="addedSynonymsTable" hidden="true" class="table table-striped table-bordered">
						<tbody>
						</tbody>
					</table>

				</div>

			</td>
			<td><form:hidden path="synonymsids"></form:hidden> <input
				type="button" name="synonym" id="addsynonym" data-toggle="modal"
				data-target="#synonymModal" value="Add Synonym" class="btn btn-primary"></td>
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
			<td><form:input  path="similar" class="form-control"/></td>
		</tr>
        <tr>
            <td>Wordnet</td>
            <td><form:input path="wordnetIds" class="form-control" /></td>
        </tr>
        
        
        
        <tr>
            <td>Create wrapper for wordnet concept:</td>
            <td><input type="text" id="name" /></td>
        </tr>
        <tr>
            <td>POS:</td>
            <td><select id="pos" class="form-control">
                    <option value="noun">Nouns</option>
                    <option value="verb">Verb</option>
                    <option value="adverb">Adverb</option>
                    <option value="adjective">Adjective</option>
                    <option value="other">Other</option>
            </select></td>
        </tr>
    </table>
    <input type="button" id="searchconcept" name="searchconcept" value="searchconcept" class="btn btn-primary">

    <c:if test="${not empty result}">
        <h2>2. Select Wordnet concept from search results</h2>
    
        <h4>The following concepts were found:</h4>
        Remember, you can only create a wrapper for concepts in Wordnet!
        <p></p>
        <table cellpadding="0" cellspacing="0"
            class="table table-striped table-bordered"
            id="conceptSearch">
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
                        <td align="justify"><font size="2">
                                <a id="${concept.entry.id}"
                                data-toggle="modal"
                                data-target="#detailsModal"
                                data-conceptid="${concept.entry.id}"><c:out
                                        value="${concept.entry.word}"></c:out></a>
                        </font></td>
                        <td align="justify"><c:out
                                value="${concept.entry.id}"></c:out></td>
                        <td align="justify"><c:out
                                value="${concept.entry.wordnetId}"></c:out></td>
                        <td align="justify"><font size="2"><c:out
                                    value="${concept.entry.pos}"></c:out></font></td>
                        <td align="justify"><font size="2"><c:out
                                    value="${concept.entry.conceptList}"></c:out></font></td>
                        <td align="justify"><font size="2">
                                ${concept.description}</font></td>
                        <td align="justify"><font size="2"><c:out
                                    value="${concept.type.typeName}"></c:out></font></td>
                        <td align="justify"><font size="2"><c:forEach
                                    var="syn"
                                    items="${concept.synonyms}">
                                    <c:out value="-> ${syn.word}"></c:out>
                                </c:forEach></font></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </c:if>

    <p />
    <div id="selectedconcepts"></div>
        
        

		<tr>
			<td><form:hidden path="conceptEntryList" /></td>
		</tr>
		<tr>
			<td><input type="text" name="conceptid" id="conceptid"
				hidden="true" value="${conceptId}"></td>
		</tr>
	</table>

	<table>
		<tr>
			<td><input type="submit" name="edit" id="edit"
				value="Store modified concept" class="btn btn-primary"></td>

			<td><c:if test="${conceptEditBean.fromHomeScreen eq true }">
					<a
						href="${pageContext.servletContext.contextPath}/auth/concepts/canceledit?fromHomeScreen=true"><input
						type="button" name="cancel" value="Cancel!" class="btn btn-primary"></a>
				</c:if> <c:if test="${conceptEditBean.fromHomeScreen eq false }">
					<a
						href="${pageContext.servletContext.contextPath}/auth/concepts/canceledit?fromHomeScreen=false"><input
						type="button" name="cancel" value="Cancel!" class="btn btn-primary"></a>
				</c:if></td>

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
										id="synonymsearch" value="Search" class="btn btn-primary"></td>
								</tr>
							</table>
						</div>


						<div class="modal-body synonym-modal-body">
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