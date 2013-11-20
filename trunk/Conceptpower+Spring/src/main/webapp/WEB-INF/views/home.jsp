<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>


<h1>Welcome to Conceptpower</h1>

<sec:authorize access="isAuthenticated()">You're logged in!
</sec:authorize>

<p>This is Conceptpower, the concept management site for Quadriga.</p>

<form
	action="/conceptpower/auth/searchitems"
	method="get">
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

<br/>


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

