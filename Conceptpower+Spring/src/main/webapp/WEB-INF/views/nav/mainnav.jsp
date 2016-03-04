<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<li ${currentPage == "home" ? "class=\"active\"" : ""}><a
	href="${pageContext.servletContext.contextPath}">Home</a></li>

<sec:authorize access="isAuthenticated()">


	<li class="dropdown"><a class="dropdown-toggle" href="#"
		data-toggle="dropdown">Concept List<strong class="caret"></strong></a>
		<div class="dropdown-menu"
			style="padding: 15px; width: 220px; margin-left: -120px">
			<div style="margin: 10px;">
				<a href="${pageContext.servletContext.contextPath}/auth/conceptlist">Concept
					List</a>
			</div>




			<div style="margin: 10px;">
				<a
					href="${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptlist">Add
					New Concept List</a>
			</div>
			<div style="margin: 10px;">
				<a
					href="${pageContext.servletContext.contextPath}/auth/conceptlist/addconcept">Add
					New Concept</a>
			</div>
			<div style="margin: 10px;">
				<a
					href="${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptwrapper">Add
					New Wordnet Concept Wrapper</a>
			</div>

		</div></li>



	<li class="dropdown"><a class="dropdown-toggle" href="#"
		data-toggle="dropdown">Concept Types<strong class="caret"></strong></a>
		<div class="dropdown-menu"
			style="padding: 15px; width: 220px; margin-left: -120px">
			<div style="margin: 10px;">
				<a href="${pageContext.servletContext.contextPath}/auth/concepttype">Concept
					Types</a>
			</div>
			<div style="margin: 10px;">
				<a
					href="${pageContext.servletContext.contextPath}/auth/concepttype/addtype">Add
					New Type</a>
			</div>
		</div></li>



	<sec:authorize access="hasRole('ROLE_CP_ADMIN')">
		<li class="dropdown"><a class="dropdown-toggle" href="#"
			data-toggle="dropdown">Users<strong class="caret"></strong></a>
			<div class="dropdown-menu"
				style="padding: 15px; width: 220px; margin-left: -120px">
				<div style="margin: 10px;">
					<a href="${pageContext.servletContext.contextPath}/auth/user/list">Manage
						Users</a>
				</div>
				<div style="margin: 10px;">
					<a href="">Add New User</a>
				</div>
			</div></li>
>>>>>>> refs/heads/story/new-layout
	</sec:authorize>
	<li ${currentPage == "logout" ? "class=\"active\"" : ""}><a
		href="<c:url value="/j_spring_security_logout" />"><i
			class="fa fa-sign-out"></i> Logout</a></li>
</sec:authorize>

<sec:authorize access="not isAuthenticated()">
	<li class="dropdown"><a class="dropdown-toggle" href="#"
		data-toggle="dropdown">Sign In <strong class="caret"></strong></a>
		<div class="dropdown-menu"
			style="padding: 15px; width: 220px; margin-left: -120px">
			<h4 style="margin-top: 0px">Login</h4>
			<form name='f' action="<c:url value='/j_spring_security_check' />"
				method='post'>
				<div style="margin: 10px;">
					<input placeholder="Username" type='text' name='j_username'
						class="form-control" value=''>
				</div>
				<div style="margin: 10px;">
					<input placeholder="Password" type='password' class="form-control"
						name='j_password' />
				</div>
				<div style="margin: 10px;">
					<input name="submit" type="submit" value="Login"
						class="btn btn-action btn-sm" />
				</div>
			</form>
		</div></li>
</sec:authorize>