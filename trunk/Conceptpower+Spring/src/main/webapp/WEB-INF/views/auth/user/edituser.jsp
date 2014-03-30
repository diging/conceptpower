<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
	var form = $('#edituserform');
	$(document)
			.on(
					'submit',
					'form',
					function() {

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
	action="${pageContext.servletContext.contextPath}/auth/user/edituser/store"
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
			<td><input type="submit" value="Store changes" name="submit"
				class="button" /></td>
			<td><input type="button" value="Cancel" name="submit"
				onclick="window.location.replace('${pageContext.servletContext.contextPath}/auth/user/canceledit')"
				class="button" /></td>
		</tr>
	</table>

</form>