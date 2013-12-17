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
			"bAutoWidth" : false
		});
	});
</script>

<h1>Concept Lists</h1>
<p>Here you find all stored concept lists.</p>

<c:if test="${not empty result}">
	<table cellpadding="0" cellspacing="0" class="display dataTable"
		id="conceptList">
		<thead>
			<tr>
				<th>Name</th>
				<th>Description</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="list" items="${result}">
				<tr class="gradeX">
					<td align="justify"><font size="2"><a
							href="${pageContext.servletContext.contextPath}/auth/concepts/conceptsview/${list.conceptListName}">${list.conceptListName}</a></font></td>
					<td align="justify"><font size="2"><c:out
								value="${list.description}"></c:out></font></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:if>