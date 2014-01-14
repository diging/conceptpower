<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<p>My the conceptpower be with you!</p>

<h2>You can</h2>
<form>
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
		<li><a href="">Add New VIAF concept</a></li>
	</ul>
	<a href="${pageContext.servletContext.contextPath}/auth/concepttype">Manage
		Concept Types</a>
	<ul>
		<li class="first"><a
			href="${pageContext.servletContext.contextPath}/auth/concepttype/addtype">Add
				New Type</a></li>
	</ul>
	<a href="${pageContext.servletContext.contextPath}/auth/user/list">Manage
		Users</a>
	<ul>
		<li class="first"><a
			href="${pageContext.servletContext.contextPath}/auth/user/add">Add
				New User</a></li>
	</ul>
</form>


<p>Need help? Write Julia an email: jdamerow@asu.edu</p>

