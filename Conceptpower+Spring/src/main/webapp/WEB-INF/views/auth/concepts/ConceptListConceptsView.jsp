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
	});
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

<h1>Concept list</h1>
<p>Here you find all stored concept list in the selected concept
	list.</p>

<h2>Concepts</h2>
<c:if test="${not empty result}">
	<table cellpadding="0" cellspacing="0" class="display dataTable"
		id="conceptList">
		<thead>
			<tr>
				<th></th>
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
					<td align="justify"><font size="2"><a
							onclick="detailsView(this);" id="${concept.entry.id}">Details</a></font></td>
					<td align="justify"><a
						href="${pageContext.servletContext.contextPath}/auth/conceptlist/deleteconcept/${concept.entry.id}"><input
							type="image"
							src="${pageContext.servletContext.contextPath}/resources/img/trash_16x16.png"></input></a>
					</td>
					<td align="justify"><a
						href="${pageContext.servletContext.contextPath}/auth/conceptlist/editconcept/${concept.entry.id}"><input
							type="image"
							src="${pageContext.servletContext.contextPath}/resources/img/edit_16x16.png"></input></a>
					</td>
					<td align="justify"><font size="2"><c:out
								value="${concept.entry.word}"></c:out></font></td>
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
					<td align="justify"><font size="2"><c:out
								value="${concept.creator.username}"></c:out></font></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:if>


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