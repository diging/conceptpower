<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('#conceptList').dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false,
			"aoColumnDefs" : [ {
				"aTargets" : [ 8 ],
				"sType" : "html",
				"fnRender" : function(o, val) {
					return $("<div/>").html(o.aData[8]).text();
				}
			} ],
		});
	
	
	$('#detailsdiv').on('show.bs.modal', function(event) {
		var button = $(event.relatedTarget) // Button that triggered the modal
		var conceptid = button
				.data('conceptid') // Extract info from data-* attributes
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
			}
		});
			var modal = $(this)
		
	});
	
	});
</script>

<h1>Concept list</h1>
<p>Here you find all stored concept list in the selected concept
	list.</p>
	
	<font color="red">${luceneError }</font>
	<font color="red">${IndexerStatus}</font>

<h2>Concepts</h2>
<c:if test="${not empty result}">
	<table cellpadding="0" cellspacing="0"
		class="table table-striped table-bordered" id="conceptList">
		<thead>
			<tr>
				<th></th>
				<th></th>
				<th>Term</th>
				<th>ID</th>
				<th>Wordnet ID</th>
				<th>POS</th>
				<th>Concept List</th>
				<th>Description</th>
				<th>Type</th>
				<th>Synonyms</th>
				<th>Created By</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="concept" items="${result}">
				<tr class="gradeX">
					<td align="justify" width="20"><a
						href="${pageContext.servletContext.contextPath}/auth/conceptlist/deleteconcept/${concept.entry.id}"><i
							class="fa fa-trash-o"></i> </a></td>
					<td align="justify" width="20"><a
						href="${pageContext.servletContext.contextPath}/auth/conceptlist/editconcept/${concept.entry.id}"><i
							class="fa fa-pencil-square-o"></i></a></td>
					<td align="justify"><font size="2"> <a
							id="${concept.entry.id}" data-toggle="modal"
							data-target="#detailsdiv" data-conceptid="${concept.entry.id}"><c:out
									value="${concept.entry.word}"></c:out></a>
					</font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.entry.id}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.entry.wordnetId}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.entry.pos}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.entry.conceptList}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.description}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.type.typeName}"></c:out></font></td>
					<td align="justify"><font size="2"><c:forEach var="syn"
								items="${concept.synonyms}">
								<c:out value="-> ${syn.word}"></c:out>
							</c:forEach></font></td>
					<td align="justify">
						<c:out value="${concept.creator.username}"></c:out>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:if>


<div class="modal fade" id="detailsdiv" tabindex="-1" role="dialog"
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