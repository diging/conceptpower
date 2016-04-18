<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<link rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/js/datatable/css/jquery.dataTables.css" />

<script type="text/javascript" charset="utf-8"
	src="${pageContext.servletContext.contextPath}/resources/js/datatable/js/jquery.js"></script>
<script type="text/javascript" charset="utf-8"
	src="${pageContext.servletContext.contextPath}/resources/js/datatable/js/jquery.dataTables.js"></script>

<script type="text/javascript">

	$(document).ready(function() {
		
		$('#conceptList').dataTable({
			"aoColumnDefs" : [
			  				{
			  					"targets": [0,1],
			  					'bSortable': false
			  				}     
			  			],
			"aaSorting" : [ [ 3, "asc" ] ]
		});
	});
</script>

<h1>Concept Lists</h1>
<center><font color="red">${IndexerStatus}</font></center>
<br />
<a
	href="${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptlist"><i
	class="fa fa-plus-circle"> </i>Add New Concept List</a>
<p>Here you find all stored concept lists.</p>

<c:if test="${not empty result}">
	<table id="conceptList" cellpadding="0" cellspacing="0" border="0"
		class="table table-striped table-bordered" width="100%">
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
				<tr>
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