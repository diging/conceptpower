<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<p>My the conceptpower be with you!</p>

<h2>You can</h2>
<form>
	<a href="conceptLists">Manage Concept Lists</a>
	<ul>
		<li class="first"><a href="">Add New Concept List</a></li>
		<li><a href="">Add New Concept</a></li>
		<li><a href="">Add New Wordnet Concept Wrapper</a></li>
		<li><a href="">Add New VIAF concept</a></li>
	</ul>
	<a href="conceptLists">Manage Concept Types</a>
	<ul>
		<li class="first"><a href="">Add New Type</a></li>
	</ul>
	<a href="conceptLists">Manage Users</a>
	<ul>
		<li class="first"><a href="">Add New User</a></li>
	</ul>
</form>


<p>Need help? Write Julia an email: jdamerow@asu.edu</p>

