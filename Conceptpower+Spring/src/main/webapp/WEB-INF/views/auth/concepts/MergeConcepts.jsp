<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<head>
<title>Merge Concept</title>
<form:form action="${pageContext.servletContext.contextPath}/auth/mergeConcepts" method='post' modelAttribute="conceptsMergeBean">

    <h1>Merge concepts</h1>

    <table>
        <tr>
            <td class="col-sm-1"> Concept Id </td>
        </tr>
        <c:forEach var="conceptId" items="${conceptsMergeBean.conceptIds}">
            <tr>
                <td class="col-sm-1">
                    <form:radiobutton path="selectedConceptId" value="${conceptId}" />
                </td>
                <td class="col-sm-11"> ${conceptId } </td>
            </tr>
        </c:forEach>
            <tr>
                <td class="col-sm-1">
                    <form:radiobutton path="selectedConceptId" value="" /> 
                    <form:hidden path="conceptIds" />
                </td>
                <td class="col-sm-11">Create new id </td>
            </tr>
        <tr>
            <td class="col-sm-1">Concept</td>
            <td class="col-sm-11"><form:input path="words"
                    class="form-control" /></td>
        </tr>
        <tr>
            <td class="col-sm-1">POS</td>
            <td class="col-sm-11"><form:select
                    path="selectedPosValue" class="form-control">
                    <form:option value="" />
                    <form:options items="${conceptsMergeBean.posMap}" />
                </form:select></td>
        </tr>
        <tr>
            <td class="col-sm-1">Concept List</td>
            <td class="col-sm-11"><form:select
                    path="selectedListName" class="form-control"
                    items="${conceptsMergeBean.conceptListValues}" /></td>
        </tr>
        <tr>
            <td class="col-sm-1">Description</td>
            <td class="col-sm-11"><form:textarea path="descriptions"
                    rows="7" cols="50" class="form-control" /></td>
        </tr>
        <tr>
            <td class="col-sm-1">Synonyms</td>
            <td class="col-sm-11">
                <form:textarea path="synonymsids" class="form-control" />
            </td>
        </tr>

        <tr>
            <td class="col-sm-1">Concept Type</td>
            <td class="col-sm-11"><form:select
                    path="selectedTypeId" class="form-control">
                    <form:option value="" />
                    <form:options items="${conceptsMergeBean.types}"
                        itemValue="typeId" itemLabel="typeName" />
                </form:select></td>
        </tr>
        <tr>
            <td class="col-sm-1">Equals</td>
            <td class="col-sm-11"><form:textarea path="equalsValues"
                    class="form-control" />
        </tr>

        <tr>
            <td class="col-sm-1">Similar</td>
            <td class="col-sm-11"><form:input path="similarValues"
                    class="form-control" /></td>
        </tr>
    </table>
    <br />
    <br />

    <table class="width: 100%;">
        <tr>
            <td><input type="submit" name="edit" id="edit"
                value="Store merged concept" class="btn btn-primary"></td>

            <td><a
                href="${pageContext.servletContext.contextPath}/auth/concepts/canceledit?fromHomeScreen=true"><input
                    type="button" name="cancel" value="Cancel!"
                    class="btn btn-primary"></a></td>
        </tr>
    </table>
</form:form>