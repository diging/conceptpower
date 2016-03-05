<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"
	href="${pageContext.servletContext.contextPath}/resources/js/datatable/css/jquery.dataTables.css" />

<script type="text/javascript" charset="utf-8"
	src="${pageContext.servletContext.contextPath}/resources/js/datatable/js/jquery.js"></script>
<script type="text/javascript" charset="utf-8"
	src="${pageContext.servletContext.contextPath}/resources/js/datatable/js/jquery.dataTables.js"></script>

<script>
	$(document).ready(function() {
		$('#userTable').dataTable({
			"aaSorting" : [ [ 3, "asc" ] ],

			"aoColumnDefs" : [ {
				"targets" : [ 0, 1, 2 ],
				'bSortable' : false
			} ]
		});
	});
</script>