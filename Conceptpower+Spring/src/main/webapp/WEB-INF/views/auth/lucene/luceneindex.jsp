<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<h2>Edit user: ${fullname}</h2>

<form:form
	action="${pageContext.servletContext.contextPath}/auth/indexLuceneWordNet"
	method='post' id="indexLucene">
	<center><b>${message }</b></center>
	<table style="vertical-align: top">
		<tr>
			<td><input type="submit" value="Index WordNet Wrappers"
				name="submit" class="button" /></td>
		</tr>
	</table>

</form:form>

<form:form
	action="${pageContext.servletContext.contextPath}/auth/deleteConcepts"
	method='post' id="deleteLucene">
	<table style="vertical-align: top">
		<tr>
			<td><input type="submit" value="Delete Indexes"
				name="submit" class="button" /></td>
		</tr>
	</table>

</form:form>