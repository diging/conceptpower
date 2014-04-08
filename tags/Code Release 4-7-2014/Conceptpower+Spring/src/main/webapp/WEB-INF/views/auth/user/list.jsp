<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>User Management</h2>

<table id="userTable" cellpadding="0" cellspacing="0" border="0"
	class="display dataTable" width="100%">
	<thead>
		<tr>
			<th>Edit User</th>
			<th>Edit Password</th>
			<th>Username</th>
			<th>Name</th>
			<th>User is admin</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="user" items="${users}">
			<tr>
				<td><a
					href="${pageContext.servletContext.contextPath}/auth/user/edituser/${user.user}"><img
						src="${pageContext.servletContext.contextPath}/resources/img/edit_16x16.png" /></a></td>
				<td><a
					href="${pageContext.servletContext.contextPath}/auth/user/editpassword/${user.user}"><img
						src="${pageContext.servletContext.contextPath}/resources/img/edit_16x16.png" /></a></td>
				<td>${user.user}</td>
				<td>${user.name}</td>
				<td><c:if test="${user.isAdmin}">
						<img
							src="${pageContext.servletContext.contextPath}/resources/img/check_mark_16x16.png" />
					</c:if></td>
			</tr>
		</c:forEach>
	</tbody>
</table>