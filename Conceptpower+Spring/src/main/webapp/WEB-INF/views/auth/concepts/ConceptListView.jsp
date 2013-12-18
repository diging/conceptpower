<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('#conceptList').dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false
		});

	});
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
					<td align="justify"><font size="2"><c:out
								value="${concept.entry.word}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.entry.word}"></c:out></font></td>
					<td align="justify"><a
						href="${pageContext.servletContext.contextPath}/auth/concepts/deleteconcept/${concept.entry.id}"><input
							type="image"
							src="${pageContext.servletContext.contextPath}/resources/img/trash_16x16.png"></input></a>
					</td>
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
					<td align="justify"><font size="2"><c:out
								value="${concept.synonyms}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.creator.user}"></c:out></font></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:if>