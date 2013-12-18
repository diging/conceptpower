<c:if test="${not empty synonyms}">
	<table cellpadding="0" cellspacing="0" class="display dataTable"
		id="synonymstable">
		<thead>
			<tr>
				<th>Term</th>
				<th>POS</th>
				<th>Description</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="synonym" items="${synonyms}">
				<tr class="gradeX">
					<td align="justify"><font size="2"><c:out
								value="${synonym.word}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${synonym.pos}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${synonym.description}"></c:out></font></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:if>