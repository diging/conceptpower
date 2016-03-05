<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<%@ page isELIgnored="false"%>


<sec:authorize access="isAnonymous()">
	<p />
	<h3>Login</h3>
	<form name='f' action="<c:url value='/j_spring_security_check' />"
		method='post'>
		<table>
			<tr>
				<td>Username:</td>
				<td><input type='text' name='j_username' class="form-control"
					value=''></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type='password' class="form-control"
					name='j_password' /></td>
			</tr>
			<tr>
				<td></td>
				<td>
					<p>
						<input name="submit" type="submit" value="Login"
							class="btn btn-action btn-sm" />
					</p>
				</td>
			</tr>
		</table>
	</form>

	<div class="text-right">
		<a href="${pageContext.servletContext.contextPath}/forgot">Forgot
			password?</a>
	</div>
</sec:authorize>


<sec:authorize access="isAuthenticated()">
	<sec:authentication var="user" property="principal.username" />
	<p>Hi ${user}!</p>
	<p>May the Conceptpower be with you.</p>

	
	<form>

		<c:if test="${currentPage eq 'ConceptList' }">
		<h2>You can</h2>
			<a href="${pageContext.servletContext.contextPath}/auth/conceptlist">Manage
				Concept Lists</a>
			<ul>
				<li class="first"><a
					href="${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptlist">Add
						New Concept List</a></li>
				<li><a
					href="${pageContext.servletContext.contextPath}/auth/conceptlist/addconcept">Add
						New Concept</a></li>
				<li><a
					href="${pageContext.servletContext.contextPath}/auth/conceptlist/addconceptwrapper">Add
						New Wordnet Concept Wrapper</a></li>
			</ul>
		</c:if>
		<c:if test="${currentPage eq 'Concept Types' }">
		<h2>You can</h2>
			<a href="${pageContext.servletContext.contextPath}/auth/concepttype">Manage
				Concept Types</a>
			<ul>
				<li class="first"><a
					href="${pageContext.servletContext.contextPath}/auth/concepttype/addtype">Add
						New Type</a></li>
			</ul>
		</c:if>

		<c:if test="${currentPage eq 'Users'}">
		<h2>You can</h2>
			<sec:authorize access="hasRole('ROLE_CP_ADMIN')">
				<a href="${pageContext.servletContext.contextPath}/auth/user/list">Manage
					Users</a>
				<ul>
					<li class="first"><a
						href="${pageContext.servletContext.contextPath}/auth/user/add">Add
							New User</a></li>
				</ul>
			</sec:authorize>

		</c:if>


	</form>
</sec:authorize>



