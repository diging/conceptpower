<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>User Management</h2>

<table id="userTable" cellpadding="0" cellspacing="0" border="0"
	class="display dataTable" width="100%">
	<thead>
		<tr>
			<th>Username</th>
			<th>Name</th>
			<th>User is admin</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="user" items="${users}">
			<tr>
				<td>${user.user}</td>
				<td>${user.name}</td>
				<td>${user.isAdmin}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>