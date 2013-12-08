<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>


<script type="text/javascript">
	$(document).ready(function() {
		$('#conceptTypes').dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false
		});

	});
</script>


<h1>Types</h1>
<p>See existing types below.</p>

<c:if test="${not empty result}">
	<table cellpadding="0" cellspacing="0" class="display dataTable"
		id="conceptTypes">
		<thead>
			<tr>
				<th>Name</th>
				<th>Type URI</th>
				<th>Description</th>
				<th>Super Type</th>
				<th>Matches</th>
				<th>Created by</th>
				<th>Modified</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="types" items="${result}">
				<tr class="gradeX">
					<td align="justify"><font size="2"><c:out
								value="${types.typeName}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.typeId}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.description}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.supertypeId}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.matches}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.creatorId}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${types.modified}"></c:out></font></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:if>