<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<li ${currentPage == "home" ? "class=\"active\"" : ""}><a
	href="${pageContext.servletContext.contextPath}">Home</a></li>

<sec:authorize access="isAuthenticated()">


	<li class="dropdown"><a class="dropdown-toggle" href="#"
		data-toggle="dropdown">Concept List<strong class="caret"></strong></a>

		<ul class="dropdown-menu"
			style="padding: 15px; width: 300px; margin-left: -120px">


			<li><a
				href="${pageContext.servletContext.contextPath}/auth/conceptlist"><i
					class="fa fa-list"></i> Show Concept Lists</a></li>
			<li><a
				href="${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptlist"><i
					class="fa fa-plus-circle"></i> Add New Concept List</a></li>
			<li><a
				href="${pageContext.servletContext.contextPath}/auth/conceptlist/addconcept">
					<i class="fa fa-plus-circle"></i> Add New Concept
			</a></li>
			<li><a
				href="${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptwrapper"><i
					class="fa fa-plus-circle"></i> Add New Wordnet Concept Wrapper</a></li>
		</ul></li>


	<li class="dropdown"><a class="dropdown-toggle" href="#"
		data-toggle="dropdown">Concept Types<strong class="caret"></strong></a>

		<ul class="dropdown-menu"
			style="padding: 15px; width: 220px; margin-left: -120px">


			<li><a
				href="${pageContext.servletContext.contextPath}/auth/concepttype"><i
					class="fa fa-tags"></i> Concept Types</a></li>
			<li><a
				href="${pageContext.servletContext.contextPath}/auth/concepttype/addtype"><i
					class="fa fa-plus-circle"></i> Add New Type</a></li>
		</ul></li>



	<sec:authorize access="hasRole('ROLE_CP_ADMIN')">

		<li class="dropdown"><a class="dropdown-toggle" href="#"
			data-toggle="dropdown">Users<strong class="caret"></strong></a>

			<ul class="dropdown-menu"
				style="padding: 15px; width: 220px; margin-left: -120px">


				<li><a
					href="${pageContext.servletContext.contextPath}/auth/user/list"><i
						class="fa fa-users"></i> Manage Users</a></li>
				<li><a
					href="${pageContext.servletContext.contextPath}/auth/user/add"><i
						class="fa fa-plus-circle"></i> Add New User</a></li>
			</ul></li>


	</sec:authorize>


	<sec:authorize access="hasRole('ROLE_CP_ADMIN')">
		<li ${currentPage == "luceneIndex" ? "class=\"selected\"" : ""}><a
			href="${pageContext.servletContext.contextPath}/auth/index">
				Index</a></li>
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
			<form action="<c:url value='/login' />"
				method='POST'>
				<div style="margin: 10px;">
					<input placeholder="Username" type='text' name='username'
						class="form-control" value=''>
				</div>
				<div style="margin: 10px;">
					<input placeholder="Password" type='password' class="form-control"
						name='password' />
				</div>
				<div style="margin: 10px;">
					<input name="submit" type="submit" value="Login"
						class="btn btn-action btn-sm" />
				</div>
			</form>
            <div class="pull-right">
            <a href="<c:url value="/forgot" />">Forgot Password?</a>
            </div>
		</div></li>
</sec:authorize>