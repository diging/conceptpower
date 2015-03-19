<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
	var form = $('#editpasswordform');
	$(document)
			.on(
					'submit',
					'form',
					function() {
						if ($("#password").val() === ""
								&& $("#repassword").val() === "") {
							$("#pwerror")
									.html(
											'Passwords can not be empty. Please re-enter the passwords or cancel !!');
							return false;
						} else if ($("#password").val() === $("#repassword")
								.val()) {
							return true;
						} else {
							$("#pwerror")
									.html(
											'Passwords does not match. Please re-enter the passwords !!');
							return false;
						}

					});
</script>

<h2>Edit user: ${fullname}</h2>

<form:form commandName="userbacking"
	action="${pageContext.servletContext.contextPath}/auth/user/editpassword/store"
	method='post' id="editpasswordform">

	<table style="vertical-align: top">
		<tr>
			<td>Username :</td>
			<form:hidden path="username" value="${username}" />
			<td>${username}</td>
			<td></td>
		</tr>
		<tr>
			<td>New password:</td>
			<td><form:input type="password" name="password" id="password"
					path="password" /></td>
			<td></td>
		</tr>
		<tr>
			<td>Re-type new password:</td>
			<td><form:input type="password" name="password" id="repassword"
					path="retypedPassword" /></td>
			<td><div id="pwerror" style="color: red;"></div></td>
		</tr>
		<tr>
			<td><input type="submit" value="Store changes" name="submit"
				class="button" /></td>
			<td><input type="button" value="Cancel" name="submit"
				onclick="window.location.replace('${pageContext.servletContext.contextPath}/auth/user/canceledit')"
				class="button" /></td>
		</tr>
	</table>

</form:form>