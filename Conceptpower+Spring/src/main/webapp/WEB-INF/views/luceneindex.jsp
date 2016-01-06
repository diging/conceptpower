<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<h2>Create Lucene Index for WordNet Concepts</h2>

<form:form
	action="${pageContext.servletContext.contextPath}/auth/indexLuceneWordNet"
	method='post' id="indexLucene">

	<center>${message }</center>
	<table style="vertical-align: top">
		<tr>
			<td><input type="submit" value="Index WordNet Wrappers"
				name="submit" class="button" /></td>
			<td><input type="button" value="Cancel" name="submit"
				onclick="window.location.replace('${pageContext.servletContext.contextPath}/auth/home')"
				class="button" /></td>
		</tr>
	</table>

</form:form>