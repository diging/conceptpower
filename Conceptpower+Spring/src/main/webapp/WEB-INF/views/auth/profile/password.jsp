<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1>Change Password</h1>

<p>Please enter your new password and repeat it.</p>

<form:form class="form-horizontal" modelAttribute="passwordBean" action="${pageContext.servletContext.contextPath}/auth/profile/password/change" method="POST">

  <div class="form-group">
    <label for="password" class="col-sm-2 control-label">Old Password</label>
    <div class="col-sm-10">
      <form:input type="password" class="form-control" id="oldPassword" path="oldPassword"
        placeholder="Old Password" />
      <small><form:errors class="error" path="oldPassword" /></small>
    </div>
  </div>
  <div class="form-group">
    <label for="password" class="col-sm-2 control-label">New Password</label>
    <div class="col-sm-10">
      <form:input type="password" class="form-control" id="password" path="password"
        placeholder="Password" />
        <small><form:errors class="error" path="password" /></small>
    </div>
  </div>
  <div class="form-group">
    <label for="passwordRepeat" class="col-sm-2 control-label">Repeat new Password</label>
    <div class="col-sm-10">
      <form:input type="password" class="form-control" id="passwordRepeat" path="passwordRepeat"
        placeholder="Repeat Password" />
      <small><form:errors class="error" path="passwordRepeat" /></small>
    </div>
  </div>

  <div class="text-right">
    <button class="btn btn-primary" type="submit">Change Password</button>
  </div>
</form:form>