<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<style type="text/css" title="currentStyle">
div.dataTables_wrapper {
	max-height: 300px;
}
</style>

<script type="text/javascript">
	$(document).ready(function() {

		oTable = $('#conceptSearch').dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false,
		});

		$('#conceptSearch tr').click(function() {
			var aData = oTable.fnGetData(this); // get datarow
			if (null != aData) // null if we clicked on title row
			{
				var conceptID = aData[2];
				var wordnetID = aData[3];
				if (conceptID === wordnetID) {
					$(this).toggleClass('row_selected');
				}
			}

		});
	});
</script>


<h1>Add new Wordnet concept wrapper</h1>
<p>Add a wrapper for a concept in Wordnet. Do that if you for
	example want to attach an "equals to" URI a concept that already exists
	in Wordnet.</p>

<h2>1. Search for Wordnet concept</h2>

<form
	action="${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptwrapper/conceptsearch"
	method='post'>
	<table class="greyContent">
		<tr>
			<td>Create wrappper for wordnet concept:</td>
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
	<input type="submit" value="Search">
</form>

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
				<tr class="gradeX">
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
					<td align="justify"><font size="2"><c:forEach var="syn"
								items="${concept.synonyms}">
								<c:out value="-> ${syn.word}"></c:out>
							</c:forEach></font></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:if>

