<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<c:if test="${not empty result}">
	<table cellpadding="0" cellspacing="0" class="display dataTable"
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
			</tr>
		</thead>
		<tbody>
			<c:forEach var="concept" items="${result}">
				<tr class="gradeX">
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
				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:if>