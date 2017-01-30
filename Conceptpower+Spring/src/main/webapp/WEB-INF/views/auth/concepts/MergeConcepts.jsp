<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<head>
<title>Merge Concept</title>
<style type="text/css">
.descriptionDiv {
	height: 100%;
}
</style>
<script>
$(document).ready(function() { 
    $('#conceptMergeForm').submit(function(event) {
        var ret = true;
        if ($("#words").val() === "") {
            $("#wordsError").html('Word cannot be empty');
            ret = false;
        } else {
            $("#wordsError").html('');
        }
        if ($("#selectedPosValue").val() === "") {
            $("#selectedPosError").html('Please select a pos.');
            ret = false;
        } else {
            $("#selectedPosError").html('');
        }
        if ($("#selectedListName").val() === "") {
            $("#selectedListNameError").html('Please select a concept list.');
            ret = false;
        } else {
            $("#selectedListNameError").html('');
        }
        if ($("#selectedTypeId").val() === "") {
            $("#selectedTypeIdError").html('Please select a concept type.');
            ret = false;
        } else {
            $("#selectedTypeIdError").html('');
        }
        
        if(ret == true) {
			$("#descriptions").val($("#descriptionDiv").text().trim());
        }
        return ret;
    });    
});
</script>
<form:form action="${pageContext.servletContext.contextPath}/auth/concepts/merge" method='post' id="conceptMergeForm" modelAttribute="conceptsMergeBean">
    
    <c:if test="${not empty conceptsMergeBean.errorMessages }">
        <div class="alert alert-warning alert-dismissable">
            <c:forEach var="error" items="${conceptsMergeBean.errorMessages }">
                <p>
                    <spring:message code="${error.errorCode}" />
                </p>
            </c:forEach>
        </div>
    </c:if>
    
    <div class="alert alert-info alert-dismissable">
        The following concepts are being merged.
        <ul class="list-group">
            <c:forEach var="conceptIdWithName" items="${conceptsMergeBean.conceptNamesWithIds}">
                <li class="list-group-item">${conceptIdWithName }</li>
            </c:forEach>
        </ul> 
    </div>

    <h1>Merge concepts</h1>
    
    <c:choose>
        <c:when test="${not empty conceptsMergeBean.conceptIds }">
            <label class="col-sm-1 control-label">Concept Id</label>
            <div class="col-sm-11">
                <!-- If none of the values are selected, we create a new concept id for the merged concept -->
                <form:radiobuttons path="selectedConceptId" items="${conceptsMergeBean.conceptIds}" element="label class='radio-inline'" />
            </div>
        </c:when>
        <c:otherwise>
            <div class="col-sm-12">
                New Id will be generated for merged concepts.
            </div>
        </c:otherwise>
    </c:choose>
    
    
    <table>
        <tr>
            <td class="col-sm-1">Concept</td>
            <td class="col-sm-9"><form:input path="word" class="form-control" /></td>
            <td class="col-sm-2">
                <div id="wordsError" style="color: red"></div>
                <form:errors path="word" class="ui-state-error-text"></form:errors>
            </td>
        </tr>
        <tr>
            <td class="col-sm-1">POS</td>
            <td class="col-sm-9">
                <form:select path="selectedPosValue" class="form-control">
                    <form:option value="" />
                    <c:forEach var="posValue" items="${posValues}">
                      <c:set var="labelVar">
                          <spring:message code="${posValue}" />
                      </c:set>
                      <form:option value="${posValue }" label="${labelVar}" />    
                    </c:forEach>
                </form:select>
            </td>
            <td class="col-sm-2">
                <div id="selectedPosError" style="color: red"></div>
                <form:errors path="selectedPosValue" class="ui-state-error-text"></form:errors>
            </td>
        </tr>
        <tr>
            <td class="col-sm-1">Concept List</td>
            <td class="col-sm-9"><form:select
                    path="selectedListName" class="form-control"
                    items="${conceptListValues}" /></td>
            <td class="col-sm-2">
                <div id="selectedListNameError" style="color: red"></div>
                <form:errors path="selectedListName" class="ui-state-error-text"></form:errors>
            </td>
        </tr>
        <tr>
            <td class="col-sm-1">Description</td>
            <td class="col-sm-9">
                <div contenteditable="true" id="descriptionDiv" class="descriptionDiv form-control">
                    <c:forEach var="description" items="${conceptsMergeBean.descriptions }">
                        <p>
                            <c:out value="${description }" />
                        </p>
                    </c:forEach>
                </div>
                <form:hidden path="descriptions" />
            </td>
        </tr>
        <tr>
            <td class="col-sm-1">Concept Type</td>
            <td class="col-sm-9"><form:select
                    path="selectedTypeId" class="form-control">
                    <form:option value="" />
                    <form:options items="${types}"
                        itemValue="typeId" itemLabel="typeName" />
                </form:select></td>
            <td class="col-sm-2">
                <div id="selectedTypeIdError" style="color: red"></div>
                <form:errors path="selectedTypeId" class="ui-state-error-text"></form:errors>
            </td>
        </tr>
        <tr>
            <td class="col-sm-1">Equals</td>
            <td class="col-sm-9"><form:textarea path="equalsValues"
                    class="form-control" />
        </tr>

        <tr>
            <td class="col-sm-1">Similar</td>
            <td class="col-sm-9"><form:input path="similarValues"
                    class="form-control" /></td>
        </tr>
    </table>
    <br />
    <br />

    <table class="width: 100%;">
        <tr>
            <td><input type="submit" value="Store merged concept" class="btn btn-primary"></td>

            <td><a
                href="${pageContext.servletContext.contextPath}/"><input
                    type="button" name="cancel" value="Cancel!"
                    class="btn btn-primary"></a></td>
        </tr>
    </table>
    <form:hidden path="conceptIds" />
    <form:hidden path="mergedIds" />
    <form:hidden path="synonymsids" />
    <form:hidden path="conceptWordnetIds" />
    <form:hidden path="alternativeIds" />
</form:form>