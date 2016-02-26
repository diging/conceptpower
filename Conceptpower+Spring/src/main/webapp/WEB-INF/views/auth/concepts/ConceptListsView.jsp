<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>


<script type="text/javascript">

	$(document).ready(function() {
		
		$('#conceptList').dataTable({
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

<h1>Concept Lists</h1>
<p>Here you find all stored concept lists.</p>

<c:if test="${not empty result}">
	<table cellpadding="0" cellspacing="0"
		class="table table-striped table-bordered " id="conceptList">
		<thead>
			<tr>
				<th></th>
				<th></th>
				<th>Name</th>
				<th>Description</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="list" items="${result}">
				<tr class="gradeX">
					<td align="justify" width="20"><a
						href="${pageContext.servletContext.contextPath}/auth/conceptlist/deletelist/${list.conceptListName}"><i
							class="fa fa-trash-o"></i> </a></td>
					<td align="justify" width="20"><a
						href="${pageContext.servletContext.contextPath}/auth/conceptlist/editlist/${list.conceptListName}"><i
							class="fa fa-pencil-square-o"></i></a></td>
					<td align="justify"><font size="2"><a
							href="${pageContext.servletContext.contextPath}/auth/${list.conceptListName}/concepts">${list.conceptListName}</a></font></td>
					<td align="justify"><font size="2"><c:out
								value="${list.description}"></c:out></font></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:if>