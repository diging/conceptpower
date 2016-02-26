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
			"bAutoWidth" : false,
			"aoColumnDefs" : [
				{
					"targets": [0,1],
					'bSortable': false
				}     
			],
		"order": [[ 2, "desc" ]]
		});

	});
</script>


<h1>Types</h1>
<p>See existing types below.</p>

<c:if test="${not empty result}">
	<table cellpadding="0" cellspacing="0" class="table table-striped table-bordered"
		id="conceptTypes">
		<thead>
			<tr>
				<th></th>
				<th></th>
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
					<td align="justify"><a
						href="${pageContext.servletContext.contextPath}/auth/concepttype/deletetype/${types.typeId}">
						<i class="fa fa-trash-o"></i>
						</a>
					</td>
					<td align="justify"><a
						href="${pageContext.servletContext.contextPath}/auth/concepttype/edittype/${types.typeId}"><i
							class="fa fa-pencil-square-o"></i></a></td>
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