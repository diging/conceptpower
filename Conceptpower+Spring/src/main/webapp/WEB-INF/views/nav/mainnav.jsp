<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
	$('#menu li').click(function() {
		$('#menu li').addClass('selected');
	});
</script>


<ul id="menu">
	<li ${currentPage == "home" ? "class=\"selected\"" : ""}><a
		href="${pageContext.servletContext.contextPath}">Home</a></li>

	<sec:authorize access="isAuthenticated()">
		<li ${currentPage == "conceptlists" ? "class=\"selected\"" : ""}><a
			href="${pageContext.servletContext.contextPath}/auth/conceptlist">Concept
				Lists</a></li>
		<li ${currentPage == "concepttypes" ? "class=\"selected\"" : ""}><a
			href="${pageContext.servletContext.contextPath}/auth/concepttype">Concept
				Types</a></li>
		<sec:authorize access="hasRole('ROLE_CP_ADMIN')">
			<li ${currentPage == "users" ? "class=\"selected\"" : ""}><a
				href="${pageContext.servletContext.contextPath}/auth/user/list">Manage
					Users</a></li>
		</sec:authorize>

		<sec:authorize access="hasRole('ROLE_CP_ADMIN')">
			<li ${currentPage == "luceneIndex" ? "class=\"selected\"" : ""}><a
				href="${pageContext.servletContext.contextPath}/auth/luceneIndex">
					Index</a></li>
		</sec:authorize>

		<li ${currentPage == "logout" ? "class=\"selected\"" : ""}><a
			href="<c:url value="/j_spring_security_logout" />">Logout</a></li>
	</sec:authorize>
</ul>