<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('#conceptSearch').dataTable({
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers",
			"bAutoWidth" : false
		});

	});
</script>


<h1>Welcome to Conceptpower</h1>

<sec:authorize access="isAuthenticated()">You're logged in!
</sec:authorize>

<p>This is Conceptpower, the concept management site for Quadriga.</p>

<form
	action="${pageContext.servletContext.contextPath}/auth/conceptsearch"
	method='post'>
	<table>
		<tr>
			<td>Word:</td>
			<td><input type="text" name="name" id="name"
				placeholder="enter a word"></td>
		</tr>
		<tr>
			<td>POS:</td>
			<td><select name="pos">
					<option value="noun">Nouns</option>
					<option value="verb">Verb</option>
					<option value="adverb">Adverb</option>
					<option value="adjective">Adjective</option>
					<option value="other">Other</option>
			</select></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Search"></td>
		</tr>
	</table>
</form>

<c:if test="${not empty result}">
	<table cellpadding="0" cellspacing="0" class="display dataTable"
		id="conceptSearch">

		<thead>
			<tr>
				<th>Term</th>
				<th>ID</th>
				<th>Wordnet ID</th>
				<th>POS</th>
				<th>Concept List</th>
				<th>Description</th>
				<th>Type</th>

			</tr>
		</thead>
		<tbody>

			<c:forEach var="concept" items="${result}">
				<tr class="gradeX">

					<td align="justify"><font size="2"><c:out
								value="${concept.entry.word}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.entry.id}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.entry.wordnetId}"></c:out></font></td>
					<td align="justify"><font size="2"><c:out
								value="${concept.entry.pos}"></c:out></font></td>
					<td  align="justify"><font size="2"><c:out
								value="${concept.entry.conceptList}"></c:out></font></td>
					<td  align="justify"><font size="2"><c:out
								value="${concept.description}"></c:out></font></td>
					<td  align="justify"><font size="2"><c:out
								value="${concept.type.typeName}"></c:out></font></td>

				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:if>

<br />


<sec:authorize access="isAnonymous()">
	<form name='f' action="<c:url value='j_spring_security_check' />"
		method='post'>

		<table>
			<tr>
				<td>Username:</td>
				<td><input type='text' name='j_username' value=''></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type='password' name='j_password' /></td>
			</tr>
			<tr>
		</table>
		<p>
			<input name="submit" type="submit" value="Login" class="button" />
		</p>


	</form>
</sec:authorize>

