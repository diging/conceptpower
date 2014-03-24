<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
	var form = $('#edituserform');
	$(document)
			.on(
					'submit',
					'form',
					function() {
						if (!$("#password").val() === $("#repassword").val()) {
							$("#pwerror")
									.html(
											'<p  style="color:red">Passwords does not match. Please re-enter the passwords<p/>');
							return false;
						}

						if ($("#fullname").val() === "") {
							$("#fnerror")
									.html(
											'<p  style="color:red">Name can not be empty !!<p/>');
							return false;
						}

						return true;
					});
</script>

<h2>Edit user: ${fullname}</h2>

<form
	action="${pageContext.servletContext.contextPath}/auth/user/edit/store"
	method='post' id="edituserform">

	<table>
		<tr>
			<td>Username :</td>
			<td>${username}</td>
			<td></td>
		</tr>
		<tr>
			<td>Name :</td>
			<td><input type="text" name="fullname" id="fullname"
				value="${fullname}"></td>
			<td><div id="fnerror"></div></td>
		</tr>
		<tr>
			<td>Is Administrator:</td>
			<td><input type="checkbox" name="isadmin" id="isadmin"
				<c:if test="${isadmin}">checked="checked"</c:if>>Yes</td>
			<td></td>
		</tr>
		<tr>
			<td>New password:</td>
			<td><input type="password" name="password" id="password"
				value="${password}"></td>
			<td><div id="pwerror"></div></td>
		</tr>
		<tr>
			<td>Re-type new password:</td>
			<td><input type="password" name="repassword" id="repassword"
				value="${repassword}"></td>
			<td></td>
		</tr>
		<tr>
			<td><input type="submit" value="Store changes" name="submit" /></td>
			<td><input type="button" value="Cancel" /></td>
		</tr>
	</table>

</form>