<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>


<h1>Reset your Password</h1>

<p>Please enter the email address to which the recovery email was sent:</p>

<form:form
	modelAttribute="emailBackBean"
	action="${pageContext.servletContext.contextPath}/reset"
	method='post'>
    <p>
	Email address: <form:input type="text" id="email" path="email" class="form-control"/>
	<form:hidden path="token"/>
    </p>
    <p>
	<input name="submit" type="submit" value="Reset" class="btn btn-primary" />
    </p>
	<form:errors path="email" cssClass="error"></form:errors>
</form:form>




