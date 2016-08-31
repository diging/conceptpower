<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>


<h1>Forgotten Password</h1>

<p>Please enter your email address below. If you have an account you will receive an email with instructions how to login to Conceptpower.</p>

<form:form
	commandName="emailBackBean"
	action="${pageContext.servletContext.contextPath}/emailSent"
	method='post'>
    <p>
	Email address: <form:input type="text" id="email" path="email" class="form-control" placeholder="Email address for your account"/>
    </p>
    <p>
	<input name="submit" type="submit" value="Submit" class="btn btn-primary" />
    </p>
	<form:errors path="email" cssClass="error"></form:errors>
</form:form>




