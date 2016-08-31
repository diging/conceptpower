<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="page-header">
  <h1>Profile: ${currentUser.fullname} <small>${currentUser.username}</small></h1>
</div>

<p>
<i class="fa fa-envelope" aria-hidden="true"></i> ${currentUser.email}
</p>
<p>
<c:if test="${currentUser.isAdmin}">
<span class="label label-danger">Admin</span>
</c:if>
<c:if test="${not currentUser.isAdmin}">
<span class="label label-info">Regular User</span>
</c:if>
</p>

<p class="text-right">
<a class="btn btn-primary btn-sm" href="<c:url value="/auth/profile/password/change" />">Change Password</a>
</p>