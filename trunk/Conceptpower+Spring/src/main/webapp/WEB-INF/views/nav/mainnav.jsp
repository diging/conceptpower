<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<ul id="menu">
	<li class="selected"><a href="">Home</a></li>
	
	<sec:authorize access="isAuthenticated()">
		<li><a href="conceptLists">Concept Lists</a></li>
		<li><a href="">Concept Types</a></li>
		<sec:authorize access="hasRole('ROLE_CP_ADMIN')"><li><a href="${pageContext.servletContext.contextPath}/auth/user/list">Manage Users</a></li></sec:authorize>
		<li><a href="<c:url value="/j_spring_security_logout" />">Logout</a></li>
	</sec:authorize>
</ul>