<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>User Management</h2>

<table id="userTable" cellpadding="0" cellspacing="0" border="0"
	class="display dataTable" width="100%">
	<thead>
		<tr>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th>Username</th>
			<th>Name</th>
			<th>Email</th>
			<th>Is admin</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="user" items="${users}">
			<tr>
				<td  align="justify" width="20"><a title="Delete User"
					href="${pageContext.servletContext.contextPath}/auth/user/deleteuser/${user.username}"><i class="fa fa-trash"></i></a></td>
				<td  align="justify" width="20"><a title="Edit User"
					href="${pageContext.servletContext.contextPath}/auth/user/edituser/${user.username}"><i class="fa fa-pencil"></i></a></td>
				<td  align="justify" width="20"><a title="Change Password"
					href="${pageContext.servletContext.contextPath}/auth/user/editpassword/${user.username}"><i class="fa fa-key"></i></a></td>
				<td  align="justify" width="20">
					<c:if test="${not user.isEncrypted}">
						<a title="Encrypt Password" href="${pageContext.servletContext.contextPath}/auth/user/encrypt/${user.username}"><i class="fa fa-lock"></i></a>
					</c:if>
					<c:if test="${user.isEncrypted}">
						<i title="Password already encrypted." class="fa fa-lock"></i>
					</c:if>
				</td>
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