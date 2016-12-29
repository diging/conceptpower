<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<head>
<title>Merge Concept</title>
<form:form action="${pageContext.servletContext.contextPath}/auth/concepts/merge" method='post' modelAttribute="conceptsMergeBean">

    <h1>Merge concepts</h1>

    <label class="col-sm-1 control-label">Concept Id</label>
    <div class="col-sm-11">
        <!-- If none of the values are selected, we create a new concept id for the merged concept -->
        <form:radiobuttons path="selectedConceptId" items="${conceptsMergeBean.conceptIds}" element="label class='radio-inline'" />
    </div>
    
    <table>
        <tr>
            <td class="col-sm-1">Concept</td>
            <td class="col-sm-11"><form:input path="words" class="form-control" /></td>
        </tr>
        <tr>
            <td class="col-sm-1">POS</td>
            <td class="col-sm-11"><form:select
                    path="selectedPosValue" class="form-control">
                    <form:option value="" />
                    <form:options items="${posMap}" />
                </form:select></td>
        </tr>
        <tr>
            <td class="col-sm-1">Concept List</td>
            <td class="col-sm-11"><form:select
                    path="selectedListName" class="form-control"
                    items="${conceptListValues}" /></td>
        </tr>
        <tr>
            <td class="col-sm-1">Description</td>
            <td class="col-sm-11"><form:textarea path="descriptions"
                    rows="7" cols="50" class="form-control" /></td>
        </tr>
        <tr>
            <td class="col-sm-1">Concept Type</td>
            <td class="col-sm-11"><form:select
                    path="selectedTypeId" class="form-control">
                    <form:option value="" />
                    <form:options items="${types}"
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
    <form:hidden path="conceptIds" />
    <form:hidden path="wordnetIds" />
    <form:hidden path="synonymsids" />
</form:form>