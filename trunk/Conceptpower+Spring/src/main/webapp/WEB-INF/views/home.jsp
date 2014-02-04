<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('#conceptSearch').dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false
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

				$("#detailsdiv").dialog();
				$("#detailstable").show();
			}
		});
	}
</script>


<h1>Welcome to Conceptpower</h1>

<sec:authorize access="isAuthenticated()">You're logged in!
</sec:authorize>

<p>This is Conceptpower, the concept management site for Quadriga.</p>

<form
	action="${pageContext.servletContext.contextPath}/home/conceptsearch"
	method='post'>
	<table>
		<tr>
			<td>Word:</td>
			<td><input type="text" name="name" id="name"
				placeholder="enter a word"></td>
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
		<tr>
			<td colspan="2"><input type="submit" value="Search"></td>
		</tr>
	</table>
</form>

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
			</tr>
		</thead>
		<tbody>
			<c:forEach var="concept" items="${result}">
				<tr class="gradeX">
					<td align="justify"><font size="2"><a
							onclick="detailsView(this);" id="${concept.entry.id}">Details</a></font></td>
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
					<td align="justify"><font size="2"> <c:out
								value="${concept.description}"></c:out>
					</font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.type.typeName}"></c:out></font></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:if>
<br />

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


