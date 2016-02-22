<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>User Management</h2>

<table id="userTable" cellpadding="0" cellspacing="0" border="0"
	class="table table-striped table-bordered" width="100%">
	<thead>
		<tr>
			<th>Delete User</th>
			<th>Edit User</th>
			<th>Edit Password</th>
			<th>Username</th>
			<th>Name</th>
			<th>Email</th>
			<th>User is admin</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="user" items="${users}">
			<tr>
				<td width="20"><a
					href="${pageContext.servletContext.contextPath}/auth/user/deleteuser/${user.username}"><img
						src="${pageContext.servletContext.contextPath}/resources/img/trash_16x16.png" /></a></td>
				<td width="20"><a
					href="${pageContext.servletContext.contextPath}/auth/user/edituser/${user.username}"><img
						src="${pageContext.servletContext.contextPath}/resources/img/edit_16x16.png" /></a></td>
				<td width="20"><a
					href="${pageContext.servletContext.contextPath}/auth/user/editpassword/${user.username}"><img
						src="${pageContext.servletContext.contextPath}/resources/img/edit_16x16.png" /></a></td>
				<td>${user.username}</td>
				<td>${user.fullname}</td>
				<td>${user.email}</td>
				<td width="20"><c:if test="${user.isAdmin}">
						<img
							src="${pageContext.servletContext.contextPath}/resources/img/check_mark_16x16.png" />
					</c:if></td>
			</tr>
		</c:forEach>
	</tbody>
</table>