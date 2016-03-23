<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>User Management</h2>
<br/>
<a href="${pageContext.servletContext.contextPath}/auth/user/add"><i class="fa fa-plus-circle"></i> Add New User</a>
<br/><br/>

<table id="userTable" cellpadding="0" cellspacing="0" border="0"
	class="table table-striped table-bordered" width="100%">
	<thead>
		<tr>
			<th></th>
			<th></th>
			<th></th>
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
					href="${pageContext.servletContext.contextPath}/auth/user/deleteuser/${user.username}"
					title="Delete User"><i class="fa fa-trash-o"></i> </a></td>
				<td width="20"><a
					href="${pageContext.servletContext.contextPath}/auth/user/edituser/${user.username}"
					title="Edit User"><i class="fa fa-pencil-square-o"></i></a></td>
				<td width="20"><a
					href="${pageContext.servletContext.contextPath}/auth/user/editpassword/${user.username}"
					title="Edit Password"><i class="fa fa-key"></i></a></td>
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